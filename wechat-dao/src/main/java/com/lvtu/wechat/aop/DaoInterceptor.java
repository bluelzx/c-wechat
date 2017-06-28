package com.lvtu.wechat.aop;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import com.lvtu.wechat.common.model.mail.Mail;
import com.lvtu.wechat.common.utils.MailUtil;
import com.lvtu.wechat.common.utils.ServiceConstants;

@Aspect
@Component
public class DaoInterceptor {
	
    private Logger logger = Logger.getLogger(DaoInterceptor.class);

    /**
     * sql执行时间超过500ms的集合，便于查看是否已经发送过邮件
     */
    private static List<String> timeOutList = new ArrayList<String>();
    
    /**
     * sql执行异常的集合，便于查看是否已经发送过邮件
     */
    private static List<String> exceptionList = new ArrayList<String>();
    
    @Around("execution(* com.lvtu.wechat.dao..*.*(..))")
    public Object around(ProceedingJoinPoint p) throws Throwable{
        long time = System.currentTimeMillis();
        Object obj = null;
        try {
            obj = p.proceed();
        }
        catch (Exception e) {
            logger.error(e);
            if(!exceptionList.contains(p.toString())) {
            	exceptionList.add(p.toString());	
            	sendMail("sql执行异常" + e.getMessage(),p);
            }
            throw e;
        }
        time = System.currentTimeMillis() - time;
    	if (time > 1500 && !timeOutList.contains(p.toString())) {
    		timeOutList.add(p.toString()); 
    		sendMail("sql执行时间太长，时间为：" + time, p);
    	}
        logger.debug("执行" + p.toString() + "-----执行时间" + time +"毫秒");
        return obj;
    }
    
    public void sendMail(String message, ProceedingJoinPoint p) {
        String sendFlag = ServiceConstants.getConfig("mail.send");
        if ("1".equals(sendFlag)) {
            new SendMailThread(message , p).start();
        }
    }
    
    class SendMailThread extends Thread {
    	
    	ProceedingJoinPoint p;
    	String message;
    	
    	public SendMailThread (String message, ProceedingJoinPoint p) {
    		this.p = p;
    		this.message = message;
    	}

		@Override
		public void run() {
			try {
		       	 Mail mail = new Mail();  
		   		 mail.setHost("smtp.163.com"); // 设置邮件服务器
		   		 mail.setSender("lv_wx_send@163.com");  
		   		 mail.setReceiver("lv_wx_Receiver@163.com"); // 接收人  
		   		 mail.setUsername("lv_wx_send@163.com"); // 登录账号,和邮箱名一样吧 
		   		 mail.setPassword("lv123456"); // 发件人邮箱的登录密码  
		   		 String sendEgine =  ServiceConstants.getConfig("mail.send.egine");
		   		 mail.setSubject(sendEgine + "_微信系统监控邮件");  
		   		 mail.setMessage(p.toString() +  "\r\n" + message);  
		   		 new MailUtil().send(mail);
	    	}
	    	 catch (Exception e) {
	    	}
		}
    }
}

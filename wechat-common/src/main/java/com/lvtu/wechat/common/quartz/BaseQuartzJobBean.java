package com.lvtu.wechat.common.quartz;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public abstract class BaseQuartzJobBean extends QuartzJobBean{
    
    public static Logger logger = Logger.getLogger(BaseQuartzJobBean.class);
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //使quartz能使用spring的注解
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        beforeInvoke();
        invoke();
        afterInvoke();
    }


    protected void afterInvoke() {
        logger.info("================" + this.getClass().getName() + ":quartz end===========");
    }

    protected void beforeInvoke() {
        logger.info("================" + this.getClass().getName() + ":quartz start===========");
    }


    public abstract void invoke();
}

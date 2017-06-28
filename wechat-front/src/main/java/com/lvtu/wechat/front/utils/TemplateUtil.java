package com.lvtu.wechat.front.utils;

import org.apache.log4j.Logger;

import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.productorder.PushRecords;
import com.lvtu.wechat.common.service.productorder.IPushRecordsService;
import com.lvtu.wechat.common.spring.SpringBeanProxy;

/** 
* @ClassName: TemplateUtil 
* @Description: 模板消息工具类
* @author zhengchongxiang
* @date 2016-9-1 上午10:14:30  
*/
public class TemplateUtil {
	
    private static Logger logger = Logger.getLogger(TemplateUtil.class);
    
    private static IPushRecordsService pushRecordsService = SpringBeanProxy.getBean(IPushRecordsService.class, "pushRecordsService");
    
    /** 
    * @Title: updatePushRecords 
    * @Description: 更新发送记录状态 
    * @param @param openid
    * @param @param msgId
    * @param @param status 
    * @return void    返回类型 
    * @throws 
    */
	public static String updatePushRecords(String openid, String msgId,String status) {
		try {
			PushRecords pushRecords = pushRecordsService.selectByMsgId(msgId);
			if (null != pushRecords && pushRecords.getId() != null) {
				pushRecords.setStatus(status);
				if (status.equals("success")) {
					pushRecords.setSuccess(CommonType.SEND_SUCCESS.getValue());// 推送成功
				} else {
					pushRecords.setSuccess(CommonType.SEND_FAILED.getValue());// 推送失败
				}
				logger.info(openid +"=msg=" + msgId+ "=" + status);
				pushRecordsService.update(pushRecords);//更新推送状态
			}
		} catch (Exception e) {
			logger.info("用户" + openid + "=msg=" + msgId + "status=" + status + "出现异常信息");
			e.printStackTrace();
		}
		return "success";
	}

}
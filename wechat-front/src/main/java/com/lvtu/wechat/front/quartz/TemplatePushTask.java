package com.lvtu.wechat.front.quartz;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisCluster;

import com.alibaba.fastjson.JSONObject;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.template.TemplateRecords;
import com.lvtu.wechat.common.quartz.BaseQuartzJobBean;
import com.lvtu.wechat.common.service.template.ITemplateService;
import com.lvtu.wechat.common.utils.TemplatesUtils;

public class TemplatePushTask extends BaseQuartzJobBean{
	
	@Autowired
	private ITemplateService templateService;
	
	@Autowired
	JedisCluster jedisCluster;

	/* (非 Javadoc) 
	* <p>Title: invoke</p> 
	* <p>Description: 模板消息推送定时任务  
	* @see com.lvtu.wechat.common.quartz.BaseQuartzJobBean#invoke() 
	*/
	@Override
	public void invoke() {
		List<TemplateRecords> list = templateService.selectByRetry();
		if(null != list && list.size()>0){
			for(TemplateRecords templateRecords : list){
				if(null != templateRecords && templateRecords.getContent()!=null){
					String result = TemplatesUtils.sendMessage(templateRecords.getContent());
					JSONObject obj = (JSONObject) JSONObject.parse(result);
					if (obj != null && "0".equals(obj.getString("errcode"))) {
						templateRecords.setSuccess(CommonType.SEND_SUCCESS.getValue());						
					}
					templateService.updateTemplateRecords(templateRecords);
				}
			}
		}
	}

}

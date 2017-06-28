package com.lvtu.wechat.front.quartz;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisCluster;

import com.lvtu.wechat.common.model.activity.flowActivity.FlowForwardNumber;
import com.lvtu.wechat.common.quartz.BaseQuartzJobBean;
import com.lvtu.wechat.common.service.activity.flow.IFlowActivityService;
import com.lvtu.wechat.common.utils.Constants;

public class FlowActivityTask extends BaseQuartzJobBean{
	
	@Autowired
	private IFlowActivityService flowActivityService;
	
	@Autowired
	JedisCluster jedisCluster;

	/* (非 Javadoc) 
	* <p>Title: invoke</p> 
	* <p>Description: 裂变流量活动定时任务  
	* @see com.lvtu.wechat.common.quartz.BaseQuartzJobBean#invoke() 
	*/
	@Override
	public void invoke() {
		Log.info("裂变流量活动定时任务执行");
		List<String> listIds = flowActivityService.queryActivityIds();
		if (null == listIds || listIds.size() < 1) {
			return;
		}
		for (String id : listIds) {
			String num = jedisCluster.get(Constants.WX_FLOW_FROWARD_NUM + id);
			if (StringUtils.isNotBlank(num)) {
				FlowForwardNumber flowForwardNumber = flowActivityService
						.queryForwardNumByActivityId(id);
				if (null != flowForwardNumber) {
					flowForwardNumber.setNumber(flowForwardNumber.getNumber()
							+ Integer.parseInt(num));
				} else {
					flowForwardNumber = new FlowForwardNumber();
					flowForwardNumber.setFlowActivityId(id);
					flowForwardNumber.setNumber(Integer.parseInt(num));
				}
				if (flowActivityService.saveOrUpdateForward(flowForwardNumber)) {
					jedisCluster.del(Constants.WX_FLOW_FROWARD_NUM + id);//保存成功后清除缓存
					Log.info("forwardNum保存成功");
				}
			}
		}
	}

}

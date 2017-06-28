package com.lvtu.wechat.common.vo.back;

import com.lvtu.wechat.common.base.BaseCondition;


/** 
* @ClassName: FlowGroupsConditionVo 
* @Description: 
* @author zhengchongxiang
* @date 2016-11-4 下午2:18:12  
*/
public class FlowGroupsConditionVo extends BaseCondition {

	private static final long serialVersionUID = -6593475806187736159L;
		
	private String flowActivityId;
	
	private String openid;
	
	public String getFlowActivityId() {
		return flowActivityId;
	}

	public void setFlowActivityId(String flowActivityId) {
		this.flowActivityId = flowActivityId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}


	
	
	

	
}

package com.lvtu.wechat.common.vo.back;

import com.lvtu.wechat.common.base.BaseCondition;

/**
 * @author zhengchongxiang
 */
public class FlowMembersConditionVo extends BaseCondition {


	private static final long serialVersionUID = 1125656816708622703L;
	
	private String activityId;
	
	private String groupId;
	
	private String openid;
	
	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	

}

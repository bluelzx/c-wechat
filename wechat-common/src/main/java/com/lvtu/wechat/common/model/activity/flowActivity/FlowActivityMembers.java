package com.lvtu.wechat.common.model.activity.flowActivity;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class FlowActivityMembers extends BaseModel{


	private static final long serialVersionUID = 6299187880130741648L;
	
	private String nickname;
	
	private String openid;
	
	private Integer flowNum;
	
	private String subscribe;
	
	private String groupId;
	
	private String isLeader;
	
	private String isComplete;
	
	private Date startTime;
	
	private Date completeTime;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getFlowNum() {
		return flowNum;
	}

	public void setFlowNum(Integer flowNum) {
		this.flowNum = flowNum;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(String isLeader) {
		this.isLeader = isLeader;
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	
	
}

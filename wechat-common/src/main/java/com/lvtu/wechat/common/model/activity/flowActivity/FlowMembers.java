package com.lvtu.wechat.common.model.activity.flowActivity;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class FlowMembers extends BaseModel{
	
 
	private static final long serialVersionUID = -7184638530882879653L;
	
	private String activityId;

	private String groupId;
	
	private String openid;
	
	private Integer isLeader;
	
	private Integer flowNum;
	
	private String back;
	
	private Date createTime;
	
	private String nickname;
	
	private String headImgUrl;
	
	private String doleCopy;

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

	public Integer getIsLeader() {
		return isLeader;
	}

	public void setIsLeader(Integer isLeader) {
		this.isLeader = isLeader;
	}

	public Integer getFlowNum() {
		return flowNum;
	}

	public void setFlowNum(Integer flowNum) {
		this.flowNum = flowNum;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getDoleCopy() {
		return doleCopy;
	}

	public void setDoleCopy(String doleCopy) {
		this.doleCopy = doleCopy;
	}
	

}

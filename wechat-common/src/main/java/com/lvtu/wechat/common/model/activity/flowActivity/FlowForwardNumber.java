package com.lvtu.wechat.common.model.activity.flowActivity;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class FlowForwardNumber extends BaseModel {

	/** 
	* @Fields serialVersionUID : TODO 
	*/ 
	private static final long serialVersionUID = 9131838871562786322L;
	
	private String flowActivityId;
	
	private Integer number;
	
	private Date createTime;

	public String getFlowActivityId() {
		return flowActivityId;
	}

	public void setFlowActivityId(String flowActivityId) {
		this.flowActivityId = flowActivityId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	

}

package com.lvtu.wechat.common.model.activity.flowActivity;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class FlowActivityGroups extends BaseModel{


	private static final long serialVersionUID = 1578629604510277219L;
	
	private String name;
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer totalPeople;
	
	private Integer totalGroups;
	
	private Integer completeGroups;
	
	private Integer forwardNum;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getTotalPeople() {
		return totalPeople;
	}

	public void setTotalPeople(Integer totalPeople) {
		this.totalPeople = totalPeople;
	}

	public Integer getTotalGroups() {
		return totalGroups;
	}

	public void setTotalGroups(Integer totalGroups) {
		this.totalGroups = totalGroups;
	}

	public Integer getCompleteGroups() {
		return completeGroups;
	}

	public void setCompleteGroups(Integer completeGroups) {
		this.completeGroups = completeGroups;
	}

	public Integer getForwardNum() {
		return forwardNum;
	}

	public void setForwardNum(Integer forwardNum) {
		this.forwardNum = forwardNum;
	}
	
	
	

}

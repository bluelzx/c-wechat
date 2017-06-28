package com.lvtu.wechat.common.model.activity.flowActivity;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class FlowGroups extends BaseModel{


	private static final long serialVersionUID = 8635855758231498915L;
	
	private String flowActivityId;
	
	private Integer totalNum;
	
	private Integer nowNum;
	
	private Integer totalFlow;
	
	private Integer surplusFlow;
	
	private String openid;
	
	private Date startTime;
	
	private Date completeTime;
	
	private Integer state;
	
	private String back;
	
	private Date createTime;
	
	private String isShow;

	public String getFlowActivityId() {
		return flowActivityId;
	}

	public void setFlowActivityId(String flowActivityId) {
		this.flowActivityId = flowActivityId;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Integer getNowNum() {
		return nowNum;
	}

	public void setNowNum(Integer nowNum) {
		this.nowNum = nowNum;
	}

	public Integer getTotalFlow() {
		return totalFlow;
	}

	public void setTotalFlow(Integer totalFlow) {
		this.totalFlow = totalFlow;
	}

	public Integer getSurplusFlow() {
		return surplusFlow;
	}

	public void setSurplusFlow(Integer surplusFlow) {
		this.surplusFlow = surplusFlow;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	
	
}

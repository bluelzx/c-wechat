package com.lvtu.wechat.common.model.activity.flowActivity;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class FlowActivity extends BaseModel{
	

	private static final long serialVersionUID = 3292451810112837718L;

	private String name;
	
	private String state;
	
	private Date startDate;
	
	private Date endDate;
	
	private String shareTemplateId;
		
	private Integer totalFlow;
	
	private Integer teamNum;
	
	private String indexTips;
	
	private String ruleCopy;
	
	private String writeTips;
	
	private String qrCodeCopy;
	
	private String back;
	
	private Date createTime;
	
	private String picUrl;
	
	private String qrCodeUrl;
	
	private String bannerUrl;
	
	private String doleOne;
	
	private String doleTwo;
	
	private String doleThree;
	
	private String doleFour;
	
	private String doleFive;
	
	private String indexUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getShareTemplateId() {
		return shareTemplateId;
	}

	public void setShareTemplateId(String shareTemplateId) {
		this.shareTemplateId = shareTemplateId;
	}

	public Integer getTotalFlow() {
		return totalFlow;
	}

	public void setTotalFlow(Integer totalFlow) {
		this.totalFlow = totalFlow;
	}

	public Integer getTeamNum() {
		return teamNum;
	}

	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}

	public String getIndexTips() {
		return indexTips;
	}

	public void setIndexTips(String indexTips) {
		this.indexTips = indexTips;
	}

	public String getRuleCopy() {
		return ruleCopy;
	}

	public void setRuleCopy(String ruleCopy) {
		this.ruleCopy = ruleCopy;
	}

	public String getWriteTips() {
		return writeTips;
	}

	public void setWriteTips(String writeTips) {
		this.writeTips = writeTips;
	}

	public String getQrCodeCopy() {
		return qrCodeCopy;
	}

	public void setQrCodeCopy(String qrCodeCopy) {
		this.qrCodeCopy = qrCodeCopy;
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

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getDoleOne() {
		return doleOne;
	}

	public void setDoleOne(String doleOne) {
		this.doleOne = doleOne;
	}

	public String getDoleTwo() {
		return doleTwo;
	}

	public void setDoleTwo(String doleTwo) {
		this.doleTwo = doleTwo;
	}

	public String getDoleThree() {
		return doleThree;
	}

	public void setDoleThree(String doleThree) {
		this.doleThree = doleThree;
	}

	public String getDoleFour() {
		return doleFour;
	}

	public void setDoleFour(String doleFour) {
		this.doleFour = doleFour;
	}

	public String getDoleFive() {
		return doleFive;
	}

	public void setDoleFive(String doleFive) {
		this.doleFive = doleFive;
	}	
	
	public String getIndexUrl() {
		return indexUrl;
	}

	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}

	public String getDoleCopy(Integer doleNum) {
		if (doleNum == 1) {
			return doleOne;
		} else if (doleNum == 2) {
			return doleTwo;
		} else if (doleNum == 3) {
			return doleThree;
		} else if (doleNum == 4) {
			return doleFour;
		} else {
			return doleFive;
		}
	}
	
	
}

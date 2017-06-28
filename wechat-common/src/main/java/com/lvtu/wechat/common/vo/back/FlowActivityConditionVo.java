package com.lvtu.wechat.common.vo.back;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseCondition;

/**
 * @author zhengchongxiang
 */
public class FlowActivityConditionVo extends BaseCondition {

	private static final long serialVersionUID = 6980218439691903873L;
	
	private String id;
	
	private String name;
	
	private String state;
	
	private Date startDate;
	
	private Date endDate;
	
	private String shareTemplateId;
		
	private Integer totalFlow;
	
	private Integer teamNumber;
	
	private String indexTips;
	
	private String ruleCopy;
	
	private String writeTips;
	
	private String qrCodeCopy;
	
	private String back;
	
	private Date createTime;
	
	private String openid;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public Integer getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(Integer teamNumber) {
		this.teamNumber = teamNumber;
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
		
	
}

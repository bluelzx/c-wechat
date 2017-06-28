package com.lvtu.wechat.common.model.activity.signflow;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 微信签到记录
 * 
 * @author xuyao
 * 
 */
public class SignInRecord extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 微信openid
	 */
	private String openid;

	/**
	 * 签到时间
	 */
	private Date signTime;
	
	/**
	 * 签到增加的流量
	 */
	private Integer addedFlow;

	/**
	 * 连续签到次数
	 */
	private Integer continuousSignCount;

	/**
	 * 额外奖励
	 */
	private Boolean additionalAward;
	
	/**
	 * 备注
	 */
	private String remark;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Integer getAddedFlow() {
		return addedFlow;
	}

	public void setAddedFlow(Integer addedFlow) {
		this.addedFlow = addedFlow;
	}

	public Integer getContinuousSignCount() {
		return continuousSignCount;
	}

	public void setContinuousSignCount(Integer continuousSignCount) {
		this.continuousSignCount = continuousSignCount;
	}

	public Boolean getAdditionalAward() {
		return additionalAward;
	}

	public void setAdditionalAward(Boolean additionalAward) {
		this.additionalAward = additionalAward;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
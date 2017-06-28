package com.lvtu.wechat.common.model.activity.flowrecharge;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class FlowRechargeRecordInfo extends BaseModel {
	
	 /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5854826851325799729L;
    
    /**
     * 充值活动名称
     */
    private String name;
    
    /**
     * 用户的openid
     */
    private String openid;
    
	
	/**
	 * 用户昵称
	 */
	private String nickname;
    
    /**
     * 充值流量
     */
    private Integer rechargeFlow;
    
    /**
     * 操作人
     */
    private String operator;
    
    
    private String flowRechargeId;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    private Date updateDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getRechargeFlow() {
		return rechargeFlow;
	}

	public void setRechargeFlow(Integer rechargeFlow) {
		this.rechargeFlow = rechargeFlow;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getFlowRechargeId() {
		return flowRechargeId;
	}

	public void setFlowRechargeId(String flowRechargeId) {
		this.flowRechargeId = flowRechargeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
    
}

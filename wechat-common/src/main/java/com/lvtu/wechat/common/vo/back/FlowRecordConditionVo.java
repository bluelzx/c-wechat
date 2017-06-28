package com.lvtu.wechat.common.vo.back;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseCondition;

public class FlowRecordConditionVo extends BaseCondition{
	
	 /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5547425995565616561L;

    /**
     * id
     */
    private String id;
    
    /**
     * name
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
     * 充值活动表的Id
     */
    private String flowRechargeId;
    
    /**
     * 操作人
     */
    private String operator;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private Date createDate;

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

	public String getFlowRechargeId() {
		return flowRechargeId;
	}

	public void setFlowRechargeId(String flowRechargeId) {
		this.flowRechargeId = flowRechargeId;
	}

}

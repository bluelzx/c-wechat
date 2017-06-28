package com.lvtu.wechat.common.model.activity.flowrecharge;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class FlowRechargeRecord extends BaseModel {
	
	 /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5854826851325799729L;
    
    
    /**
     * 用户的openid
     */
    private String openid;
    
    /**
     * 充值流量
     */
    private Integer rechargeFlow;
    
    /**
     * 操作人
     */
    private String flowRechargeId;
    
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    private Date updateDate;
    

	
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}


	public Integer getRechargeFlow() {
		return rechargeFlow;
	}

	public void setRechargeFlow(Integer rechargeFlow) {
		this.rechargeFlow = rechargeFlow;
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
}

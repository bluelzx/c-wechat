package com.lvtu.wechat.common.model.activity.signflow;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 微信流量
 * @author xuyao
 *
 */
public class Flow extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3264402920305867171L;

	/**
	 * 微信openid
	 */
	private String openid;

	/**
	 * 总共获得的流量
	 */
	private Integer totalFlow;

	/**
	 * 剩余可兑换流量
	 */
	private Integer surplusFlow;
	
	 /**
     * 充值流量
     */
    private Integer rechargeFlow;
	
	/**
	 * 是否已经领取过首次赠送流量
	 */
	private boolean gotFirstFlow = false;
	
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
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

	public boolean isGotFirstFlow() {
		return gotFirstFlow;
	}

	public void setGotFirstFlow(boolean gotFirstFlow) {
		this.gotFirstFlow = gotFirstFlow;
	}
	
	public Integer getRechargeFlow() {
        return rechargeFlow;
    }

    public void setRechargeFlow(Integer rechargeFlow) {
        this.rechargeFlow = rechargeFlow;
    }

    /**
	 * 增加流量
	 * @param flow
	 */
	public void addFlow(int flow) {
		this.totalFlow = this.totalFlow + flow;
		this.surplusFlow = this.surplusFlow + flow;
	}
}

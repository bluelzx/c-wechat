package com.lvtu.wechat.common.model.activity.signflow;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 微信流量兑换记录
 * @author xuyao
 *
 */
public class FlowExchange extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 微信openid
	 */
    private String openid;

    /**
     * 手机号码
     */
    private String phoneNum;
    
    /**
     * 运营商
     */
    private String operator;

    /**
     * 兑换时间
     */
    private Date exchangeTime;

    /**
     * 兑换流量
     */
    private Integer exchangeFlow;
    
    /**
     * 接口第三方回调时间
     */
    private Date returnTime;
    
    /**
     * 第三方回调返回码
     */
    private String returnCode;
    
    /**
     * 第三方回调返回信息
     */
    private String returnDesc;
    
    /**
     * 是否成功
     */
    private Boolean succeed;
    
    public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public Integer getExchangeFlow() {
        return exchangeFlow;
    }

    public void setExchangeFlow(Integer exchangeFlow) {
        this.exchangeFlow = exchangeFlow;
    }

    public Boolean getSucceed() {
        return succeed;
    }

    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}

}
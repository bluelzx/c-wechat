package com.lvtu.wechat.common.model.activity.signflow;


import com.lvtu.wechat.common.base.BaseModel;

/**
 * 微信流量兑换记录
 * @author xuyao
 *
 */
public class FlowPartner extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 合作商名
	 */
    private String partnerName;

    /**
     * 合作商对应的url
     */
    private String partnerUrl;
    
    private Integer usedFlag;

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getPartnerUrl() {
		return partnerUrl;
	}

	public void setPartnerUrl(String partnerUrl) {
		this.partnerUrl = partnerUrl;
	}

	public Integer getUsedFlag() {
		return usedFlag;
	}

	public void setUsedFlag(Integer usedFlag) {
		this.usedFlag = usedFlag;
	}





  
}
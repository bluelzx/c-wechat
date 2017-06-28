package com.lvtu.wechat.common.model.activity.discount;

import com.lvtu.wechat.common.base.BaseModel;
public class DiscountCoupon extends BaseModel{
	
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = -3325988235792895369L;
	
	/**
	 * 优惠券名称
	 */
	private String name;
	
	/**
	 * 优惠券批次号
	 */
	private String couponCodes;
	
	/**
	 * 发放量 
	 */
	private Integer paymentAmount;
	
	/**
	 * 根据条件查询发放量
	 * 
	 */
	private String total;
	
	
	/**
     * 扩展字段
     */
    private String back;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getCouponCodes() {
		return couponCodes;
	}

	public void setCouponCodes(String couponCodes) {
		this.couponCodes = couponCodes;
	}
	
	
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Integer getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Integer paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}
	
}

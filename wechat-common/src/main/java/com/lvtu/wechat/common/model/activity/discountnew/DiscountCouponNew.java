package com.lvtu.wechat.common.model.activity.discountnew;

import com.lvtu.wechat.common.base.BaseModel;
import java.util.Date;
public class DiscountCouponNew extends BaseModel{
	
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
	 * 
	 * 券金额
	 */
	private String couponMoney;
	
	/**
	 * 券使用条件
	 * 
	 */
	private String couponCondition;
	
	/**
	 * 
	 * 券有效期
	 */
	private String couponTime;
	
	/**
	 * 
	 * 券url
	 */
	private String couponUrl;
	
	/**
	 * 发放量 
	 */
	private Integer paymentAmount;
	
	/**
	 * 
	 * 状态 1-新增 2-发布 3-预删除 4-删除
	 */
	private String state;
	
	/**
	 * 
	 * 顺序
	 */
	private String currOrder;
	
	/**
	 * 
	 * 下一个顺序状态
	 */
	private Integer nextOrder;
	
	/**
	 * 根据条件查询发放量
	 * 
	 */
	private String total;
	
	/**
	 * 
	 * 创建时间
	 */
	 private Date createTime;
	 
	 
	 /**
	  * 是否已经获取
	  */
	 private String isAquire;
	
	/**
	 * 排序规则
	 */
	 private String orderBy;
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
	
	
	public String getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(String couponMoney) {
		this.couponMoney = couponMoney;
	}

	public String getCouponCondition() {
		return couponCondition;
	}

	public void setCouponCondition(String couponCondition) {
		this.couponCondition = couponCondition;
	}

	public String getCouponTime() {
		return couponTime;
	}

	public void setCouponTime(String couponTime) {
		this.couponTime = couponTime;
	}

	public String getCouponUrl() {
		return couponUrl;
	}

	public void setCouponUrl(String couponUrl) {
		this.couponUrl = couponUrl;
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
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCurrOrder() {
		return currOrder;
	}

	public void setCurrOrder(String currOrder) {
		this.currOrder = currOrder;
	}

	public Integer getNextOrder() {
		return nextOrder;
	}

	public void setNextOrder(Integer nextOrder) {
		this.nextOrder = nextOrder;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public String getIsAquire() {
		return isAquire;
	}

	public void setIsAquire(String isAquire) {
		this.isAquire = isAquire;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	
}

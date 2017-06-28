package com.lvtu.wechat.common.model.activity.discount;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;
public class DiscountGet extends BaseModel {
	
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = -2123888235792888743L;
	
	/**
	 * 
     * 用户ID
     */
    private String openid;
    /**
     * 
     * 驴妈妈账号
     */
    private String lvmamaUserno;
    
    /**
     * 
     * 活动编号
     */
    private String discountActivityid;
    
    /**
     * 
     * 优惠券编号
     */
    private String discountCouponid;
    
    /**
     * 
     * 优惠券号
     */
    private String discountCouponcode;
    
    /**
	 * 创建时间 
	 */
	private Date createDate;
	
	 /**
     * 扩展字段
     */
    private String back;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	
	public String getLvmamaUserno() {
		return lvmamaUserno;
	}

	public void setLvmamaUserno(String lvmamaUserno) {
		this.lvmamaUserno = lvmamaUserno;
	}

	public String getDiscountActivityid() {
		return discountActivityid;
	}

	public void setDiscountActivityid(String discountActivityid) {
		this.discountActivityid = discountActivityid;
	}

	public String getDiscountCouponid() {
		return discountCouponid;
	}

	public void setDiscountCouponid(String discountCouponid) {
		this.discountCouponid = discountCouponid;
	}

	public String getDiscountCouponcode() {
		return discountCouponcode;
	}

	public void setDiscountCouponcode(String discountCouponcode) {
		this.discountCouponcode = discountCouponcode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}
    
    
}

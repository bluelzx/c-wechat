package com.lvtu.wechat.common.model.activity.coupon;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class CouponRecord extends BaseModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4413425948317701431L;

	/**
     * 卡券ID
     */
    private String couponId;

    /**
     * 活动ID
     */
    private String couponActId;

    /**
     * 用户ID
     */
    private String openid;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 是否为新用户
     */
    private Integer isNew;

    /**
     * 手机号
     */
    private String mobile;
    
    /**
     * 扩展字段1
     */
    private String back1;

    /**
     * 扩展字段2
     */
    private String back2;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponActId() {
        return couponActId;
    }

    public void setCouponActId(String couponActId) {
        this.couponActId = couponActId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public String getBack1() {
        return back1;
    }

    public void setBack1(String back1) {
        this.back1 = back1;
    }

    public String getBack2() {
        return back2;
    }

    public void setBack2(String back2) {
        this.back2 = back2;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
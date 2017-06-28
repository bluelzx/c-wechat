package com.lvtu.wechat.common.model.activity.coupon;

import com.lvtu.wechat.common.base.BaseModel;

public class Coupon extends BaseModel {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4707889969930521455L;

    /**
     * 卡券活动ID
     */
    private String couponActId;

    /**
     * 优惠券码
     */
    private String couponCode;

    /**
     * 是否已经被领取完
     */
    private Integer state;
    
    /**
     * oldState
     */
    private Integer stateHis;
    
    /**
     * 扩展字段1
     */
    private String back1;

    /**
     * 扩展字段2
     */
    private String back2;

    public String getCouponActId() {
        return couponActId;
    }

    public void setCouponActId(String couponActId) {
        this.couponActId = couponActId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Integer getStateHis() {
        return stateHis;
    }

    public void setStateHis(Integer stateHis) {
        this.stateHis = stateHis;
    }

}

package com.lvtu.wechat.common.model.activity.h5coupon;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class H5CouponRecord extends BaseModel{

    /**
     * 
     */
    private static final long serialVersionUID = -3556453632207659210L;
    
    
    /**
     * 活动ID
     */
    private String h5CouponActId;
    
    
    /**
     * 优惠券码
     */
    private String h5CouponCode;
    
    /**
     * 用户ID
     */
    private String openid;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 手机号
     */
    private String mobile;
    
    /**
     * 驴妈妈用户userNO
     */
    private String lvmamaUserNo;
    
    /**
     * 扩展字段1
     */
    private String back1;

    /**
     * 扩展字段2
     */
    private String back2;

    public String getH5CouponActId() {
        return h5CouponActId;
    }

    public void setH5CouponActId(String h5CouponActId) {
        this.h5CouponActId = h5CouponActId;
    }
    
    public String getH5CouponCode() {
        return h5CouponCode;
    }

    public void setH5CouponCode(String h5CouponCode) {
        this.h5CouponCode = h5CouponCode;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLvmamaUserNo() {
        return lvmamaUserNo;
    }

    public void setLvmamaUserNo(String lvmamaUserNo) {
        this.lvmamaUserNo = lvmamaUserNo;
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
    
    
    
}

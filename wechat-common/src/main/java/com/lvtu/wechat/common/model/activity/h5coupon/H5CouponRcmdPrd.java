package com.lvtu.wechat.common.model.activity.h5coupon;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 微信H5优惠券活动-推荐活动类
 * @author qianqc
 *
 */
public class H5CouponRcmdPrd extends BaseModel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3522352433329613295L;
    
    /**
     * H5Coupon id
     */
    private String h5CouponId;
    
    /**
     * 推荐产品类型
     */
    private String rcmdPrdType;
    
    /**
     * 推荐产品Id
     */
    private String rcmdPrdId;
    
    /**
     * 扩展字段
     */
    private String back;

    public String getH5CouponId() {
        return h5CouponId;
    }

    public void setH5CouponId(String h5CouponId) {
        this.h5CouponId = h5CouponId;
    }

    public String getRcmdPrdType() {
        return rcmdPrdType;
    }

    public void setRcmdPrdType(String rcmdPrdType) {
        this.rcmdPrdType = rcmdPrdType;
    }

    public String getRcmdPrdId() {
        return rcmdPrdId;
    }

    public void setRcmdPrdId(String rcmdPrdId) {
        this.rcmdPrdId = rcmdPrdId;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
    
    
}

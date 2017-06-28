package com.lvtu.wechat.common.service.activity.coupon;

import java.util.Map;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.coupon.Coupon;
import com.lvtu.wechat.common.model.activity.coupon.CouponActivity;
import com.lvtu.wechat.common.model.activity.coupon.CouponRecord;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.vo.back.CouponActConditionVo;

/**
 * 给驴粉发券码活动表
 * 
 * @author qianqc
 */
@RemoteService("couponActivityService")
public interface ICouponActivityService {
    /**
     * 获取所有流量包信息
     * 
     * @return
     */
    public PageInfo<CouponActivity> queryCouponActivityList(CouponActConditionVo couponActConditionVo);

    /**
     * 获取发券码活动信息
     */
    public CouponActivity queryCouponActivityById(String couponActId);

    /**
     * 查询coupon_record表
     * 
     * @param mobile
     * @param couponActivity
     * @return
     */
    CouponRecord queryCouponReocrd(CouponRecord couponRecord);

    /**
     * 插入表coupon_record
     * 
     * @param couponRecord
     */
    void insertCouponRecord(CouponRecord couponRecord);

    /**
     * 根据活动ID查询可以发放的券码
     */
    Coupon queryCoupon(Coupon coupon);

    /**
     * 更新Coupon表
     * 
     * @param couponRecord
     */
    boolean updateCoupon(Coupon coupon, CouponRecord couponRecord);

    /**
     * 更新状态是否可用
     * 
     * @param couponactivity
     */
    public void updateState(CouponActivity couponActivity);

    /**
     * 领取券码
     * 
     * @param mobile
     * @param couponActivity
     * @param result
     * @param isNewUser
     * @return
     */
    public Map<String, Object> aquireCoupon(String mobile, CouponActivity couponActivity, Map<String, Object> result,
        boolean isNewUser, WechatUser wechatUser);

    
    /**
     * 保存活动
     * @param couponActivityform
     * @param coupons
     */
    public void saveCouponActivityform(CouponActivity couponActivityform, List<Coupon> coupons);

    /**
     * 增加页面浏览量
     * @param couponActId
     */
    public void addPageView(String couponActId);

    /**
     * 针对微信用户，判断是否已经领取过了
     * @param queryParam
     * @param result
     * @return
     */
    public Map<String, Object> isAquiredCoupon(CouponRecord queryParam, Map<String, Object> result);

}

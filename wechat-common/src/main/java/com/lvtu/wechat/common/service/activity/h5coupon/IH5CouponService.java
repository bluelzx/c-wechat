package com.lvtu.wechat.common.service.activity.h5coupon;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.h5coupon.H5Coupon;
import com.lvtu.wechat.common.model.activity.h5coupon.H5CouponRcmdPrd;
import com.lvtu.wechat.common.model.activity.h5coupon.H5CouponRecord;
import com.lvtu.wechat.common.vo.back.H5CouponConditionVo;

/**
 * H5优惠券活动
 * 
 * @author qianqc & wxlizhi 
 */
@RemoteService("h5CouponService")
public interface IH5CouponService {

    /**
     * 分页查询H5Coupon
     * @param h5CouponConditionVo
     * @return
     */
    PageInfo<H5Coupon> queryH5CouponList(H5CouponConditionVo h5CouponConditionVo);

    /**
     * 更改H5优惠券活动状态
     * @param h5Coupon
     */
    void changeState(H5Coupon h5Coupon);


    /**
     * 根据活动ID获取活动信息
     * @param h5couponActId
     * @return
     */
    H5Coupon queryH5CouponActivityById(String h5couponActId);


    /**
     * 查询H5优惠券活动
     * @param h5Coupon
     * @return
     */
    H5Coupon queryH5Coupon(H5Coupon h5Coupon);

    /**
     * 添加或者删除优惠券活动
     * @param h5Coupon
     */
    void saveOrUpdate(H5Coupon h5Coupon);
    
    /**
     * 查询该手机号参加H5优惠券活动
     * @param mobile
     * @param activityId
     * @return
     */
    List<H5CouponRecord> queryH5CouponReocrd(String mobile, String activityId);

    /**
     * 插入H5优惠券的领取信息
     * @param h5CouponRecord
     */
    void insertH5CouponRecord(H5CouponRecord h5CouponRecord);

    /**
     * 根据活动ID查询产品信息
     * @param id
     * @return
     */
    List<H5CouponRcmdPrd> queryH5CouponRcmdPrdById(String h5CouponId);

    /**
     * 针对微信用户，判断是否已经领取
     * @param queryParam
     * @return
     */
    List<H5CouponRecord> isAquiredH5Coupon(H5CouponRecord queryParam);
    
}

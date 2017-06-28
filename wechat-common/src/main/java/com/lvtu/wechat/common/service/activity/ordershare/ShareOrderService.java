package com.lvtu.wechat.common.service.activity.ordershare;

import java.util.List;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.ordershare.ShareOrderCoupon;
import com.lvtu.wechat.common.model.activity.ordershare.ShareOrderCouponHis;

/**
 * 微信订单分享 相关服务类接口
 * @author xuyao
 *
 */
@RemoteService("shareOrderService")
public interface ShareOrderService {
	
	/**
	 * 更具驴妈妈订单ID抽取优惠券
	 * @param orderId 驴妈妈订单号
	 * @param openid 微信openid
	 * @return
	 */
	public ShareOrderCoupon getRandomCoupon(String orderId, String openid);
	
	/**
	 * 获取所有优惠券批次号信息
	 * @return
	 */
	public List<ShareOrderCoupon> getAllCoupons();
	
	/**
	 * 删除优惠券信息
	 * @param id
	 */
	public void deleteCoupon(String id);
	
	/**
	 * 新增优惠券
	 * @param coupon
	 */
	public void addCoupon(ShareOrderCoupon coupon);
	
	/**
	 * 根据驴妈妈订单ID获取领取记录
	 * @param orderId 驴妈妈订单号
	 * @return
	 */
	public List<ShareOrderCouponHis> getCouponRecords(String orderId);

	/**
	 * 获取用户领取某个订单的优惠券数据
	 * @param orderId 驴妈妈订单号
	 * @param openid 微信openid
	 * @return
	 */
	public ShareOrderCouponHis getCouponRecord(String orderId, String openid);
	
}

package com.lvtu.wechat.front.vo;

import com.lvmama.comm.pet.po.mark.MarkCoupon;
import com.lvmama.comm.pet.po.mark.MarkCouponCode;

/**
 * 优惠券值对象
 * 
 * @author xuyao
 *
 */
public class CouponVO {
	/**
	 * 优惠券活动
	 */
	private MarkCoupon coupon;

	/**
	 * 优惠券码
	 */
	private MarkCouponCode couponCode;

	public CouponVO(MarkCoupon coupon, MarkCouponCode couponCode) {
		this.coupon = coupon;
		this.couponCode = couponCode;
	}

	public MarkCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(MarkCoupon coupon) {
		this.coupon = coupon;
	}

	public MarkCouponCode getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(MarkCouponCode couponCode) {
		this.couponCode = couponCode;
	}
}
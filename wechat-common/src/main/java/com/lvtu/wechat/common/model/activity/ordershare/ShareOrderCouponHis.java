package com.lvtu.wechat.common.model.activity.ordershare;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;
import com.lvtu.wechat.common.model.weixin.WechatUser;

/**
 * 微信分享优惠券领取记录
 * 
 * @author xuyao
 *
 */
public class ShareOrderCouponHis extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9086440551320111490L;

	/**
	 * 订单ID
	 */
	private String orderId;

	/**
	 * 微信用户
	 */
	private WechatUser wxUser;

	/**
	 * ShareOrderCoupon
	 */
	private ShareOrderCoupon coupon;

	/**
	 * 领取时间
	 */
	private Date createTime;

	public ShareOrderCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(ShareOrderCoupon coupon) {
		this.coupon = coupon;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public WechatUser getWxUser() {
		return wxUser;
	}

	public void setWxUser(WechatUser wxUser) {
		this.wxUser = wxUser;
	}
}
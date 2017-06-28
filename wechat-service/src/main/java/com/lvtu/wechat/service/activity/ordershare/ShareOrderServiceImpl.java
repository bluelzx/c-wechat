package com.lvtu.wechat.service.activity.ordershare;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.ordershare.ShareOrderCoupon;
import com.lvtu.wechat.common.model.activity.ordershare.ShareOrderCouponHis;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.ordershare.ShareOrderService;
import com.lvtu.wechat.dao.activity.ordershare.dao.ShareOrderCouponDAO;
import com.lvtu.wechat.dao.activity.ordershare.dao.ShareOrderCouponHisDAO;

@HessianService("shareOrderService")
@Service("shareOrderService")
public class ShareOrderServiceImpl implements ShareOrderService {

	Logger logger = Logger.getLogger(ShareOrderServiceImpl.class);

	@Autowired
	protected ShareOrderCouponHisDAO couponHisDao;

	@Autowired
	protected ShareOrderCouponDAO couponDao;

	@Override
	public ShareOrderCoupon getRandomCoupon(String orderId, String openid) {
		// 订单和用户都存在，进行优惠券抽取
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("delFlag", "0");
		List<ShareOrderCoupon> coupons = couponDao.selectByConditions(conditions);
		if (coupons == null || coupons.size() == 0)
			return null;

		// 检查优惠券中奖几率是否正确
		Integer totalProbability = 0;
		for (ShareOrderCoupon coupon : coupons)
			totalProbability += coupon.getProbability();
		if (totalProbability != 100) {
			logger.error("优惠券中奖几率有误！");
			return null;
		}

		// 根据概率抽取优惠券
		int randomNum = new Random().nextInt(100);
		int tmpProbabilityCount = 0;
		ShareOrderCoupon targetCoupon = null;
		for (ShareOrderCoupon coupon : coupons) {
			tmpProbabilityCount += coupon.getProbability();
			if (randomNum <= tmpProbabilityCount) {
				targetCoupon = coupon;
				// 记录写入数据库
				ShareOrderCouponHis couponHis = new ShareOrderCouponHis();
				couponHis.preInsert();
				couponHis.setCoupon(coupon);
				couponHis.setCreateTime(new Date());
				couponHis.setOrderId(orderId);
				WechatUser wxUser = new WechatUser();
				wxUser.setOpenid(openid);
				couponHis.setWxUser(wxUser);
				couponHisDao.insertSelective(couponHis);
				break;
			}
		}
		return targetCoupon;
	}

	@Override
	public List<ShareOrderCouponHis> getCouponRecords(String orderId) {
		if (StringUtils.isNotBlank(orderId)) {
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("orderId", orderId);
			return couponHisDao.selectByConditions(conditions);
		}
		return null;
	}

	@Override
	public ShareOrderCouponHis getCouponRecord(String orderId, String openid) {
		// 判断订单是否存在
		if (StringUtils.isNotBlank(orderId)) {
			Map<String, Object> conditions = new HashMap<String, Object>();
			conditions.put("orderId", orderId);
			conditions.put("openid", openid);
			List<ShareOrderCouponHis> couponHiss = couponHisDao.selectByConditions(conditions);
			if (couponHiss != null && couponHiss.size() > 0) {
				return couponHiss.get(0);
			}
		}
		return null;
	}

	@Override
	public List<ShareOrderCoupon> getAllCoupons() {
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("delFlag", "0");
		List<ShareOrderCoupon> coupons = couponDao.selectByConditions(conditions);
		return coupons;
	}

	@Override
	public void deleteCoupon(String id) {
		if (StringUtils.isNotBlank(id))
			couponDao.deleteById(id);
	}

	@Override
	public void addCoupon(ShareOrderCoupon coupon) {
		coupon.preInsert();
		couponDao.addCoupon(coupon);
	}
}

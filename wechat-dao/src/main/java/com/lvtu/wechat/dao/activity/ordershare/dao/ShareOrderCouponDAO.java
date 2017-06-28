package com.lvtu.wechat.dao.activity.ordershare.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.ordershare.ShareOrderCoupon;


@Repository
public class ShareOrderCouponDAO extends BaseIbatisDAO {

	public ShareOrderCouponDAO() {
		super("SHARE_ORDER_COUPON");
	}

	/**
	 * 根据条件查询优惠券
	 * 
	 * @param condition
	 * @return
	 */
	public List<ShareOrderCoupon> selectByConditions(Map<String, Object> condition) {
		return super.getList("selectByConditions", condition);
	}
	
	/**
	 * 通过ID删除优惠券信息
	 * @param id
	 */
	public void deleteById(String id) {
		super.update("delete", id);
	}
	
	/**
	 * 添加流量包
	 * @param coupon
	 */
	public void addCoupon(ShareOrderCoupon coupon) {
		super.insert("insert", coupon);
	}
}
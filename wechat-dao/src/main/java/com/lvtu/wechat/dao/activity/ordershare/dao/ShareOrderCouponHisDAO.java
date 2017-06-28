package com.lvtu.wechat.dao.activity.ordershare.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.ordershare.ShareOrderCouponHis;

@Repository
public class ShareOrderCouponHisDAO extends BaseIbatisDAO {

	public ShareOrderCouponHisDAO() {
		super("SHARE_ORDER_COUPON_HIS");
	}

	/**
	 * 根据条件查询用户领取记录
	 * 
	 * @param condition
	 * @return
	 */
	public List<ShareOrderCouponHis> selectByConditions(Map<String, Object> condition) {
		return super.getList("selectByConditions", condition);
	}

	/**
	 * 插入领取记录
	 * @param couponHis
	 * @return
	 */
	public ShareOrderCouponHis insertSelective(ShareOrderCouponHis couponHis) {
		return (ShareOrderCouponHis) super.insert("insertSelective", couponHis);
	}

	/**
	 * 根据主键ID查询领取记录
	 * @param id
	 * @return
	 */
	public ShareOrderCouponHis selectByPrimaryKey(String id) {
		return super.get("selectByPrimaryKey", id);
	}
}
package com.lvtu.wechat.dao.activity.coupon.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.coupon.Coupon;

/**
 * 
 * @author wxlizhi
 *
 */
@Repository
public class CouponDAO extends BaseIbatisDAO {
    public CouponDAO() {
        super("COUPON");
    }

	/**
	 * 插入兑券码
	 * @param list
	 */
	public void insert(List<Coupon> list) {
		super.insert("insert", list);
	}
	
	public void update(Coupon coupon) {
		super.update("update", coupon);
	}
	
	/**
	 * 根据活动ID查询兑券码数量
	 * @param id
	 * @return
	 */
	public Integer selectCouponNum(String id) {
		return super.get("selectCouponNum",id);
	}

	/**
	 * 更新coupon
	 * @param coupon
	 * @return
	 */
    public Integer updateCoupon(Coupon coupon) {
        return super.update("updateCoupon", coupon);
    }
    /**
     * 查询可用的coupon
     * @param coupon
     * @return
     */
    public Coupon queryUseableCoupon(Coupon coupon) {
        return super.get("queryUseableCoupon", coupon);
    }
	
    /**
     * 根据ID查询优惠券
     * @param coupon
     * @return
     */
    public Coupon queryCouponById(Coupon coupon) {
        return super.get("queryCouponById", coupon);
    }
}

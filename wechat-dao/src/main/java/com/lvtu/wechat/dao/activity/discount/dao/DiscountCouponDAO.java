package com.lvtu.wechat.dao.activity.discount.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.discount.DiscountCoupon;
import com.lvtu.wechat.common.model.activity.discount.DiscountGet;
import com.lvtu.wechat.common.vo.back.DiscountActivityConditionVo;

@Repository
public class DiscountCouponDAO extends BaseIbatisDAO {
	
	public DiscountCouponDAO(){
		super("DISCOUNT_COUPON");
	}
	
	/**
	 * 
	 * 380优惠券的活动统计
	 * @param discountActivityConditionVo
	 * @return
	 */
	public List<DiscountCoupon> queryDiscountCouponList(DiscountActivityConditionVo discountActivityConditionVo) {
        return super.getList("queryDiscountCouponList", discountActivityConditionVo);
    }

	
    /**
     * 针对微信用户，判断是否已经领取优惠券
     * @param discountGet
     * @return
     */
    public List<String> isAquiredDiscount(DiscountGet discountGet) {
        return super.getList("isAquiredDiscount", discountGet);
    }

    /**
     * 根据条件查询优惠券
     * @param discountCoupon
     * @return
     */
    public DiscountCoupon queryDiscountCoupon(DiscountCoupon discountCoupon) {
        return super.get("queryDiscountCoupon", discountCoupon);
    }

    /**
     * 插入优惠券领取记录
     * @param discountGet
     */
    public void insertDiscountGet(DiscountGet discountGet) {
        super.insert("insertDiscountGet", discountGet);
    }

    
    /**
     * 查询所有优惠券的批次号
     * @return
     */
    public List<String> queryDiscountCouponCodes() {
        return super.getList("queryDiscountCouponCodes");
    }

    /**
     * 优惠券发放量+1
     * @param discountGet
     */
    public void updateDiscountPaymentAmount(DiscountGet discountGet) {
        super.update("updateDiscountPaymentAmount", discountGet);
    }

}

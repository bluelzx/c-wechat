package com.lvtu.wechat.dao.activity.coupon.dao;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.coupon.CouponRecord;

/**
 * coupon_record表dao层
 * @author qianqc
 *
 */
@Repository
public class CouponRecordDAO extends BaseIbatisDAO {
    public CouponRecordDAO() {
        super("COUPON_RECORD");
    }

    /**
     * 查询coupon_record表
     * @param couponRecord
     * @return
     */
    public CouponRecord queryCouponRecord(CouponRecord couponRecord) {
        return super.get("queryCouponRecord", couponRecord);
    }

    /**
     * 插入表coupon_record表
     * @param couponRecord
     */
    public void insertCouponRecord(CouponRecord couponRecord) {
        super.insert("insertCouponRecord", couponRecord);
    }
    
    public Integer queryCouponRecordCount(CouponRecord couponRecord) {
        return super.get("queryCouponRecordCount", couponRecord);
    }
}

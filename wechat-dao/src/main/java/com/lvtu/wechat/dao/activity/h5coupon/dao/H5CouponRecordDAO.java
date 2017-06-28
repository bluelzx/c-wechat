package com.lvtu.wechat.dao.activity.h5coupon.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.h5coupon.H5CouponRecord;

/**
 * h5coupon_record的dao层
 * @author wxlizhi
 *
 */
@Repository
public class H5CouponRecordDAO extends BaseIbatisDAO{

    public H5CouponRecordDAO() {
        super("H5COUPON_RECORD");
    }

    /**
     * 根据手机号和H5优惠券活动ID，查询是否领取过
     * @param h5CouponRecord
     * @return
     */
    public List<H5CouponRecord> queryH5CouponRecord(H5CouponRecord h5CouponRecord) {
        return super.getList("queryH5CouponRecord", h5CouponRecord);
    }

    /**
     * 插入H5优惠券活动的领取信息
     * @param h5CouponRecord
     * @return
     */
    public void insertH5CouponRecord(H5CouponRecord h5CouponRecord) {
        super.insert("insertH5CouponRecord", h5CouponRecord);
    }

    /**
     * 针对微信用户，判断是否已经领取
     * @param queryParam
     * @return
     */
    public List<H5CouponRecord> isAquiredH5Coupon(H5CouponRecord queryParam) {
        return super.getList("isAquiredH5Coupon", queryParam);
    }

}

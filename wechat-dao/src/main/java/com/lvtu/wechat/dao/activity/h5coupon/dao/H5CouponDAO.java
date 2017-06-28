package com.lvtu.wechat.dao.activity.h5coupon.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.h5coupon.H5Coupon;
import com.lvtu.wechat.common.vo.back.H5CouponConditionVo;

/**
 * h5coupon dao层
 * @author qianqc
 *
 */
@Repository
public class H5CouponDAO extends BaseIbatisDAO {
    
    public H5CouponDAO() {
        super("H5COUPON");
    }
    
    /**
     * 插入
     * @param h5Coupon
     */
    public void insert(H5Coupon h5Coupon) {
        super.insert("insert", h5Coupon);
    }
    
    /**
     * 更新
     * @param h5Coupon
     */
    public Integer update(H5Coupon h5Coupon) {
        return super.update("update", h5Coupon);
    }
    
    /**
     * 查询单条记录
     * @param h5Coupon
     * @return
     */
    public H5Coupon select(H5Coupon h5Coupon) {
        return super.get("select", h5Coupon);
    }
    
    
    /**
     * 分页查询 H5Coupon
     */
    public List<H5Coupon> selectList(H5CouponConditionVo h5CouponConditionVo) {
        return super.getList("select", h5CouponConditionVo);
    }
    
    /**
     * 根据活动ID查询活动信息
     * @param h5couponActId
     * @return
     */
    public H5Coupon queryH5CouponActivityById(String h5couponActId) {
        return super.get("queryH5CouponActivity", h5couponActId);
    }
}

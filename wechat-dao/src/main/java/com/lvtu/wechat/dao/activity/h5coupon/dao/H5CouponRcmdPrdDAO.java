package com.lvtu.wechat.dao.activity.h5coupon.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.h5coupon.H5CouponRcmdPrd;

/**
 * h5coupon_rcmd_prd dao层
 * @author qianqc
 *
 */
@Repository
public class H5CouponRcmdPrdDAO extends BaseIbatisDAO {
    
    public H5CouponRcmdPrdDAO() {
        super("H5COUPON_RCMD_PRD");
    }
    
    /**
     * 插入
     * @param h5Coupon
     */
    public void insert(H5CouponRcmdPrd h5CouponRcmdPrd) {
        super.insert("insert", h5CouponRcmdPrd);
    }
    
    /**
     * 更新
     * @param h5Coupon
     */
    public Integer update(H5CouponRcmdPrd h5CouponRcmdPrd) {
        return super.update("update", h5CouponRcmdPrd);
    }
    
    /**
     * 查询集合
     * @param h5Coupon
     * @return
     */
    public List<H5CouponRcmdPrd> selectList(H5CouponRcmdPrd h5CouponRcmdPrd) {
        return super.getList("selectList", h5CouponRcmdPrd);
    }
    
    /**
     * 删除
     * @param h5CouponRcmdPrd
     */
    public void delete(H5CouponRcmdPrd h5CouponRcmdPrd) {
        super.delete("delete", h5CouponRcmdPrd);
    }
    
    /**
     * 根据H5CouponId删除
     * @param h5CouponRcmdPrd
     */
    public void deleteByH5CouponId(String h5CouponId) {
        super.delete("deleteByH5CouponId", h5CouponId);
    }
    
    /**
     * 根据活动ID查询产品信息
     * @param id
     * @return
     */
    public List<H5CouponRcmdPrd> queryH5CouponRcmdPrdById(String h5CouponId) {
        return super.getList("queryH5CouponRcmdPrdById", h5CouponId);
    }

}

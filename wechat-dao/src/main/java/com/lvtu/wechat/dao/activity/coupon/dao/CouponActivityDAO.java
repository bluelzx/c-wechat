package com.lvtu.wechat.dao.activity.coupon.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.coupon.CouponActivity;
import com.lvtu.wechat.common.vo.back.CouponActConditionVo;

/**
 * coupon_act表dao层
 * @author qianqc
 *
 */
@Repository
public class CouponActivityDAO extends BaseIbatisDAO {
    public CouponActivityDAO() {
        super("COUPON_ACTIVITY");
    }
    
    /**
     * 查询coupon_act数组
     * @param couponActConditionVo
     * @return
     */
    public List<CouponActivity> queryCouponActivityList(CouponActConditionVo couponActConditionVo) {
        return super.getList("queryCouponActivityList", couponActConditionVo);
    }
    
    /**
     * 更新发券码活动的状态
     * @param couponActivity
     */
    public void updateState(CouponActivity couponActivity) {
		super.update("updateState", couponActivity);
	}

    /**
     * 根据id查询coupon_act
     * @param couponActId
     * @return
     */
    public CouponActivity queryCouponActivityById(String couponActId) {
        return super.get("queryCouponActivity", couponActId);
    }

	/**
	 * 插入发券码活动
	 * @param couponActivityform
	 */
	public void insert(CouponActivity couponActivityform) {
		super.insert("insert", couponActivityform);
	}

	/**
	 * 更新发券码活动
	 * @param couponActivityform
	 */
	public void update(CouponActivity couponActivityform) {
		super.update("update", couponActivityform);
	}

    public void addPageView(String couponActId) {
        super.update("addPageView", couponActId);
    }
}

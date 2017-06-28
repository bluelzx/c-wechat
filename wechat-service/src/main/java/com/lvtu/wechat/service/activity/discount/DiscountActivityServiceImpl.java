package com.lvtu.wechat.service.activity.discount;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.discount.DiscountActivity;
import com.lvtu.wechat.common.model.activity.discount.DiscountCoupon;
import com.lvtu.wechat.common.model.activity.discount.DiscountGet;
import com.lvtu.wechat.common.service.activity.discount.IDiscountActivityService;
import com.lvtu.wechat.common.vo.back.DiscountActivityConditionVo;
import com.lvtu.wechat.dao.activity.discount.dao.DiscountActivityDAO;
import com.lvtu.wechat.dao.activity.discount.dao.DiscountCouponDAO;

@HessianService("discountActivitiesService")
@Service("discountActivitiesService")
@Transactional(readOnly = true)
public class DiscountActivityServiceImpl implements IDiscountActivityService{
	
	Logger logger = Logger.getLogger(DiscountActivityServiceImpl.class);
	 
	@Autowired
	private DiscountActivityDAO discountActivityDAO;
	
	@Autowired
	private DiscountCouponDAO discountCouponDAO;

	/**
	 * 380优惠券的活动管理添加
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveDiscountActivity(DiscountActivity discountActivity) {
		if (!StringUtils.isBlank(discountActivity.getId())) {
            Date now = new Date();
            discountActivity.setUpdateTime(now);
            discountActivityDAO.updatediscountActivity(discountActivity);
        }else{
        	  discountActivity.preInsert();
        	  Date now = new Date();
              discountActivity.setUpdateTime(now);
              discountActivityDAO.insertdiscountActivity(discountActivity);
        }
		
	}
	
	/**
	 * 
	 * 380优惠券的活动统计
	 */
	@Override
	public PageInfo<DiscountCoupon> queryDiscountCouponList(DiscountActivityConditionVo discountActivityConditionVo) {
		  PageHelper.startPage(discountActivityConditionVo.getPage(), discountActivityConditionVo.getPageSize());
	      List<DiscountCoupon> discountCouponList = discountCouponDAO.queryDiscountCouponList(discountActivityConditionVo);
	      PageInfo<DiscountCoupon> pageInfo = new PageInfo<DiscountCoupon>(discountCouponList);
	      return pageInfo;
	}

	
	/**
     * 针对微信用户，判断是否已经领取优惠券
     * @param discountGet
     * @return
     */
    @Override
    public List<String> isAquiredDiscount(DiscountGet discountGet) {
        return discountCouponDAO.isAquiredDiscount(discountGet);
    }

    
    /**
     * 查询符合条件的优惠券
     * @param discountCoupon
     * @return
     */
    @Override
    public DiscountCoupon queryDiscountCoupon(DiscountCoupon discountCoupon) {
        return discountCouponDAO.queryDiscountCoupon(discountCoupon);
    }

    /**
     * 插入优惠券领取记录
     * @param discountGet
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void insertDiscountGet(DiscountGet discountGet) {
        discountCouponDAO.insertDiscountGet(discountGet);
        discountCouponDAO.updateDiscountPaymentAmount(discountGet);
    }

    /**
     * 编辑活动的查询
     * 
     */
    @Override
    public DiscountActivity selectDiscountActivity(DiscountActivity discountActivity) {
    	DiscountActivity discountActivityselect = discountActivityDAO.selectDiscountActivity(discountActivity);
        return discountActivityselect;
    }
    
    /**
     * 查询380优惠券活动
     * @return
     */
    @Override
    public DiscountActivity queryDiscountActivity() {
        return discountActivityDAO.queryDiscountActivity();
    }

    
    /**
     * 查询所有优惠券的批次号
     * @return
     */
    @Override
    public List<String> queryDiscountCouponCodes() {
        return discountCouponDAO.queryDiscountCouponCodes();
    }

}

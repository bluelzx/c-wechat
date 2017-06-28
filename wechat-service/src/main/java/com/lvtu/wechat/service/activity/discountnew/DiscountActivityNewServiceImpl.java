package com.lvtu.wechat.service.activity.discountnew;

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
import com.lvtu.wechat.common.model.activity.discountnew.DiscountActivityNew;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountCouponNew;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountGetNew;
import com.lvtu.wechat.common.service.activity.discountnew.IDiscountActivityNewService;
import com.lvtu.wechat.common.vo.back.DiscountActivityConditionNewVo;
import com.lvtu.wechat.dao.activity.discountnew.dao.DiscountActivityNewDAO;
import com.lvtu.wechat.dao.activity.discountnew.dao.DiscountCouponNewDAO;

@HessianService("discountActivitiesNewService")
@Service("discountActivitiesNewService")
@Transactional(readOnly = true)
public class DiscountActivityNewServiceImpl implements IDiscountActivityNewService{
	
	Logger logger = Logger.getLogger(DiscountActivityNewServiceImpl.class);
	 
	@Autowired
	private DiscountActivityNewDAO discountActivityNewDAO;
	
	@Autowired
	private DiscountCouponNewDAO discountCouponNewDAO;
	
	
	/**
	 * 380优惠券的活动管理添加
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveDiscountActivityNew(DiscountActivityNew discountActivityNew) {
		if (!StringUtils.isBlank(discountActivityNew.getId())) {
            Date now = new Date();
            discountActivityNew.setUpdateTime(now);
            discountActivityNewDAO.updatediscountActivityNew(discountActivityNew);
        }else{
        	  discountActivityNew.preInsert();
        	  Date now = new Date();
        	  discountActivityNew.setUpdateTime(now);
        	  discountActivityNewDAO.insertdiscountActivityNew(discountActivityNew);
        }
		
	}
	
	/**
	 * 
	 * 380优惠券的活动统计
	 */
	@Override
	public PageInfo<DiscountCouponNew> queryDiscountCouponNewList(DiscountActivityConditionNewVo discountActivityConditionNewVo) {
		  PageHelper.startPage(discountActivityConditionNewVo.getPage(), discountActivityConditionNewVo.getPageSize());
	      List<DiscountCouponNew> discountCouponNewList = discountCouponNewDAO.queryDiscountCouponNewList(discountActivityConditionNewVo);
	      PageInfo<DiscountCouponNew> pageInfo = new PageInfo<DiscountCouponNew>(discountCouponNewList);
	      return pageInfo;
	}

	
    
    /**
     * 查询符合条件的优惠券
     * @param discountCoupon
     * @return
     */
    @Override
    public DiscountCouponNew queryDiscountCouponNew(DiscountCouponNew discountCouponNew) {
        return discountCouponNewDAO.queryDiscountCouponNew(discountCouponNew);
    }

    /**
     * 插入优惠券领取记录
     * @param discountGet
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void insertDiscountGetNew(DiscountGetNew discountGetNew) {
        discountCouponNewDAO.insertDiscountGetNew(discountGetNew);
        discountCouponNewDAO.updateDiscountNewPaymentAmount(discountGetNew);
    }

    /**
     * 编辑活动的查询
     * 
     */
    @Override
    public DiscountActivityNew selectDiscountActivityNew(DiscountActivityNew discountActivityNew) {
    	DiscountActivityNew discountActivityNewselect = discountActivityNewDAO.selectDiscountActivityNew(discountActivityNew);
        return discountActivityNewselect;
    }
    
    /**
     * 查询380优惠券活动activity
     * @return
     */
    @Override
    public DiscountActivityNew queryDiscountActivityNew() {
        return discountActivityNewDAO.queryDiscountActivityNew();
    }

    
    /**
     * 查询所有优惠券的批次号
     * @return
     */
    @Override
    public List<String> queryDiscountNewCouponCodes() {
        return discountCouponNewDAO.queryDiscountNewCouponCodes();
    }

	@Override
	@Transactional(readOnly = false)
	public Object deleteDiscountCouponNew(String id) {
		return discountCouponNewDAO.deleteDiscountCouponNew(id);
		
	}

    /**
     * 
     * 新增添加优惠券
     */
	@Override
	@Transactional(readOnly = false)
	public void saveDiscountCouponNew(DiscountCouponNew discountCouponNew) {
		if(StringUtils.isBlank(discountCouponNew.getId())) {
			  discountCouponNew.preInsert();
        	  Date now = new Date();
        	  discountCouponNew.setCreateTime(now);
        	  discountCouponNewDAO.saveDiscountCouponNew(discountCouponNew);
		}
		else {
			 discountCouponNewDAO.updateDiscountCouponNew(discountCouponNew);
		 }
	}

	/**
	 * 编辑优惠券，按照id
	 */
	@Override
	@Transactional(readOnly = false)
	public DiscountCouponNew queryDiscountCouponByid(String id) {

		DiscountCouponNew discountCouponNew = discountCouponNewDAO.queryDiscountCouponByid(id);
		return discountCouponNew;
	}

    /**
     * 活动页面点击保存，保存优惠券，修改nextOrder
     */
	@Override
	@Transactional(readOnly = false)
	public void updateNextOrder(List<DiscountCouponNew> list) {
		for (DiscountCouponNew discountCoupon : list) {
			discountCouponNewDAO.updateNextOrder(discountCoupon);
		}
	}

	/**
	 * 点击发布，修改currOrder状态
	 */
	@Override
	@Transactional(readOnly = false)
	public void changeCurrOrder(List<DiscountCouponNew> list) {
		for (DiscountCouponNew discountCoupon : list) {
			discountCouponNewDAO.updateState(discountCoupon);
			discountCouponNewDAO.updateCurrOrder(discountCoupon);
		}
	}
    
	/**
	 * 
	 * 活动管理界面，根据id恢复
	 */
	@Override
	@Transactional(readOnly = false)
	public void recoveryDiscountCouponNew(String id) {
		 discountCouponNewDAO.recoveryDiscountCouponNew(id);
	}

	/**
	 * 
	 * 查询所有380优惠券
	 */
	@Override
	@Transactional(readOnly = false)
	public List<DiscountCouponNew> queryallDiscountCouponNew(DiscountCouponNew discountCoupon) {
		return discountCouponNewDAO.queryallDiscountCouponNew(discountCoupon);
	}

	/**
	 * 查询用户是否领取优惠券，没有则插入优惠券
	 */
	@Override
	@Transactional(readOnly = false)
	public List<String> queryDiscountGet(DiscountGetNew discountGet) {
		return discountCouponNewDAO.queryDiscountGet(discountGet);
	}

	/**
	 * 获取用户已领取的所有的优惠券
	 */
	@Override
	@Transactional(readOnly = false)
	public List<DiscountGetNew> getAquireCoupons(DiscountGetNew discountGet) {
		return discountCouponNewDAO.getAquireCoupons(discountGet);
	}

	/**
	 * 查询状态为1 , 2的优惠券
	 */
	@Override
	@Transactional(readOnly = false)
	public List<DiscountCouponNew> querycouponOneTwoList(DiscountCouponNew discountCoupon) {
		return discountCouponNewDAO.querycouponOneTwoList(discountCoupon);
	}
    
	/**
	 * 
	 * 查询状态为 3 , 4的优惠券
	 */
	@Override
	public List<DiscountCouponNew> querycouponThreeFourList(DiscountCouponNew discountCoupon) {
		return discountCouponNewDAO.querycouponThreeFourList(discountCoupon);
	}


}

package com.lvtu.wechat.dao.activity.discountnew.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountCouponNew;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountGetNew;
import com.lvtu.wechat.common.vo.back.DiscountActivityConditionNewVo;

@Repository
public class DiscountCouponNewDAO extends BaseIbatisDAO {
	
	public DiscountCouponNewDAO(){
		super("DISCOUNT_NEW_COUPON");
	}
	
	/**
	 * 
	 * 380优惠券的活动统计
	 * @param discountActivityConditionVo
	 * @return
	 */
	public List<DiscountCouponNew> queryDiscountCouponNewList(DiscountActivityConditionNewVo discountActivityConditionNewVo) {
        return super.getList("queryDiscountCouponNewList", discountActivityConditionNewVo);
    }

	
    /**
     * 根据条件查询优惠券
     * @param discountCoupon
     * @return
     */
    public DiscountCouponNew queryDiscountCouponNew(DiscountCouponNew discountCouponNew) {
        return super.get("queryDiscountCouponNew", discountCouponNew);
    }

    /**
     * 插入优惠券领取记录
     * @param discountGet
     */
    public void insertDiscountGetNew(DiscountGetNew discountGetNew) {
        super.insert("insertDiscountGetNew", discountGetNew);
    }

    
    /**
     * 查询所有优惠券的批次号
     * @return
     */
    public List<String> queryDiscountNewCouponCodes() {
        return super.getList("queryDiscountNewCouponCodes");
    }

    /**
     * 优惠券发放量+1
     * @param discountGet
     */
    public void updateDiscountNewPaymentAmount(DiscountGetNew discountGetNew) {
        super.update("updateDiscountNewPaymentAmount", discountGetNew);
    }

    /**
     * 
    * @Title: deleteDiscountCouponNew 
    * @Description: TODO(删除优惠券修改satae为3) 
    * @param @param couponCodes
    * @param @return    设定文件 
    * @return Object    返回类型 
    * @throws
     */
	public Object deleteDiscountCouponNew(String couponCodes) {
		return super.update("deleteDiscountCouponNew", couponCodes);
	}

	/**
	* @Title: addDiscountCouponNew 
	* @Description: TODO(添加优惠券) 
	* @param @param discountCouponNew    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void saveDiscountCouponNew(DiscountCouponNew discountCouponNew) {
		super.insert("saveDiscountCouponNew", discountCouponNew);
	}

	/**
	 * 
	* @Title: queryDiscountCouponByCouponCodes 
	* @Description: TODO(根据优惠券码进行编辑) 
	* @param @param couponCodes
	* @param @return    设定文件 
	* @return DiscountCouponNew    返回类型 
	* @throws
	 */
	public DiscountCouponNew queryDiscountCouponByid(String id) {

		return super.get("queryDiscountCouponByid", id);
	}
    
	/**
	 * 
	* @Title: updateDiscountCouponNew 
	* @Description: TODO(编辑更改) 
	* @param @param discountCouponNew    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void updateDiscountCouponNew(DiscountCouponNew discountCouponNew) {
          super.update("updatediscountCouponNew", discountCouponNew);	
	}
    /**
     * 
    * @Title: updateNextOrder 
    * @Description: TODO(活动管理界面点击保存，修改nextOrder) 
    * @param @param discountCoupon    设定文件 
    * @return void    返回类型 
    * @throws
     */
	public void updateNextOrder(DiscountCouponNew discountCoupon) {
		super.update("updateNextOrder", discountCoupon);
	}

	/**
	 * 
	* @Title: updateState 
	* @Description: TODO(活动管理界面点击发布，修改state) 
	* @param @param discountCoupon    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void updateState(DiscountCouponNew discountCoupon) {

		super.update("updateState", discountCoupon);
	}

	/**
	 * 
	* @Title: updateCurrOrder 
	* @Description: TODO(活动管理界面点击发布，修改currOrder) 
	* @param @param discountCoupon    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void updateCurrOrder(DiscountCouponNew discountCoupon) {
		super.update("updateCurrOrder", discountCoupon);
	}

	/**
	 * 
	 * 
	* @Title: recoveryDiscountCouponNew 
	* @Description: TODO(根据id,实现点击恢复按钮) 
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void recoveryDiscountCouponNew(String id) {
		 super.update("recoveryDiscountCouponNew", id);
	}


	public List<DiscountCouponNew> queryallDiscountCouponNew(DiscountCouponNew discountCoupon) {
		return super.getList("queryallDiscountCouponNew", discountCoupon);
	}

	/**
	 * 
	 * 
	* @Title: queryDiscountGet 
	* @Description: TODO( 查询用户是否领取优惠券，没有则插入优惠券) 
	* @param @param discountGet
	* @param @return    设定文件 
	* @return List<String>    返回类型 
	* @throws
	 */
	public List<String> queryDiscountGet(DiscountGetNew discountGet) {
		return super.getList("queryDiscountGet",discountGet);
	}

	/**
	 * 
	* @Title: getAquireCoupons 
	* @Description: TODO(获取用户已领取的所有的优惠券) 
	* @param @param discountGet
	* @param @return    设定文件 
	* @return List<DiscountGetNew>    返回类型 
	* @throws
	 */
	public List<DiscountGetNew> getAquireCoupons(DiscountGetNew discountGet) {
		return super.getList("getAquireCoupons",discountGet);
	}
    
	/**
	 * 
	* @Title: querycouponOneTwoList 
	* @Description: TODO(查询状态为1 , 2的优惠券) 
	* @param @param discountCoupon
	* @param @return    设定文件 
	* @return List<DiscountCouponNew>    返回类型 
	* @throws
	 */
	public List<DiscountCouponNew> querycouponOneTwoList(DiscountCouponNew discountCoupon) {
		return super.getList("querycouponOneTwoList", discountCoupon);
	}
    
	/**
	 * 
	* @Title: querycouponThreeFourList 
	* @Description: TODO(查询状态为3  , 4的优惠券) 
	* @param @param discountCoupon
	* @param @return    设定文件 
	* @return List<DiscountCouponNew>    返回类型 
	* @throws
	 */
	public List<DiscountCouponNew> querycouponThreeFourList(DiscountCouponNew discountCoupon) {
		return super.getList("querycouponThreeFourList", discountCoupon);
	}
}

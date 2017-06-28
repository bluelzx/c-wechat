package com.lvtu.wechat.dao.activity.discount.dao;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.discount.DiscountActivity;


@Repository
public class DiscountActivityDAO extends BaseIbatisDAO{
	
	public DiscountActivityDAO() {
        super("DISCOUNT_ACTIVITY");
    }
	
	
	/**
	 * 
	 * 380优惠券的活动添加
	 * @param discountActivityform
	 */
	public void updatediscountActivity(DiscountActivity discountActivity){
		super.update("updatediscountActivity", discountActivity);
	}


    /**
     * 查询380优惠券活动
     * @return
     */
    public DiscountActivity queryDiscountActivity() {
        return super.get("queryDiscountActivity");
    }
	
	/**
	 * 优惠券编辑活动的查询
	 * @param discountActivity
	 * @return
	 */
	 public DiscountActivity selectDiscountActivity(DiscountActivity discountActivity) {
	        return super.get("selectDiscountActivity", discountActivity);
	    }
	 
	 /**
	  * 
	  * 优惠券为空时插入活动
	  * @param discountActivity
	  */
	 public void insertdiscountActivity(DiscountActivity discountActivity){
		 super.insert("insertdiscountActivity",discountActivity);
	 }
}

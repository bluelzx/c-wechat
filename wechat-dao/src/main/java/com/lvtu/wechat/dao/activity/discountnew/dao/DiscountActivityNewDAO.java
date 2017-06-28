package com.lvtu.wechat.dao.activity.discountnew.dao;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountActivityNew;


@Repository
public class DiscountActivityNewDAO extends BaseIbatisDAO{
	
	public DiscountActivityNewDAO() {
        super("DISCOUNT_NEW_ACTIVITY");
    }
	
	
	/**
	 * 
	 * 380优惠券的活动添加
	 * @param discountActivityform
	 */
	public void updatediscountActivityNew(DiscountActivityNew discountActivityNew){
		super.update("updatediscountActivityNew", discountActivityNew);
	}


    /**
     * 查询380优惠券活动
     * @return
     */
    public DiscountActivityNew queryDiscountActivityNew() {
        return super.get("queryDiscountActivityNew");
    }
	
	/**
	 * 优惠券编辑活动的查询
	 * @param discountActivity
	 * @return
	 */
	 public DiscountActivityNew selectDiscountActivityNew(DiscountActivityNew discountActivityNew) {
	        return super.get("selectDiscountActivityNew", discountActivityNew);
	    }
	 
	 /**
	  * 
	  * 优惠券为空时插入活动
	  * @param discountActivity
	  */
	 public void insertdiscountActivityNew(DiscountActivityNew discountActivityNew){
		 super.insert("insertdiscountActivityNew",discountActivityNew);
	 }

}

package com.lvtu.wechat.dao.productorder.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.productorder.ExportOrders;
import com.lvtu.wechat.common.model.productorder.ProductOrder;
import com.lvtu.wechat.common.vo.back.ProductOrderConditionVo;

/**
 * @author zhengchongxiang
 *
 */
@Repository
public class ProductOrderDao extends BaseIbatisDAO {
	
	public ProductOrderDao(){
		super("T_WX_PRODUCT_ORDER");
	}

	public ProductOrder insertProductOrder(ProductOrder productOrder){
		
		return (ProductOrder) super.insert("insertOrder", productOrder);
	}

	public List<ProductOrder> selectOrderByOpenid(String openid){
		
		return getListFree("selectOrderByOpenid",openid);		
	}
	
	public Integer updateOrder(ProductOrder productOrder){
		
		return update("updateOrderByOpenid",productOrder);
	}
	
	public List<ProductOrder> selectOrderByCondition(ProductOrderConditionVo vo){		
		return getList("selectOrderByCondition", vo);
	}
	
	public List<String> selectOpenidByType(ProductOrderConditionVo vo){
		return getList("selectOpenidByType", vo);
	}
	
	public List<ExportOrders> selectByExport(){
		
		return super.getList("selectByExport");
	}
	
	public ExportOrders selectCount(){
		return super.get("selectCount");
	}
	
	public void updateList(List<ProductOrderConditionVo> list){
		super.update("updateList", list);
	}
	
	public List<String> selectAllOpenids(){
		return super.getList("selectAllOpenids");
	}

}

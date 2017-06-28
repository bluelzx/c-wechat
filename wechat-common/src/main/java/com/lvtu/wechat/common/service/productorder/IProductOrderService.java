package com.lvtu.wechat.common.service.productorder;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.productorder.ExportOrders;
import com.lvtu.wechat.common.model.productorder.ProductOrder;
import com.lvtu.wechat.common.vo.back.ProductOrderConditionVo;

/**
 * @ClassName: IProductOrderService
 * @Description: 甩尾产品订阅service
 * @author zhengchongxiang
 * @date 2016-8-26 下午2:10:20
 */
@RemoteService("productOrderService")
public interface IProductOrderService {

	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或者更新用户订阅信息
	 * @param @param 订阅信息实体
	 * @param @return 操作结果
	 * @return boolean 返回类型
	 * @throws
	 */
	boolean saveOrUpdate(ProductOrder productOrder);

	/**
	 * @Title: selectOrderByOpenid
	 * @Description: 根据用户openid查询订阅产品信息
	 * @param @param openid
	 * @param @return 用户订阅详情
	 * @return ProductOrder 返回类型
	 * @throws
	 */
	public ProductOrder selectOrderByOpenid(String openid);

	/**
	 * @Title: selectOrderByCondition
	 * @Description: 条件查询甩尾产品用户订阅信息
	 * @param @param 查询条件
	 * @param @return 分页查询结果
	 * @return PageInfo<ProductOrder> 返回类型
	 */
	public PageInfo<ProductOrder> selectOrderByCondition(ProductOrderConditionVo vo);
	
	/** 
	* @Title: selectOpenidByType 
	* @Description: 根据订阅类型查询订阅者openid集合 
	* @param @param vo
	* @return List<String>    返回类型 
	*/
	public List<String> selectOpenidByType(ProductOrderConditionVo vo);
	
	/** 
	* @Title: selectByExport 
	* @Description: 查询所有订阅信息（导出） 
	* @return List<ExportOrders>    返回类型 
	*/
	public List<ExportOrders> selectByExport();
	
	/** 
	* @Title: selectCount 
	* @Description: 分别查询产品的订阅数量 
	* @param @return    设定文件 
	* @return ProductOrder    返回类型 
	* @throws 
	*/
	public ExportOrders selectCount();
	
	/** 
	* @Title: updateList 
	* @Description: 2.0批量更新地址信息
	* @param @param list    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void updateList(List<ProductOrderConditionVo> list);
	
	public List<String> selectAllOpenids();
	

}

package com.lvtu.wechat.service.activity.productorder;

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
import com.lvtu.wechat.common.model.productorder.ExportOrders;
import com.lvtu.wechat.common.model.productorder.ProductOrder;
import com.lvtu.wechat.common.service.productorder.IProductOrderService;
import com.lvtu.wechat.common.vo.back.ProductOrderConditionVo;
import com.lvtu.wechat.dao.productorder.dao.ProductOrderDao;


/** 
* @ClassName: ProductOrderServiceImpl 
* @Description: 甩尾产品订阅实现类
* @author zhengchongxiang
* @date 2016-9-5 下午5:28:58  
*/
@HessianService("productOrderService")
@Service("productOrderService")
@Transactional(readOnly = true)
public class ProductOrderServiceImpl implements IProductOrderService {

	Logger logger = Logger.getLogger(ProductOrderServiceImpl.class);

	@Autowired
	protected ProductOrderDao productOrderDao;

	/**
	 * 新增或修改订阅信息
	 * @param productOrder
	 */
	@Override
	@Transactional(readOnly = false)
	public boolean saveOrUpdate(ProductOrder productOrder) {
		if (StringUtils.isBlank(productOrder.getId())) {
			productOrder.preInsert();
			productOrder.setCreateTime(new Date());
			productOrder = productOrderDao.insertProductOrder(productOrder);
			if (StringUtils.isNotBlank(productOrder.getId()))
				return true;
		} else {
			Integer temp = productOrderDao.updateOrder(productOrder);
			if (temp > 0)
				return true;
		}
		return false;
	}

	/**
	 * 根据用户openid查询订阅产品信息
	 * @param openid
	 * @return
	 */
	@Override
	public ProductOrder selectOrderByOpenid(String openid) {
		if (null != productOrderDao.selectOrderByOpenid(openid)
				&& productOrderDao.selectOrderByOpenid(openid).size() > 0) {
			return productOrderDao.selectOrderByOpenid(openid).get(0);
		}
		return null;
	}

	@Override
	public PageInfo<ProductOrder> selectOrderByCondition(ProductOrderConditionVo vo) {
		PageHelper.startPage(vo.getPage(), vo.getPageSize());
		List<ProductOrder> list = productOrderDao.selectOrderByCondition(vo);
		PageInfo<ProductOrder> pageInfo = new PageInfo<ProductOrder>(list);
		return pageInfo;
	}

	@Override
	public List<String> selectOpenidByType(ProductOrderConditionVo vo) {
		List<String> list = productOrderDao.selectOpenidByType(vo);
		return list;
	}
	
	public List<ExportOrders> selectByExport(){
		List<ExportOrders> list = productOrderDao.selectByExport();
		return list;
	}

	@Override
	public ExportOrders selectCount() {
		return productOrderDao.selectCount();
	}

	@Override
	@Transactional(readOnly = false)
	public void updateList(List<ProductOrderConditionVo> list) {
		productOrderDao.updateList(list);
	}

	@Override
	public List<String> selectAllOpenids() {
		return productOrderDao.selectAllOpenids();
	}

}

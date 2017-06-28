package com.lvtu.wechat.front.web.productorder;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.JedisCluster;

import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.productorder.ProductOrder;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.productorder.IProductOrderService;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.MemcachedUtil;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.Areasutils;

/**
 * 甩尾产品订阅
 * @author zhengchongxiang 
 */
@Controller
@RequestMapping("/order")
@NeedOauth
public class ProductOrderController extends BaseController {

	@Autowired
	private IProductOrderService productOrderService;
	
	@Autowired
	private JedisCluster jedisCluster;

	/**
	 * 产品订阅首页
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		//获取当前登录用户信息
		WechatUser userInfo = getWechatUser(request);
		request.setAttribute("wxUser", userInfo);
		//查询用户订阅记录
		ProductOrder productOrder = productOrderService.selectOrderByOpenid(userInfo.getOpenid());
		if (productOrder != null) {
			request.setAttribute("productOrder", productOrder);
		}
		return "productOrder/index";
	}
	/**
	 * 执行订阅(取消订阅)
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "addOrder")
	public Object addOrder(HttpServletRequest request, HttpServletResponse response,String latitude,String longitude){
		boolean flag = false;
		Map<String, Object> result = getResultMap();
		WechatUser userInfo = getWechatUser(request);
		String out = request.getParameter("outboundTourism");
		String in = request.getParameter("inboundTourism");
		String ticket = request.getParameter("specialTicket");
		String homeTourism = request.getParameter("homeTourism");
		String hotTicket = request.getParameter("hotTicket");
		String openid = userInfo.getOpenid();
		String provinceName = userInfo.getProvince();
		String cityName = userInfo.getCity();
		MemcachedUtil mem = MemcachedUtil.getInstance();
		Map<String,Integer> map= (Map<String, Integer>) mem.get(Constants.WX_ORDER_AREAS_KEY+openid);
		Integer provinceId =0;
		Integer cityId = 0;
		if(null ==map){
			map = Areasutils.getAdd(longitude,latitude,provinceName,cityName);
			mem.set(Constants.WX_ORDER_AREAS_KEY+openid, map);
		}
		provinceId = map.get("provinceId");
		cityId = map.get("cityId");
		ProductOrder productOrder = productOrderService.selectOrderByOpenid(openid);
		if (productOrder == null) {
			productOrder = new ProductOrder();
		}
		productOrder.setProvinceId(provinceId);
		productOrder.setCityId(cityId);
		productOrder.setOpenid(userInfo.getOpenid());
		if (CommonType.SUBSCRIBE.getStringValue().equals(out)){
			productOrder.setOutboundTourism(true);
		}else{
			productOrder.setOutboundTourism(false);}
		if (CommonType.SUBSCRIBE.getStringValue().equals(in)){
			productOrder.setInboundTourism(true);
		}else{
			productOrder.setInboundTourism(false);}
		if (CommonType.SUBSCRIBE.getStringValue().equals(ticket)){
			productOrder.setSpecialTicket(true);
		}else{
			productOrder.setSpecialTicket(false);}
		if (CommonType.SUBSCRIBE.getStringValue().equals(homeTourism)){
			productOrder.setHomeTourism(true);
		}else{
			productOrder.setHomeTourism(false);}
		if (CommonType.SUBSCRIBE.getStringValue().equals(hotTicket)){
			productOrder.setHotTicket(true);
		}else{
			productOrder.setHotTicket(false);}
		flag = productOrderService.saveOrUpdate(productOrder);
		if (!flag) {
			result.put("code", -1);
		}
		logger.info("用户"+openid+"订阅产品"+productOrder+"结果="+result);
		return result;
	}

}

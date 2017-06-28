package com.lvtu.wechat.back.web.activity.ordershare;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.activity.ordershare.ShareOrderCoupon;
import com.lvtu.wechat.common.service.activity.ordershare.ShareOrderService;

/**
 * 签到送流量活动
 * @author xuyao
 *
 */
@Controller
@RequestMapping("${adminPath}/activity")
public class OrderShareAction extends BaseActionSupport {
	
	@Autowired
	private ShareOrderService shareOrderService;
	
	/**
	 * 流量包列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/orderShare/coupons")
	public String flowPacks(Model model) {
		model.addAttribute("coupons", shareOrderService.getAllCoupons());
		return "/activity/ordershare/coupons";
	}
	
	@RequestMapping("/orderShare/addCoupon")
	public String addCoupon(ShareOrderCoupon coupon, RedirectAttributes redirectAttrs) {
		if(beanValidator(redirectAttrs, coupon)) {
			shareOrderService.addCoupon(coupon);
			addMessage(redirectAttrs, "添加优惠券成功！");
		}
		return "redirect:" + adminPath + "/activity/orderShare/coupons";
	}
	
	/**
	 * 删除流量包
	 * @param id
	 * @param redirectAttrs
	 * @return
	 */
	@RequestMapping("/orderShare/delCoupon")
	public String delCoupon(String id, RedirectAttributes redirectAttrs) {
		if(StringUtils.isBlank(id)) {
			addMessage(redirectAttrs, "删除优惠券失败！");
		} else {
			shareOrderService.deleteCoupon(id);
			addMessage(redirectAttrs, "删除优惠券成功！");
		}
		return "redirect:" + adminPath + "/activity/orderShare/coupons";
	}
}

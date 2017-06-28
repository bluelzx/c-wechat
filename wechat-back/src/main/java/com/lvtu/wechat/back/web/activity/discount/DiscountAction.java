package com.lvtu.wechat.back.web.activity.discount;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.UploadImg;
import com.lvtu.wechat.common.model.activity.discount.DiscountActivity;
import com.lvtu.wechat.common.model.activity.discount.DiscountCoupon;
import com.lvtu.wechat.common.service.activity.discount.IDiscountActivityService;
import com.lvtu.wechat.common.vo.back.DiscountActivityConditionVo;

/**
 * 380优惠券
 * 
 * @author  majun
 */
@Controller
@RequestMapping("${adminPath}/activity/old")
public class DiscountAction extends BaseActionSupport {

	@Autowired
	private IDiscountActivityService discountActivitiesService;

	/**
	 * 
	 * 380优惠券初始页面
	 * 
	 */
	@RequestMapping("/discount/old")
	public String discount(DiscountActivity discountActivity,DiscountActivityConditionVo discountActivityConditionVo, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String tab = request.getParameter("tab");
		if (tab == null) {
			tabManage(discountActivity, model);
			tabStatistical(discountActivityConditionVo, model);
			model.addAttribute("tab", "tabManage");
		} else if (tab.equals("tabManage")) {
			tabManage(discountActivity, model);
			tabStatistical(dcNull(), model);
			model.addAttribute("tab", "tabManage");
		} else if (tab.equals("tabStatistical")) {
			tabStatistical(discountActivityConditionVo, model);
			model.addAttribute("tab", "tabStatistical");
		}
		return "/activity/discount/discountManager";
	}

	/**
	 * 活动管理
	 * 
	 * @param discountActivity
	 * @param model
	 */
	private void tabManage(DiscountActivity discountActivity, Model model) {
		DiscountActivity discountActivityselect = discountActivitiesService.selectDiscountActivity(discountActivity);
        model.addAttribute("discountActivity", discountActivityselect);
	}

	/**
	 * 
	 * 添加380优惠券活动
	 * 
	 */
	@RequestMapping("/discount/old/save")
	public String saveDiscountActivity(DiscountActivity discountActivity,Model model,
			MultipartHttpServletRequest request, HttpServletResponse response) {

		String discountname = request.getParameter("Name");
		String discounturl = request.getParameter("banner");

		discountActivity.setName(discountname);
		discountActivity.setBanner(discounturl);
		discountActivitiesService.saveDiscountActivity(discountActivity);

		return "redirect:" + adminPath + "/activity/discount";
	}

	/**
	 * 
	 * 活动管理--上传单个图片
	 * 
	 */
	@RequestMapping("/discount/old/imageUpload")
	@ResponseBody
	public Map<String, Object> imageUpload(Model model,
			MultipartHttpServletRequest request, HttpServletResponse response) {
		String fileDiv = "_product";
		int width = 750;// 宽度固定750
		int height = 260;// 高度固定260
		boolean isFixed = true;// 尺寸固定
		MultipartFile image = request.getFile("pushedMessageUpload");
		UploadImg uploadImg = new UploadImg();
		return uploadImg.imageUpload(width, height, isFixed, fileDiv, image);
	}

	/**
	 * 
	 * 活动统计
	 * 
	 * @param discountActivity
	 * @param model
	 */
	private void tabStatistical(DiscountActivityConditionVo discountActivityConditionVo, Model model) {
		model.addAttribute("conditionStatistical", discountActivityConditionVo);
		PageInfo<DiscountCoupon> pageInfo = discountActivitiesService.queryDiscountCouponList(discountActivityConditionVo);
		model.addAttribute("discountCouponListStatistical", pageInfo.getList());
		model.addAttribute("pageStatistical", pageInfo);
	}
	
	
	private DiscountActivityConditionVo dcNull() {
		DiscountActivityConditionVo dcNull = new DiscountActivityConditionVo();
		dcNull.setId(null);
		dcNull.setName(null);
		dcNull.setStartDate(null);
		dcNull.setEndDate(null);
		return dcNull;
	}

}

package com.lvtu.wechat.back.web.activity.discountnew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.UploadImg;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountActivityNew;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountCouponNew;
import com.lvtu.wechat.common.service.activity.discountnew.IDiscountActivityNewService;
import com.lvtu.wechat.common.vo.back.DiscountActivityConditionNewVo;

/**
 * 380优惠券
 * 
 * @author  majun
 */
@Controller
@RequestMapping("${adminPath}/activity")
public class DiscountNewAction extends BaseActionSupport {

	@Autowired
	private IDiscountActivityNewService discountActivitiesNewService;

	/**
	 * 
	 * 380优惠券初始页面
	 * 
	 */
	@RequestMapping("/discount")
	public String discount(DiscountActivityNew discountActivityNew,DiscountActivityConditionNewVo discountActivityConditionNewVo, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String tab = request.getParameter("tab");
		if (tab == null || tab.equals("tabManage")) {
			tabManage(discountActivityNew, discountActivityConditionNewVo, model);
			model.addAttribute("tab", "tabManage");
		} else {
			tabStatistical(discountActivityConditionNewVo, model);
			model.addAttribute("tab", "tabStatistical");
		}
		return "/activity/discountnew/discountNewManager";
	}

	/**
	 * 活动管理
	 * 
	 * @param discountActivity
	 * @param model
	 */
	private void tabManage(DiscountActivityNew discountActivityNew, DiscountActivityConditionNewVo discountActivityConditionNewVo, Model model) {
		DiscountActivityNew discountActivityNewselect = discountActivitiesNewService.selectDiscountActivityNew(discountActivityNew);
        model.addAttribute("discountActivityNew", discountActivityNewselect);
        model.addAttribute("conditionStatistical", discountActivityConditionNewVo);
		DiscountCouponNew discountCoupon = new DiscountCouponNew();
		List<DiscountCouponNew> couponOneTwoList = discountActivitiesNewService.querycouponOneTwoList(discountCoupon);
		List<DiscountCouponNew> couponThreeFourList = discountActivitiesNewService.querycouponThreeFourList(discountCoupon);
		couponOneTwoList.addAll(couponThreeFourList);
		List<DiscountCouponNew> discountCouponList = new ArrayList<DiscountCouponNew>();
		discountCouponList.addAll(couponOneTwoList);
		PageInfo<Object> pageInfo = new PageInfo<Object>();
		discountCouponList = pagination(discountActivityConditionNewVo, discountCouponList, pageInfo);
		model.addAttribute("discountCouponList", discountCouponList);
		model.addAttribute("pageStatistical", pageInfo);
	}

	/**
	 * 
	 * 添加380优惠券活动
	 * 
	 */
	@RequestMapping("/discount/save")
	public String saveDiscountActivity(DiscountActivityNew discountActivityNew, Model model,
			MultipartHttpServletRequest request, HttpServletResponse response) {

		String discountname = request.getParameter("Name");
		String discounturl = request.getParameter("banner");
		String discountperiod = request.getParameter("period");

		discountActivityNew.setName(discountname);
		discountActivityNew.setBanner(discounturl);
		discountActivityNew.setPeriod(discountperiod);
		discountActivitiesNewService.saveDiscountActivityNew(discountActivityNew);

		return "redirect:" + adminPath + "/activity/discount";
	}

	/**
	 * 
	* @Title: addDiscountCoupon 
	* @Description: TODO(添加优惠券) 
	* @param @param model
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/discount/add")
	public String addDiscountCoupon( Model model) {

		   return "/activity/discountnew/discountCouponForm";

	}
	
	/**
	 * 
	* @Title: editDiscountCoupon 
	* @Description: TODO(根据优惠券码，进行编辑) 
	* @param @param model
	* @param @param couponCodes
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
    @RequestMapping("/discount/edit")
    public String editDiscountCoupon(Model model, String id) {
        DiscountCouponNew discountCouponNew = discountActivitiesNewService.queryDiscountCouponByid(id);
        model.addAttribute("discountCouponNew", discountCouponNew);
        return "/activity/discountnew/discountCouponForm";
    }
    
    /**
     * 
    * @Title: changenextOrder 
    * @Description: TODO(活动管理界面点击保存，更改优惠券状态,nextOrder) 
    * @param @param model
    * @param @param id
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    @RequestMapping("/discount/changeNextOrder")
    @ResponseBody
    public Map<String, Object> changenextOrder(Model model, String id) {
    	Map<String, Object> result = getResult();
    	String couponId=StringEscapeUtils.unescapeXml(id);
        JSONObject jsoncouponId = JSONObject.parseObject(couponId);
        JSONArray jsoncouponArray = jsoncouponId.getJSONArray("id");
        DiscountCouponNew discountCouponNew = null;
        List<DiscountCouponNew> list = new ArrayList<DiscountCouponNew>();
        int i = 0;
        for (Object couponstr : jsoncouponArray) {
        	if (((JSONObject) couponstr).getString("state").equals("1") || ((JSONObject) couponstr).getString("state").equals("2")) {
        	    discountCouponNew = new DiscountCouponNew();
	        	discountCouponNew.setId(((JSONObject) couponstr).getString("id"));
	        	
	            discountCouponNew.setState(((JSONObject) couponstr).getString("state"));
	        	
	        	discountCouponNew.setNextOrder(i);
	        	list.add(discountCouponNew);
	        	i++;
        	}
        	 
        }
        
        discountActivitiesNewService.updateNextOrder(list);
         
        return result;
    }
    
    
    
    
    /**
    * @Title: changenextOrder 
    * @Description: TODO(点击发布优惠券) 
    * @param @param model
    * @param @param id
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    @RequestMapping("/discount/pushDiscountCoupon")
    @ResponseBody
    public Map<String, Object> pushDiscountCoupon(Model model, String id) {
    	Map<String, Object> result = getResult();
    	String couponId=StringEscapeUtils.unescapeXml(id);
        JSONObject jsoncouponId = JSONObject.parseObject(couponId);
        JSONArray jsoncouponArray = jsoncouponId.getJSONArray("id");
        DiscountCouponNew discountCouponNew = null;
        List<DiscountCouponNew> list = new ArrayList<DiscountCouponNew>();
        for (Object couponstr : jsoncouponArray) {
        	    discountCouponNew = new DiscountCouponNew();
	        	discountCouponNew.setId(((JSONObject) couponstr).getString("id"));
	        	discountCouponNew.setState(((JSONObject) couponstr).getString("state"));
	        	list.add(discountCouponNew);
        	}
        	 
        discountActivitiesNewService.changeCurrOrder(list);
         
        return result;
    }
    
    
    
	/**
	 * 
	* @Title: saveDiscountCoupon 
	* @Description: TODO(保存新增优惠券) 
	* @param @param discountCouponNew
	* @param @param model
	* @param @param request
	* @param @param response
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/discount/saveDiscountCoupon")
	public String saveDiscountCoupon(DiscountCouponNew discountCouponNew, Model model,
			MultipartHttpServletRequest request, HttpServletResponse response) {
		
		String discountCouponCodes = request.getParameter("couponCodes");
		String discountCouponname = request.getParameter("name");
		String discountCouponMoney = request.getParameter("couponMoney");
		String discountCouponCondition = request.getParameter("couponCondition");
		String discountCouponTime = request.getParameter("couponTime");
		String discountCouponUrl = request.getParameter("couponUrl");
		
		discountCouponNew.setCouponCodes(discountCouponCodes);
		discountCouponNew.setName(discountCouponname);
		discountCouponNew.setCouponMoney(discountCouponMoney);
		discountCouponNew.setCouponCondition(discountCouponCondition);
		discountCouponNew.setCouponTime(discountCouponTime);
		discountCouponNew.setCouponUrl(discountCouponUrl);
		discountCouponNew.setState("1");
		
		discountActivitiesNewService.saveDiscountCouponNew(discountCouponNew);
		

		return "redirect:" + adminPath + "/activity/discount";

	}
	/**
	 * 
	 * 活动管理--上传单个图片
	 * 
	 */
	@RequestMapping("/discount/imageUpload")
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
	* @Title: tabStatistical 
	* @Description: TODO(380优惠券的查询统计) 
	* @param @param discountActivityConditionNewVo
	* @param @param model    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	private void tabStatistical(DiscountActivityConditionNewVo discountActivityConditionNewVo, Model model) {
		model.addAttribute("conditionStatistical", discountActivityConditionNewVo);
		PageInfo<DiscountCouponNew> pageInfo = discountActivitiesNewService.queryDiscountCouponNewList(discountActivityConditionNewVo);
		model.addAttribute("discountCouponList",  pageInfo.getList());
		model.addAttribute("pageStatistical", pageInfo);
		
	}

	/**
	 * 
	* @Title: pagination 
	* @Description: TODO(手动添加分页方法) 
	* @param @param discountActivityConditionNewVo
	* @param @param discountCouponList
	* @param @param pageInfo
	* @param @return    设定文件 
	* @return List<DiscountCouponNew>    返回类型 
	* @throws
	 */
	private List<DiscountCouponNew> pagination(
			DiscountActivityConditionNewVo discountActivityConditionNewVo,
			List<DiscountCouponNew> discountCouponList,
			PageInfo<Object> pageInfo) {
		int page = discountActivityConditionNewVo.getPage();
		int pageSize = discountActivityConditionNewVo.getPageSize();
		int endRow = page * pageSize - 1;
		int startRow = (page - 1) * pageSize;
		boolean isLastPage = false;
		boolean isFirstPage = false;
		if (endRow > discountCouponList.size()) {
			endRow = discountCouponList.size();
			isLastPage = true;
		}
		if (page == 0) {
			isFirstPage = true;
		}
		int lastPage =  (discountCouponList.size() % pageSize) == 0 ? discountCouponList.size() / pageSize : discountCouponList.size() / pageSize + 1;
		int nextPage = page + 1;
		int prePage = page - 1;
		pageInfo.setEndRow(endRow + 1);
		pageInfo.setStartRow(startRow + 1);
		pageInfo.setIsFirstPage(isFirstPage);
		pageInfo.setFirstPage(1);
		pageInfo.setPrePage(prePage);
		pageInfo.setNextPage(nextPage);
		pageInfo.setIsLastPage(isLastPage);
		pageInfo.setLastPage(lastPage);
		pageInfo.setPageNum(page);
		pageInfo.setTotal(discountCouponList.size());
		pageInfo.setPages(lastPage);
		return discountCouponList.subList(startRow, endRow);
	}

	
	/**
	 * 
	* @Title: delete 
	* @Description: TODO(根据优惠券id,删除) 
	* @param @param id
	* @param @param redirectAttrs
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/discount/delete")
    public String delete(String id, RedirectAttributes redirectAttrs) {
    	
		discountActivitiesNewService.deleteDiscountCouponNew(id);
		addMessage(redirectAttrs, "删除券成功！");
		
		return "redirect:" + adminPath + "/activity/discount";
	}
	
	/**
	 * 
	* @Title: recovery 
	* @Description: TODO(根据优惠券id,恢复) 
	* @param @param id
	* @param @param redirectAttrs
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/discount/recovery")
    public String recovery(String id, RedirectAttributes redirectAttrs) {
    	
		discountActivitiesNewService.recoveryDiscountCouponNew(id);
		addMessage(redirectAttrs, "恢复券成功！");
		
		return "redirect:" + adminPath + "/activity/discount";
	}
	
	
	 public Map<String, Object> getResult() {
	        Map<String, Object> result = new HashMap<String, Object>();
	        result.put("code", "1");
	        result.put("msg", "操作成功");
	        return result;
	    }

}

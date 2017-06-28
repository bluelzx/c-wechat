package com.lvtu.wechat.back.web.activity.signflow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.activity.signflow.FlowPack;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowPackService;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowPartnerService;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.MemcachedUtil;

/**
 * 签到送流量活动
 * @author xuyao
 *
 */
@Controller
@RequestMapping("${adminPath}/activity")
public class SignFLowAction extends BaseActionSupport {
	
	@Autowired
	private IWxFlowPackService flowPackService;
	
	@Autowired
	private IWxFlowPartnerService flowPartnerService;
	
	/**
	 * 流量包列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/signflow/flowPacks")
	public String flowPacks(Model model, HttpServletRequest request,
	        HttpServletResponse response) {
			model.addAttribute("flowPacks", flowPackService.getAllFlowPacks());
			model.addAttribute("flowPartners", flowPartnerService.getAllFlowPartners());
			model.addAttribute("tab", "flowPack");
		return "/activity/signflow/flowPacks";
	}
	
	@RequestMapping("/signflow/addPack")
	public String addPacks(FlowPack pack, RedirectAttributes redirectAttrs) {
		if(beanValidator(redirectAttrs, pack)) {
			flowPackService.save(pack);
			addMessage(redirectAttrs, "添加流量包成功！");
		}
		return "redirect:" + adminPath + "/activity/signflow/flowPacks";
	}
	
	/**
	 * 删除流量包
	 * @param id
	 * @param redirectAttrs
	 * @return
	 */
	@RequestMapping("/signflow/delPack")
	public String delPacks(String id, RedirectAttributes redirectAttrs) {
		flowPackService.delete(id);
		addMessage(redirectAttrs, "删除流量包成功！");
		return "redirect:" + adminPath + "/activity/signflow/flowPacks";
	}
	/**
	 * 切换流量合作商
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/flow/change")
	public Object changePartner(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		String id = request.getParameter("id");
		boolean flag = flowPartnerService.updatePartner(id);
		if(flag){
			logger.info("流量合作商url切换成功");
			result.put("code", 1);
		}else{
			result.put("code", 2);
		}
		return result;
	}
	
	/**
	 * 清除缓存中选择流量合作商
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/flow/removeCache")
	public Object removeCachePartner() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 3);
		MemcachedUtil mem = MemcachedUtil.getInstance();
		String partnerUrl = (String) mem.get(Constants.WX_FLOW_PARTNER_KEY);
		if(StringUtils.isBlank(partnerUrl)){
			return result;
		}
		boolean flag = mem.releaseLock(Constants.WX_FLOW_PARTNER_KEY);
		if(!flag){
			logger.info("清除缓存失败！");
			result.put("code", 4);
		}
		return result;
	}
	
}

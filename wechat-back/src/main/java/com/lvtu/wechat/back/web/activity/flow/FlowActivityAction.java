package com.lvtu.wechat.back.web.activity.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.helper.StringUtil;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivity;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivityGroups;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivityMembers;
import com.lvtu.wechat.common.model.activity.group.GroupActivityBanner;
import com.lvtu.wechat.common.model.qrcode.QRCode;
import com.lvtu.wechat.common.service.activity.flow.IFlowActivityService;
import com.lvtu.wechat.common.service.qrcode.IQRCodeService;
import com.lvtu.wechat.common.utils.MemcachedUtil;
import com.lvtu.wechat.common.vo.back.FlowActivityConditionVo;

/** 
* @ClassName: FlowActivityAction 
* @Description: 裂变流量活动
* @author zhengchongxiang
* @date 2016-10-10 下午8:32:11  
*/
@Controller
@RequestMapping("${adminPath}/activity/flow")
public class FlowActivityAction extends BaseActionSupport {

	@Autowired
	private IFlowActivityService flowActivityService;
	   
    @Autowired
    private IQRCodeService qRCodeService;

	/** 
	* @Title: index 
	* @Description: 裂变流量列表页 
	* @param @param vo
	* @param @param model
	* @param @param tab
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("manage")
	public String index(FlowActivityConditionVo vo, Model model,String tab) {
		if(StringUtil.isBlank(tab)){
			tabManage(vo, model);
			tabStatistical(vo, model);
			model.addAttribute("tab", "tabManage");
		}else if(tab.equals("tabManage")){
			FlowActivityConditionVo newVo = new FlowActivityConditionVo();
			tabManage(vo, model);
			tabStatistical(newVo, model);
			model.addAttribute("tab", "tabManage");
		}else if(tab.equals("tabClick")){
			FlowActivityConditionVo newVo = new FlowActivityConditionVo();
			tabManage(newVo, model);
			tabStatistical(vo, model);
			model.addAttribute("tab", "clicks");
		}
		
		return "activity/flow/manage";
	}
	
	private void tabManage(FlowActivityConditionVo vo, Model model){
		PageInfo<FlowActivity> pageInfo = flowActivityService.queryFlowActivity(vo);
		model.addAttribute("flowActivitys", pageInfo.getList());
		model.addAttribute("pageManage", pageInfo);
		model.addAttribute("flowActivityVo", vo);
	}
	
	private void tabStatistical(FlowActivityConditionVo vo, Model model){
		PageInfo<FlowActivityGroups> pageInfos = flowActivityService.queryActivityGroupById(vo);
		model.addAttribute("flowActivityGroups", pageInfos.getList());
		model.addAttribute("clicksPage", pageInfos);
		model.addAttribute("flowActivityVo1", vo);
	}

	/** 
	* @Title: newActivity 
	* @Description: 新增活动 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("new")
	public String newActivity() {
		return "activity/flow/add";
	}

	/** 
	* @Title: saveActivity 
	* @Description: 保存或者修改活动信息
	* @param @param flowActivity
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("save")
	public String saveActivity(FlowActivity flowActivity) {
		flowActivityService.saveOrUpdateFlowActivity(flowActivity);
		return "redirect:" + adminPath + "/activity/flow/manage";
	}

	/** 
	* @Title: editActivity 
	* @Description: 编辑活动信息
	* @param @param id
	* @param @param model
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("edit/{id}")
	public String editActivity(@PathVariable String id, Model model) {
		FlowActivity flowActivity = flowActivityService.queryFLowActivityById(id);
		List<GroupActivityBanner> bannerUrlList = flowActivityService.queryBannersById(id);
		model.addAttribute("flowform", flowActivity);
		model.addAttribute("bannerUrlList", bannerUrlList);
		return "activity/flow/add";
	}

	/** 
	* @Title: deleteActivity 
	* @Description: 删除活动
	* @param @param id
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("delete")
	@ResponseBody
	public Object deleteActivity(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", -1);
		if (flowActivityService.deleteFlowActivity(id)) {
			result.put("code", 1);
		}
		return result;
	}
	
	/** 
	* @Title: deleteActivityCache 
	* @Description: 清除活动缓存 
	* @param @param id
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("delCache")
	@ResponseBody
	public Object deleteActivityCache(String id) {
		
		MemcachedUtil mem = MemcachedUtil.getInstance();
        mem.remove(id);
        Log.info(id);
        FlowActivity flowActivity = flowActivityService.queryFLowActivityById(id);
		if(null != flowActivity && !StringUtil.isBlank(flowActivity.getShareTemplateId())){
			mem.remove(flowActivity.getShareTemplateId());
		}
		return getResult();
	}
	
	/** 
	* @Title: detailMembers 
	* @Description: 参加活动的详细成员信息 
	* @param @param id
	* @param @param vo
	* @param @param model
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("detail/{id}")
	public String detailMembers(@PathVariable String id,FlowActivityConditionVo vo, Model model){
		vo.setId(id);
		PageInfo<FlowActivityMembers> pageInfo = flowActivityService.queryDetailMembers(vo);
		model.addAttribute("flowMembers", pageInfo.getList());
		model.addAttribute("pageManage", pageInfo);
		model.addAttribute("id", id);
		return "activity/flow/detail";
	}
	
    /**
     * 点击生成二维码
     * @param groupId
     * @return
     */
    /** 
    * @Title: createQRcode 
    * @Description: 生成活动二维码 
    * @param @param id
    * @param @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @throws 
    */
    @RequestMapping("/createQRcode")
    @ResponseBody
    public Map<String,Object> createQRcode(String id){
    	FlowActivity flowActivity = flowActivityService.queryFLowActivityById(id);
        Map<String, Object> result = getResult();
        synchronized (this) {
        	QRCode qRcode = qRCodeService.queryQRCodeByActId(id);
        	if(null != qRcode){
        		result.put("code", -1);
        		result.put("msg", "请不要重复获取！");
        		return result;
        	}
            result=qRCodeService.aquireQRCode(flowActivity,result);
        }
        return result;
    } 
	 
    public Map<String, Object> getResult() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "1");
        result.put("msg", "操作成功");
        return result;
    }

}

package com.lvtu.wechat.back.web.productorder;

import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.ExcelUtil;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.enums.ProductOrderType;
import com.lvtu.wechat.common.model.productorder.AreaGroup;
import com.lvtu.wechat.common.model.productorder.ExportOrders;
import com.lvtu.wechat.common.model.productorder.ProductOrder;
import com.lvtu.wechat.common.model.productorder.PushRecords;
import com.lvtu.wechat.common.model.productorder.PushTemplate;
import com.lvtu.wechat.common.model.weixin.template.WxTemplate;
import com.lvtu.wechat.common.service.productorder.IAreasService;
import com.lvtu.wechat.common.service.productorder.IProductOrderService;
import com.lvtu.wechat.common.service.productorder.IPushRecordsService;
import com.lvtu.wechat.common.service.productorder.IPushTemplateService;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.utils.StringUtil;
import com.lvtu.wechat.common.utils.TemplatesUtils;
import com.lvtu.wechat.common.vo.back.ProductOrderConditionVo;
import com.lvtu.wechat.common.vo.back.PushTemplateConditionVo;

/** 
* @ClassName: PushTemplateAction 
* @Description: 模板消息
* @author zhengchongxiang
* @date 2016-8-29 下午4:19:58  
*/
@Controller
@RequestMapping("${adminPath}/pushTemplate")
public class PushTemplateAction extends BaseActionSupport {

	@Autowired
	private IPushTemplateService pushTemplateService;
	
	@Autowired
	private IProductOrderService productOrderService;
	
	@Autowired
	private IPushRecordsService pushRecordsService;
	
	@Autowired
	private IAreasService areasService;

	/** 
	* @Title: getPushTemplates 
	* @Description: 进入活动信息详情列表页 
	* @param @param 查询条件
	* @param @param model
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("manage")
	public String getPushTemplates(PushTemplateConditionVo vo,Model model,ProductOrderConditionVo productOrderVo,
			Integer state,String tab) {		
		PageInfo<PushTemplate> pageInfo = pushTemplateService.get(vo);
		model.addAttribute("pageManage", pageInfo);
		model.addAttribute("pushTemplates", pageInfo.getList());	
		model.addAttribute("pushConditionVo", vo);
		model.addAttribute("orderCount",productOrderService.selectCount());
		if(ProductOrderType.IN_TOURISM.getValue().equals(state)){
			productOrderVo.setInboundTourism(true);
		}else if(ProductOrderType.OUT_TOURISM.getValue().equals(state)){
			productOrderVo.setOutboundTourism(true);
		}else if(ProductOrderType.SPECIAL_TICKET.getValue().equals(state)){
			productOrderVo.setSpecialTicket(true);
		}else if(ProductOrderType.HOME_TOURISM.getValue().equals(state)){
			productOrderVo.setHomeTourism(true);
		}else if(ProductOrderType.HOT_TICKET.getValue().equals(state)){
			productOrderVo.setHotTicket(true);
		}
		PageInfo<ProductOrder> pageInfos = productOrderService.selectOrderByCondition(productOrderVo);
		model.addAttribute("state", state);
		model.addAttribute("openid", productOrderVo.getOpenid());
		model.addAttribute("clicksPage", pageInfos);
		model.addAttribute("productOrders", pageInfos.getList());
		if("tabClick".equals(tab)){
			model.addAttribute("tab", "clicks");
		}else if("tabManage".equals(tab)){
			model.addAttribute("tab", "manages");
		}
		return "productorder/manage";
	}
	
	/** 
	* @Title: newPushTemplate 
	* @Description: 点击新增活动跳转 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("new")
	public String newPushTemplate(Model model){
		List<AreaGroup> list = areasService.selectAreaGroups();
		model.addAttribute("areaGroups", list);
		return "productorder/add";
	}
	
	/** 
	* @Title: editPushTemplate 
	* @Description: 更新活动信息
	* @param @param 活动ID
	* @return String    返回类型 
	*/
	@RequestMapping("edit/{id}")
	public String editPushTemplate(@PathVariable String id,Model model){
		PushTemplate pushTemplate = pushTemplateService.getByPrimary(id);
		model.addAttribute("pushTemplate", pushTemplate);
		List<AreaGroup> list = areasService.selectAreaGroups();
		model.addAttribute("areaGroups", list);
		return "productorder/add";
	}
	
	/** 
	* @Title: addPushTemplate 
	* @Description: 新增或编辑活动信息 
	* @param @param pushTemplate
	* @return Object    返回类型 
	* @throws 
	*/
	@ResponseBody
	@RequestMapping(value="add")
	public Object addPushTemplate(PushTemplate pushTemplate) {
		logger.info(pushTemplate);
		Map<String ,Object> result = new HashMap<String, Object>();
		result.put("code", 1);
		boolean flag = pushTemplateService.saveOrUpdate(pushTemplate);
		if(!flag){
			result.put("code", -1);
			return result;
		}
		return result;
	}
	
	/** 
	* @Title: sendTemplateMessage 
	* @Description: 模板消息发送方法 
	* @param @param 活动id
	* @return Object    返回类型 
	* @throws 
	*/
	@ResponseBody
	@RequestMapping("sendMessage")
	public Object sendTemplateMessage(String id){
		logger.info("开始时间"+DateUtils.getDate("yyyy年MM月dd日 HH:mm:ss"));
		Map<String ,Object> result = new HashMap<String, Object>();
		result.put("code", 1);
		PushTemplate pushTemplate = pushTemplateService.getByPrimary(id);
		String type = pushTemplate.getType();
		//推送记录
		PushRecords pushRecords = new PushRecords();
		pushRecords.setActivityId(id);
		pushRecords.setCreateTime(new Date());
		ProductOrderConditionVo vo = new ProductOrderConditionVo();
		String cityIds = "";
		if (null != pushTemplate && StringUtil.isNotEmptyString(pushTemplate.getAreasId())) {
			String areasIds = pushTemplate.getAreasId();
			if (!areasIds.equals("0")) {
				for (String areasId : areasIds.split(",")) {
					AreaGroup areaGroup = areasService.selectAreaGroupById(areasId);
					if (null != areaGroup && StringUtil.isNotEmptyString(areaGroup.getCityIds())) {
						cityIds += areaGroup.getCityIds() + ",";
					}
				}
				if (StringUtil.isNotEmptyString(cityIds)) {
					cityIds = cityIds.substring(0, cityIds.length() - 1);
					vo.setCityIds(cityIds);
				}
			}
		}
		if(type.indexOf("1")!=-1){
			vo.setOutboundTourism(true);
		} if(type.indexOf("2")!=-1){
			vo.setInboundTourism(true);
		} if(type.indexOf("3")!=-1){
			vo.setSpecialTicket(true);
		} if(type.indexOf("4")!=-1){
			vo.setHomeTourism(true);
		} if(type.indexOf("5")!=-1){
			vo.setHotTicket(true);
		}
		List<String> list= productOrderService.selectOpenidByType(vo);
		logger.info("发送用户数量："+list.size());
		for (String openid : list) {
			WxTemplate wxTemplate = TemplatesUtils.makeWxTemplate(pushTemplate,openid);
			String datas = TemplatesUtils.sendMessage(wxTemplate);
			logger.info("用户："+openid+"发送result="+datas);
			JSONObject obj = (JSONObject) JSONObject.parse(datas);
			pushRecords.setOpenid(openid);
			if (obj != null && "0".equals(obj.getString("errcode"))) {
				pushRecords.setMsgId(obj.getString("msgid"));
				pushRecords.setSuccess(CommonType.SENDING.getValue());// 发送成功				
				pushRecordsService.save(pushRecords);
			}
		}		
		logger.info("结束时间："+DateUtils.getDate("yyyy年MM月dd日 hh:mm:ss"));
		return result;
	}
	
	
	/** 
	* @Title: sendTemplateTest 
	* @Description: 针对开发和运营人员的测试 
	* @param @param 活动Id
	* @return Object    返回类型 
	* @throws 
	*/
	@ResponseBody
	@RequestMapping("sendMessageTest")
	public Object sendTemplateTest(String id){
		logger.info("测试开始时间"+DateUtils.getDate("yyyy年MM月dd日 HH:mm:ss"));
		String openids = Constants.getConfig("push.test.openid");
		String[] ids = openids.split(",");
		Map<String ,Object> result = new HashMap<String, Object>();
		result.put("code", 1);
		PushTemplate pushTemplate = pushTemplateService.getByPrimary(id);
		String cityIds = "";
		if(null != pushTemplate && StringUtil.isNotEmptyString(pushTemplate.getAreasId())){
			String areasIds = pushTemplate.getAreasId();
			if(areasIds.equals("0")){
				areasIds = "";
			}else{
				for(String areasId : areasIds.split(",")){
					AreaGroup areaGroup = areasService.selectAreaGroupById(areasId);
					if(null != areaGroup && StringUtil.isNotEmptyString(areaGroup.getCityIds())){
						cityIds += areaGroup.getCityIds()+",";
					}
				}
				if(StringUtil.isNotEmptyString(cityIds)){
					cityIds = cityIds.substring(0, cityIds.length()-1);
				}	
			}		
		}
		String type = pushTemplate.getType();
		//推送记录
		PushRecords pushRecords = new PushRecords();
		pushRecords.setActivityId(id);
		pushRecords.setCreateTime(new Date());
		ProductOrderConditionVo vo = new ProductOrderConditionVo();
		if(StringUtil.isNotEmptyString(cityIds)){
			vo.setCityIds(cityIds);
		}
		if(type.indexOf("1")!=-1){
			vo.setOutboundTourism(true);
		} if(type.indexOf("2")!=-1){
			vo.setInboundTourism(true);
		} if(type.indexOf("3")!=-1){
			vo.setSpecialTicket(true);
		} if(type.indexOf("4")!=-1){
			vo.setHomeTourism(true);
		} if(type.indexOf("5")!=-1){
			vo.setHotTicket(true);
		}
		List<String> list= productOrderService.selectOpenidByType(vo);
		logger.info("发送用户数量："+list.size());
		for (String openid : list) {
			for(int i=0;i<ids.length;i++){
				if(openid.equals(ids[i])){
					WxTemplate wxTemplate = TemplatesUtils.makeWxTemplate(pushTemplate,openid);
					String datas = TemplatesUtils.sendMessage(wxTemplate);
					logger.info("用户："+openid+"发送result="+datas);
					JSONObject obj = (JSONObject) JSONObject.parse(datas);
					pushRecords.setOpenid(openid);
					if (obj != null && "0".equals(obj.getString("errcode"))) {
						pushRecords.setMsgId(obj.getString("msgid"));
						pushRecords.setSuccess(CommonType.SENDING.getValue());// 发送成功				
						pushRecordsService.save(pushRecords);
					}
				}
			}
		}		
		logger.info("结束时间："+DateUtils.getDate("yyyy年MM月dd日 hh:mm:ss"));
		return result;
	}
	
	/** 
	* @Title: getPushSuccess 
	* @Description: 根据时间分组查询消息推送的记录详情
	* @param @param id 活动ID
	* @param @param model
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("records/{id}")
	public String getPushSuccess(@PathVariable String id,Model model){
		List<PushRecords> list = pushRecordsService.selectBySuccess(id);
		model.addAttribute("pushRecords", list);		
		return "productorder/records";
	}
	
	/** 
	* @Title: exportExcel 
	* @Description: 导出订阅记录 
	* @param @param request
	* @param @param response 
	* @return void    返回类型 
	* @throws 
	*/
	@ResponseBody
	@RequestMapping("export")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response){
		//获取所有订阅信息
		List<ExportOrders> list = productOrderService.selectByExport();
        String[] headers = { "微信名", "openid", "出境游", "国内游", "特价机票","亲子游","特价门票", "订阅时间"};
        String codedFileName = null;  
        OutputStream fOut = null; 
        try {
         // 生成提示信息，  
            response.setContentType("application/vnd.ms-excel");  
         // 进行转码，使其支持中文文件名  
            codedFileName = java.net.URLEncoder.encode("甩尾产品订阅用户信息", "UTF-8");  
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            logger.info("导出数据条数：" + list.size());
            ExcelUtil.exportExcel(headers, list, fOut, "yyyy-MM-dd HH:mm:ss");
        }catch(Exception e){
        	e.printStackTrace();  
        	logger.info("导出数据异常");
        }				
	}
	
	@ResponseBody
	@RequestMapping("getCount")
	public Object getOrderCount(String type,String areasIds){		
		String cityIds = "";
			if(areasIds.equals("0")){
				areasIds = "";
			}else{
				for(String areasId : areasIds.split(",")){
					AreaGroup areaGroup = areasService.selectAreaGroupById(areasId);
					if(null != areaGroup && StringUtil.isNotEmptyString(areaGroup.getCityIds())){
						cityIds += areaGroup.getCityIds()+",";
					}
				}
				if(StringUtil.isNotEmptyString(cityIds)){
					cityIds = cityIds.substring(0, cityIds.length()-1);
				}	
			}		
		
		ProductOrderConditionVo vo = new ProductOrderConditionVo();
		if(StringUtil.isNotEmptyString(cityIds)){
			vo.setCityIds(cityIds);
		}
		if(type.indexOf("1")!=-1){
			vo.setOutboundTourism(true);
		} if(type.indexOf("2")!=-1){
			vo.setInboundTourism(true);
		} if(type.indexOf("3")!=-1){
			vo.setSpecialTicket(true);
		} if(type.indexOf("4")!=-1){
			vo.setHomeTourism(true);
		} if(type.indexOf("5")!=-1){
			vo.setHotTicket(true);
		}
		List<String> list= productOrderService.selectOpenidByType(vo);
		Map<String ,Object> result = new HashMap<String, Object>();
		result.put("code", 1);
		result.put("count", list.size());		
		return result;
	}

}

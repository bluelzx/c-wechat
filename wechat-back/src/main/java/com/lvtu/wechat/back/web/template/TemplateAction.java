package com.lvtu.wechat.back.web.template;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.lvmama.comm.user.utils.StringUtil;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.template.Template;
import com.lvtu.wechat.common.model.template.TemplateRecords;
import com.lvtu.wechat.common.service.template.ITemplateService;
import com.lvtu.wechat.common.vo.back.TemplateConditionVo;

@Controller
@RequestMapping("${adminPath}/template")
public class TemplateAction extends BaseActionSupport {
	
	@Autowired
	private ITemplateService templateService;

	/** 
	* @Title: index 
	* @Description: 模板消息列表页展示 
	* @param @param condition
	* @param @param tab
	* @param @param model
	* @throws 
	*/
	@RequestMapping("manage")
	public String index(TemplateConditionVo condition,String tab,Model model) {		

			PageInfo<TemplateRecords> pageInfo =  templateService.selectTemplateRecords(condition);
			model.addAttribute("clicksPage", pageInfo);
			model.addAttribute("templateRecords", pageInfo.getList());
			model.addAttribute("condition", condition);

			PageInfo<Template> page = templateService.selectTemplates(condition);
			model.addAttribute("pageManage", page);
			model.addAttribute("templates", page.getList());
			model.addAttribute("condition", condition);
			
			if("tabClick".equals(tab)){
				model.addAttribute("tab", "clicks");
			}

		return "template/manage";
	}
	
	@RequestMapping("add")
	public String add(){
		return "template/add";
	}
	
	/** 
	* @Title: save 
	* @Description: 新增或编辑模板消息配置
	* @param @param template
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@ResponseBody
	@RequestMapping("save")
	public Object save(Template template){
		Map<String ,Object> result = new HashMap<String,Object>();
		result.put("code", 1);
		boolean flag = templateService.saveOrUpdateTemplate(template);
		if(!flag){
			result.put("code", -1);
		}
		return result;
		
	}
	
	/** 
	* @Title: edit 
	* @Description: 跳转编辑页面
	* @param @param id
	* @param @param model
	* @throws 
	*/
	@RequestMapping("edit/{id}")
	public String edit(@PathVariable String id,Model model){
		Template template = templateService.selectByPrimary(id);
		model.addAttribute("template", template);
		return "template/add";
	}
	
	@RequestMapping("detail")
	@ResponseBody
	public Object detail(String id){
		TemplateRecords templateRecords = templateService.selectById(id);
		return templateRecords;
		} 

}

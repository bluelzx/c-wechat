package com.lvtu.wechat.back.web.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.sys.Menu;
import com.lvtu.wechat.common.service.sys.MenuService;
import com.lvtu.wechat.common.utils.StringUtil;

@Controller
public class MenuAction extends BaseActionSupport {

	@Autowired
	private MenuService menuService;

	@RequestMapping("${adminPath}/menu/index")
	public String index(Model model) {
		List<Menu> lists = menuService.selectAllMenu();
		model.addAttribute("menus", lists);
		return "sys/menuList";
	}
	
	@RequestMapping("${adminPath}/menu/new")
	public String newMenu(Model model,String id) {
		if(StringUtil.isNotEmptyString(id)){
			Menu menu = menuService.selectById(id);
			model.addAttribute("menu", menu);
		}
		return "/sys/menuForm";
	}
	
	@RequestMapping("${adminPath}/menu/save")
	public String saveMenu(Menu menu,String parentId,RedirectAttributes redirectAttrs){
		if(!getUser().isAdmin()){
			addMessage(redirectAttrs, "账号无权限！");
			return "redirect:" + adminPath + "/menu/index";	
		}
		Menu parent = new Menu();
		parent.setId(parentId);
		menu.setParent(parent);
		menuService.saveOrUpdate(menu);
		addMessage(redirectAttrs, "菜单保存成功！");
		return "redirect:" + adminPath + "/menu/index";
	}
	
	@RequestMapping("${adminPath}/menu/delete")
	public String deleteMenu(String id,RedirectAttributes redirectAttrs){
		menuService.deleteMenu(id);
		addMessage(redirectAttrs, "菜单删除成功！");
		return "redirect:" + adminPath + "/menu/index";
	}


}

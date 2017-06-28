package com.lvtu.wechat.back.web.sys;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.sys.Role;
import com.lvtu.wechat.common.model.sys.User;
import com.lvtu.wechat.common.service.sys.RoleService;
import com.lvtu.wechat.common.service.sys.UserService;

@Controller
public class UserAction extends BaseActionSupport {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	@RequiresPermissions("sys:user:view")
	@RequestMapping("${adminPath}/users/list")
	public String list(ModelMap model) {
		List<User> users = userService.findAllUser();
		model.addAttribute("users", users);
		
		return "sys/userList";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping("${adminPath}/users/new")
	public String newUser(Model model) {
		model.addAttribute("roles", roleService.findAllRole());

		return "/sys/userForm";
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("${adminPath}/users/edit")
	public String editUser(String id, Model model) {
		model.addAttribute("roles", roleService.findAllRole());
		model.addAttribute("user", userService.getUserById(id));

		return "/sys/userForm";
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping("${adminPath}/users/save")
	public String save(User user, Model model, RedirectAttributes redirectAttrs) {
		if (beanValidator(model, user)) {
			User existUser = userService.getUserByLoginName(user.getLoginName());
			if(existUser == null || existUser.equals(user)) {
				//读取权限信息
				List<Role> roleList = new ArrayList<Role>();
				List<String> roleIdList = user.getRoleIdList();
				for (Role r : roleService.findAllRole()){
					if (roleIdList.contains(r.getId())){
						roleList.add(r);
					}
				}
				user.setRoleList(roleList);
				//写入数据库
				userService.save(user);
				
				addMessage(redirectAttrs, "保存用户成功");
				return "redirect:" + adminPath + "/users/list";
			} else {
				addMessage(model, "保存用户失败，用户名已存在！");
			}
		}
		//编辑和添加跳到不同页面
		if(StringUtils.isBlank(user.getId())) {
			model.addAttribute("user", user);
			return newUser(model);
		} else {
			return editUser(user.getId(), model);
		}
	}

	@RequiresPermissions("sys:user:edit")
	@RequestMapping("${adminPath}/users/delete")
	public String deletePlatform(String id, RedirectAttributes redirectAttrs) {
		userService.delete(id);
		addMessage(redirectAttrs, "删除用户成功！");

		return "redirect:" + adminPath + "/users/list";
	}
	
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("${adminPath}/users/usable")
	public String useable(User user, RedirectAttributes redirectAttrs) {
		userService.updateUseable(user);
		if(user.isUseable()) {
			addMessage(redirectAttrs, "启用用户成功");
		} else {
			addMessage(redirectAttrs, "禁用用户成功");
		}

		return "redirect:" + adminPath + "/users/list";
	}
}

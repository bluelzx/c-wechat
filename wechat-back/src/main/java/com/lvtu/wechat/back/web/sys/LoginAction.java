package com.lvtu.wechat.back.web.sys;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.sys.Menu;
import com.lvtu.wechat.common.service.sys.MenuService;

@Controller
public class LoginAction extends BaseActionSupport {
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping("${adminPath}/login")
	public String login() {
		if(SecurityUtils.getSubject().isAuthenticated()) {
			return "redirect:" + adminPath + "/index";
		}
		
		return "login";
	}

	@RequestMapping("${adminPath}/login_ajax")
	@ResponseBody
	public Object login(String username, String password, String imgCode) {
		JSONObject result = new JSONObject();
		result.put("code", "1");
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			result.put("code", "-1");
			result.put("message", "用户名/密码不能为空！");
		} else {
				UsernamePasswordToken token = new UsernamePasswordToken(username, password);
				try {
					SecurityUtils.getSubject().login(token);
				} catch (AuthenticationException e) {
					result.put("code", "-1");
					if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
						result.put("message", StringUtils.replace(e.getMessage(), "msg:", ""));
					} else {
						logger.error("登录失败！", e);
						result.put("message", "登录失败，用户名或密码错误！");
					}
				}
			}
		return result;
	}
	
	@RequestMapping("${adminPath}/logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		
		return "redirect:" + adminPath + "/index";
	}
	
	@RequestMapping("${adminPath}/index")
	public String index(ModelMap model) {
		List<Menu> menus = menuService.getMenuList(getUser());
		model.addAttribute("menus", menus);
		model.addAttribute("currentUser", getUser());

		return "index";
	}
}

package com.lvtu.wechat.back.web.sys;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.sys.Menu;
import com.lvtu.wechat.common.model.sys.Role;
import com.lvtu.wechat.common.model.sys.User;
import com.lvtu.wechat.common.model.sys.UserRole;
import com.lvtu.wechat.common.service.sys.MenuService;
import com.lvtu.wechat.common.service.sys.RoleService;
import com.lvtu.wechat.common.service.sys.UserService;

@Controller
public class RoleAction extends BaseActionSupport {

	@Autowired
	private RoleService roleService;

	@Autowired
	private MenuService menuService;
	
	@Autowired
	private UserService userService;

	/**
	 * 角色列表
	 * @param user
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:view")
	@RequestMapping("${adminPath}/roles/list")
	public String list(User user, ModelMap model) {
		List<Role> roles = roleService.findAllRole();
		model.addAttribute("roles", roles);

		return "sys/roleList";
	}

	/**
	 * 新增角色
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping("${adminPath}/roles/new")
	public String newRole(Model model) {
		List<Menu> menus = menuService.getMenuList(getUser());
		JSONArray menuJson = new JSONArray();
		for (Menu menu : menus) {
			JSONObject node = new JSONObject();
			node.put("id", menu.getId());
			node.put("pId", menu.getParentId());
			node.put("name", menu.getName());
			node.put("open", true);
			menuJson.add(node);
		}
		model.addAttribute("menus", menuJson.toJSONString());
		return "sys/roleForm";
	}
	
	/**
	 * 编辑角色
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping("${adminPath}/roles/edit")
	public String editRole(Model model, String id) {
		Role role = roleService.getById(id);
		List<Menu> menuList = role.getMenuList();
		List<Menu> menus = menuService.getMenuList(getUser());
		JSONArray menuJson = new JSONArray();
		for (Menu menu : menus) {
			JSONObject node = new JSONObject();
			node.put("id", menu.getId());
			node.put("pId", menu.getParentId());
			node.put("name", menu.getName());
			node.put("open", true);
			node.put("checked", menuList.contains(menu));
			menuJson.add(node);
		}
		model.addAttribute("menus", menuJson.toJSONString());
		model.addAttribute("role", role);
		return "sys/roleForm";
	}

	/**
	 * 保存角色
	 * @param role
	 * @param model
	 * @param redirectAttrs
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping("${adminPath}/roles/save")
	public String saveRole(Role role, Model model, RedirectAttributes redirectAttrs) {
		if (beanValidator(model, role)) {
			Role exsitRole = roleService.getByEnname(role.getEnname());
			if(exsitRole == null || exsitRole.equals(role)) {
				roleService.saveRole(role);
				addMessage(redirectAttrs, "保存角色成功");
				return "redirect:" + adminPath + "/roles/list";
			} else {
				addMessage(model, "保存角色失败，角色英文名已存在！");
			}
		}
		//编辑和添加跳到不同页面
		if(StringUtils.isBlank(role.getId())) {
			model.addAttribute("role", role);
			return newRole(model);
		} else {
			return editRole(model, role.getId());
		}
	}
	
	/**
	 * 
	* @Title: deleteRole 
	* @Description: TODO(删除sys_role表中的角色) 
	* @param @param id
	* @param @param redirectAttrs
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping("${adminPath}/roles/delete")
	public String deleteRole(String roleId, RedirectAttributes redirectAttrs) {
        List<UserRole> userRoleList = roleService.queryByid(roleId);
        if (CollectionUtils.isEmpty(userRoleList)) {
            roleService.deleteRole(roleId);
            addMessage(redirectAttrs, "角色删除成功!");
            return "redirect:" + adminPath + "/roles/list";
        }
        boolean flag = true;
            for (UserRole userRole : userRoleList) {
                String userId = userRole.getUserId();
                User user = userService.getUserByuserId(userId);
                if (user.getDelFlags().equals("0")) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                roleService.deleteRole(roleId);
                addMessage(redirectAttrs, "角色删除成功!");
            }
            else {
                addMessage(redirectAttrs, "该角色下面关联有用户, 不可删除, 如若删除, 请先删除用户, 然后删除角色!");
            }
       
        return "redirect:" + adminPath + "/roles/list";
    }
}

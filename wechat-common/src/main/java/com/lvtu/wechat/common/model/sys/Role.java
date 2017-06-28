package com.lvtu.wechat.common.model.sys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.lvtu.wechat.common.base.BaseModel;


/**
 * 角色Entity
 * @author xuyao
 *
 */
public class Role extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message="角色名称不能为空")
	private String name; 	// 角色名称
	
	@NotBlank(message="英文名称不能为空")
	private String enname;	// 英文名称
	
	private boolean useable; 		//是否是可用

	private List<Menu> menuList = new ArrayList<Menu>(); // 拥有菜单列表
	
	public Role() {
		super();
		this.setUseable(true);
	}
	
	public Role(String id){
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}
	
	public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public List<String> getMenuIdList() {
		List<String> menuIdList = new ArrayList<String>();
		for (Menu menu : menuList) {
			menuIdList.add(menu.getId());
		}
		return menuIdList;
	}

	public void setMenuIdList(List<String> menuIdList) {
		menuList = new ArrayList<Menu>();
		for (String menuId : menuIdList) {
			Menu menu = new Menu();
			menu.setId(menuId);
			menuList.add(menu);
		}
	}

	public String getMenuIds() {
		return StringUtils.join(getMenuIdList(), ",");
	}
	
	public void setMenuIds(String menuIds) {
		menuList = new ArrayList<Menu>();
		if (menuIds != null){
			String[] ids = StringUtils.split(menuIds, ",");
			setMenuIdList(Arrays.asList(ids));
		}
	}
	
	/**
	 * 获取权限字符串列表
	 */
	public List<String> getPermissions() {
		List<String> permissions = new ArrayList<String>();
		for (Menu menu : menuList) {
			if (menu.getPermission()!=null && !"".equals(menu.getPermission())){
				permissions.add(menu.getPermission());
			}
		}
		return permissions;
	}

	public boolean isUseable() {
		return useable;
	}

	public void setUseable(boolean useable) {
		this.useable = useable;
	}
}

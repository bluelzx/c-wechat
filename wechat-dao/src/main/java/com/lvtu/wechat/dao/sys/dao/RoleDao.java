package com.lvtu.wechat.dao.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.sys.Menu;
import com.lvtu.wechat.common.model.sys.Role;

/**
 * 系统角色DAO
 * 
 * @author xuyao
 *
 */
@Repository
public class RoleDao extends BaseIbatisDAO {

	public RoleDao() {
		super("sys_role");
	}
	
	/**
	 * 获取所以角色信息
	 * @return
	 */
	public List<Role> findAllList() {
		return super.getList("findAllList");
	}
	
	/**
	 * 根据参数查询角色信息
	 * @param params
	 * @return
	 */
	public List<Role> findList(Map<String, Object> params) {
		return super.getList("findList", params);
	}

	/**
	 * 通过ID获取角色信息
	 * 
	 * @param id
	 * @return
	 */
	public Role getById(String id) {
		return super.get("get", id);
	}
	
	/**
	 * 通过ename获取角色
	 * @param enname
	 * @return
	 */
	public Role getByEnname(String enname) {
		return super.get("getByEnname", enname);
	}

	/**
	 * 保存角色信息
	 * 
	 * @param role
	 */
	public void saveRole(Role role) {
		super.insert("insert", role);
	}
	
	/**
	 * 根据角色ID删除角色, 逻辑删除update
	 * @param id
	 */
	public void delete(String id) {
		super.delete("delete", id);
	}
	
	/**
	 * 更新角色
	 * @param role
	 */
	public void updateRole(Role role) {
		super.update("update", role);
	}
	
	/**
	 * 删除角色菜单
	 * @param id 角色ID
	 */
	public void deleteRoleMenu(String id) {
		super.delete("deleteRoleMenu", id);
	}
	
	/**
	 * 插入角色菜单
	 * @param id
	 * @param menuList
	 */
	public void insertRoleMenu(String id, List<Menu> menuList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("menuList", menuList);
		super.insert("insertRoleMenu", params);
	}

	/**
	 * 
	* @Title: deleteRole 
	* @Description: TODO(删除sys_role表中的角色) 
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws
	 */
    public void deleteRole(String id) {
        super.delete("deleteRole", id);
    }

}

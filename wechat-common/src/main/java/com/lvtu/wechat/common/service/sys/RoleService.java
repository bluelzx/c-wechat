package com.lvtu.wechat.common.service.sys;

import java.util.List;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.sys.Role;
import com.lvtu.wechat.common.model.sys.UserRole;


/**
 * 角色服务接口
 * @author xuyao
 *
 */
@RemoteService("remoteRoleService")
public interface RoleService {
	
	/**
	 * 获取所有角色信息
	 * @return
	 */
	public List<Role> findAllRole();
	
	/**
	 * 保存角色信息
	 * @param role
	 */
	public void saveRole(Role role);
	
	/**
	 * 根据ID获取角色
	 * @param id
	 * @return
	 */
	public Role getById(String id);
	
	/**
	 * 根据enname获取角色
	 * @param enname
	 * @return
	 */
	public Role getByEnname(String enname);

	
	/**
	 * 
	* @Title: delete 
	* @Description: TODO(删除sys_role表中的角色) 
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws
	 */
    public void deleteRole(String id);

    /**
     * 
    * @Title: queryByid 
    * @Description: TODO(根据roleidz在sys_user_role表中查询) 
    * @param @param id
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public List<UserRole> queryByid(String id);
    
    
}

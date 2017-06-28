package com.lvtu.wechat.service.sys;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.sys.Role;
import com.lvtu.wechat.common.model.sys.UserRole;
import com.lvtu.wechat.common.service.sys.RoleService;
import com.lvtu.wechat.dao.sys.dao.RoleDao;
import com.lvtu.wechat.dao.sys.dao.UserRoleDao;

@HessianService("remoteRoleService")
@Service("roleService")
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public List<Role> findAllRole() {
		return roleDao.findAllList();
	}

	@Override
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		// 判断是更新还是新增
		if (StringUtils.isBlank(role.getId())) {
			role.preInsert();
			roleDao.saveRole(role);
			roleDao.insertRoleMenu(role.getId(), role.getMenuList());
		} else {
			// 更新角色信息
			roleDao.updateRole(role);
			// 清除现有菜单权限后重新插入
			roleDao.deleteRoleMenu(role.getId());
			roleDao.insertRoleMenu(role.getId(), role.getMenuList());
		}
	}

	@Override
	public Role getById(String id) {
		if (StringUtils.isBlank(id))
			return null;

		return roleDao.getById(id);
	}

	@Override
	public Role getByEnname(String enname) {
		if (StringUtils.isBlank(enname))
			return null;

		return roleDao.getByEnname(enname);
	}

	/**
	 * 删除sys_role表中的角色
	 */
    @Override
    @Transactional(readOnly = false)
    public void deleteRole(String id) {
        roleDao.deleteRole(id);
        userRoleDao.deleteUserRole(id);
    }
    
    /**
     * 根据roleidz在sys_user_role表中查询
     */
    @Override
    @Transactional(readOnly = false)
    public List<UserRole> queryByid(String id) {
     return userRoleDao.queryByid(id);
    }

}

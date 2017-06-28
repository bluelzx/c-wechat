package com.lvtu.wechat.service.sys;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.sys.Role;
import com.lvtu.wechat.common.model.sys.User;
import com.lvtu.wechat.common.service.sys.UserService;
import com.lvtu.wechat.dao.sys.dao.RoleDao;
import com.lvtu.wechat.dao.sys.dao.UserDao;


@HessianService("remoteUserService")
@Service("userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public User getUserByLoginName(String loginName) {
		return userDao.getByLoginName(loginName);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(User user) {
		//判断是更新还是新增
		if(StringUtils.isBlank(user.getId())) {
			user.preInsert();
			user.setPassword(new Sha512Hash(user.getPassword(), user.getId()).toHex());
			userDao.insert(user);
		} else {
			//判断密码是否更新
			if(!user.getPassword().equals(user.getOldPassword())) {
				user.setPassword(new Sha512Hash(user.getPassword(), user.getId()).toHex());
			}
			userDao.updateUser(user);
		}
		//处理角色
		if(StringUtils.isNotBlank(user.getId())) {
			userDao.deleteUserRole(user.getId());
			if (user.getRoleList() != null && user.getRoleList().size() > 0){
				userDao.insertUserRole(user);
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		user.setLoginIp("");
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}
	
	@Override
	public List<User> findAllUser() {
		return userDao.findAllList();
	}

	@Override
	public List<User> findUser(Map<String,Object> params){
		List<User> list = userDao.findList(params);
		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(String id) {
		userDao.deleteUser(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateUseable(User user) {
		userDao.updateUser(user);
	}

	@Override
	public User getUserById(String id) {
		if(StringUtils.isNotBlank(id)) {
			User user = userDao.getByPrimaryKey(id);
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("userId", user.getId());
			List<Role> roles = roleDao.findList(params);
			user.setRoleList(roles);
			return user;
		}
		return null;
	}

	/**
	 * 根据UserId查询用户
	 */
    @Override
    @Transactional(readOnly = false)
    public User getUserByuserId(String userId) {
            return  userDao.getByPrimaryKey(userId);
    }
}

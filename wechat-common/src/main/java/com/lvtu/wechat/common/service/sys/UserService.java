package com.lvtu.wechat.common.service.sys;

import java.util.List;
import java.util.Map;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.sys.User;

@RemoteService("remoteUserService")
public interface UserService {

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName);
	
	/**
	 * 根据id获取用户
	 * @param id
	 * @return
	 */
	public User getUserById(String id);
	
	/**
	 * 更新用户登录信息
	 * @param user
	 */
	public void updateUserLoginInfo(User user);
	
	/**
	 * 无分页查询用户信息
	 * @param user
	 * @return
	 */
	public List<User> findAllUser();
	
	/**
	 * 无分页查询用户信息
	 * @param user
	 * @return
	 */
	public List<User> findUser(Map<String,Object> params);
	
	/**
	 * 删除用户
	 * @param user
	 */
	public void delete(String id);
	
	/**
	 * 更新用户是否可用
	 * @param user
	 */
	public void updateUseable(User user);
	
	/**
	 * 保存用户
	 * @param user
	 */
	public void save(User user);

	/**
	 * 
	* @Title: getUserByuserId 
	* @Description: TODO(根据UserId查询用户) 
	* @param @param userId
	* @param @return    设定文件 
	* @return User    返回类型 
	* @throws
	 */
    public User getUserByuserId(String userId);
}

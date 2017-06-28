package com.lvtu.wechat.dao.sys.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.sys.User;

@Repository
public class UserDao extends BaseIbatisDAO {
	
	public UserDao() {
		super("sys_user");
	}
	
	public User getByPrimaryKey(String id) {
		return super.get("getByPrimaryKey", id);
	}
	
	public List<User> findAllList() {
		return super.getList("findAllList");
	}
	
	public List<User> findList(Map<String,Object> params) {
		return super.getList("findList", params);
	}
	
	public User getByLoginName(String loginName) {
		return super.get("getByLoginName", loginName);
	}
	
	public void insert(User user) {
		super.insert("insert", user);
	}
	
	public void updateUser(User user) {
		super.update("update", user);
	}

	public void deleteUserRole(String userId) {
		super.delete("deleteUserRole", userId);
	}

	public void insertUserRole(User user) {
		super.insert("insertUserRole", user);
	}

	public void updateLoginInfo(User user) {
		super.update("updateLoginInfo", user);
	}

	public void deleteUser(String id) {
		super.update("delete", id);
	}
}

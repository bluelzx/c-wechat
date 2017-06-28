package com.lvtu.wechat.dao.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.sys.Menu;

/**
 * 系统菜单DAO
 * 
 * @author xuyao
 * 
 */
@Repository
public class MenuDao extends BaseIbatisDAO {
	
	public MenuDao() {
		super("sys_menu");
	}

	public List<Menu> findAllList() {
		return super.getList("findAllList");
	}

	public List<Menu> findListByUser(String userId) {
		return super.getList("findByUserId", userId);
	}
	
	public List<Menu> selectAll() {
		return super.getList("selectAll");
	}
	
	public Menu selectById(String id){
		return super.get("get", id);
	}
	
	public void insert(Menu menu){
		super.insert("insert", menu);
	}
	
	public void update(Menu menu){
		super.update("update", menu);
	}
	
	public void delete(String id){
		super.delete("delete", id);
	}
}

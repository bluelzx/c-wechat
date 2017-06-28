package com.lvtu.wechat.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.sys.Menu;
import com.lvtu.wechat.common.model.sys.User;
import com.lvtu.wechat.common.service.sys.MenuService;
import com.lvtu.wechat.common.utils.StringUtil;
import com.lvtu.wechat.dao.sys.dao.MenuDao;

@HessianService("remoteMenuService")
@Service("menuService")
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;

	@Override
	public List<Menu> getMenuList(User user) {
		List<Menu> menuList = null;
		if (user.isAdmin()) {
			menuList = menuDao.findAllList();
		} else {
			menuList = menuDao.findListByUser(user.getId());
		}
		return menuList;
	}
	
	@Override
	public List<Menu> selectAllMenu(){
		return menuDao.selectAll();
	}

	@Override
	public Menu selectById(String id) {
		return menuDao.selectById(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void saveOrUpdate(Menu menu) {
		Menu oldMenu = menuDao.selectById(menu.getId());
		if(null != oldMenu){
			menuDao.update(menu);
		}else{
		 menuDao.insert(menu);	
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteMenu(String id) {
		menuDao.delete(id);		
	}


}

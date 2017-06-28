package com.lvtu.wechat.common.service.sys;

import java.util.List;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.sys.Menu;
import com.lvtu.wechat.common.model.sys.User;

/**
 * 系统菜单服务类
 * @author xuyao
 *
 */
@RemoteService("remoteMenuService")
public interface MenuService {
	
	/**
	 * 获得用户所能看到的菜单列表
	 * @param user
	 * @return
	 */
	public List<Menu> getMenuList(User user);
	
	public List<Menu> selectAllMenu();
	
	public Menu selectById(String id);
	
	public void saveOrUpdate(Menu menu);
	
	public void deleteMenu(String id);
	
}

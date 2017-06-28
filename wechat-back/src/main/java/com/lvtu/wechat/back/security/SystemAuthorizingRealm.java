package com.lvtu.wechat.back.security;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.lvtu.wechat.common.model.sys.Menu;
import com.lvtu.wechat.common.model.sys.Role;
import com.lvtu.wechat.common.model.sys.User;
import com.lvtu.wechat.common.service.sys.MenuService;
import com.lvtu.wechat.common.service.sys.UserService;

public class SystemAuthorizingRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MenuService menuService;
	
	public SystemAuthorizingRealm() {
		setName("systemAuthorizingRealm");
		HashedCredentialsMatcher hcm = new HashedCredentialsMatcher();
		//使用SHA-512 加密
		hcm.setHashAlgorithmName(Sha512Hash.ALGORITHM_NAME);
		setCredentialsMatcher(hcm);
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		User user = userService.getUserByLoginName(principal.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			User nowUser = userService.getUserByLoginName(principal.getLoginName());
			List<Menu> list = menuService.getMenuList(nowUser);
			for (Menu menu : list){
				if (StringUtils.isNotBlank(menu.getPermission())) {
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(menu.getPermission(),",")){
						info.addStringPermission(permission);
					}
				}
			}
			// 添加用户权限
			info.addStringPermission("user");
			// 添加用户角色信息
			for (Role role : user.getRoleList()) {
				info.addRole(role.getEnname());
			}
			// 更新登录IP和时间
			userService.updateUserLoginInfo(user);

			return info;
		} else {
			return null;
		}
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		// 调用操作数据库的方法查询user信息
		User user = userService.getUserByLoginName(token.getUsername());
		if (user != null) {
			if (!user.isUseable()) {
				throw new AuthenticationException("msg:该帐号已禁止登录.");
			}
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(new Principal(user),user.getPassword(), getName());
			info.setCredentialsSalt(ByteSource.Util.bytes(user.getId()));
			return info;
		}
		return null;
	}

	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;

		private String id; // 编号
		private String loginName; // 登录名
		private String name; // 姓名

		public Principal(User user) {
			this.id = user.getId();
			this.loginName = user.getLoginName();
			this.name = user.getName();
		}

		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return id;
		}

	}
}
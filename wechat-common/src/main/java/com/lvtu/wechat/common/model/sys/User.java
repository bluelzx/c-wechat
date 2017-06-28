package com.lvtu.wechat.common.model.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lvtu.wechat.common.base.BaseModel;

/**
 * 用户Entity
 * @author xuyao
 */
public class User extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户登录名
	 */
	@NotBlank(message="登录名不能为空")
	private String loginName;
	/**
	 * 用户密码
	 */
	@JsonIgnore
	@NotBlank(message="密码不能为空")
	private String password;
	/**
	 * 姓名
	 */
	@NotBlank(message="姓名不能为空")
	private String name;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * 电话号码
	 */
	private String phone;
	/**
	 * 最后登录IP
	 */
	private String loginIp;
	/**
	 * 最后登录日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date loginDate;
	/**
	 * 是否可用
	 */
	private boolean useable;	// 是否允许登陆

	private String oldPassword;	// 新密码
	
	private List<Role> roleList = new ArrayList<Role>(); // 拥有角色列表
	
	 private String delFlags;


	public User() {
		super();
	}
	
	public User(String id){
		super(id);
	}

	public User(String id, String loginName){
		super(id);
		this.loginName = loginName;
	}

	public String getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRemarks() {
		return remarks;
	}
	
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	@JsonIgnore
	public List<Role> getRoleList() {
		return roleList;
	}
	
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@JsonIgnore
	public List<String> getRoleIdList() {
		List<String> roleIdList = new ArrayList<String>();
		for (Role role : roleList) {
			roleIdList.add(role.getId());
		}
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		roleList = new ArrayList<Role>();
		for (String roleId : roleIdList) {
			Role role = new Role();
			role.setId(roleId);
			roleList.add(role);
		}
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		List<String> roleNames = new ArrayList<String>();
		for(Role role : roleList) {
			roleNames.add(role.getName());
			roleNames.add(",");
		}
		if(roleNames.size() > 0)//移除最后一个','
			roleNames.remove(roleNames.size() - 1);

		return roleNames.toString();
	}
	
	public boolean isAdmin(){
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id){
		return id != null && "1".equals(id);
	}
	
	@Override
	public String toString() {
		return id;
	}

	public boolean isUseable() {
		return useable;
	}

	public void setUseable(boolean useable) {
		this.useable = useable;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

    public String getDelFlags() {
        return delFlags;
    }

    public void setDelFlags(String delFlags) {
        this.delFlags = delFlags;
    }

}
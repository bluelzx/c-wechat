package com.lvtu.wechat.common.model.third;

import org.hibernate.validator.constraints.NotBlank;

import com.lvtu.wechat.common.base.BaseModel;
import com.lvtu.wechat.common.enums.ForwardType;

/**
 * 第三方平台
 * 
 * @author xuyao
 * 
 */
public class ThirdPlatform extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@NotBlank(message="平台名称不能为空")
	private String name;
	/**
	 * 安全域名
	 */
	@NotBlank(message="安全域名不能为空")
	private String domain;
	/**
	 * 和微信通信的url地址
	 */
	@NotBlank(message="服务器url地址不能为空")
	private String apiUrl;
	/**
	 * 和微信通信的token
	 */
	@NotBlank(message="服务器token不能为空")
	private String apiToken;
	/**
	 * 消息转发类型
	 */
	private ForwardType forwardType;
	/**
	 * 转发匹配关键字
	 */
	private String forwardKeyword;
	/**
	 * 是否可用
	 */
	private boolean useable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public ForwardType getForwardType() {
		return forwardType;
	}

	public void setForwardType(ForwardType forwardType) {
		this.forwardType = forwardType;
	}

	public String getForwardKeyword() {
		return forwardKeyword;
	}

	public void setForwardKeyword(String forwardKeyword) {
		this.forwardKeyword = forwardKeyword;
	}

	public boolean isUseable() {
		return useable;
	}

	public void setUseable(boolean useable) {
		this.useable = useable;
	}
}

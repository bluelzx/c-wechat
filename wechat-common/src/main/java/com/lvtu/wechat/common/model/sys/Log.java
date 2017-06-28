package com.lvtu.wechat.common.model.sys;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 日志Entity
 * @author xuyao
 */
public class Log extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String title;		// 日志标题
	private String remoteAddr; 	// 操作用户的IP地址
	private String requestUri; 	// 操作的URI
	private String params; 		// 操作提交的数据
	private String operator;      // 创建者
	private Date createDate;    // 创建时间
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	/**
	 * 设置请求参数
	 * @param paramMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setParams(Map paramMap){
		if (paramMap == null){
			return;
		}
		StringBuilder params = new StringBuilder();
		for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
			if(param.getKey().contains("password"))
				continue;
			params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
			String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
			params.append(paramValue);
		}
		this.params = params.toString();
	}
}
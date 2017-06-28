package com.lvtu.wechat.common.model.comm;

import java.io.Serializable;

public class ApiResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1322549783804835520L;

	/**
	 * 状态码 1为成功
	 */
	private String code = "1";

	/**
	 * 消息描述
	 */
	private String msg = "";

	/**
	 * 实际返回数据
	 */
	private T data;

	public ApiResponse() {

	}
	
	public ApiResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}

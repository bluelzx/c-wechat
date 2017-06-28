package com.lvtu.wechat.common.exception;

/**
 * 自定义异常
 * 
 * @author xuyao
 *
 */
public class CustomerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7366772038881993618L;

	public CustomerException() {
		super();
	}

	public CustomerException(String msg) {
		super(msg);
	}
}

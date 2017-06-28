package com.lvtu.wechat.common.base;

import java.io.Serializable;

/**
 * 查询条件基类
 * @author xuyao
 *
 */
public abstract class BaseCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 当前页数
	 */
	private int page = 1;

	/**
	 * 每页显示记录数
	 */
	private int pageSize = 10;
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}

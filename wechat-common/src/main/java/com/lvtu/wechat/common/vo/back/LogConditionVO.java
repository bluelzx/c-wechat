package com.lvtu.wechat.common.vo.back;

import com.lvtu.wechat.common.base.BaseCondition;

/**
 * 日志查询条件vo
 * 
 * @author xuyao
 *
 */
public class LogConditionVO extends BaseCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 775961162896069214L;

	/**
	 * 开始时间
	 */
	private String beginDate;

	/**
	 * 结束时间
	 */
	private String endDate;

	/**
	 * 请求地址
	 */
	private String requestUri;

	/**
	 * 操作者用户名
	 */
	private String operator;

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
}
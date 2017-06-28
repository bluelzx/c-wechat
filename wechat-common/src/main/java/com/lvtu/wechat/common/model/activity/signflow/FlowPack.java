package com.lvtu.wechat.common.model.activity.signflow;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 运营商支持的流量包
 * 
 * @author xuyao
 *
 */
public class FlowPack extends BaseModel {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "流量包ID不能为空")
	private String flowCode;

	@Min(value = 1, message = "流量不能小于1")
	private Integer flow;

	@NotBlank(message = "运营商不能为空")
	private String operator;

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode == null ? null : flowCode.trim();
	}

	public Integer getFlow() {
		return flow;
	}

	public void setFlow(Integer flow) {
		this.flow = flow;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator == null ? null : operator.trim();
	}
}
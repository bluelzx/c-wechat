package com.lvtu.wechat.common.model.activity.ordershare;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 订单分享抢优惠券
 * 
 * @author xuyao
 *
 */
public class ShareOrderCoupon extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2742876996312440489L;

	/**
	 * 优惠券代码
	 */
	@NotBlank(message="优惠券批次号不能为空")
	private String code;

	/**
	 * 优惠券金额
	 */
	@NotNull(message="优惠券优惠金额不能为空")
	private Integer amount;

	/**
	 * 详细描述
	 */
	@NotBlank(message="优惠券明细不能为空")
	private String detail;

	/**
	 * 抽中几率
	 */
	@NotNull(message="中奖几率不能为空")
	private Integer probability;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code == null ? null : code.trim();
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail == null ? null : detail.trim();
	}

	public Integer getProbability() {
		return probability;
	}

	public void setProbability(Integer probability) {
		this.probability = probability;
	}
}
package com.lvtu.wechat.common.vo.back;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseCondition;

/**
 * @ClassName: ProductOrderCondition
 * @Description: 甩尾产品订阅详情条件vo
 * @author zhengchongxiang
 * @date 2016-8-26 下午1:54:33
 */
public class ProductOrderConditionVo extends BaseCondition {

	/**
	 * 序列化Id
	 */
	private static final long serialVersionUID = 4627120070377345148L;

	/**
	 * 用户openid
	 */
	private String openid;

	/**
	 * 出境游订阅
	 */
	private boolean outboundTourism;

	/**
	 * 国内游订阅
	 */
	private boolean inboundTourism;

	/**
	 * 特价机票订阅
	 */
	private boolean specialTicket;
	
	/** 
	* 亲子游
	*/ 
	private boolean homeTourism;
	
	/** 
	* 特价景点门票
	*/ 
	private boolean hotTicket;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	private Integer provinceId;
	
	private Integer cityId;
	
	private String cityIds;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public boolean isOutboundTourism() {
		return outboundTourism;
	}

	public void setOutboundTourism(boolean outboundTourism) {
		this.outboundTourism = outboundTourism;
	}

	public boolean isInboundTourism() {
		return inboundTourism;
	}

	public void setInboundTourism(boolean inboundTourism) {
		this.inboundTourism = inboundTourism;
	}

	public boolean isSpecialTicket() {
		return specialTicket;
	}

	public void setSpecialTicket(boolean specialTicket) {
		this.specialTicket = specialTicket;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityIds() {
		return cityIds;
	}

	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	public boolean isHomeTourism() {
		return homeTourism;
	}

	public void setHomeTourism(boolean homeTourism) {
		this.homeTourism = homeTourism;
	}

	public boolean isHotTicket() {
		return hotTicket;
	}

	public void setHotTicket(boolean hotTicket) {
		this.hotTicket = hotTicket;
	}

}

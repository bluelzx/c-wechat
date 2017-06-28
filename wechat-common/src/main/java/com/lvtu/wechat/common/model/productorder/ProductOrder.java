package com.lvtu.wechat.common.model.productorder;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

/** 
* @ClassName: ProductOrder 
* @Description: 甩尾产品订阅信息实体
* @author zhengchongxiang
* @date 2016-9-5 下午4:59:53  
*/
public class ProductOrder extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4694970278690374754L;

	/**
	 * 微信openid
	 */
	private String openid;

	/**
	 * 出境游订阅
	 */
	private boolean outboundTourism = false;

	/**
	 * 国内游订阅
	 */
	private boolean inboundTourism = false;

	/**
	 * 特价机票订阅
	 */
	private boolean specialTicket = false;
	
	private boolean homeTourism = false;
	
	private boolean hotTicket = false;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/** 
	* 更新时间 
	*/ 
	private Date updateTime;
	
	/** 
	* 用户微信名 
	*/ 
	private String nickName;	
	
	/** 
	*  所属省份ID
	*/ 
	private Integer provinceId;
	
	/** 
	* 归属城市ID
	*/ 
	private Integer cityId;

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

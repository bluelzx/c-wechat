package com.lvtu.wechat.common.model.productorder;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

/** 
* @ClassName: AreaGroup 
* @Description: 地区分组实体类
* @author zhengchongxiang
* @date 2017-3-13 下午3:49:54  
*/
public class AreaGroup extends BaseModel {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -813606139977347832L;

	/** 
	* @Fields name : 分组名称
	*/ 
	private String name;

	private String cityNames;

	/** 
	* @Fields cityIds : 城市ID组合 
	*/ 
	private String cityIds;

	private Date createTime;
	
	private String orderNum;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityNames() {
		return cityNames;
	}

	public void setCityNames(String cityNames) {
		this.cityNames = cityNames;
	}

	public String getCityIds() {
		return cityIds;
	}

	public void setCityIds(String cityIds) {
		this.cityIds = cityIds;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	
}

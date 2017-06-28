package com.lvtu.wechat.common.model.productorder;

import com.lvtu.wechat.common.base.BaseModel;

/** 
* @ClassName: City 
* @Description: 城市实体
* @author zhengchongxiang
* @date 2017-3-13 下午3:51:09  
*/
public class City extends BaseModel{
	
	/** 
	* @Fields serialVersionUID : TODO 
	*/ 
	private static final long serialVersionUID = -3616773177451894469L;

	/** 
	* @Fields code : 城市对应code 
	*/ 
	private Integer code;
	
	/** 
	* @Fields provinceId : 所属省份ID
	*/ 
	private Integer provinceId;
	
	/** 
	* @Fields name : 城市名 
	*/ 
	private String name;


	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	
	

}

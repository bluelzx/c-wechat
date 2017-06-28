package com.lvtu.wechat.common.model.productorder;

import com.lvtu.wechat.common.base.BaseModel;

/** 
* @ClassName: Province 
* @Description: 省份实体类
* @author zhengchongxiang
* @date 2017-3-13 下午3:52:47  
*/
public class Province extends BaseModel{

	/** 
	* 
	*/ 
	private static final long serialVersionUID = -257689131009273180L;
	
	/** 
	* 省份ID 
	*/ 
	private Integer provinceId;
	
	/** 
	* 省份名称 
	*/ 
	private String provinceName;

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	
	

}

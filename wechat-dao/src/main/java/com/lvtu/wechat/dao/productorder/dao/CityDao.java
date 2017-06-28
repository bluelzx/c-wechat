package com.lvtu.wechat.dao.productorder.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.productorder.City;

/** 
* @ClassName: CityDao 
* @Description: 城市分组dao层操作
* @author zhengchongxiang
* @date 2017-3-16 下午4:46:45  
*/
@Repository
public class CityDao  extends BaseIbatisDAO{
	
	public CityDao (){
		super("CITY");
	}

	public City selectByName(String name){
		return super.get("selectByName", name);
	}
	
	public List<City> selectByParentId(Integer provinceId){
		return super.getList("selectByParentId", provinceId);
	}
	
	public City selectByLikeName(City city){
		return super.get("selectByLikeName", city);
	}
	
	public List<City> selectByIds(String ids){
		return super.getList("selectByIds", ids);
	}
	

}

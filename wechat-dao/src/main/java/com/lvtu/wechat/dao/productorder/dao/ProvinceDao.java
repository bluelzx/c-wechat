package com.lvtu.wechat.dao.productorder.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.productorder.Province;

/** 
* @ClassName: ProvinceDao 
* @Description: 省份分组dao层操作
* @author zhengchongxiang
* @date 2017-3-16 下午4:47:17  
*/
@Repository
public class ProvinceDao extends BaseIbatisDAO{
	
	public ProvinceDao(){
		
		super("PROVINCE");
	}
	
	public List<Province> selectAll(){
		return super.getList("selectAll");
	}
	
	public Integer selectByName(String name){
		return super.get("selectByName", name);
	}

}

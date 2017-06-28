package com.lvtu.wechat.dao.productorder.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.productorder.AreaGroup;

/** 
* @ClassName: AreaGroupDao 
* @Description: 地区分组dao层操作
* @author zhengchongxiang
* @date 2017-3-16 下午4:46:30  
*/
@Repository
public class AreaGroupDao extends BaseIbatisDAO{
	
	public AreaGroupDao(){
		super("AREA_GROUP");
	}
	
	public List<AreaGroup> selectAll(){
		return super.getList("selectAll");
	}
	
	public void save(AreaGroup areaGroup){
		super.insert("insert", areaGroup);
	}
	
	public void update(AreaGroup areaGroup){
		super.update("update", areaGroup);
	}
	
	public void delete(String id){
		super.delete("delete", id);
	}
	
	public AreaGroup selectById(String id){
		return super.get("selectById", id);
	}
	
	public List<AreaGroup> selectByIds(String ids){
		return super.getList("selectByIds", ids);
	}

}

package com.lvtu.wechat.dao.activity.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowGroups;
import com.lvtu.wechat.common.vo.back.FlowGroupsConditionVo;

@Repository
public class FlowGroupsDao extends BaseIbatisDAO{
	
	public FlowGroupsDao(){
		super("FLOW_GROUPS");
	}
	
	public FlowGroups save(FlowGroups flowGroups){
		return (FlowGroups) super.insert("insert", flowGroups);
	}
	
	public Integer update(FlowGroups flowGroups){
		return super.update("update", flowGroups);
	}
	
	public FlowGroups queryById(String id){
		return super.get("queryById", id);
	}
	
	public List<FlowGroups> queryByCondition(FlowGroupsConditionVo vo){
		return super.getList("queryByCondition", vo);
	}

}

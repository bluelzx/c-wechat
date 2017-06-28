package com.lvtu.wechat.dao.activity.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivity;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivityGroups;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivityMembers;
import com.lvtu.wechat.common.vo.back.FlowActivityConditionVo;

@Repository
public class FlowActivityDao extends BaseIbatisDAO {
	
	public FlowActivityDao(){
		super("FLOW_ACTIVITY");
	}
	
	public void save(FlowActivity flowActivity){
		super.insert("insertActivity", flowActivity);
	}

	public void update(FlowActivity flowActivity){
		super.update("updateActivity", flowActivity);
	}
	
	public List<FlowActivity> query(FlowActivityConditionVo vo){
		return super.getList("queryActivity", vo);
	}
	
	public FlowActivity queryById(String id){
		return super.get("queryById", id);
	}
	
	public Integer deleteById(String id){
		return super.delete("deleteActivity", id);
	}
	
	public List<FlowActivityGroups> queryGroupById(FlowActivityConditionVo vo){
		return super.getList("queryGroupById", vo);
	}
	
	public List<FlowActivityMembers> queryDetailMembers(FlowActivityConditionVo vo){
		return super.getList("queryDetailMembers",vo);
	}
	
	public FlowActivity queryActivityQrCode(String id){
		return super.get("selectActivityQrCode", id);
	}
	
	public List<String> queryActivityIds(){
		return super.getList("queryActivityIds");
	}
}

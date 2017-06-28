package com.lvtu.wechat.dao.activity.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowForwardNumber;

@Repository
public class FlowForwardNumberDao extends BaseIbatisDAO {
	
	public FlowForwardNumberDao(){
		super("FLOW_FORWARD_NUMBER");
	}
	
	public FlowForwardNumber save(FlowForwardNumber flowForwardNumber){
		return (FlowForwardNumber) super.insert("insert", flowForwardNumber);
	}

	public Integer update(FlowForwardNumber flowForwardNumber){
		return super.update("update", flowForwardNumber);
	}
	
	public FlowForwardNumber selectByActivityId(String flowActivityId){	
		 List<FlowForwardNumber> list =  super.getList("selectByActivityId", flowActivityId);
		 if(null != list && list.size()>0){
			 return list.get(0);
		 }
		 return null;
	}

}

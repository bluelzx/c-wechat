package com.lvtu.wechat.dao.activity.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowMembers;
import com.lvtu.wechat.common.vo.back.FlowMembersConditionVo;

@Repository
public class FlowMembersDao extends BaseIbatisDAO {
	
	public FlowMembersDao(){
		super("FLOW_MEMBERS");
	}

	public List<FlowMembers> queryByOpenid(FlowMembersConditionVo vo) {
		return super.getList("queryByOpenid", vo);
	}

	public FlowMembers save(FlowMembers flowMembers) {
		return (FlowMembers) super.insert("insert", flowMembers);
	}

	public Integer update(FlowMembers flowMembers) {
		return super.update("update", flowMembers);
	}
	
	public List<FlowMembers> queryByGroupId(String groupId){
		return super.getList("queryByGroupId", groupId);
	}

}

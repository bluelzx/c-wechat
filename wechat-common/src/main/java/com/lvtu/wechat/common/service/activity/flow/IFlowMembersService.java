package com.lvtu.wechat.common.service.activity.flow;

import java.util.List;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowMembers;
import com.lvtu.wechat.common.vo.back.FlowMembersConditionVo;

@RemoteService("flowMembersService")
public interface IFlowMembersService {
	
	public List<FlowMembers> queryMembersByOpenid(FlowMembersConditionVo vo);
		
	public List<FlowMembers> queryByGroupId(String groupId);


}

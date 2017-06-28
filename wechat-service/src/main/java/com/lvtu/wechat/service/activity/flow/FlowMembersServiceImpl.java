package com.lvtu.wechat.service.activity.flow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowMembers;
import com.lvtu.wechat.common.service.activity.flow.IFlowMembersService;
import com.lvtu.wechat.common.vo.back.FlowMembersConditionVo;
import com.lvtu.wechat.dao.activity.flow.dao.FlowMembersDao;

/** 
* @ClassName: FlowMembersServiceImpl 
* @Description: 裂变流量参与用户service
* @author zhengchongxiang
* @date 2016-10-17 下午2:49:44  
*/
@HessianService("flowMembersService")
@Service("flowMembersService")
@Transactional(readOnly = true)
public class FlowMembersServiceImpl implements IFlowMembersService {

	@Autowired
	private FlowMembersDao flowMembersDao;

	@Override
	public List<FlowMembers> queryMembersByOpenid(FlowMembersConditionVo vo) {
		return flowMembersDao.queryByOpenid(vo);
	}

	@Override
	public List<FlowMembers> queryByGroupId(String groupId) {
		return  flowMembersDao.queryByGroupId(groupId);
	}

}
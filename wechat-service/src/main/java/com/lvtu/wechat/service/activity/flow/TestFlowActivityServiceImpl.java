package com.lvtu.wechat.service.activity.flow;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivity;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivityGroups;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivityMembers;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowForwardNumber;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowGroups;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowMembers;
import com.lvtu.wechat.common.model.activity.group.GroupActivityBanner;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.flow.TestIFlowActivityService;
import com.lvtu.wechat.common.utils.RandomUtils;
import com.lvtu.wechat.common.vo.back.FlowActivityConditionVo;
import com.lvtu.wechat.common.vo.back.FlowGroupsConditionVo;
import com.lvtu.wechat.common.vo.back.FlowMembersConditionVo;
import com.lvtu.wechat.dao.activity.flow.dao.FlowActivityDao;
import com.lvtu.wechat.dao.activity.flow.dao.FlowForwardNumberDao;
import com.lvtu.wechat.dao.activity.flow.dao.FlowGroupsDao;
import com.lvtu.wechat.dao.activity.flow.dao.FlowMembersDao;
import com.lvtu.wechat.dao.activity.group.dao.GroupActivityBannerDAO;
import com.lvtu.wechat.dao.activity.signflow.dao.FlowDAO;

@HessianService("testflowActivityService")
@Service("testflowActivityService")
@Transactional(readOnly = true)
public class TestFlowActivityServiceImpl implements TestIFlowActivityService {

	@Autowired
	private FlowActivityDao flowActivityDao;

	@Autowired
	private GroupActivityBannerDAO groupActivityBannerDAO;
	
	@Autowired
	private FlowGroupsDao flowGroupsDao;
	
	@Autowired
	private FlowMembersDao flowMembersDao;
	
	@Autowired
	private FlowDAO flowDao;
	
	@Autowired
	private FlowForwardNumberDao flowForwardNumberDao;

	@Override
	@Transactional(readOnly = false)
	public void saveOrUpdateFlowActivity(FlowActivity flowActivity) {
		if (StringUtils.isBlank(flowActivity.getId())) {
			flowActivity.preInsert();
			flowActivity.setCreateTime(new Date());
			flowActivityDao.save(flowActivity);
		} else {
			flowActivityDao.update(flowActivity);
		}
	}

	@Override
	public PageInfo<FlowActivity> queryFlowActivity(FlowActivityConditionVo vo) {
		PageHelper.startPage(vo.getPage(), vo.getPageSize());
		List<FlowActivity> list = flowActivityDao.query(vo);
		PageInfo<FlowActivity> pageInfo = new PageInfo<FlowActivity>(list);
		return pageInfo;
	}

	@Override
	public FlowActivity queryFLowActivityById(String id) {
		FlowActivity flowActivity = flowActivityDao.queryById(id);
		if (null != flowActivity) {
			flowActivity.setRuleCopy(StringEscapeUtils
					.unescapeHtml4((flowActivity.getRuleCopy())));
		}
		return flowActivity;
	}

	@Override
	public List<GroupActivityBanner> queryBannersById(String id) {
		List<GroupActivityBanner> list = groupActivityBannerDAO
				.getBannerByGroupId(id);
		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean deleteFlowActivity(String id) {
		boolean flag = false;
		if (flowActivityDao.deleteById(id) > 0) {
			flag = true;
		}
		return flag;
	}
	@Override
	public FlowGroups queryGroupsById(String groupId){
		return flowGroupsDao.queryById(groupId);
	}
	@Override
	public PageInfo<FlowActivityGroups> queryActivityGroupById(FlowActivityConditionVo vo){
		PageHelper.startPage(vo.getPage(), vo.getPageSize());
		List<FlowActivityGroups> list = flowActivityDao.queryGroupById(vo);
		PageInfo<FlowActivityGroups> pageInfo = new PageInfo<FlowActivityGroups>(list);
		return pageInfo;
	}
	@Override
	public PageInfo<FlowActivityMembers> queryDetailMembers(FlowActivityConditionVo vo){
		PageHelper.startPage(vo.getPage(),vo.getPageSize());
		List<FlowActivityMembers> list = flowActivityDao.queryDetailMembers(vo);
		PageInfo<FlowActivityMembers> pageInfo = new PageInfo<FlowActivityMembers>(list);
		return pageInfo;
	}
	//开团
	@Override
	@Transactional(readOnly = false)
	public FlowGroups openGroup(FlowActivity flowActivity, WechatUser userInfo) throws CustomerException {
		FlowGroupsConditionVo vo = new FlowGroupsConditionVo();
		vo.setOpenid(userInfo.getOpenid());
		vo.setFlowActivityId(flowActivity.getId());
		List<FlowGroups> list = flowGroupsDao.queryByCondition(vo);
		if(null != list && list.size()>0){
			throw new CustomerException("只能开团一次！");
		}
		int flowNum = 0;
		try {
			flowNum = RandomUtils.randomNum(((flowActivity.getTotalFlow() / flowActivity.getTeamNum())/4)
					,((flowActivity.getTotalFlow() / flowActivity.getTeamNum())/2));
			if(flowNum == 0){
				flowNum = 1;
			}
		} catch (Exception e) {
			flowNum = 1;
			e.printStackTrace();
		}		
		FlowGroups flowGroups = new FlowGroups();
		flowGroups.preInsert();
		flowGroups.setFlowActivityId(flowActivity.getId());
		flowGroups.setTotalFlow(flowActivity.getTotalFlow());
		flowGroups.setTotalNum(flowActivity.getTeamNum());
		flowGroups.setNowNum(1);
		flowGroups.setSurplusFlow(flowActivity.getTotalFlow() - flowNum);
		flowGroups.setOpenid(userInfo.getOpenid());
		flowGroups.setStartTime(new Date());
		flowGroups.setState(0);// TODO 进行中
		flowGroups.setCreateTime(new Date());
		if(flowActivity.getTeamNum().equals(1)){
			flowNum = flowActivity.getTotalFlow();
			flowGroups.setSurplusFlow(0);
			flowGroups.setCompleteTime(new Date());
			flowGroups.setState(1);// TODO 进行中
		}
		FlowGroups newflowGroups = flowGroupsDao.save(flowGroups);//新增团队信息
		FlowMembers flowMembers = new FlowMembers();
		flowMembers.preInsert();
		flowMembers.setActivityId(flowActivity.getId());
		flowMembers.setGroupId(newflowGroups.getId());
		flowMembers.setOpenid(userInfo.getOpenid());
		flowMembers.setIsLeader(1);// TODO 开团者
		flowMembers.setFlowNum(flowNum);
		flowMembers.setCreateTime(new Date());
		Integer doleNum = RandomUtils.randomNum(1,5);
		flowMembers.setDoleCopy(flowActivity.getDoleCopy(doleNum));
		flowMembersDao.save(flowMembers);//新增成员信息（开团者）
		addFlow(userInfo.getOpenid(), flowNum);//增加流量
		return flowGroups;
	}
	
	
	//参团
	@Override
	@Transactional(readOnly = false)
	public FlowMembers joinGroup(FlowGroups flowGroups, FlowActivity flowActivity,
			WechatUser userInfo) throws CustomerException {		
		FlowMembers flowMembers = new FlowMembers();
		if (StringUtils.isNotBlank(flowGroups.getId())) {
			flowGroups = flowGroupsDao.queryById(flowGroups.getId());
			FlowMembersConditionVo vo = new FlowMembersConditionVo();
			vo.setOpenid(userInfo.getOpenid());
			vo.setGroupId(flowGroups.getId());
			List<FlowMembers> list = flowMembersDao.queryByOpenid(vo);
			if (null != list && list.size() > 0) {
				throw new CustomerException("你已经参加过活动");
			}
			int flowNum = 0;
			try{
				flowNum = RandomUtils.randomNum((flowGroups.getSurplusFlow() / flowGroups.getTotalNum()),
						(flowGroups.getSurplusFlow() / (flowGroups.getTotalNum()-flowGroups.getNowNum())));
				if(flowNum == flowGroups.getSurplusFlow()){
					flowNum = flowNum/2;
				}
				if(flowNum == 0){
					flowNum = 1;
				}
			}catch(Exception e){
				flowNum = 1;
				e.printStackTrace();
			}			
			flowGroups.setNowNum(flowGroups.getNowNum() + 1);
			flowGroups.setSurplusFlow(flowGroups.getSurplusFlow() - flowNum);
			// 满团
			if (flowGroups.getTotalNum().equals(flowGroups.getNowNum())) {
				FlowMembersConditionVo fvo = new FlowMembersConditionVo();
				fvo.setOpenid(flowGroups.getOpenid());
				fvo.setGroupId(flowGroups.getId());
				FlowMembers openMembers = flowMembersDao.queryByOpenid(fvo).get(0);
				openMembers.setFlowNum(openMembers.getFlowNum() + flowGroups.getSurplusFlow());
				flowMembersDao.update(openMembers);
				addFlow(flowGroups.getOpenid(), flowGroups.getSurplusFlow());
				flowGroups.setCompleteTime(new Date());
				flowGroups.setState(1);// TODO 已完成
				//flowGroups.setSurplusFlow(0);// 清空剩余流量(考虑到后续提示，次字段暂时不清零)
			}
			flowGroupsDao.update(flowGroups);			
			flowMembers.preInsert();
			flowMembers.setActivityId(flowActivity.getId());
			flowMembers.setGroupId(flowGroups.getId());
			flowMembers.setOpenid(userInfo.getOpenid());
			flowMembers.setIsLeader(0);// TODO 参团者
			flowMembers.setFlowNum(flowNum);
			flowMembers.setCreateTime(new Date());
			Integer doleNum = RandomUtils.randomNum(1,5);
			flowMembers.setDoleCopy(flowActivity.getDoleCopy(doleNum));
			flowMembers = flowMembersDao.save(flowMembers);
			addFlow(userInfo.getOpenid(), flowNum);
		}
		return flowMembers;
	}
		
	//为用户增加流量
	@Transactional(readOnly = false)
	public void addFlow(String openid, Integer flowNum) {
//		Flow flow = flowDao.selectByOpenid(openid);
//		if (null != flow) {
//			flow.setTotalFlow(flow.getTotalFlow() + flowNum);
//			flow.setSurplusFlow(flow.getSurplusFlow() + flowNum);
//			flowDao.updateByPrimaryKey(flow);
//		} else {
//			flow = new Flow();
//			flow.setGotFirstFlow(false);
//			flow.preInsert();
//			flow.setOpenid(openid);
//			flow.setSurplusFlow(flowNum);
//			flow.setTotalFlow(flowNum);
//			flowDao.addwxFlow(flow);
//		}
	}

	@Override
	public FlowGroups queryGroupsByCondition(FlowGroupsConditionVo vo) {
		List<FlowGroups> list = flowGroupsDao.queryByCondition(vo);
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public FlowActivity queryActivityQrCode(String activityId) {
		return flowActivityDao.queryActivityQrCode(activityId);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateFlowGroups(FlowGroups flowGroups) {
		flowGroupsDao.update(flowGroups);		
	}

	@Override
	@Transactional(readOnly = false)
	public boolean saveOrUpdateForward(FlowForwardNumber flowForwardNumber) {
		if (StringUtils.isBlank(flowForwardNumber.getId())) {
			flowForwardNumber.preInsert();
			flowForwardNumber.setCreateTime(new Date());
			FlowForwardNumber newflowForwardNumber = flowForwardNumberDao.save(flowForwardNumber);
			if (null != newflowForwardNumber) {
				return true;
			}
		} else {
			Integer num = flowForwardNumberDao.update(flowForwardNumber);
			if (num > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> queryActivityIds() {		
		return flowActivityDao.queryActivityIds();
	}
	
	@Override
	public FlowForwardNumber queryForwardNumByActivityId(String activityId){
		return flowForwardNumberDao.selectByActivityId(activityId);
	}

}

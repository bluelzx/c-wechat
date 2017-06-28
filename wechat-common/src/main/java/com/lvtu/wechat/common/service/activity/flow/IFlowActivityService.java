package com.lvtu.wechat.common.service.activity.flow;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivity;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivityGroups;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivityMembers;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowForwardNumber;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowGroups;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowMembers;
import com.lvtu.wechat.common.model.activity.group.GroupActivityBanner;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.vo.back.FlowActivityConditionVo;
import com.lvtu.wechat.common.vo.back.FlowGroupsConditionVo;

@RemoteService("flowActivityService")
public interface IFlowActivityService {
	
	/** 
	* @Title: saveOrUpdateFlowActivity 
	* @Description: 新增、修改活动信息 
	* @param @param flowActivity
	* @param @param bannerUrls    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void saveOrUpdateFlowActivity(FlowActivity flowActivity);
	
	/** 
	* @Title: queryFlowActivity 
	* @Description: 条件查询活动信息 
	* @param @param vo
	* @param @return    设定文件 
	* @return PageInfo<FlowActivity>    返回类型 
	* @throws 
	*/
	public PageInfo<FlowActivity> queryFlowActivity(FlowActivityConditionVo vo);	
	
	/** 
	* @Title: queryFLowActivityById 
	* @Description: 主键查询活动信息
	* @param @param id
	* @param @return    设定文件 
	* @return FlowActivity    返回类型 
	* @throws 
	*/
	public FlowActivity queryFLowActivityById(String id);
	
	/** 
	* @Title: queryBannersById 
	* @Description: 活动banner图片信息 
	* @param @param id
	* @param @return    设定文件 
	* @return List<GroupActivityBanner>    返回类型 
	* @throws 
	*/
	public List<GroupActivityBanner> queryBannersById(String id);
	
	/** 
	* @Title: deleteFlowActivity 
	* @Description: 删除活动
	* @param @param id
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws 
	*/
	public boolean deleteFlowActivity(String id);
	
	/** 
	* @Title: queryGroupsById 
	* @Description: 主键查询团队信息 
	* @param @param groupId
	* @param @return    设定文件 
	* @return FlowGroups    返回类型 
	* @throws 
	*/
	public FlowGroups queryGroupsById(String groupId);
	
	/** 
	* @Title: queryGroupById 
	* @Description: 分组查询活动信息 
	* @param @param vo
	* @param @return    设定文件 
	* @return PageInfo<FlowActivityGroups>    返回类型 
	* @throws 
	*/
	public PageInfo<FlowActivityGroups> queryActivityGroupById(FlowActivityConditionVo vo);
	
	/** 
	* @Title: queryDetailMembers 
	* @Description: 条件查询成员信息 
	* @param @param vo
	* @param @return    设定文件 
	* @return PageInfo<FlowActivityMembers>    返回类型 
	* @throws 
	*/
	public PageInfo<FlowActivityMembers> queryDetailMembers(FlowActivityConditionVo vo);
	
	/** 
	* @Title: openGroup 
	* @Description: 开团
	* @param @param flowActivity
	* @param @param userInfo
	* @param @throws CustomerException    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public FlowGroups openGroup(FlowActivity flowActivity,WechatUser userInfo) throws CustomerException;
	
	/** 
	* @Title: joinGroup 
	* @Description: 参团
	* @param @param flowGroups
	* @param @param flowActivity
	* @param @param userInfo
	* @param @throws CustomerException    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public FlowMembers joinGroup(FlowGroups flowGroups,FlowActivity flowActivity, WechatUser userInfo) throws CustomerException;
	
	/** 
	* @Title: queryByCondition 
	* @Description: 条件查询开团信息 
	* @param @param vo
	* @param @return    设定文件 
	* @return FlowGroups    返回类型 
	* @throws 
	*/
	public FlowGroups queryGroupsByCondition(FlowGroupsConditionVo vo);
	
	/** 
	* @Title: queryActivityQrCode 
	* @Description: 根据活动ID查询二维码信息
	* @param @param activityId
	* @param @return    设定文件 
	* @return FlowActivity    返回类型 
	* @throws 
	*/
	public FlowActivity queryActivityQrCode(String activityId);
	
	public void updateFlowGroups(FlowGroups flowGroups);
	
	public boolean saveOrUpdateForward(FlowForwardNumber flowForwardNumber);
	
	public List<String> queryActivityIds();

	public FlowForwardNumber queryForwardNumByActivityId(String activityId);


}

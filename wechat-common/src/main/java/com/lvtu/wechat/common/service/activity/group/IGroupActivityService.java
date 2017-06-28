package com.lvtu.wechat.common.service.activity.group;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.group.ExportGroupMembers;
import com.lvtu.wechat.common.model.activity.group.Group;
import com.lvtu.wechat.common.model.activity.group.GroupActivity;
import com.lvtu.wechat.common.model.activity.group.GroupActivityBanner;
import com.lvtu.wechat.common.model.activity.group.GroupMembers;
import com.lvtu.wechat.common.vo.back.GroupActivitiesConditionVo;
import com.lvtu.wechat.common.vo.back.GroupMembersConditionVo;

/**
 * 集齐召唤神龙活动表
 * 
 * @author wxlizhi
 */
@RemoteService("groupActivitiesService")
public interface IGroupActivityService {

    /**
     * 根据查询条件获得活动信息
     * @param groupActivitiesConditionVo
     * @return
     */
    PageInfo<GroupActivity> queryGroupActivitiesList(GroupActivitiesConditionVo groupActivitiesConditionVo);

    /**
     * 根据id获得活动信息
     * @param id
     * @return
     */
    GroupActivity queryGroupActivitiesById(String id);

    /**
     * 切换活动状态（启用或禁用）
     * @param groupActivities
     */
    void updateState(GroupActivity groupActivities);

    /**
     * 保存活动
     * @param groupActivitiesform
     * @param jsonBannerUrls 
     * @param jsonPrizes 
     */
    void saveGroupActivityform(GroupActivity groupActivitiesform, JSONObject jsonBannerUrls, JSONObject jsonPrizes);

    /**
     * 根据活动ID查询成员详情
     * @param groupMembersConditionVo
     * @return
     */
    PageInfo<GroupMembers> queryGroupMembersByGroupId(GroupMembersConditionVo groupMembersConditionVo);
    
    /**
     * 根据团成员的id查询成员信息
     * @param groupMemberId
     * @return
     */
    GroupMembers queryMemberInfoById(String groupMemberId);
    
    /**
     * 保存成员信息
     * @param groupMembersForm
     */
    void saveGroupMemberForm(GroupMembers groupMembersForm);

    /**
     * 根据条件查询导出成员信息
     * @param groupMembersConditionVo
     * @return
     */
    PageInfo<ExportGroupMembers> queryExportGroupMembersByGroupId(GroupMembersConditionVo groupMembersConditionVo);
    
    /**
     * 查询成员列表
     * @param groupMember
     * @return
     */
    List<GroupMembers> queryGroupMembers(GroupMembers groupMember);
    /**
     * 删除图片
     * @param bannerImage
     */
    void deleteBannerImage(GroupActivityBanner bannerImage);

    /**
     * 添加groupmembers
     * @param groupMembers
     * @param groupActivity 
     */
    int insertGroupMembers(GroupMembers groupMembers, GroupActivity groupActivity);

    /**
     * 
     * 查询组团成功，需要发送组团提醒的团队
     * @return
     */
    List<Group> queryNeedSendMessageGroup();

    /**
     * 更新group is_send字段
     * @param groupList
     */
    void updateGroupState(List<Group> groupList);

    /**
     * 更新group is_send字段
     * @param group
     */
    void updateGroupState(Group group);


    /**
     * 根据团ID查询团
     * @param groupId
     * @return
     */
	Group queryGroupsByGroupId(String groupId);

	
	/**
	 * 删除团成员
	 * @param groupMember
	 * @param group
	 */
	void delete(GroupMembers groupMember, Group group);

}
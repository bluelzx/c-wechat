package com.lvtu.wechat.service.activity.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.group.ExportGroupMembers;
import com.lvtu.wechat.common.model.activity.group.Group;
import com.lvtu.wechat.common.model.activity.group.GroupActivity;
import com.lvtu.wechat.common.model.activity.group.GroupActivityBanner;
import com.lvtu.wechat.common.model.activity.group.GroupActivityPrize;
import com.lvtu.wechat.common.model.activity.group.GroupMembers;
import com.lvtu.wechat.common.model.qrcode.UseQRCode;
import com.lvtu.wechat.common.service.activity.group.IGroupActivityService;
import com.lvtu.wechat.common.vo.back.GroupActivitiesConditionVo;
import com.lvtu.wechat.common.vo.back.GroupMembersConditionVo;
import com.lvtu.wechat.dao.activity.group.dao.GroupActivityBannerDAO;
import com.lvtu.wechat.dao.activity.group.dao.GroupActivityDAO;
import com.lvtu.wechat.dao.activity.group.dao.GroupActivityPrizeDAO;
import com.lvtu.wechat.dao.activity.group.dao.GroupDAO;
import com.lvtu.wechat.dao.activity.group.dao.GroupMembersDAO;
import com.lvtu.wechat.dao.qrcode.dao.UseQRCodeDAO;

/**
 * @author wxlizhi
 */
@HessianService("groupActivitiesService")
@Service("groupActivitiesService")
@Transactional(readOnly = true)
public class GroupActivityServiceImpl implements IGroupActivityService {

    Logger logger = Logger.getLogger(GroupActivityServiceImpl.class);
    
    @Autowired
    private GroupActivityDAO groupActivitiesDAO;
    
    @Autowired
    private GroupMembersDAO groupMembersDAO;
    
    @Autowired
    private GroupActivityBannerDAO groupActivityBannerDAO;
    
    @Autowired
    private GroupActivityPrizeDAO groupActivityPrizeDAO;
    
    @Autowired
    private GroupDAO groupDAO;
    
    @Autowired
    private UseQRCodeDAO useQRCodeDAO;

    /**
     * 根据查询条件获得活动信息
     * @param groupActivitiesConditionVo
     * @return
     */
    @Override
    public PageInfo<GroupActivity> queryGroupActivitiesList(GroupActivitiesConditionVo groupActivitiesConditionVo) {
        PageHelper.startPage(groupActivitiesConditionVo.getPage(), groupActivitiesConditionVo.getPageSize());
        List<GroupActivity> groupActivitiesList = groupActivitiesDAO.queryGroupActivitiesList(groupActivitiesConditionVo);
        PageInfo<GroupActivity> pageInfo = new PageInfo<GroupActivity>(groupActivitiesList);
        return pageInfo;
    }


    /**
     * 根据id获得活动信息
     * @param id
     * @return
     */
    @Override
    public GroupActivity queryGroupActivitiesById(String id) {
        GroupActivity groupActivity = groupActivitiesDAO.queryGroupActivitiesById(id);
        if (groupActivity != null) {
            groupActivity.bannerList = groupActivityBannerDAO.getBannerByGroupId(id);
            groupActivity.prizeList = groupActivityPrizeDAO.getPrizeByGroupId(id);
        }
        return groupActivity;
    }

    /**
     * 切换活动状态（启用或禁用）
     * @param groupActivities
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void updateState(GroupActivity groupActivities) {
        groupActivitiesDAO.updateState(groupActivities);
    }


    /**
     * 保存活动
     * @param groupActivitiesform
     * @param jsonBannerUrl
     * @param jsonPrizes
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void saveGroupActivityform(GroupActivity groupActivitiesform,JSONObject jsonBannerUrl,JSONObject jsonPrizes) {
        if (StringUtils.isBlank(groupActivitiesform.getId())) {
            groupActivitiesform.preInsert();
            Date now = new Date();
            groupActivitiesform.setCreateDate(now);
            groupActivitiesform=insertImg(groupActivitiesform,jsonBannerUrl,jsonPrizes);
            groupActivitiesDAO.insert(groupActivitiesform);
            if(!groupActivitiesform.bannerList.isEmpty()){
                groupActivityBannerDAO.insert(groupActivitiesform.bannerList);
            }
            if(!groupActivitiesform.prizeList.isEmpty()){
                groupActivityPrizeDAO.insert(groupActivitiesform.prizeList);
            }
        }
        else {
            groupActivitiesform=insertImg(groupActivitiesform,jsonBannerUrl,jsonPrizes);
            UseQRCode useQRCode= new UseQRCode();
            useQRCode.setActId(groupActivitiesform.getId());
            useQRCode.setTitle(groupActivitiesform.getName());
            useQRCode.setUrl("https://weixin.lvmama.com/groupActivity/"+groupActivitiesform.getId());
            useQRCode.setPicUrl(groupActivitiesform.getPicUrl());
            useQRCodeDAO.update(useQRCode);
            groupActivitiesDAO.update(groupActivitiesform);
            if(!groupActivitiesform.bannerList.isEmpty()){
                groupActivityBannerDAO.insert(groupActivitiesform.bannerList);
            }
            if(!groupActivitiesform.prizeList.isEmpty()){
                groupActivityPrizeDAO.deletePrizeByActId(groupActivitiesform.getId());
                groupActivityPrizeDAO.insert(groupActivitiesform.prizeList);
            }
        }
    }
    
    /**
     * 插入图片
     * @param groupActivitiesform
     * @param jsonObject
     * @return
     */
    public GroupActivity insertImg(GroupActivity groupActivitiesform,JSONObject jsonBannerUrl,JSONObject jsonPrizes){
        JSONArray jsonBannerArray = jsonBannerUrl.getJSONArray("bannerImgs");
        JSONArray jsonPrizesArray = jsonPrizes.getJSONArray("prizes");
        GroupActivityBanner groupActivityBanner=null;
        GroupActivityPrize groupActivityPrize=null;
        for (Object url : jsonBannerArray) {
            groupActivityBanner = new GroupActivityBanner();
            groupActivityBanner.setUrl((String) url);
            groupActivityBanner.setGroupActivityId(groupActivitiesform.getId());
            groupActivityBanner.preInsert();
            groupActivitiesform.bannerList.add(groupActivityBanner);
        }
        for (Object prize : jsonPrizesArray) {
            groupActivityPrize = new GroupActivityPrize();
            groupActivityPrize.setGroupActivityId(groupActivitiesform.getId());
            groupActivityPrize.preInsert();
            groupActivityPrize.setPrizeDetail(((JSONObject) prize).getString("prizesDetail"));
            groupActivityPrize.setPrizeLevel(((JSONObject) prize).getString("prizeLevel"));
            groupActivityPrize.setPeopleNum(((JSONObject) prize).getString("peopleNum"));
            groupActivityPrize.setLinkUrl(((JSONObject) prize).getString("linkUrl"));
            groupActivityPrize.setPrizeOrder(((JSONObject) prize).getString("prizeOrder"));
            groupActivityPrize.setUrl(((JSONObject) prize).getString("prizesCopyImgInfo"));
            groupActivitiesform.prizeList.add(groupActivityPrize);
        }
        return groupActivitiesform;
    }


    /**
     * 根据活动ID查询成员详情
     * @param groupMembersConditionVo
     * @return
     */
    @Override
    public PageInfo<GroupMembers> queryGroupMembersByGroupId(GroupMembersConditionVo groupMembersConditionVo) {
        PageHelper.startPage(groupMembersConditionVo.getPage(), groupMembersConditionVo.getPageSize());
        List<GroupMembers> groupActivitiesList = groupMembersDAO.queryGroupMembersByGroupId(groupMembersConditionVo);
        PageInfo<GroupMembers> pageInfo = new PageInfo<GroupMembers>(groupActivitiesList);
        return pageInfo;
    }


    /**
     * 根据团成员的id查询成员信息
     * @param groupMemberId
     * @return
     */
    @Override
    public GroupMembers queryMemberInfoById(String groupMemberId) {
        GroupMembers groupMember = groupMembersDAO.queryMemberInfoById(groupMemberId);
        return groupMember;
    }


    /**
     * 保存成员信息
     * @param groupMembersForm
     */
    @Override
    @Transactional(readOnly = false)
    public void saveGroupMemberForm(GroupMembers groupMembersForm) {
        groupMembersDAO.updateGroupMembers(groupMembersForm);
    }


    /**
     * 根据条件查询导出成员信息
     * @param groupMembersConditionVo
     * @return
     */
    @Override
    public PageInfo<ExportGroupMembers> queryExportGroupMembersByGroupId(GroupMembersConditionVo groupMembersConditionVo) {
        PageHelper.startPage(groupMembersConditionVo.getPage(), 0);
        List<ExportGroupMembers> groupActivitiesList = groupMembersDAO.queryExportGroupMembersByGroupId(groupMembersConditionVo);
        PageInfo<ExportGroupMembers> pageInfo = new PageInfo<ExportGroupMembers>(groupActivitiesList);
        return pageInfo;
    }

    
    /**
     * 删除图片
     * @param bannerImage
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteBannerImage(GroupActivityBanner bannerImage) {
        groupActivityBannerDAO.deleteBannerImage(bannerImage);
    }

    @Override
    public List<GroupMembers> queryGroupMembers(GroupMembers groupMember) {
        return groupMembersDAO.queryGroupMembers(groupMember);
    }


    @Override
    @Transactional(readOnly = false)
    public int insertGroupMembers(GroupMembers groupMembers,GroupActivity groupActivity) {
        Date now = new Date(); 
        Group group = new Group();
        //新团
        if (StringUtils.isBlank(groupMembers.getGroupId())) {
            groupMembers.setIsLeader("1");
            group.preInsert();
            group.setGroupActivityId(groupActivity.getId());
            group.setTotalNum(groupActivity.getTeamNumber());
            group.setStartTime(now);
            groupMembers.preInsert();
            groupMembers.setGroupId(group.getId());
            groupMembers.setCreateDate(now);
            groupDAO.insertGroup(group);
            groupMembersDAO.insertGroupMembers(groupMembers);
        }
        else {
            groupMembers.setIsLeader("0");
            groupMembers.preInsert();
            groupMembers.setCreateDate(now);
            group.setId(groupMembers.getGroupId());
            int result = groupDAO.addParticipantsNum(group);
            if (result == 1) {
                groupMembersDAO.insertGroupMembers(groupMembers);
            }
            return result;
        }
        return 1;
    }

    /**
     * 查询组团成功，需要发送组团提醒的团队
     */
    @Override
    public List<Group> queryNeedSendMessageGroup() {
        return groupDAO.queryNeedSendMessageGroup();
    }


    /**
     * 更新group表状态
     */
    @Override
    @Transactional(readOnly = false)
    public void updateGroupState(List<Group> groupList) {
        groupDAO.updateGroupState(groupList);
    }

    /**
     * 更新group表状态
     */
    @Override
    @Transactional(readOnly = false)
    public void updateGroupState(Group group) {
        List<Group> groupList = new ArrayList<Group>();
        groupList.add(group);
        groupDAO.updateGroupState(groupList);
    }
    

	@Override
	@Transactional(readOnly = false)
	public Group queryGroupsByGroupId(String groupId) {
		Group group = groupDAO.queryGroupsByGroupId(groupId);
		return group;
	}


	@Override
	@Transactional(readOnly = false)
	public void delete(GroupMembers groupMember, Group group) {
		Date now = new Date();
		groupMember.setDeleteDate(now);
	    groupMembersDAO.inserGroupMemberHis(groupMember);
		groupMembersDAO.deleteGroupMember(groupMember.getId());
		if ("1".equals(groupMember.getIsLeader())) {
			group.setDeleteDate(now);
			groupDAO.insertGroupsHis(group);
			groupDAO.deleteGroups(group.getId());
		}
		else {
			groupDAO.updateGroups(group.getId());
		}
	}
}
package com.lvtu.wechat.dao.activity.group.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.group.ExportGroupMembers;
import com.lvtu.wechat.common.model.activity.group.GroupMembers;
import com.lvtu.wechat.common.vo.back.GroupMembersConditionVo;

/**
 * group_members表dao层
 * @author wxlizhi
 *
 */
@Repository
public class GroupMembersDAO extends BaseIbatisDAO{
    public GroupMembersDAO(){
        super("GROUP_MEMBERS");
    }

    
    /**
     * 根据活动ID查询成员详情
     * @param groupMembersConditionVo
     * @return
     */
    public List<GroupMembers> queryGroupMembersByGroupId(GroupMembersConditionVo groupMembersConditionVo) {
        return super.getList("queryGroupMembersByGroupId", groupMembersConditionVo);
    }


    /**
     * 根据团成员的id查询成员信息
     * @param groupMemberId
     * @return
     */
    public GroupMembers queryMemberInfoById(String groupMemberId) {
        return super.get("queryMemberInfoById", groupMemberId);
    }


    /**
     * 更新团成员信息
     * @param groupMembersForm
     */
    public void updateGroupMembers(GroupMembers groupMembersForm) {
        super.update("updateGroupMembers", groupMembersForm);
    }


    /**
     * 根据条件查询导出成员信息
     * @param groupMembersConditionVo
     * @return
     */
    public List<ExportGroupMembers> queryExportGroupMembersByGroupId(GroupMembersConditionVo groupMembersConditionVo) {
        return super.getList("queryExportGroupMembersByGroupId", groupMembersConditionVo);
    }
    
    
    /**
     * 查询成员列表
     * @param groupMembersConditionVo
     * @return
     */
    public List<GroupMembers> queryGroupMembers(GroupMembers groupMember) {
        return super.getList("queryGroupMemberList", groupMember);
    }


    /**
     * 创建群成员
     * @param groupMembers
     */
    public void insertGroupMembers(GroupMembers groupMembers) {
        super.insert("insert", groupMembers);
    }


    /**
     * 
     * 删除成员信息
     * @param groupMembers
     */
	public void deleteGroupMember(String groupMemberId) {
		 super.delete("deleteGroupMember", groupMemberId);
	}

    /**
     * 
     * 插入成员信息到group_members_history
     * @param groupMember
     */
	public void inserGroupMemberHis(GroupMembers groupMember) {
         super.insert("inserGroupMemberHis",groupMember);	
	}

}

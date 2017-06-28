package com.lvtu.wechat.dao.activity.group.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.group.Group;

/**
 * group表dao层
 * @author wxlizhi
 *
 */
@Repository
public class GroupDAO extends BaseIbatisDAO{
    public GroupDAO() {
        super("GROUP");
    }

    /**
     * 插入groups表
     * @param group
     */
    public void insertGroup(Group group) {
       super.insert("insert", group);
    }
    
    /**
     * 参加人员+1
     * @param group
     */
    public int addParticipantsNum(Group group) {
       return super.update("addParticipantsNum", group);
    }

    /**
     * 查询组团成功，需要发送组团提醒的团队
     */
    public List<Group> queryNeedSendMessageGroup() {
        
        return super.getList("needSendMessageGroup");
    }
    
    /**
     * 更新团队状态 is_send
     * @param groupList
     */
    public void updateGroupState(List<Group> groupList) {
        super.update("updateIsSend", groupList);
    }


    public void deletegroupParticipantsNum(String groupMembergroupId) {
		super.update("deletegroupParticipantsNum", groupMembergroupId);
	}
    
    /**
     * 根据团的id查询信息
     * @param groupMemberId
     * @return
     */
    public Group queryGroupsByGroupId(String groupId) {
        return super.get("queryGroupsByGroupId", groupId);
    }

    /**
     * 插入删除的团到历史记录表
     * @param group
     */
	public void insertGroupsHis(Group group) {
		super.insert("insertGroupsHis", group);
		
	}

	/**
	 * 根据团ID删除团
	 * @param groupId
	 */
	public void deleteGroups(String groupId) {
		 super.delete("deleteGroups", groupId);
	}

	/**
	 * 
	 * 修改参与人数
	 * @param groupId
	 */
	public void updateGroups(String groupId) {
		super.update("updateGroups", groupId);
	}

}

package com.lvtu.wechat.dao.activity.group.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.group.GroupActivity;
import com.lvtu.wechat.common.vo.back.GroupActivitiesConditionVo;

/**
 * group_activities表dao层
 * @author wxlizhi
 *
 */
@Repository
public class GroupActivityDAO extends BaseIbatisDAO{
    public GroupActivityDAO(){
        super("GROUP_ACTIVITY");
    }

    /**
     * 根据查询条件获得活动信息
     * @param groupActivitiesConditionVo
     * @return
     */
    public List<GroupActivity> queryGroupActivitiesList(GroupActivitiesConditionVo groupActivitiesConditionVo) {
        return super.getList("queryGroupActivitiesList", groupActivitiesConditionVo);
    }

    
    /**
     * 根据id获得活动信息
     * @param id
     * @return
     */
    public GroupActivity queryGroupActivitiesById(String id) {
        return super.get("queryGroupActivitiesById", id);
    }

    /**
     * 切换活动状态（启用或禁用）
     * @param groupActivities
     */
    public void updateState(GroupActivity groupActivities) {
        super.update("updateState", groupActivities);
    }

    /**
     * 保存活动
     * @param groupActivitiesform
     */
    public void insert(GroupActivity groupActivitiesform) {
        super.insert("insert", groupActivitiesform);
    }
    
    /**
     * 更新活动
     * @param groupActivitiesform
     */
    public void update(GroupActivity groupActivitiesform) {
        super.update("update", groupActivitiesform);
    }
}

package com.lvtu.wechat.dao.activity.group.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.group.GroupActivityPrize;

@Repository
public class GroupActivityPrizeDAO extends BaseIbatisDAO {
    public GroupActivityPrizeDAO(){
        super("GROUP_ACTIVITY_PRIZE");
    }
    
    /**
     * 根据活动表id查询顶部banner集合
     * @param groupActivityId
     * @return
     */
    public List<GroupActivityPrize> getPrizeByGroupId(String groupActivityId) {
        return this.getList("select", groupActivityId);
    }

    /**
     * 插入奖品文案
     * @param prizeList
     */
    public void insert(List<GroupActivityPrize> prizeList) {
        super.insert("insertPrize", prizeList);
    }

    /**
     * 根据活动id删除奖品文案
     * @param id
     */
    public void deletePrizeByActId(String actId) {
        super.delete("deletePrizeByActId", actId);
    }
}

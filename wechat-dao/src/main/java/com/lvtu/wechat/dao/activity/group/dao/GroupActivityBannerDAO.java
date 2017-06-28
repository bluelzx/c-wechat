package com.lvtu.wechat.dao.activity.group.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.group.GroupActivityBanner;

/**
 * 组团活动顶部bannerDAO层
 * @author qianqc
 *
 */
@Repository
public class GroupActivityBannerDAO extends BaseIbatisDAO {

    public GroupActivityBannerDAO(){
        super("GROUP_ACTIVITY_BANNER");
    }
    
    /**
     * 根据活动表id查询顶部banner集合
     * @param groupActivityId
     * @return
     */
    public List<GroupActivityBanner> getBannerByGroupId(String groupActivityId) {
        return this.getList("select", groupActivityId);
    }

    /**
     * 添加顶部banner图片
     * @param bannerList
     */
    public void insert(List<GroupActivityBanner> bannerList) {
        super.insert("insertBannerImage", bannerList);
    }

    /** 
     * 删除图片
     * @param bannerImage
     */
    public void deleteBannerImage(GroupActivityBanner bannerImage) {
        super.delete("deleteBannerImage", bannerImage);
    }
}

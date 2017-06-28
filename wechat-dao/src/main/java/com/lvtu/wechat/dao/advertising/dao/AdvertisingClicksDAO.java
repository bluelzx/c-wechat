package com.lvtu.wechat.dao.advertising.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.advertising.AdvertisingClicks;
import com.lvtu.wechat.common.vo.back.AdvertisingClicksConditionVo;

/**
 * @author zhengchongxiang
 *
 */
@Repository
public class AdvertisingClicksDAO extends BaseIbatisDAO {
    
    public AdvertisingClicksDAO() {
        super("ADVERTISING_CLICKS");
    }

    /**
     * 根据查询条件获取广告位点击量的信息
     * @param advertisingConditionVo
     * @return
     */
    public List<AdvertisingClicks> queryAdvertisingClicksList(AdvertisingClicksConditionVo advertisingClicksConditionVo) {
        return super.getList("queryAdvertisingClicksList", advertisingClicksConditionVo);
    }
    
    /**
     * 根据查询条件获取指定时间段点击量
     * @param advertisingConditionVo
     * @return
     */
    public List<AdvertisingClicks> queryAdsClicksByTime(AdvertisingClicksConditionVo advertisingClicksConditionVo) {
        return super.getList("queryAdsClicksByTime", advertisingClicksConditionVo);
    }

    /**
     * 新增广告位点击
     * @param advertisingform
     */
    public void insert(AdvertisingClicks advertisingClicks) {
    	super.insert("insert", advertisingClicks);
    }
    
    /**
     * 更新广告位点击
     * @param advertisingform
     */
    public void update(AdvertisingClicks advertisingClicks) {
        super.update("update", advertisingClicks);
    }

}

package com.lvtu.wechat.common.service.advertising;


import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.advertising.AdvertisingClicks;
import com.lvtu.wechat.common.vo.back.AdvertisingClicksConditionVo;

/**
 * 广告位点击量
 * @author zhengchongxiang
 *
 */
@RemoteService("advertisingClicksService")
public interface IAdvertisingClicksService {

    /**
     * 查询广告位点击量信息
     * @param advertisingConditionVo
     * @return
     */
    PageInfo<AdvertisingClicks> queryAdsClicksByTime(AdvertisingClicksConditionVo advertisingClicksConditionVo);
    
    /**
     * 保存广告位点击量信息
     * @param advertisingClicks
     * @return
     */
    public void saveAdvertisingClicks(AdvertisingClicks advertisingClicks);
    /**
     * 根据时间和广告位Id查询点击量信息
     * @param advertisingClicks
     * @return
     */
    public List<AdvertisingClicks> queryAdvertisingClicksList(AdvertisingClicksConditionVo advertisingClicksConditionVo);

}

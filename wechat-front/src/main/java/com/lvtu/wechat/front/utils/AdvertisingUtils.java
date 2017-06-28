package com.lvtu.wechat.front.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.lvtu.wechat.common.model.advertising.Advertising;
import com.lvtu.wechat.common.service.advertising.IAdvertisingService;
import com.lvtu.wechat.common.spring.SpringBeanProxy;
import com.lvtu.wechat.common.utils.MemcachedUtil;

/**
 * 广告位工具类
 * @author wxlizhi
 *
 */
public class AdvertisingUtils {
    
    private static Logger log = Logger.getLogger(AdvertisingUtils.class);
    
    /**
     * 获取广告位的信息
     * @param primaryClassification
     * @param secondaryClassification
     * @return
     */
    public static List<Advertising> getAdvertisingInfo(String primaryClassification,String secondaryClassification){
        if(StringUtils.isBlank(primaryClassification) || StringUtils.isBlank(secondaryClassification))
            return null;
        
        //先从缓存中获取广告位信息
        String conditions=primaryClassification+"_"+secondaryClassification;
        List<Advertising> advertisingInfo=getCachedAdvertisingInfo(conditions);
        if(advertisingInfo!=null)
            return advertisingInfo;
        
        //从数据库中获取广告位信息
        IAdvertisingService advertisingService = SpringBeanProxy.getBean(IAdvertisingService.class, "advertisingService");
        advertisingInfo = advertisingService.queryAdvertisingByClassification(primaryClassification,secondaryClassification);
        if(advertisingInfo!=null){
            cacheAdvertisingInfo(conditions,advertisingInfo,7 * 24 * 60 * 60);
            return advertisingInfo;
        }
        return advertisingInfo;
    }

    /**
     * 缓存或更新缓存广告栏信息
     * @param conditions
     * @param advertisingInfo
     * @param i
     */
    private static void cacheAdvertisingInfo(String conditions,List<Advertising> advertisingInfo, int i) {
        if (advertisingInfo == null) {
            return;
        }
        MemcachedUtil mem = MemcachedUtil.getInstance();
        List<Advertising> cachedAdvertisingListInfo = getCachedAdvertisingInfo(conditions);
        if (cachedAdvertisingListInfo != null) {
            // 修改缓存广告位信息
            mem.replace(conditions, i, advertisingInfo);
        } else {
            // 缓存广告位信息
            boolean isAddSuccess = mem.set(conditions, i, advertisingInfo);
            log.info("add cache success=" + isAddSuccess);
        }
        
    }

    /**
     * 从缓存中获取广告位信息
     * @param id
     * @return
     */
    private static List<Advertising> getCachedAdvertisingInfo(String conditions) {
        MemcachedUtil mem=MemcachedUtil.getInstance();
        @SuppressWarnings("unchecked")
        List<Advertising> advertisingObject = (List<Advertising>) mem.get(conditions);
        return advertisingObject;
    }

}

package com.lvtu.wechat.service.activity.advertising;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.advertising.AdvertisingClicks;
import com.lvtu.wechat.common.service.advertising.IAdvertisingClicksService;
import com.lvtu.wechat.common.vo.back.AdvertisingClicksConditionVo;
import com.lvtu.wechat.dao.advertising.dao.AdvertisingClicksDAO;

@HessianService("advertisingClicksService")
@Service("advertisingClicksService")
@Transactional(readOnly = true)
public class AdvertisingClicksServiceImpl implements IAdvertisingClicksService{
    
    Logger logger = Logger.getLogger(AdvertisingClicksServiceImpl.class);
    
    @Autowired
    private AdvertisingClicksDAO advertisingClicksDao;

    @Override
    public PageInfo<AdvertisingClicks> queryAdsClicksByTime(AdvertisingClicksConditionVo advertisingClicksConditionVo) {
        PageHelper.startPage(advertisingClicksConditionVo.getPage(), advertisingClicksConditionVo.getPageSize());
        List<AdvertisingClicks> advertisingClicksList = advertisingClicksDao.queryAdsClicksByTime(advertisingClicksConditionVo);
        PageInfo<AdvertisingClicks> pageInfo = new PageInfo<AdvertisingClicks>(advertisingClicksList);
        return pageInfo;
    }
    
    @Override
    @Transactional(readOnly = false)
    public void saveAdvertisingClicks(AdvertisingClicks advertisingClicks) {
        if (StringUtils.isBlank(advertisingClicks.getId())) {
        	advertisingClicks.preInsert();
        	advertisingClicksDao.insert(advertisingClicks);
        }
        else {
        	advertisingClicksDao.update(advertisingClicks);
        }
    }
    
	@Override
	public List<AdvertisingClicks> queryAdvertisingClicksList(AdvertisingClicksConditionVo advertisingClicksConditionVo) {		
        PageHelper.startPage(advertisingClicksConditionVo.getPage(), advertisingClicksConditionVo.getPageSize());
        List<AdvertisingClicks> advertisingClicksList = advertisingClicksDao.queryAdvertisingClicksList(advertisingClicksConditionVo);
        return advertisingClicksList;
	}

}

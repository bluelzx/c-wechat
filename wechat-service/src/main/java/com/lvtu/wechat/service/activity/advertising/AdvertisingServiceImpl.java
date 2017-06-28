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
import com.lvtu.wechat.common.model.advertising.Advertising;
import com.lvtu.wechat.common.service.advertising.IAdvertisingService;
import com.lvtu.wechat.common.vo.back.AdvertisingConditionVo;
import com.lvtu.wechat.dao.advertising.dao.AdvertisingDAO;

@HessianService("advertisingService")
@Service("advertisingService")
@Transactional(readOnly = true)
public class AdvertisingServiceImpl implements IAdvertisingService{
    
    Logger logger = Logger.getLogger(AdvertisingServiceImpl.class);
    
    @Autowired
    private AdvertisingDAO advertisingDao;

    /**
     * 根据条件查询广告栏的信息
     * 
     * @param advertisingConditionVo
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public PageInfo<Advertising> queryAdvertisingList(AdvertisingConditionVo advertisingConditionVo) {
        PageHelper.startPage(advertisingConditionVo.getPage(), advertisingConditionVo.getPageSize());
        List<Advertising> advertisingList = advertisingDao.queryAdvertisingList(advertisingConditionVo);
        PageInfo<Advertising> pageInfo = new PageInfo<Advertising>(advertisingList);
        return pageInfo;
    }

    
    /**
     * 编辑广告位
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Advertising queryAdvertisingById(String id) {
        Advertising advertising = advertisingDao.queryAdvertisingById(id);
        return advertising;
    }


    /**
     * 根据ID删除广告位
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteAdvertisingById(String id) {
        advertisingDao.deleteAdvertisingById(id);
    }


    /**
     * 保存修改或者添加的广告位
     * @param advertisingform
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void saveAdvertisingform(Advertising advertisingform) {
        if (StringUtils.isBlank(advertisingform.getId())) {
            advertisingform.preInsert();
            advertisingDao.insert(advertisingform);
        }
        else {
            advertisingDao.update(advertisingform);
        }
    }


    /**
     * 根据一级分类和二级分类查询广告位信息
     * @param primaryClassification
     * @param secondaryClassification
     * @return
     */
    @Override
    public List<Advertising> queryAdvertisingByClassification(String primaryClassification, String secondaryClassification) {
        AdvertisingConditionVo advertisingConditionVo =new AdvertisingConditionVo();
        advertisingConditionVo.setPrimaryClassification(primaryClassification);
        advertisingConditionVo.setSecondaryClassification(secondaryClassification);
        List<Advertising> advertisingList = advertisingDao.queryAdvertisingList(advertisingConditionVo);
        return advertisingList;
    }

}

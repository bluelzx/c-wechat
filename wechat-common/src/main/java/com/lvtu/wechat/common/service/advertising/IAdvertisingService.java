package com.lvtu.wechat.common.service.advertising;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.advertising.Advertising;
import com.lvtu.wechat.common.vo.back.AdvertisingConditionVo;

/**
 * 广告位
 * @author wxlizhi
 *
 */
@RemoteService("advertisingService")
public interface IAdvertisingService {

    /**
     * 查询广告位的列表信息
     * @param advertisingConditionVo
     * @return
     */
    PageInfo<Advertising> queryAdvertisingList(AdvertisingConditionVo advertisingConditionVo);

    /**
     * 编辑广告位
     * @param id
     * @return
     */
    Advertising queryAdvertisingById(String id);

    /**
     * 根据ID删除广告位
     * @param id
     */
    void deleteAdvertisingById(String id);

    /**
     * 保存添加或者修改的广告位
     * @param advertisingform
     */
    void saveAdvertisingform(Advertising advertisingform);

    /**
     * 根据一级分类和二级分类查询广告位信息
     * @param primaryClassification
     * @param secondaryClassification
     * @return
     */
    List<Advertising> queryAdvertisingByClassification(String primaryClassification, String secondaryClassification);

}

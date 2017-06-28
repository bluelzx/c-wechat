package com.lvtu.wechat.dao.advertising.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.advertising.Advertising;
import com.lvtu.wechat.common.vo.back.AdvertisingConditionVo;

/**
 * @author wxlizhi
 *
 */
@Repository
public class AdvertisingDAO extends BaseIbatisDAO {
    
    public AdvertisingDAO() {
        super("ADVERTISING");
    }

    /**
     * 根据查询条件获取广告位的信息
     * @param advertisingConditionVo
     * @return
     */
    public List<Advertising> queryAdvertisingList(AdvertisingConditionVo advertisingConditionVo) {
        return super.getList("queryAdvertisingList", advertisingConditionVo);
    }

    /**
     * 编辑广告位
     * @param id
     * @return
     */
    public Advertising queryAdvertisingById(String id) {
        return super.get("queryAdvertisingById", id);
    }

    /**
     * 根据ID删除广告位
     * @param id
     */
    public void deleteAdvertisingById(String id) {
        super.insert("deleteAdvertising", id);
    }

    /**
     * 新增广告位
     * @param advertisingform
     */
    public void insert(Advertising advertisingform) {
        super.insert("insert", advertisingform);
    }

    /**
     * 更新广告位
     * @param advertisingform
     */
    public void update(Advertising advertisingform) {
        super.update("update", advertisingform);
    }

}

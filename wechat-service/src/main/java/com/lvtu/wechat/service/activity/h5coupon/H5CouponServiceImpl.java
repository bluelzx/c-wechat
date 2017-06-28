package com.lvtu.wechat.service.activity.h5coupon;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.enums.StatusType;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.h5coupon.H5Coupon;
import com.lvtu.wechat.common.model.activity.h5coupon.H5CouponRcmdPrd;
import com.lvtu.wechat.common.model.activity.h5coupon.H5CouponRecord;
import com.lvtu.wechat.common.service.activity.h5coupon.IH5CouponService;
import com.lvtu.wechat.common.vo.back.H5CouponConditionVo;
import com.lvtu.wechat.dao.activity.h5coupon.dao.H5CouponDAO;
import com.lvtu.wechat.dao.activity.h5coupon.dao.H5CouponRcmdPrdDAO;
import com.lvtu.wechat.dao.activity.h5coupon.dao.H5CouponRecordDAO;

/**
 * H5优惠券模版活动
 * @author qianqc
 *
 */
@HessianService("h5CouponService")
@Service("h5CouponService")
@Transactional(readOnly = true)
public class H5CouponServiceImpl implements IH5CouponService {

    @Autowired
    private H5CouponDAO h5CouponDao;
    
    @Autowired
    private H5CouponRcmdPrdDAO h5CouponRcmdPrdDao;
    
    @Autowired
    private H5CouponRecordDAO h5CouponRecordDao;
    
    /**
     * 分页查询H5Coupon活动
     */
    @Override
    public PageInfo<H5Coupon> queryH5CouponList(H5CouponConditionVo h5CouponConditionVo) {
        PageHelper.startPage(h5CouponConditionVo.getPage(), h5CouponConditionVo.getPageSize());
        List<H5Coupon> h5CouponList = h5CouponDao.selectList(h5CouponConditionVo);
        PageInfo<H5Coupon> pageInfo = new PageInfo<H5Coupon>(h5CouponList);
        return pageInfo;
    }

    /**
     * 更改活动状态
     */
    @Override
    @Transactional(readOnly = false)
    public void changeState(H5Coupon h5Coupon) {
        h5CouponDao.update(h5Coupon);
    }
    
    /**
     * 根据活动ID获取活动信息
     */
    @Override
    public H5Coupon queryH5CouponActivityById(String h5couponActId) {
        return h5CouponDao.queryH5CouponActivityById(h5couponActId);
    }
    
    

    /**
     * 查询H5优惠券活动
     */
    @Override
    public H5Coupon queryH5Coupon(H5Coupon h5Coupon) {
        h5Coupon = h5CouponDao.select(h5Coupon);
        H5CouponRcmdPrd h5CouponRcmdPrd = new H5CouponRcmdPrd();
        h5CouponRcmdPrd.setH5CouponId(h5Coupon.getId());
        List<H5CouponRcmdPrd> h5CouponRcmdPrdList = h5CouponRcmdPrdDao.selectList(h5CouponRcmdPrd);
        h5Coupon.setH5CouponRcmdPrdList(h5CouponRcmdPrdList);
        return h5Coupon;
    }

    /**
     * 新增和修改H5优惠券活动
     */
    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(H5Coupon h5Coupon) {
        List<H5CouponRcmdPrd> list = h5Coupon.getH5CouponRcmdPrdList();
        Date now = new Date(); 
        //id为空则为新增
        if (h5Coupon.getId() == null) {
            h5Coupon.preInsert();
            h5Coupon.setCreateDate(now);
            h5Coupon.setState(StatusType.OPEN.getValue());
            h5CouponDao.insert(h5Coupon);
            for(H5CouponRcmdPrd prd : list) {
                prd.setH5CouponId(h5Coupon.getId());
                prd.preInsert();
                h5CouponRcmdPrdDao.insert(prd);
            }
        }
        //修改
        else {
            h5CouponDao.update(h5Coupon);
            h5CouponRcmdPrdDao.deleteByH5CouponId(h5Coupon.getId());
            for(H5CouponRcmdPrd prd : list) {
                prd.setH5CouponId(h5Coupon.getId());
                prd.preInsert();
                h5CouponRcmdPrdDao.insert(prd);
            }
        }
    }  
    
    /**
     * 查询该手机号参加H5优惠券活动
     */
    @Override
    public List<H5CouponRecord> queryH5CouponReocrd(String mobile, String activityId) {
        H5CouponRecord h5CouponRecord = new H5CouponRecord();
        h5CouponRecord.setMobile(mobile);
        h5CouponRecord.setH5CouponActId(activityId);
        return h5CouponRecordDao.queryH5CouponRecord(h5CouponRecord);
    }
    
    /**
     * 插入H5优惠券的领取信息
     */
    @Override
    @Transactional(readOnly = false)
    public void insertH5CouponRecord(H5CouponRecord h5CouponRecord) {
        h5CouponRecordDao.insertH5CouponRecord(h5CouponRecord);
    }

    /**
     * 根据活动ID查询产品信息
     */
    @Override
    public List<H5CouponRcmdPrd> queryH5CouponRcmdPrdById(String h5CouponId) {
        return h5CouponRcmdPrdDao.queryH5CouponRcmdPrdById(h5CouponId);
    }

    /**
     * 针对微信用户，判断是否已经领取
     */
    @Override
    public List<H5CouponRecord> isAquiredH5Coupon(H5CouponRecord queryParam) {
        return h5CouponRecordDao.isAquiredH5Coupon(queryParam);
    }
}

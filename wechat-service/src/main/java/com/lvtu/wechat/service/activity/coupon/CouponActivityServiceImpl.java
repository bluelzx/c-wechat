package com.lvtu.wechat.service.activity.coupon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.enums.StatusType;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.coupon.Coupon;
import com.lvtu.wechat.common.model.activity.coupon.CouponActivity;
import com.lvtu.wechat.common.model.activity.coupon.CouponRecord;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.coupon.ICouponActivityService;
import com.lvtu.wechat.common.vo.back.CouponActConditionVo;
import com.lvtu.wechat.dao.activity.coupon.dao.CouponActivityDAO;
import com.lvtu.wechat.dao.activity.coupon.dao.CouponDAO;
import com.lvtu.wechat.dao.activity.coupon.dao.CouponRecordDAO;

/**
 * @author qianqc & wxlizhi
 */
@HessianService("couponActivityService")
@Service("couponActivityService")
@Transactional(readOnly = true)
public class CouponActivityServiceImpl implements ICouponActivityService {

    Logger logger = Logger.getLogger(CouponActivityServiceImpl.class);

    @Autowired
    private CouponActivityDAO couponActivityDao;

    @Autowired
    private CouponRecordDAO couponRecordDao;

    @Autowired
    private CouponDAO couponDao;

    @Override
    public PageInfo<CouponActivity> queryCouponActivityList(CouponActConditionVo couponActConditionVo) {
        PageHelper.startPage(couponActConditionVo.getPage(), couponActConditionVo.getPageSize());
        List<CouponActivity> couponActivityList = couponActivityDao.queryCouponActivityList(couponActConditionVo);
        PageInfo<CouponActivity> pageInfo = new PageInfo<CouponActivity>(couponActivityList);
        return pageInfo;
    }

    @Override
    public CouponActivity queryCouponActivityById(String couponActId) {
        CouponActivity couponActivity = couponActivityDao.queryCouponActivityById(couponActId);
        return couponActivity;
    }

    /**
     * 查询coupon_record表
     * 
     * @param mobile
     * @param couponActivity
     * @return
     */
    @Override
    public CouponRecord queryCouponReocrd(CouponRecord couponRecord) {
        return couponRecordDao.queryCouponRecord(couponRecord);
    }

    /**
     * 插入表coupon_record
     * 
     * @param couponRecord
     */
    @Override
    @Transactional(readOnly = false)
    public void insertCouponRecord(CouponRecord couponRecord) {
        couponRecordDao.insertCouponRecord(couponRecord);
    }

    /**
     * 根据活动ID查询可以发放的券码
     */
    @Override
    public Coupon queryCoupon(Coupon coupon) {
        return couponDao.queryUseableCoupon(coupon);
    }

    /**
     * 更新表Coupon状态 插入表coupon_record
     */
    @Override
    @Transactional(readOnly = false)
    public boolean updateCoupon(Coupon coupon, CouponRecord couponRecord) {
        Integer i = couponDao.updateCoupon(coupon);
        if (i != 1) {
            return true;
        }
        else {
            couponRecordDao.insertCouponRecord(couponRecord);
            return false;
        }
    }

    /**
     * 领取券码
     * 
     * @param mobile
     * @param couponActivity
     * @param result
     * @param isNewUser
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> aquireCoupon(String mobile, CouponActivity couponActivity, Map<String, Object> result,
        boolean isNewUser, WechatUser wechatUser) {
        Coupon coupon = new Coupon();
        coupon.setCouponActId(couponActivity.getId());
        coupon.setState(CommonType.UNUSED.getValue());
        CouponRecord couponRecord = new CouponRecord();
        couponRecord.setCouponActId(couponActivity.getId());
        // 查询已被领取的数量
        int couponRecordCount = couponRecordDao.queryCouponRecordCount(couponRecord);
        if ((couponActivity.getUsableNum() - couponRecordCount) <= 0) {
            couponActivity.setState(StatusType.CLOSE.getValue());
            updateState(couponActivity);
            result.put("code", "-1");
            result.put("msg", "活动已结束");
            return result;
        }
        coupon = couponDao.queryUseableCoupon(coupon);
        if (coupon != null) {
            // 设置状态为已使用
            coupon.setStateHis(coupon.getState());
            coupon.setState(CommonType.USED.getValue());
        }
        else {
            couponActivity.setState(StatusType.CLOSE.getValue());
            updateState(couponActivity);
            result.put("code", "-1");
            result.put("msg", "活动已结束");
            return result;
        }

        couponRecord.setCouponId(coupon.getId());
        couponRecord.setIsNew(isNewUser ? CommonType.NEW.getValue() : CommonType.OLD.getValue());
        couponRecord.setMobile(mobile);
        if (wechatUser != null) {
            couponRecord.setOpenid(wechatUser.getOpenid()); 
        }
        // 做插入操作得做id生成
        couponRecord.preInsert();
        couponRecord.setCreateDate(new Date());
        // 更新操作
        Integer i = couponDao.updateCoupon(coupon);
        if (i == 1) {
            couponRecordDao.insertCouponRecord(couponRecord);
            result.put("coupon", coupon);
        }
        return result;
    }

    /**
     * 保存发券码活动
     * 
     * @param couponActivityform
     * @param coupons
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void saveCouponActivityform(CouponActivity couponActivityform, List<Coupon> coupons) {
        if (StringUtils.isBlank(couponActivityform.getId())) {
            couponActivityform.preInsert();
            couponActivityform.setTotalNum(0);
            couponActivityDao.insert(couponTxt(couponActivityform, coupons));
        }
        else {
            couponActivityDao.update(couponTxt(couponActivityform, coupons));
        }

    }

    /**
     * 导入兑券码
     * 
     * @param couponActivityform
     * @param coupons
     * @return
     */
    public CouponActivity couponTxt(CouponActivity couponActivityform, List<Coupon> coupons) {
        String id = couponActivityform.getId();
        List<Coupon> list = new ArrayList<Coupon>();
        Coupon coupon = null;
        for (int i = 0; i < coupons.size(); i++) {
            coupon = coupons.get(i);
            coupon.setCouponActId(id);
            list.add(coupon);
        }
        List<List<Coupon>> couponlist = splitList(list, 100);
        for (int i = 0; i < couponlist.size(); i++) {
            couponDao.insert(couponlist.get(i));
        }
        Date now = new Date();
        couponActivityform.setCreateDate(now);
        int oldTotalNum = couponDao.selectCouponNum(id);
        couponActivityform.setTotalNum(coupons.size() + oldTotalNum);
        return couponActivityform;
    }

    /**
     * 分割List
     * 
     * @param list
     * @param pageSize
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        int listSize = list.size(); // list的大小
        int page = (listSize + (pageSize - 1)) / pageSize;// 页数
        List<List<T>> listArray = new ArrayList<List<T>>();// 创建list数组 ,用来保存分割后的list
        List<T> subList = null;
        int splitIndex = 0; // 分割节点
        for (int i = 0; i < page; i++) { // 按照数组大小遍历
            subList = new ArrayList<T>(); // 数组每一位放入一个分割后的list
            for (int j = splitIndex; j < listSize; j++) {// 遍历待分割的list
                int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;// 当前记录的页码(第几页)
                if (pageIndex == (i + 1)) {// 当前记录的页码等于要放入的页码时
                    subList.add(list.get(j)); // 放入list中的元素到分割后的list(subList)
                }
                if ((j + 1) == ((i + 1) * pageSize)) {// 当放满一页时退出当前循环
                    splitIndex = j + 1; // 获得下一个子list的分割节点
                    break;
                }
            }
            listArray.add(subList);// 将分割后的list放入对应的数组的位中
        }
        return listArray;
    }

    /**
     * 更新发券码活动的状态
     * 
     * @param couponActivity
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public void updateState(CouponActivity couponActivity) {
        couponActivityDao.updateState(couponActivity);
    }

    /**
     * 增加页面浏览量
     */
    @Override
    @Transactional(readOnly = false)
    public void addPageView(String couponActId) {
        couponActivityDao.addPageView(couponActId);
    }
    /**
     * 针对微信用户，判断是否已经领取过了
     * @param queryParam
     * @param result
     * @return
     */
    @Override
    public Map<String, Object> isAquiredCoupon(CouponRecord queryParam, Map<String, Object> result) {
        CouponRecord couponRecord = couponRecordDao.queryCouponRecord(queryParam);
        if (couponRecord != null) {
            Coupon coupon = new Coupon();
            coupon.setId(couponRecord.getCouponId());
            coupon = couponDao.queryCouponById(coupon);
            result.put("coupon", coupon);
        }
        return result;
    }
}

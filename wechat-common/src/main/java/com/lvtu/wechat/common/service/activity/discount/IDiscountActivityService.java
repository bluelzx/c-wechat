package com.lvtu.wechat.common.service.activity.discount;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.discount.DiscountActivity;
import com.lvtu.wechat.common.model.activity.discount.DiscountCoupon;
import com.lvtu.wechat.common.model.activity.discount.DiscountGet;
import com.lvtu.wechat.common.vo.back.DiscountActivityConditionVo;

/**
 * 380优惠券
 * @author majun
 *
 */
@RemoteService("discountActivitiesService")
public interface IDiscountActivityService {

	/**
	 * 
	 * 添加380优惠券服务
	 * @param discountActivity
	 */
	  public void saveDiscountActivity(DiscountActivity discountActivity);
	  
	  
	  /**
	   * 
	   * 统计查询
	   * @param discountActivityConditionVo
	   * @return
	   */
	  PageInfo<DiscountCoupon> queryDiscountCouponList(
			DiscountActivityConditionVo discountActivityConditionVo);


    /**
     * 针对微信用户，判断是否已经领取优惠券
     * @param discountGet
     * @return
     */
    public List<String> isAquiredDiscount(DiscountGet discountGet);


    /**
     * 查询符合条件的优惠券
     * @param discountCoupon
     * @return
     */
    public DiscountCoupon queryDiscountCoupon(DiscountCoupon discountCoupon);


    /**
     * 插入优惠券领取记录
     * @param discountGet
     */
    public void insertDiscountGet(DiscountGet discountGet);


    /**
     * 编辑活动的查询
     * @param discountActivity
     * @return
     */
	public DiscountActivity selectDiscountActivity(
			DiscountActivity discountActivity);

    /**
     * 查询380优惠券活动
     * @return
     */
    public DiscountActivity queryDiscountActivity();


    /**
     * 查询所有优惠券的批次号
     * @return
     */
    public List<String> queryDiscountCouponCodes();

}

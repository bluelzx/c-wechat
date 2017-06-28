package com.lvtu.wechat.common.service.activity.discountnew;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountActivityNew;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountCouponNew;
import com.lvtu.wechat.common.model.activity.discountnew.DiscountGetNew;
import com.lvtu.wechat.common.vo.back.DiscountActivityConditionNewVo;

/**
 * 380优惠券
 * @author majun
 *
 */
@RemoteService("discountActivitiesNewService")
public interface IDiscountActivityNewService {

	/**
	 * 
	 * 添加380优惠券服务
	 * @param discountActivity
	 */
	  public void saveDiscountActivityNew(DiscountActivityNew discountActivityNew);
	  
	  
	  /**
	   * 
	   * 统计查询
	   * @param discountActivityConditionVo
	   * @return
	   */
	  PageInfo<DiscountCouponNew> queryDiscountCouponNewList(
			DiscountActivityConditionNewVo discountActivityConditionNewVo);


    /**
     * 查询符合条件的优惠券
     * @param discountCoupon
     * @return
     */
    public DiscountCouponNew queryDiscountCouponNew(DiscountCouponNew discountCouponNew);


    /**
     * 插入优惠券领取记录
     * @param discountGet
     */
    public void insertDiscountGetNew(DiscountGetNew discountGetNew);


    /**
     * 编辑活动的查询
     * @param discountActivity
     * @return
     */
	public DiscountActivityNew selectDiscountActivityNew(
			DiscountActivityNew discountActivityNew);

    /**
     * 查询380优惠券活动
     * @return
     */
    public DiscountActivityNew queryDiscountActivityNew();


    /**
     * 查询所有优惠券的批次号
     * @return
     */
    public List<String> queryDiscountNewCouponCodes();


    /**
     * 
    * @Title: deleteDiscountCouponNew 
    * @Description: TODO(根据id,点击删除，修改state状态) 
    * @param @param id
    * @param @return    设定文件 
    * @return Object    返回类型 
    * @throws
     */
	public Object deleteDiscountCouponNew(String id);


	/**
	 * 
	* @Title: addDiscountCouponNew 
	* @Description: TODO(添加优惠券) 
	* @param @param discountCouponNew    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void saveDiscountCouponNew(DiscountCouponNew discountCouponNew);

    /**
     * 
    * @Title: queryDiscountCouponByid 
    * @Description: TODO(编辑优惠券，按照id) 
    * @param @param id
    * @param @return    设定文件 
    * @return DiscountCouponNew    返回类型 
    * @throws
     */
	public DiscountCouponNew queryDiscountCouponByid(String id);

    /**
     * 
    * @Title: changenextOrderByid 
    * @Description: TODO(更改nextOrder,根据id) 
    * @param @param id
    * @param @return    设定文件 
    * @return DiscountCouponNew    返回类型 
    * @throws
     */
	public void updateNextOrder(List<DiscountCouponNew> list);


	/**
	 * 
	* @Title: updateCurrOrder 
	* @Description: TODO(点击发布修改currOrder) 
	* @param @param list    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void changeCurrOrder(List<DiscountCouponNew> list);

    /**
     * 
    * @Title: recoveryDiscountCouponNew 
    * @Description: TODO(活动管理界面，根据id恢复优惠券) 
    * @param @param id    设定文件 
    * @return void    返回类型 
    * @throws
     */
	public void recoveryDiscountCouponNew(String id);


	/**
	 * @param discountCoupon 
	 * 
	* @Title: queryDiscountCouponNew 
	* @Description: TODO(查询所有380优惠券) 
	* @param @return    设定文件 
	* @return DiscountCouponNew    返回类型 
	* @throws
	 */
	List<DiscountCouponNew> queryallDiscountCouponNew(DiscountCouponNew discountCoupon);

	/**
	 * 
	 * 
	* @Title: queryDiscountGet 
	* @Description: TODO(查询用户是否领取优惠券，没有则插入优惠券) 
	* @param @param openid
	* @param @return    设定文件 
	* @return List<String>    返回类型 
	* @throws
	 */
	public List<String> queryDiscountGet(DiscountGetNew discountGet);


	/**
	 * 
	* @Title: getAquireCoupons 
	* @Description: TODO(获取用户已领取的所有的优惠券) 
	* @param @param discountGet
	* @param @return    设定文件 
	* @return List<DiscountGetNew>    返回类型 
	* @throws
	 */
	public List<DiscountGetNew> getAquireCoupons(DiscountGetNew discountGet);


	/**
	 * 
	* @Title: queryAcuponList 
	* @Description: TODO(查询状态为1 , 2的优惠券) 
	* @param @param discountCoupon
	* @param @return    设定文件 
	* @return List<DiscountCouponNew>    返回类型 
	* @throws
	 */
	public List<DiscountCouponNew> querycouponOneTwoList(
			DiscountCouponNew discountCoupon);

    /**
     * 
    * @Title: querycouponThreeFourList 
    * @Description: TODO(查询状态为3 , 4的优惠券) 
    * @param @param discountCoupon
    * @param @return    设定文件 
    * @return List<DiscountCouponNew>    返回类型 
    * @throws
     */
	public List<DiscountCouponNew> querycouponThreeFourList(
			DiscountCouponNew discountCoupon);

}

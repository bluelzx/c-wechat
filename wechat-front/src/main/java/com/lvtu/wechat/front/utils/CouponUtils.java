package com.lvtu.wechat.front.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.lvmama.comm.pet.po.mark.MarkCoupon;
import com.lvmama.comm.pet.po.mark.MarkCouponCode;
import com.lvmama.comm.pet.po.user.UserUser;
import com.lvmama.comm.pet.service.mark.MarkCouponService;
import com.lvmama.comm.utils.DateUtil;
import com.lvtu.wechat.common.spring.SpringBeanProxy;
import com.lvtu.wechat.front.vo.CouponVO;

/**
 * 优惠券工具类
 * @author xuyao
 *
 */
public class CouponUtils {
    
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(CouponUtils.class);
	
	private static MarkCouponService markCouponService = SpringBeanProxy.getBean(MarkCouponService.class, "markCouponService");
	
	/**
	 * 生成并绑定优惠券给用户
	 * @param couponIds 优惠券批次号
	 * @param lvUser 驴妈妈用户
	 * @return
	 */
	public static List<CouponVO> genAndBindCoupon(String[] couponIds, UserUser lvUser) {
		List<CouponVO> couponList = new ArrayList<CouponVO>();
		for(String couponId : couponIds) {
			if(StringUtils.isNumeric(couponId)) {
				Long realCouponId = Long.parseLong(couponId);
				logger.info("H5送优惠券领取优惠券工具类中,优惠券批次号："+realCouponId);
				MarkCouponCode couponCode = markCouponService.generateSingleMarkCouponCodeByCouponId(realCouponId);
				logger.info("H5送优惠券领取优惠券工具类中,优惠券code值："+couponCode.getCouponCode());
				MarkCoupon coupon = markCouponService.selectMarkCouponByPk(realCouponId);
				logger.info("H5送优惠券领取优惠券工具类中,获得生成并绑定优惠券给用户的优惠券的信息"+coupon);
				if(couponCode != null && couponCode.getCouponCode() != null) {
					//动态计算有效期
					Date startTime = null;
					Date endTime = null;
					if ("FIXED".equalsIgnoreCase(coupon.getValidType())) {
						startTime = coupon.getBeginTime();
						endTime = DateUtil.getDateAfter2359Date(startTime, coupon.getTermOfValidity().intValue());
					} else if ("UNFIXED".equalsIgnoreCase(coupon.getValidType())) {
						startTime = DateUtil.getDateAfter0000Date(new Date(), coupon.getDayAfter().intValue());
						endTime = DateUtil.getDateAfter2359Date(startTime, coupon.getTermOfValidity().intValue() - 1);
					} else if ("GOT".equalsIgnoreCase(coupon.getValidType())) {
						startTime = DateUtil.getDateAfter0000Date(new Date(), coupon.getDayAfter().intValue());
						endTime = DateUtil.getDateAfter2359Date(startTime, coupon.getTermOfValidity().intValue() - 1);
					}
					couponCode.setBeginTime(startTime);
					couponCode.setEndTime(endTime);
					//绑定优惠券到用户账户
					markCouponService.bindingUserAndCouponCode(lvUser, couponCode.getCouponCode());
					couponList.add(new CouponVO(coupon, couponCode));
				}
			}
		}
		return couponList;
	}
	
	/**
	 * 生成优惠券信息
	 * @param couponCodes 优惠券码
	 * @return
	 */
	public static List<CouponVO> getCouponsByCode(String[] couponCodes) {
		List<CouponVO> couponList = new ArrayList<CouponVO>();
		for(String couponCode:couponCodes){
		    if(StringUtils.isNotBlank(couponCode)) {
		        MarkCouponCode markCouponCode = markCouponService.getMarkCouponCodeByCouponIdAndCode(null, couponCode);
		        MarkCoupon markCoupon=markCouponService.selectMarkCouponByCouponCode(couponCode,true);
		        logger.info("H5送优惠券领取优惠券工具类中,获得优惠券的信息"+markCoupon);
		        if(markCouponCode != null && markCouponCode.getCouponCode() != null) {
                    //动态计算有效期
                    Date startTime = null;
                    Date endTime = null;
                    if ("FIXED".equalsIgnoreCase(markCoupon.getValidType())) {
                        startTime = markCoupon.getBeginTime();
                        endTime = DateUtil.getDateAfter2359Date(startTime, markCoupon.getTermOfValidity().intValue());
                    } else if ("UNFIXED".equalsIgnoreCase(markCoupon.getValidType())) {
                        startTime = DateUtil.getDateAfter0000Date(new Date(), markCoupon.getDayAfter().intValue());
                        endTime = DateUtil.getDateAfter2359Date(startTime, markCoupon.getTermOfValidity().intValue() - 1);
                    } else if ("GOT".equalsIgnoreCase(markCoupon.getValidType())) {
                        startTime = DateUtil.getDateAfter0000Date(new Date(), markCoupon.getDayAfter().intValue());
                        endTime = DateUtil.getDateAfter2359Date(startTime, markCoupon.getTermOfValidity().intValue() - 1);
                    }
                    markCouponCode.setBeginTime(startTime);
                    markCouponCode.setEndTime(endTime);
                    couponList.add(new CouponVO(markCoupon, markCouponCode));
                }
		        
		    }
		}
		return couponList;
	}
}

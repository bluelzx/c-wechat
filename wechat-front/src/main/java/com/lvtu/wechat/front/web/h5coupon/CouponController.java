package com.lvtu.wechat.front.web.h5coupon;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvmama.allin.common.services.activity.IActivityService;
import com.lvmama.allin.common.services.wxcoupon.IWxcouponService;
import com.lvmama.allin.po.activity.OActDiscount;
import com.lvmama.allin.po.wxcoupon.OWxCoupon;
import com.lvmama.comm.pet.po.mark.MarkCoupon;
import com.lvmama.comm.pet.po.mark.MarkCouponCode;
import com.lvmama.comm.pet.po.user.UserUser;
import com.lvmama.comm.pet.service.mark.MarkCouponService;
import com.lvmama.comm.pet.service.user.UserUserProxy;
import com.lvmama.comm.pet.service.user.UserUserProxy.USER_IDENTITY_TYPE;
import com.lvmama.comm.utils.DateUtil;
import com.lvmama.comm.utils.MemcachedUtil;
import com.lvtu.common.utils.JSONUtil;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.utils.AuthSmsCodeUtils;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.PhoneUtil;
import com.lvtu.wechat.front.utils.WebchatUtil;
import com.lvtu.wechat.front.vo.CouponVO;

/**
 * H5送优惠券活动     2016-08-23 产品经理沟通，第三方合作送优惠券的以后都不用了，该业务被废弃
 * @author xuyao
 *
 */
@Controller
@RequestMapping("/h5coupon")
@NeedOauth
public class CouponController extends BaseController {
	
	/**
	 * 活动短信验证码缓存key前缀
	 */
	public static final String COUPON_ACT_SMS_CACHE_KEY_PREF = "wx_act_coupon_sms_";
	
	/**
	 * 短信验证错误计数前缀,后跟openid
	 */
	public static final String COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF = "wx_act_coupon_sms_code_wrong_count_";
	
	/**
	 * 最大允许输入验证码错误次数
	 */
	public static final Integer MAX_SMS_CODE_WRONG_VERIFY = 3;
	
	/**
	 * 频繁操作后静默时间
	 */
	public static final Integer WRONG_SMS_CODE_QUIET_SECONDS = 600;
	
	/**
	 * 活动名称前缀，后跟活动ID。用于驴途后台统计用
	 */
	public static final String COUPON_ACTIVITY_NAME_PREF = "WXCOUPON_";
	
	@Autowired
	private IWxcouponService couponService;

	@Autowired
	private IActivityService actService; //旅途活动service
	
	@Autowired
	private MarkCouponService markCouponService;
	
	@Autowired
	private UserUserProxy userProxy;
	
	/**
	 * 优惠券活动首页
	 * @param activityId 活动ID
	 * @param model
	 * @param resp
	 * @return
	 */
	@RequestMapping("/act/{activityId}")
	public String index(@PathVariable String activityId, Model model, HttpServletResponse resp, HttpServletRequest request) {
		if(StringUtils.isNotBlank(activityId) && StringUtils.isNumeric(activityId)) {
			Integer id = Integer.parseInt(activityId);
			OWxCoupon activity = couponService.getOWxCouponByPrimaryKey(id);
			if(activity != null) {
				Date nowDate = new Date();
				if(nowDate.compareTo(activity.getOnlineTime()) < 0) {
					activity.setStatus(0);
				} else if(nowDate.compareTo(activity.getOfflineTime()) > 0) {
					activity.setStatus(2);
				}
				model.addAttribute("activity", activity);
				model.addAttribute("isBusyVisit", isBusyVisit(request));
				return "h5coupon/index";
			} else {
				return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
			}
		} else {
			return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
		}
	}
	
	/**
	 * 发送短信验证码
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendAuthSms")
	public Object sendAuthSMS(String mobile, String activityId, HttpServletRequest request) {
		Map<String, Object> result = getResultMap();
		if (StringUtils.isNotBlank(mobile) && PhoneUtil.isValide(mobile) && StringUtils.isNumeric(activityId)) {
			if (isAttended(mobile, activityId)) {
				result.put("code", "-1");
				result.put("msg", "该手机号已领取过了！");
				return result;
			}
			try {
				// 发送短信
				String response = AuthSmsCodeUtils.sendAuthSMS(request, mobile);
				JSONObject jo = JSONUtil.getObject(response);
				if (jo != null && jo.get("code").equals("1")) {
					result.put("msg", "验证码已发送到手机");
				} else {
					result.put("code", "-1");
					result.put("msg",jo.get("message") != null ? jo.get("message") : "验证码发送失败");
				}
			} catch (Exception e) {
				result.put("code", "-1");
				result.put("msg", "验证码发送失败");
			}
		}
		return result;
	}
	
	/**
	 * 领取优惠券
	 * @return
	 */
	@RequestMapping("/getCoupons")
	public String getCoupons(Model model, String activityId, String mobile, String smsCode, HttpServletRequest request, HttpServletResponse response) {
		String jsonType = "application/json;charset=UTF-8";
		//活动活动信息
		OWxCoupon activity = null;
		if(StringUtils.isNumeric(activityId)) {
			int actId = Integer.parseInt(activityId);
			activity = couponService.getOWxCouponByPrimaryKey(actId);
		}
		//校验数据
		if(activity == null || !StringUtils.isNumeric(activityId)) {
			return renderString(response, "{\"code\":\"-1\", \"msg\":\"不存在的活动！\"}", jsonType);
		} else if(activity.getStatus() != 1) {
			return renderString(response, "{\"code\":\"-1\", \"msg\":\"活动还未开始，或者已经结束！\"}", jsonType);
		} else if (StringUtils.isBlank(mobile)) {
			return renderString(response, "{\"code\":\"-1\", \"msg\":\"手机号码不能为空！\"}", jsonType);
		} else if(isAttended(mobile, activityId)) {
			return renderString(response, "{\"code\":\"-1\", \"msg\":\"该手机号已领取过了！\"}", jsonType);
		} else if (StringUtils.isBlank(smsCode)) {
			return renderString(response, "{\"code\":\"-1\", \"msg\":\"请输入短信验证码！\"}", jsonType);
		} else if (isBusyVisit(request)) {
			return renderString(response, "{\"code\":\"-1\", \"msg\":\"您的操作太频繁了，休息一会儿，再来领取！\"}", jsonType);
		}
		
		WechatUser nowUser = getWechatUser(request);
		MemcachedUtil mem = MemcachedUtil.getInstance();
		if (AuthSmsCodeUtils.checkSMSCodeRedis(mobile, smsCode)) {
			//验证成功后清除错误计数缓存
			if (nowUser != null && nowUser.getOpenid() != null) {
				mem.remove(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid());
			}
			//检查当前手机号是否已经注册过。没有注册就通过手机号自动注册
			boolean isNewUser = false;
			UserUser lvUser = userProxy.getUsersByIdentity(mobile, USER_IDENTITY_TYPE.MOBILE);
			if(lvUser == null) { //如果没有注册进行注册
				lvUser = WebchatUtil.regUserByMobile(mobile);
				isNewUser = true;
				if(lvUser == null) {
					return renderString(response, "{\"code\":\"-1\", \"msg\":\"系统出错！\"}", jsonType);
				}
			}
			//微信账户和驴妈妈账户都没有绑定的情况下才进行绑定操作
			boolean isBindWx = WebchatUtil.isBind(nowUser);
			if (!isBindWx && StringUtils.isBlank(lvUser.getWechatId())
					&& StringUtils.isBlank(lvUser.getWechatUnionId())) {
				boolean isBinded = WebchatUtil.bindWxLvmama(nowUser, lvUser);
				logger.info("H5送优惠券绑定驴妈妈账户结果：" + isBinded);
			}
			//送优惠券给用户
			String couponIds = activity.getCoupon();
			logger.info("[H5送优惠券活动]赠送优惠券开始：" + couponIds);
			List<CouponVO> couponList = new ArrayList<CouponVO>();
			if(StringUtils.isNotBlank(couponIds)) {
				String[] coupons = couponIds.trim().split(",");
				for(String couponId : coupons) {
					if(StringUtils.isNumeric(couponId)) {
						Long realCouponId = Long.parseLong(couponId);
						MarkCoupon coupon = markCouponService.selectMarkCouponByPk(realCouponId);
						if(coupon == null) {
							logger.warn("优惠券批次号不存在或者已过期，无法获取到优惠券信息！活动ID:" + activityId + " coupon:" + couponId);
							continue;
						}
						//进行优惠券绑定操作
						MarkCouponCode couponCode = markCouponService.generateSingleMarkCouponCodeByCouponId(realCouponId);
						//GOT类型动态计算有效期
						if (StringUtils.equals(coupon.getValidType(), "GOT")) {
							Date startTime = DateUtil.getDateAfter0000Date(new Date(), coupon.getDayAfter().intValue());
							Date endTime = DateUtil.getDateAfter2359Date(startTime, coupon.getTermOfValidity().intValue() - 1);
							couponCode.setBeginTime(startTime);
							couponCode.setEndTime(endTime);
						}
						if(couponCode != null && couponCode.getCouponCode() != null) {
							Long userCode = markCouponService.bindingUserAndCouponCode(lvUser, couponCode.getCouponCode());
							logger.info("[H5送优惠券活动]为用户绑定优惠券code：" + userCode);
							couponList.add(new CouponVO(coupon, couponCode));
						}
					}
				}
			}
			logger.info("[H5送优惠券活动]赠送优惠券完成！");
			//统计活动记录
			OActDiscount oActDiscount = new OActDiscount();
			oActDiscount.setActName("WXCOUPON_" + activity.getId());
			oActDiscount.setTel(mobile);
			oActDiscount.setUserName(lvUser.getUserName());
			oActDiscount.setUserNo(lvUser.getUserNo());
			oActDiscount.setRemark(isNewUser ? "new_user" : "old_user");
			oActDiscount.setCreatedTime(new Date());
			oActDiscount.setBack1("WXCOUPON");
			actService.addOActDiscountSelective(oActDiscount);
			
			model.addAttribute("couponList", couponList);
			model.addAttribute("activity", activity);
			model.addAttribute("mobile", mobile);
			return "/h5coupon/success";
		} else {
			//验证码校验失败处理逻辑
			if (nowUser != null && nowUser.getOpenid() != null) {
				Object countObj = mem.get(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid());
				if (null == countObj) {
					mem.set(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid(), WRONG_SMS_CODE_QUIET_SECONDS, 1);
				} else {
					int count = (Integer) countObj;
					mem.set(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid(), WRONG_SMS_CODE_QUIET_SECONDS, ++count);
				}
			}
			return renderString(response, "{\"code\":\"-1\", \"msg\":\"验证码错误！\"}", jsonType);
		}
	}
	
	/**
	 * 查看活动规则
	 * @return
	 */
	@RequestMapping("/rule")
	public String rule() {
		return "/h5coupon/rule";
	}
	
	/**
	 * 是否频繁操作。 超过3次输入错误的验证码视为频繁操作
	 * @param request 
	 * @return
	 */
	private boolean isBusyVisit(HttpServletRequest request) {
		WechatUser nowUser = getWechatUser(request);
		if (nowUser != null && nowUser.getOpenid() != null) {
			MemcachedUtil mem = MemcachedUtil.getInstance();
			Object countObj = mem.get(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + nowUser.getOpenid());
			if (countObj != null && ((Integer) countObj) >= MAX_SMS_CODE_WRONG_VERIFY) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断该手机号是否已经参加过活动
	 * @param mobile 手机号
	 * @param activityId 活动ID
	 * @return
	 */
	private boolean isAttended(String mobile, String activityId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actName", "WXCOUPON_" + activityId);
		params.put("tel", mobile);
		List<OActDiscount> actDiscounts = actService.selectOActDiscountListByParams(params);
		if (actDiscounts == null || actDiscounts.size() == 0)
			return false;

		return true;
	}
}
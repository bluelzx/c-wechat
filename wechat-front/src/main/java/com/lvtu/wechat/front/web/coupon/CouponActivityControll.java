package com.lvtu.wechat.front.web.coupon;

import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lvmama.comm.pet.po.user.UserUser;
import com.lvmama.comm.pet.service.user.UserUserProxy;
import com.lvmama.comm.pet.service.user.UserUserProxy.USER_IDENTITY_TYPE;
import com.lvmama.comm.utils.MemcachedUtil;
import com.lvtu.common.utils.JSONUtil;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.enums.StatusType;
import com.lvtu.wechat.common.model.activity.coupon.CouponActivity;
import com.lvtu.wechat.common.model.activity.coupon.CouponRecord;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.coupon.ICouponActivityService;
import com.lvtu.wechat.common.utils.AuthSmsCodeUtils;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.PhoneUtil;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 给驴粉发券码
 * 
 * @NeedOauth
 * @author qianqc
 */
@Controller
@RequestMapping("/couponAct")
@NeedOauth(isForceOauth = false)
public class CouponActivityControll extends BaseController {

    /**
     * 短信验证错误计数前缀,后跟mobile
     */
    public static final String COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF = "wx_couponact_sms_code_wrong_count_";

    /**
     * 最大允许输入验证码错误次数
     */
    public static final Integer MAX_SMS_CODE_WRONG_VERIFY = 3;

    /**
     * 频繁操作后静默时间
     */
    public static final Integer WRONG_SMS_CODE_QUIET_SECONDS = 600;

    /**
     * couponActId的正则校验，防止注入
     */
    public static final String regex = "^([a-z]|[A-Z]|[0-9]){32}$";

    @Autowired
    private ICouponActivityService couponActivityService;

    @Autowired
    private UserUserProxy userProxy;

    @RequestMapping("/{couponActId}")
    public Object index(@PathVariable String couponActId, Model model, HttpServletResponse resp, HttpServletRequest request) {
        Map<String, Object> result = getResultMap();
        if (!StringUtils.isNotBlank(couponActId) || !couponActId.matches(regex)) {
            return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
        }
        CouponActivity couponActivity = couponActivityService.queryCouponActivityById(couponActId);
        if (couponActivity == null) {
            return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
        }
        if (!StatusType.OPEN.getValue().equals(couponActivity.getState())) {
            return renderString(resp, "活动已结束!", "text/html;charset=UTF-8");
        }
        // 日期校验，如果单纯的是yyyy-mm-dd格式，结束时间得做offset处理
        Date now = new Date();
        if (couponActivity.getEndDate() != null && now.after(couponActivity.getEndDate())) {
            return renderString(resp, "活动已结束!", "text/html;charset=UTF-8");
        }
        model.addAttribute("activity", couponActivity);
        WechatUser wechatUser = getWechatUser(request);
        if (wechatUser != null && wechatUser.getOpenid() != null) {
            CouponRecord queryParam = new CouponRecord();
            queryParam.setOpenid(wechatUser.getOpenid());
            queryParam.setCouponActId(couponActId);
            result = couponActivityService.isAquiredCoupon(queryParam, result);
            if (result.get("coupon") != null) {
                couponActivity.setSuccSubTitle("您已领取过优惠券！");
                model.addAttribute("activity", couponActivity);
                model.addAttribute("coupon", result.get("coupon"));
                return "/coupon/success";
            }
        }
        couponActivityService.addPageView(couponActId);
        return "/coupon/index";

    }

    /**
     * 发送短信验证码
     * 
     * @param mobile
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendAuthSms")
	public Object sendAuthSMS(String mobile, HttpServletRequest request) {
		Map<String, Object> result = getResultMap();
		if (StringUtils.isNotBlank(mobile) && PhoneUtil.isPhoneNum(mobile)) {
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
		} else {
			result.put("code", "-1");
			result.put("msg", "请输入正确的手机号");
		}
		return result;
	}

    /**
     * 领取优惠券
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCoupons")
    public Object getCoupons(RedirectAttributes redirectAttributes, String inviteCode, String mobile, String smsCode,
        String couponActId, Model model, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("inviteCode", inviteCode);
        redirectAttributes.addFlashAttribute("mobile", mobile);
        redirectAttributes.addFlashAttribute("smsCode", smsCode);

        Map<String, Object> result = getResultMap();
        // 校验参数
        if (!beforeValidate(inviteCode, mobile, smsCode, couponActId, result)) {
            return result;
        }
        CouponActivity couponActivity = couponActivityService.queryCouponActivityById(couponActId);
        // 校验活动
        if (!afterValidate(result, couponActivity, inviteCode, mobile)) {
            return result;
        }
        
        MemcachedUtil mem = MemcachedUtil.getInstance();
        boolean isNewUser = false;
        UserUser lvUser = null;
        if (AuthSmsCodeUtils.checkSMSCodeRedis(mobile, smsCode)) {
            // 验证成功后清除错误计数缓存
            mem.remove(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + mobile);
            // 检查当前手机号是否已经注册过。没有注册就通过手机号自动注册
            lvUser = userProxy.getUsersByIdentity(mobile, USER_IDENTITY_TYPE.MOBILE);
            if (lvUser == null) {
                // 如果没有注册进行注册
                lvUser = WebchatUtil.regUserByMobile(mobile);
                isNewUser = true;
                if (lvUser == null) {
                    result.put("code", "-1");
                    result.put("msg", "系统异常，请稍后再试");
                    return result;
                }
            }
        }
        else {
            // 验证码校验失败处理逻辑
            Object countObj = mem.get(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + mobile);
            if (null == countObj) {
                mem.set(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + mobile, WRONG_SMS_CODE_QUIET_SECONDS, 1);
            }
            else {
                int count = (Integer) countObj;
                mem.set(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + mobile, WRONG_SMS_CODE_QUIET_SECONDS, ++count);
            }
            result.put("code", "-1");
            result.put("msg", "验证码错误");
            return result;
        }
        // 获取微信用户，如果wechatUser不为空则是微信授权的用户，如果wechatUser为空则是其他浏览器打开的 // 微信绑定操作
        WechatUser wechatUser = getWechatUser(request);
        if (wechatUser != null && wechatUser.getOpenid() != null && !WebchatUtil.isBind(wechatUser)) {
            boolean isBinded = WebchatUtil.bindWxLvmama(wechatUser, lvUser);
            logger.info("给驴粉送券码绑定驴妈妈账户结果：" + isBinded);
        }

        //判断是否已经领取过，领取过就直接显示领取的优惠券码
        CouponRecord queryParam = new CouponRecord();
        queryParam.setCouponActId(couponActId);
        queryParam.setMobile(mobile);
        result = couponActivityService.isAquiredCoupon(queryParam, result);
        if (result.get("coupon") != null) {
            couponActivity.setSuccSubTitle("您已领取过优惠券！");
            result.put("activity", couponActivity);
            result.put("coupon", result.get("coupon"));
            return result;
        }
        
        // 获取锁
        if (!mem.tryLock(couponActId)) {
            result.put("code", "-1");
            result.put("msg", "系统繁忙，请稍后再试！");
            return result;
        }
        else {
            // 防止重排序    
            synchronized (this) {
                // 进行优惠券领取
                result = couponActivityService.aquireCoupon(mobile, couponActivity, result, isNewUser, wechatUser);
                // 释放锁
                mem.releaseLock(couponActId);
            }
        }
        if (result.get("coupon") == null) {
            return result;
        }
        result.put("activity", couponActivity);
        result.put("coupon", result.get("coupon"));
        return result;
    }

    /**
     * 初始校验
     * 
     * @param inviteCode
     * @param mobile
     * @param smsCode
     * @param couponActId
     * @param result
     * @return
     */
    public boolean beforeValidate(String inviteCode, String mobile, String smsCode, String couponActId,
        Map<String, Object> result) {
        if (!StringUtils.isNotBlank(mobile) || !PhoneUtil.isPhoneNum(mobile)) {
            result.put("code", "-1");
            result.put("msg", "请输入正确的手机号");
            return false;
        }
        if (StringUtils.isBlank(inviteCode) || StringUtils.isBlank(couponActId)) {
            result.put("code", "-1");
            result.put("msg", "请输入邀请码");
            return false;
        }
        if (!couponActId.matches(regex)) {
            result.put("code", "-1");
            result.put("msg", "活动不存在");
            return false;
        }
        if (StringUtils.isBlank(smsCode)) {
            result.put("code", "-1");
            result.put("msg", "请输入短信验证码！");
            return false;
        }
        if (isBusyVisit(mobile)) {
            result.put("code", "-1");
            result.put("msg", "您的操作太频繁了，休息一会儿，再来领取！");
            return false;
        }
        return true;
    }

    /**
     * 参数校验
     * 
     * @param result
     * @param couponActivity
     * @param inviteCode
     * @param mobile
     * @param couponActId
     * @param smsCode
     * @return boolean
     */
    private boolean afterValidate(Map<String, Object> result, CouponActivity couponActivity, String inviteCode,
        String mobile) {

        // 判断活动是否存在以及是否有效
        if (couponActivity == null) {
            result.put("code", "-1");
            result.put("msg", "活动不存在");
            return false;
        }
        if (!inviteCode.equals(couponActivity.getInviteCode())) {
            result.put("code", "-1");
            result.put("msg", "邀请码错误");
            return false;
        }
        if (!StatusType.OPEN.getValue().equals(couponActivity.getState())) {
            result.put("code", "-1");
            result.put("msg", "活动已结束");
            return false;
        }
        // 日期校验，如果单纯的是yyyy-mm-dd格式，结束时间得做offset处理
        Date now = new Date();
        if (couponActivity.getStartDate() != null && now.before(couponActivity.getStartDate())) {
            result.put("code", "-1");
            result.put("msg", "活动尚未开始");
            return false;
        }
        if (couponActivity.getEndDate() != null && now.after(couponActivity.getEndDate())) {
            result.put("code", "-1");
            result.put("msg", "活动已结束");
            return false;
        }
        return true;
    }

    /**
     * 是否频繁操作。 超过3次输入错误的验证码视为频繁操作
     * 
     * @return booleanwo
     */
    private boolean isBusyVisit(String mobile) {
        MemcachedUtil mem = MemcachedUtil.getInstance();
        Object countObj = mem.get(COUNT_WRONG_SMS_CODE_INPUT_CACHE_KEY_PREF + mobile);
        if (countObj != null && ((Integer) countObj) >= MAX_SMS_CODE_WRONG_VERIFY) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        MemcachedUtil mem = MemcachedUtil.getInstance();
        System.out.println(mem.tryLock("2"));
        System.out.println(mem.releaseLock("2"));
    }
}

package com.lvtu.wechat.front.web.ordershare;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvmama.allin.common.services.api.IApiClientService;
import com.lvmama.allin.po.activity.OActThematicShare;
import com.lvmama.allin.po.cms.OWxShareOrder;
import com.lvmama.comm.pet.po.mark.MarkCouponCode;
import com.lvmama.comm.pet.po.user.UserUser;
import com.lvmama.comm.pet.service.mark.MarkCouponService;
import com.lvmama.comm.pet.service.user.UserUserProxy;
import com.lvmama.comm.pet.service.user.UserUserProxy.USER_IDENTITY_TYPE;
import com.lvtu.common.utils.JSONUtil;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.model.activity.ordershare.ShareOrderCoupon;
import com.lvtu.wechat.common.model.activity.ordershare.ShareOrderCouponHis;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.ordershare.ShareOrderService;
import com.lvtu.wechat.common.utils.AuthSmsCodeUtils;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.utils.MD5;
import com.lvtu.wechat.common.utils.MemcachedUtil;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.PhoneUtil;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 支付完成分享抽红包活动
 * 
 * @author xuyao
 *
 */
@Controller
@RequestMapping("/orderShare")
@NeedOauth
public class OrderShareController extends BaseController {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 活动类型，用于插入cms
	 */
	private static final String ACT_TYPE = "SHARE_HONGBAO";

	/**
	 * 每个订单最大可领取红包数量
	 */
	private static final int MAX_COUPON_PER_ORDER = 10;

	/**
	 * 订单分享红包的有效期
	 */
	private static final int SHARE_ORDER_EXPIRY_DAYS = 7;
	
	/**
	 * 领取红包锁
	 */
	private final static String WX_ORDER_SHARE_COUPON_LOCK_KEY = "WX_ORDER_SHARE_COUPON_LOCK_KEY";
	
	/**
	 * 活动地址
	 */
	private final static String ORDER_SHARE_ACT_URL_BASE = "https://weixin.lvmama.com/orderShare/index";
	
	/**
	 * 分享图片前缀
	 */
	private final static String ORDER_SHARE_ACT_IMG_URL_PREF = "http://pics.lvjs.com.cn/pics/";

	@Autowired
	private IApiClientService clientService;

	@Autowired
	private ShareOrderService soService;

	@Autowired
	private UserUserProxy userProxy;
	
	@Autowired
	private MarkCouponService markCouponService;

	/**
	 * 支付分享抽红包首页
	 * 
	 * @param param1 订单ID
	 * @param param2 用户ID
	 * @param sign 签名
	 * @param resp
	 * @return
	 */
	@RequestMapping("/index")
	public String index(String param1, String param2, String param3, String sign, Model model, HttpServletRequest request, HttpServletResponse response) {
		String orderId = param1;
		String userId = param2;
		String objectId = param3;
		boolean isOver = false; //是否已领完
		boolean isOverDays = false;//分享是否已过期
		// 数据完整性校验
		if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId) || StringUtils.isBlank(objectId)
				|| !StringUtils.isNumeric(objectId) || StringUtils.isBlank(sign)) {
			return renderString(response, "非法请求!", "text/html;charset=UTF-8");
		}
		// 签名校验
		String signKey = Constants.getConfig("mobile.sign.key");
		String serverSign = MD5.md5(String.format("%s%s%s%s", String.valueOf(orderId), String.valueOf(userId), String.valueOf(objectId), signKey));
		if (!StringUtils.equals(sign, serverSign)) {
			return renderString(response, "非法请求!", "text/html;charset=UTF-8");
		}
		
		//获得分享文案
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("objectId", Integer.parseInt(objectId));
		param.put("type", "APP_750");
		OActThematicShare shareResult = clientService.getWXShare(param);
		if(shareResult != null) {
			String imgUrl = shareResult.getShareImgUrl();
			if(StringUtils.isNotBlank(imgUrl) && !imgUrl.startsWith("http://")) {
				shareResult.setShareImgUrl(ORDER_SHARE_ACT_IMG_URL_PREF + imgUrl);
			}
			shareResult.setShareUrl(ORDER_SHARE_ACT_URL_BASE + "?param1=" + param1 + "&param2=" + param2 + "&param3="
					+ param3 + "&sign=" + sign);
			model.addAttribute("shareInfo", shareResult);
		}
		
		// 如果已经领取过红包
		WechatUser nowUser = getWechatUser(request);
		ShareOrderCouponHis couponHis = soService.getCouponRecord(orderId, nowUser.getOpenid());
		if(couponHis != null) {
			UserUser lvUser = WebchatUtil.getWechatCoopUser(nowUser.getUnionid());
			List<ShareOrderCouponHis> couponHiss = soService.getCouponRecords(orderId);
			model.addAttribute("couponHiss", couponHiss);
			model.addAttribute("coupon", couponHis.getCoupon());
			model.addAttribute("lvUser", lvUser);
			model.addAttribute("wxUser", nowUser);
			model.addAttribute("isGotCoupon", true);
			return "orderShare/getCoupon";
		}
		//判断微信用户是否已经绑定过 ，绑定过直接领取到绑定的账户
		if (WebchatUtil.isBind(nowUser)) {
			String lockKey = WX_ORDER_SHARE_COUPON_LOCK_KEY + orderId;
			if (!MemcachedUtil.getInstance().tryLock(lockKey, 5, 5)) {
				return renderString(response, "红包领取失败，请刷新页面重试！", "text/html;charset=UTF-8");
			}
			try {
				OWxShareOrder shareOrder = null;
				// 获取订单信息
				Map<String, Object> condtions = new HashMap<String, Object>();
				condtions.put("userno", userId);
				condtions.put("orderid", orderId);
				List<OWxShareOrder> shareOrders = clientService.getOWxShareOrder(condtions);
				//cms中不存在时，有微信活动端插入记录
				if (shareOrders == null || shareOrders.isEmpty()) {
					shareOrder = new OWxShareOrder();
					shareOrder.setCreatedTime(new Date());
					shareOrder.setOrderid(orderId);
					shareOrder.setUserno(userId);
					shareOrder.setNum(0);
					shareOrder.setActType(ACT_TYPE);
					clientService.insertOWxShareOrder(shareOrder);
				} else {
					shareOrder = shareOrders.get(0);
				}
				// 判断是否已领完
				if (shareOrder.getNum() != null && shareOrder.getNum() >= MAX_COUPON_PER_ORDER) {
					isOver = true;
					model.addAttribute("isOver", isOver);
				}
				// 判断是否已过期
				if (DateUtils.getDistanceOfTwoDate(shareOrder.getCreatedTime(), new Date()) >= SHARE_ORDER_EXPIRY_DAYS) {
					isOverDays = true;
					model.addAttribute("isOverDays", isOverDays);
				}
				//没有过期并且还有剩余红包
				if (!isOver && !isOverDays) {
					//随机一张优惠券并绑定
					UserUser lvUser = WebchatUtil.getWechatCoopUser(nowUser.getUnionid());
					ShareOrderCoupon gotCoupon = soService.getRandomCoupon(orderId, nowUser.getOpenid());
					Long realCouponId = Long.parseLong(gotCoupon.getCode());
					MarkCouponCode couponCode = markCouponService.generateSingleMarkCouponCodeByCouponId(realCouponId);
					logger.info("订单分享,优惠券code值："+couponCode.getCouponCode());
					Long result = markCouponService.bindingUserAndCouponCode(lvUser, couponCode.getCouponCode());
					logger.info("直接红包领取结果result="+result);
					// 更新红包发放数量
					clientService.updateOWxShareOrder(condtions);
					model.addAttribute("coupon", gotCoupon);
					model.addAttribute("lvUser", lvUser);
				}
				// 获得所有领取记录
				List<ShareOrderCouponHis> couponHiss = soService.getCouponRecords(orderId);
				model.addAttribute("couponHiss", couponHiss);
				model.addAttribute("wxUser", nowUser);
				return "orderShare/getCoupon";
			} finally {
				MemcachedUtil.getInstance().releaseLock(lockKey);
			}
		}else{
			//没有绑定的输入手机号 ，领取到手机号对应账户，不执行绑定操作
			String mobile = request.getParameter("phoneNum");
			if(StringUtils.isNotBlank(mobile)){				
				String lockKey = WX_ORDER_SHARE_COUPON_LOCK_KEY + orderId;
				if (!MemcachedUtil.getInstance().tryLock(lockKey, 5, 5)) {
					return renderString(response, "红包领取失败，请刷新页面重试！", "text/html;charset=UTF-8");
				}
				try {
					OWxShareOrder shareOrder = null;
					// 获取订单信息
					Map<String, Object> condtions = new HashMap<String, Object>();
					condtions.put("userno", userId);
					condtions.put("orderid", orderId);
					List<OWxShareOrder> shareOrders = clientService.getOWxShareOrder(condtions);
					//cms中不存在时，有微信活动端插入记录
					if (shareOrders == null || shareOrders.isEmpty()) {
						shareOrder = new OWxShareOrder();
						shareOrder.setCreatedTime(new Date());
						shareOrder.setOrderid(orderId);
						shareOrder.setUserno(userId);
						shareOrder.setNum(0);
						shareOrder.setActType(ACT_TYPE);
						clientService.insertOWxShareOrder(shareOrder);
					} else {
						shareOrder = shareOrders.get(0);
					}
					// 判断是否已领完
					if (shareOrder.getNum() != null && shareOrder.getNum() >= MAX_COUPON_PER_ORDER) {
						isOver = true;
						model.addAttribute("isOver", isOver);
					}
					// 判断是否已过期
					if (DateUtils.getDistanceOfTwoDate(shareOrder.getCreatedTime(), new Date()) >= SHARE_ORDER_EXPIRY_DAYS) {
						isOverDays = true;
						model.addAttribute("isOverDays", isOverDays);
					}
					//没有过期并且还有剩余红包
					if (!isOver && !isOverDays) {
						//随机一张优惠券并绑定
						UserUser lvUser = userProxy.getUsersByIdentity(mobile, USER_IDENTITY_TYPE.MOBILE);
						logger.info("根据输入手机号得到驴妈妈账户："+lvUser);
						ShareOrderCoupon gotCoupon = soService.getRandomCoupon(orderId, nowUser.getOpenid());
						Long realCouponId = Long.parseLong(gotCoupon.getCode());
						logger.info("订单分享,优惠券批次号值："+realCouponId);
						MarkCouponCode couponCode = markCouponService.generateSingleMarkCouponCodeByCouponId(realCouponId);
						logger.info("订单分享,优惠券code值："+couponCode.getCouponCode());
						//为驴妈妈账户绑定优惠券
						long result = markCouponService.bindingUserAndCouponCode(lvUser, couponCode.getCouponCode());
						logger.info("输入手机号红包领取结果result="+result);
						// 更新红包发放数量
						clientService.updateOWxShareOrder(condtions);
						model.addAttribute("coupon", gotCoupon);
						model.addAttribute("lvUser", lvUser);
					}
					// 获得所有领取记录
					List<ShareOrderCouponHis> couponHiss = soService.getCouponRecords(orderId);
					model.addAttribute("couponHiss", couponHiss);
					model.addAttribute("wxUser", nowUser);
					return "orderShare/getCoupon";
				} finally {
					MemcachedUtil.getInstance().releaseLock(lockKey);
				}			
			}
			
		}
		return "orderShare/index";
	}

	/**
	 * 领取优惠券
	 * 
	 * @param phone
	 * @param smsCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getCoupon")
	public Object getCoupon(String mobile, String smsCode) {
		Map<String, Object> result = getResultMap();
		// 参数校验
		if (StringUtils.isBlank(mobile)) {
			result.put("code", "-1");
			result.put("msg", "手机号不能为空！");
		} else if (!PhoneUtil.isValide(mobile)) {
			result.put("code", "-1");
			result.put("msg", "请输入正确的手机号码！");
		} else if (StringUtils.isBlank(mobile)) {
			result.put("code", "-1");
			result.put("msg", "短信验证码不能为空！");
		} else if (!AuthSmsCodeUtils.checkSMSCodeRedis(mobile, smsCode)) {
			result.put("code", "-1");
			result.put("msg", "请输入正确的短信验证码！");
		} else {
			UserUser lvUser = userProxy.getUsersByIdentity(mobile, USER_IDENTITY_TYPE.MOBILE);
			// 判断手机号是否已经注册，未注册的进行注册，不绑定微信号
			if (lvUser == null) {
				lvUser = WebchatUtil.regUserByMobile(mobile);
				logger.info("手机号注册生成驴妈妈账户："+lvUser);
				logger.info("支付分享抽红包自动注册用户：" + mobile);
			}
		}
		return result;
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param phone
	 *            手机号码
	 * @param imgCode
	 *            图形验证码
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sendSmsCode")
	public Object sendSmsCode(String phone, String imgCode, HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		// 校验入参
		if (StringUtils.isBlank(phone)) {
			resultJson.put("code", "-1");
			resultJson.put("msg", "手机号不能为空！");
			return resultJson;
		}
		if (StringUtils.isBlank(imgCode)) {
			resultJson.put("code", "-1");
			resultJson.put("msg", "图形验证码不能为空！");
			return resultJson;
		}
		// 校验图形验证码
		String lvsessionid = CookieUtils.getCookie(request, "lvsessionid");
		String storedCode = (String) MemcachedUtil.getInstance()
				.get(Constants.WX_IMG_AUTH_CODE_CACHE_KEY_PREF + lvsessionid);
		if (StringUtils.isBlank(storedCode) || !imgCode.equalsIgnoreCase(storedCode)) {
			resultJson.put("code", "-1");
			resultJson.put("msg", "验证码输入有误，或已经过期！");
			return resultJson;
		}
        try {
			// 发送短信
			String response = AuthSmsCodeUtils.sendAuthSMS(getRequest(), phone);
			JSONObject jo = JSONUtil.getObject(response);
			if (jo != null && jo.get("code").equals("1")) {
				resultJson.put("code", "1");
				resultJson.put("msg", "验证码已发送到手机");
			} else {
				resultJson.put("code", "-1");
				resultJson.put("msg",jo.get("message") != null ? jo.get("message") : "验证码发送失败");
			}
		} catch (Exception e) {
			resultJson.put("code", "-1");
			resultJson.put("msg", "验证码发送失败");
		}
        return resultJson;
	}
}

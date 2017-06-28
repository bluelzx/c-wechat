package com.lvtu.wechat.front.web.binding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.mortbay.log.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lvmama.comm.pet.po.user.UserCooperationUser;
import com.lvmama.comm.pet.po.user.UserUser;
import com.lvmama.comm.pet.service.user.UserCooperationUserService;
import com.lvmama.comm.pet.service.user.UserUserProxy;
import com.lvmama.comm.utils.ServletUtil;
import com.lvmama.comm.vo.Constant;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.spring.SpringBeanProxy;
import com.lvtu.wechat.common.utils.AuthSmsCodeUtils;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.common.utils.HttpsUtil;
import com.lvtu.wechat.common.utils.JSONUtil;
import com.lvtu.wechat.common.utils.MD5;
import com.lvtu.wechat.common.utils.RSAUtil;
import com.lvtu.wechat.common.utils.StringUtil;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.WebchatUtil;
/** 
* @ClassName: BindingController 
* @Description: 微信绑定驴妈妈账号相关类
* @author zhengchongxiang
* @date 2017-3-28 下午6:03:16  
*/
@Controller
@RequestMapping("binding")
@NeedOauth
public class BindingController extends BaseController {

	UserUserProxy userUserProxy = SpringBeanProxy.getBean(UserUserProxy.class,
			"userUserProxy");

	UserCooperationUserService userCooperationUserService = SpringBeanProxy
			.getBean(UserCooperationUserService.class,"userCooperationUserService");


	/** 
	* @Title: weixinAccountControlBef 
	* @Description: 微信绑定首页
	* @param @param request
	* @param @param model
	* @param @return
	* @param @throws IOException    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("index")
	public String weixinAccountControlBef(HttpServletRequest request,
			Model model) throws IOException {
		// 获取当前登录用户信息
		WechatUser userInfo = getWechatUser(request);
		String openid = userInfo.getOpenid();
		String callback = request.getParameter("callback");
		String channel = request.getParameter("channel");
		if (!StringUtil.isEmptyString(callback)) {
			callback = new String(Base64.decodeBase64(callback));
		} else {
			callback = "https://m.lvmama.com/my";
		}
		String unionid = request.getParameter("unionid");
		if (!StringUtil.isEmptyString(channel)) {
			model.addAttribute("channel", channel);
		}
		// 先从UserUser表获取
		UserUser bindUser = userUserProxy.getUsersByMobOrNameOrEmailOrCard(openid);
		// 再从第三方联合登录中查询用户信息
		if (bindUser == null) {
			WechatUser wechatUser = WebchatUtil.getUserInfo(openid);
			if (wechatUser != null && StringUtil.isNotEmptyString(wechatUser.getUnionid())) {
				unionid = wechatUser.getUnionid();
				bindUser = getWechatCoopUser(unionid);
				if (bindUser != null) { // 更新UserUser数据，方便消息推送
					bindUser.setWechatId(openid);
					bindUser.setSubScribe("1");
					userUserProxy.update(bindUser);
				}
			}
		}
		// UserUser表和第三方表中都没有即为未绑定
		if (bindUser == null) {
			String username = CookieUtils.getCookie(request, "mb_u");
			if (!StringUtil.isEmptyString(username)) {
				try {
					username = URLDecoder.decode(username, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					username = "";
				}
			}
			String udid = CookieUtils.getCookie(getRequest(), "mb_udid");
			if (null == udid || udid.isEmpty()) {
				udid = UUID.randomUUID().toString();
				CookieUtils.addHttpOnlyCookie(getResponse(), "mb_udid", udid,30, false);// 默认保存30天
			}
			
			model.addAttribute(Constants.LV_SESSION_ID,ServletUtil.getLvSessionId(getRequest(), getResponse()));			 
			model.addAttribute("openId", openid);
			model.addAttribute("unionid", unionid);
			model.addAttribute("channel", channel);
			model.addAttribute("callbackUrl", callback);
			String lvsessionid = ServletUtil.getLvSessionId(getRequest(),getResponse());
			// 生成校验码
			model.addAttribute(
							"imageAuthCodeUrl","https://api3g2.lvmama.com/api/router/rest.do"
									+ "?method=api.com.validateCode.createNewValidateCode&version=1.0.0&lvsessionid="
									+ lvsessionid);
			return "wechat/binding";
		} else {
			getResponse().sendRedirect(callback);// 跳转到原请求url
			return null;
		}
	}

	/** 
	* @Title: getWechatCoopUser 
	* @Description:根据unionid获取合作登录用户信息
	* @param @param unionid
	* @param @return    设定文件 
	* @return UserUser    返回类型 
	* @throws 
	*/
	private UserUser getWechatCoopUser(String unionid) {
		UserUser bindUser = null;
		if (StringUtil.isNotEmptyString(unionid)) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cooperationUserAccount", unionid);
			parameters.put("cooperation", "WEIXIN");
			List<UserCooperationUser> cooperationUseres = userCooperationUserService
					.getCooperationUsers(parameters);
			if (cooperationUseres != null && cooperationUseres.size() > 0) {
				UserCooperationUser copUser = cooperationUseres.get(0);
				bindUser = userUserProxy.getUserUserByPk(copUser.getUserId());
			}
		}
		return bindUser;
	}

	/** 
	* @Title: register 
	* @Description: 进入微信注册绑定页面 
	* @param @param request
	* @param @param model
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("register")
	public String register(HttpServletRequest request, Model model) {
		WechatUser userInfo = getWechatUser(request);
		String openid = userInfo.getOpenid();
		String callback = getRequest().getParameter("callback");
		String channel = getRequest().getParameter("channel");
		if (!StringUtil.isEmptyString(callback)) {
			callback = new String(Base64.decodeBase64(callback));
		} else {
			callback = "http://m.lvmama.com/my";
		}
		String unionid = userInfo.getUnionid();
		String mb = "";

		mb = ServletUtil.getCookieValue(getRequest(), "mb_mb_em");// 默认保存30天
		if (null == mb || "null".equals(mb)) {
			mb = "";
		}
		model.addAttribute("openId", openid);
		model.addAttribute("unionid", unionid);
		model.addAttribute("channel", channel);
		model.addAttribute("mobile", null == mb ? "" : mb);
		String lvsessionid = ServletUtil.getLvSessionId(getRequest(),getResponse());
		model.addAttribute("lvsessionid", lvsessionid);
		model.addAttribute("callbackUrl", callback);
		return "wechat/register";
	}

	/** 
	* @Title: validateCode 
	* @Description: 注册验证图形验证码
	* @param @param mobileOrEMail
	* @param @param authCode
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@ResponseBody
	@RequestMapping("register_code")
	public Object validateCode(String mobileOrEMail, String authCode,HttpServletRequest request) {
		// 手机号校验
		JSONObject resJson = new JSONObject();
		try {
			if (!StringUtil.validMobileNumber(mobileOrEMail)) {
				throw new CustomerException("请输入正确的手机号");
			}
			String udid = ServletUtil.getCookieValue(getRequest(), "mb_udid");
			if (null == udid || udid.isEmpty()) {
				udid = UUID.randomUUID().toString();
				CookieUtils.addHttpOnlyCookie(getResponse(), "mb_udid", udid,30, false);// 默认保存30天
			}
			String lvsessionid = ServletUtil.getLvSessionId(getRequest(),getResponse());
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("method", "api.com.validateCode.register");
			paraMap.put("version", "1.0.0");
			paraMap.put("udid", udid);
			paraMap.put("firstChannel", Constant.CHANNEL.TOUCH.name());
			paraMap.put("secondChannel", "LVMM");
			paraMap.put("lvversion", "7.8.3");
			paraMap.put("mobile", mobileOrEMail);
			paraMap.put("lvsessionid", lvsessionid);
			paraMap.put("ip", getRemoteAddr());
			if (StringUtil.isNotEmptyString(authCode)) {
				paraMap.put("validateCode", authCode);
			}

			String loginUrl = AuthSmsCodeUtils.genLoginUrl(
					"http://api3g2.lvmama.com/usso/router/rest.do?", paraMap);
			Log.info("loginUrl=" + loginUrl);
			String jsons = HttpsUtil.proxyRequestGet(loginUrl,getRemoteAddr(request));
			Log.info(jsons);
			JSONObject jo = JSONUtil.getObject(jsons);
			if (jo.getString("code") != null && jo.getString("code").equals("1")) {
				resJson.put("success", true);
			} else {
				resJson.put("code", jo.getString("code"));
				resJson.put("errorText", jo.getString("message"));
			}
		} catch (Exception e) {
			Log.info("msg" + e.getMessage());
			resJson.put("errorText", e.getMessage());
		}
		return resJson;
	}
	
	/** 
	* @Title: validateMsgauthCode 
	* @Description:  注册校验短信验证码
	* @param @param mobileOrEMail
	* @param @param authenticationCode
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("validateMsgauthCode")
	@ResponseBody
	public Object validateMsgauthCode(String mobileOrEMail,String authenticationCode){
		JSONObject resJson=new JSONObject();
		try {
			if (!StringUtil.validMobileNumber(mobileOrEMail)) {
				throw new CustomerException("请输入正确的手机号");
			}
			if (StringUtil.isEmptyString(authenticationCode)) {
				throw new CustomerException("请输入短信验证码");
			}
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("method","api.com.user.sso.validate.msgauthcode.register");
			paraMap.put("version", "1.0.0");
			paraMap.put("lvversion", "7.8.3");
			paraMap.put("firstChannel", Constant.CHANNEL.TOUCH.name());
			paraMap.put("secondChannel", Constants.SECONDCHANNEL);
			paraMap.put("mobile", mobileOrEMail);
			paraMap.put("msgAuthCode", authenticationCode);
			paraMap.put("lvsessionid",ServletUtil.getLvSessionId(getRequest(), getResponse()));
			Map<String, String> paraMappost = AuthSmsCodeUtils.getMapParamsForHttpPost(paraMap);
			String jsons = HttpsUtil.requestPostForm(
							"http://api3g2.lvmama.com/usso/router/rest.do",paraMappost);
			logger.info("注册校验验证码result="+jsons);
			JSONObject jo = JSONUtil.getObject(jsons);
			resJson.put("code", jo.getString("code"));
			if (!jo.getString("code").equals("1")) {
				resJson.put("errorText", jo.getString("message")!= null ? jo.getString("message") : "网络异常，请稍后再试！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resJson.put("errorText", e.getMessage()!=null  ? e.getMessage() : "网络异常，请稍后再试！");
		}
		return resJson;
	}

	/** 
	* @Title: register 
	* @Description: 驴妈妈账号注册 
	* @param @param password
	* @param @param mobileOrEMail
	* @param @param authenticationCode
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("userRegister")
	@ResponseBody
	public Object register(String password,String mobileOrEMail,String authenticationCode){
		JSONObject resJson=new JSONObject();
		try {
			String lvsessionid = ServletUtil.getLvSessionId(getRequest(),getResponse());
			password=URLDecoder.decode(password, "UTF-8");
			//7.8.1需求 登录密码RSA加密 start
			password = RSAUtil.encryptByPublicKey(password,Constants.getConfig("password.RSA.publicKey"));
			Map<String,Object> paraMap = new HashMap<String, Object>();
			paraMap.put("method", "api.com.user.sso.register");
			paraMap.put("version","1.0.0");
			paraMap.put("firstChannel", Constant.CHANNEL.TOUCH.name());
			paraMap.put("secondChannel", "LVMM");
			paraMap.put("lvversion", "7.8.3");
			paraMap.put("lvsessionid", lvsessionid);
			paraMap.put("mobile", mobileOrEMail);
			paraMap.put("msgAuthCode", authenticationCode);
			paraMap.put("password", password);
			paraMap.put("msgAuthCode", authenticationCode);
			
			Map<String,String> paraMappost = AuthSmsCodeUtils.getMapParamsForHttpPost(paraMap);
			String jsons = HttpsUtil.requestPostForm("http://api3g2.lvmama.com/usso/router/rest.do", paraMappost);		
			Log.info("注册打印jsons="+jsons);
			JSONObject jo = JSONUtil.getObject(jsons);
			if(jo.getString("code") != null && jo.getString("code").equals("1")){
				resJson.put("success", true);
				resJson.put("data",jo.getString("data"));
			}else if(jo.getString("code") != null && jo.getString("code").equals("3")){
				//极验
				JSONObject geetestJsonObject = jo.getJSONObject("data");
				resJson.put("geetest", true);
				resJson.put("geetestSuccess",geetestJsonObject.getString("geetestSuccess"));
				resJson.put("geetestGt",geetestJsonObject.getString("geetestGt"));
				resJson.put("geetestChallenge",geetestJsonObject.getString("geetestChallenge"));
			}else{
				resJson.put("errorText", jo.getString("message"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resJson.put("errorText", e.getMessage());
		}
		return resJson;
	}
	
	/** 
	* @Title: regGivecoupon 
	* @Description: 微信绑定注册账号送优惠券 
	* @param @param openId
	* @param @param unionid
	* @param @param request
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("regGivecoupon")
	@ResponseBody
	public Object regGivecoupon(String openId,String unionid,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		String uid = request.getParameter("uid");
		String channel = request.getParameter("channel");
		try {
			if (StringUtil.isNotEmptyString(openId) && StringUtil.isNotEmptyString(unionid) && StringUtil.isNotEmptyString(uid)) {
				if (StringUtil.isEmptyString(channel)) {
					channel = "0";
				}
				UserUser userUser = userUserProxy.getUserUserByUserNo(uid);
				boolean bdresult = bindWechatCoopUser(userUser, openId, unionid, channel);
				result.put("bdresult", bdresult);
				Log.info("注册账号送优惠券结果：" + result);
			} else {
				result.put("bdresult", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("注册异常日志" + e.getMessage());
			result.put("bdresult", false);
		}
		return result;
	}
	
	/** 
	* @Title: bindWechatCoopUser 
	* @Description: 绑定驴妈妈用户 和微信服务号
	* @param @param user
	* @param @param openid
	* @param @param unionid
	* @param @param channel
	* @param @return
	* @param @throws CustomerException    设定文件 
	* @return boolean    返回类型 
	* @throws 
	*/
	private boolean bindWechatCoopUser (UserUser user, String openid, String unionid, String channel) throws CustomerException {
		if (user != null && user.getId() != null && getWechatCoopUserByUid(user.getId()) != null)
			throw new CustomerException("该驴妈妈账户已经绑定了微信账户！");
		if(StringUtil.isNotEmptyString(unionid) && getWechatCoopUser(unionid) != null)
			throw new CustomerException("该微信号已经绑定了驴妈妈账户！");
		//更新用户表，1.为了区分第三方联合登录和公众号绑定。 2.消息推送
		user.setWechatId(openid);
		user.setSubScribe("1");
		try{
		userUserProxy.update(user);
		}catch(Exception e){
			e.printStackTrace();
		}
		//绑定关系，插入记录到第三方联合登录表
		UserCooperationUser coopUser = new UserCooperationUser();
		coopUser.setUserId(Long.valueOf(user.getId()));
		coopUser.setCooperation("WEIXIN");
		coopUser.setCooperationUserAccount(unionid);
		userCooperationUserService.save(coopUser);		
		//首次绑定送20M流量
		try {
			String bindCallbackUrl = "http://weixin.lvmama.com/wechat/bindCallback/" + openid+"/"+user.getId()+"/"+channel;
			String result = HttpsUtil.requestGet(bindCallbackUrl);
			Log.info("微信绑定送流量： #0, url: #1", result, bindCallbackUrl);
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("微信绑定送流量失败！", e);
		}		
		return true;
	}
	
	/** 
	* @Title: getWechatCoopUserByUid 
	* @Description: 根据userid获取合作登录用户信息
	* @param @param userId
	* @param @return    设定文件 
	* @return UserUser    返回类型 
	* @throws 
	*/
	private UserUser getWechatCoopUserByUid(Long userId) {
		UserUser bindUser = null;
		if(userId != null) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("userId", userId);
			parameters.put("cooperation", "WEIXIN");
			List<UserCooperationUser> cooperationUseres = userCooperationUserService.getCooperationUsers(parameters);
			if(cooperationUseres != null && cooperationUseres.size() > 0) {
				UserCooperationUser copUser = cooperationUseres.get(0);
				bindUser = userUserProxy.getUserUserByPk(copUser.getUserId());
			}
		}
		return bindUser;
	}
	
	/** 
	* @Title: doGeeTest 
	* @Description: 注册极验验证逻辑 
	* @param @param mobileOrEMail
	* @param @param geetest_challenge
	* @param @param geetest_validate
	* @param @param geetest_seccode
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("doGeeTest")
	@ResponseBody
	public Object doGeeTest(String mobileOrEMail,String geetest_challenge,String geetest_validate,String geetest_seccode){
		JSONObject resJson=new JSONObject();
		try {
			String lvsessionid = ServletUtil.getLvSessionId(getRequest(),getResponse());			
			Map<String,Object> paraMap = new HashMap<String, Object>();
			paraMap.put("method", "api.com.user.geetest.enhencedValidate");
			paraMap.put("version","1.0.0");
			paraMap.put("firstChannel", Constant.CHANNEL.TOUCH.name());
			paraMap.put("secondChannel", "LVMM");
			paraMap.put("lvversion", "7.8.3");
			paraMap.put("lvsessionid", lvsessionid);
			paraMap.put("mobile", mobileOrEMail);
			paraMap.put("challenge", geetest_challenge);
			paraMap.put("validate", geetest_validate);
			paraMap.put("seccode", geetest_seccode);			
			Map<String,String> paraMappost = AuthSmsCodeUtils.getMapParamsForHttpPost(paraMap);
			 			
			String jsons = HttpsUtil.requestPostForm("http://api3g2.lvmama.com/usso/router/rest.do", paraMappost);
			Log.info("doGeeTest result="+jsons);			
			JSONObject jo = JSONUtil.getObject(jsons);
			if(jo.getString("code") != null && jo.getString("code").equals("1")){
				resJson.put("success", true);
				resJson.put("data",jo.getString("data"));
			}else{
				resJson.put("errorText", jo.getString("message"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resJson.put("errorText", e.getMessage());
		}
		return resJson;
	}
	
	/** 
	* @Title: t_weixin_login_v7 
	* @Description: 账号登录形式进行微信绑定
	* @param @param openId
	* @param @param unionid
	* @param @param username
	* @param @param password
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("t_weixin_login_v7")
	@ResponseBody
	public Object t_weixin_login_v7(String openId,String unionid,String username,String password) {
		JSONObject result = new JSONObject();
		String channel = getRequest().getParameter("channel");
		try {
			if (StringUtil.isEmptyString(openId) || StringUtil.isEmptyString(unionid)){
				result.put("success", false);
				result.put("errorText", "参数错误，请刷新重试！");
				return result;	
			}
			// 判断当前微信号是否已经绑定过驴妈妈账户
			UserUser bindUser = getWechatCoopUser(unionid);
			if (bindUser != null) {
				result.put("success", false);
				result.put("errorText", "该微信号已经绑定了驴妈妈账户！");
				return result;
			}
			// 进行登录操作
			String lvsessionId = ServletUtil.getLvSessionId(getRequest(),getResponse());
			String udid = ServletUtil.getCookieValue(getRequest(), "mb_udid");
			if (null == udid || udid.isEmpty()) {
				udid = UUID.randomUUID().toString();
				CookieUtils.addHttpOnlyCookie(getResponse(), "mb_udid", udid,30, false);// 默认保存30天
			}
			// 图片验证码验证
			String ptAuthCode = getRequest().getParameter("ptAuthCode");
			if (StringUtil.isEmptyString(ptAuthCode)) {
				result.put("success", false);
				result.put("errorText", "请输入右侧校验码");
				return result;
			}
			username = URLDecoder.decode(username, "UTF-8");
			password = URLDecoder.decode(password, "UTF-8");
			// 以下是对数据进行解密
			byte[] userBytes = Base64.decodeBase64(username);
			byte[] passBytes = Base64.decodeBase64(password);
			username = new String(userBytes, "UTF-8");
			password = new String(passBytes, "UTF-8");
			 //登录密码RSA加密 start
			password = RSAUtil.encryptByPublicKey(password,Constants.getConfig("password.RSA.publicKey"));
			this.getRequest().setAttribute(Constants.LV_SESSION_ID, lvsessionId);
			String signKey = Constant.getInstance().getValue("mobile.sign.key");
			String sign = MD5.md5(String.format("%s%s%s%s", username, password,lvsessionId, signKey));
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("method", "api.com.user.sso.login");
			paraMap.put("version", "1.0.0");
			paraMap.put("udid", udid);
			paraMap.put("firstChannel", Constant.CHANNEL.TOUCH.name());
			paraMap.put("secondChannel", "LVMM");			
			paraMap.put("lvversion", "7.8.3");
			paraMap.put("username", username);
			paraMap.put("password", password);			
			paraMap.put("lvsessionid", lvsessionId);
			paraMap.put("verificationCode", ptAuthCode);
			paraMap.put("sign", sign);
			Map<String,String> paraMappost = AuthSmsCodeUtils.commonGenRegUrl(paraMap);
			Log.info("微信账号形式绑定登录url=http://api3g2.lvmama.com/usso/router/rest.do");
			String jsons = HttpsUtil.requestPostForm("http://api3g2.lvmama.com/usso/router/rest.do", paraMappost);			
			Log.info("微信账号形式绑定登录result="+jsons);
			JSONObject jo = JSONUtil.getObject(jsons);
			// 登录成功
			if (jo.getString("code").equals("2") || jo.getString("code").equals("1")) {
				HttpServletResponse response = getResponse();
				// 用户名密码需要加密
				ServletUtil.addCookie(response, "mb_u",URLEncoder.encode(username, "utf-8"), 30, false);// 默认保存30天
				ServletUtil.addCookie(response, "mb_p", "", 30, false);// 默认保存30天
				UserUser nowUser = getLvUser();//获取当前登录用户信息
				if (null != nowUser) {
					bindUser = getWechatCoopUserByUid(nowUser.getId());
					if (bindUser != null) {
						result.put("success", false);
						result.put("errorText", "该驴妈妈账户已经绑定了微信账户！");
					} else {
						boolean bdresult = bindWechatCoopUser(nowUser, openId,unionid,channel);//进行绑定操作
						result.put("success", bdresult);
						result.put("uid", nowUser.getUserId());
					}
				} else {
					result.put("success", false);
					result.put("errorText", "系统异常，请稍后再试！");
				}
			} else if(jo.getString("code").equals("3")){				
				//极验
				JSONObject geetestJsonObject = jo.getJSONObject("data");
				geetestJsonObject.put("geetest", "true");
				geetestJsonObject.put("code", "3");
				return geetestJsonObject;
			}else{
				result.put("success", false);
				result.put("errorText",jo.getString("message") != null ? jo.getString("message") : "绑定失败，请稍后再试！");
			}
		} catch (CustomerException e) {
			result.put("success", false);
			result.put("errorText", e.getMessage() != null && e.getMessage() != "" ? e.getMessage() : "系统错误，请稍后再试。");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("errorText", "系统错误，请稍后再试。");
		}
		return result;
	}
		
	/** 
	* @Title: sendMobileCode 
	* @Description: 快捷绑定时发送手机验证码
	* @param @param mobileOrEMail 手机号
	* @param @param authCode 图形验证码
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("sendMobileCode")
	@ResponseBody
	public Object sendMobileCode(String mobileOrEMail,String authCode,HttpServletRequest request){
		// 手机号校验 
		Map<String, Object> resultMap = getResultMap();
		if (!StringUtil.validMobileNumber(mobileOrEMail)) {
			resultMap.put("message", "请输入正确的手机号码");
			resultMap.put("code", -1);
			return resultMap;
		}
		String needImageAuthCode = getRequest().getParameter("needImageAuthCode");//需要图片验证码
		//需要图片验证码才进行图片验证码非空校验
		if(StringUtil.isNotEmptyString(needImageAuthCode)){
			if (StringUtil.isEmptyString(authCode)) {
				resultMap.put("message", "请输入右侧校验码");
				resultMap.put("code", -1);
				return resultMap;
			}
		}
		String lvsessionId = ServletUtil.getLvSessionId(getRequest(),getResponse());		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("method", "api.com.user.sso.fastlogin.getMsgAuthCode");
		paraMap.put("version","1.0.0");
		paraMap.put("mobile", mobileOrEMail);
		paraMap.put("validateCode", authCode);
		paraMap.put("lvsessionid", lvsessionId);
		paraMap.put("ip", getRemoteAddr(request));
		Map<String,String> paraMappost = AuthSmsCodeUtils.getMapParamsForHttpPost(paraMap);
		String jsons = HttpsUtil.requestPostForm("http://api3g2.lvmama.com/usso/router/rest.do", paraMappost);
		JSONObject json = JSONUtil.getObject(jsons);
		logger.info("快捷发送验证码result="+jsons);
		return json;
	}

	/** 
	* @Title: weixinLoginByMobileAndAuthCode 
	* @Description: 快捷手机验证方式微信绑定 
	* @param @param openId
	* @param @param unionid
	* @param @param mobileOrEMail
	* @param @param authenticationCode
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("weixinLoginByMobileAndAuthCode")
	@ResponseBody
	public Object weixinLoginByMobileAndAuthCode(String openId,String unionid,String mobileOrEMail,String authenticationCode,
			HttpServletRequest request) {
		JSONObject result = new JSONObject();
		String channel = getRequest().getParameter("channel");		
		//空校验
		if(StringUtil.isEmptyString(openId) || StringUtil.isEmptyString(unionid)){
			result.put("success", false);
			result.put("errorText", "参数错误，请刷新重试！");
			return result;	
		}			
		//判断当前微信号是否已经绑定过驴妈妈账户
		UserUser bindUser = getWechatCoopUser(unionid);
		if(bindUser != null) {
			result.put("success", false);
			result.put("errorText", "该微信号已经绑定了驴妈妈账户！");
			return result;
		}
		// 手机号校验 
		if (!StringUtil.validMobileNumber(mobileOrEMail)) {
			result.put("success", false);
			result.put("errorText", "请输入正确的手机号码");
			return result;
		}
		// 验证码不能为空 ，且长度必须为6 
		if(StringUtil.isEmptyString(authenticationCode) || authenticationCode.length() != 6){
			result.put("success", false);
			result.put("errorText", "请输入正确的短信验证码");
			return result;
		}
		String lvsessionId = ServletUtil.getLvSessionId(getRequest(),getResponse());
		String signKey = Constant.getInstance().getValue("mobile.sign.key");
		String sign = MD5.md5(String.format("%s%s%s%s",mobileOrEMail,authenticationCode,lvsessionId,signKey));
		
		Map<String,Object> paraMap = new HashMap<String, Object>();
		paraMap.put("method", "api.com.user.sso.fastlogin");
		paraMap.put("version","1.0.0");
		paraMap.put("firstChannel", Constant.CHANNEL.TOUCH.name());
		paraMap.put("secondChannel", "LVMM");
		paraMap.put("lvversion", "7.8.3");
		paraMap.put("mobile", mobileOrEMail);
		paraMap.put("msgAuthCode", authenticationCode);
		paraMap.put("lvsessionid", lvsessionId);
		paraMap.put("sign", sign);		
		String loginUrl = AuthSmsCodeUtils.genLoginUrl("http://api3g2.lvmama.com/usso/router/rest.do?",paraMap);
		Log.info("快捷验证方式url="+loginUrl);		
		String jsons = HttpsUtil.proxyRequestGet(loginUrl,getRemoteAddr(request));//请求登陆接口				
		Log.info("快捷验证方式登录结果="+jsons);
		try {
			JSONObject jo = JSONUtil.getObject(jsons);
			String code = jo.getString("code");
			if (code.equals("1")) {
				//登录成功
				ServletUtil.addCookie(getResponse(), "lvtu_mb_mobile",mobileOrEMail, 30, false);// 默认保存30天
				Log.info("....loginByMobileAndAuthCode     success    mobile="+ mobileOrEMail);
				UserUser nowUser = getLvUser();
				if (null != nowUser) {
					bindUser = getWechatCoopUserByUid(nowUser.getId());
					if (bindUser != null) {
						
						
						result.put("success", false);
						result.put("errorText", "该驴妈妈账户已经绑定了微信账户！");
					} else {
						boolean bdresult = bindWechatCoopUser(nowUser,openId,unionid,channel);//进行绑定操作
						result.put("success", bdresult);
						result.put("uid", nowUser.getUserId());
					}
				} else {
					//由于缓存服务器问题可能导致获取用户信息失败，此处有可能发生系统错误。
					result.put("success", false);
					result.put("errorText", "系统错误，请稍后再试。");
				}
			} else if(jo.getString("code") != null && jo.getString("code").equals("3")){
				//极验
				JSONObject geetestResult = jo.getJSONObject("data");
				JSONObject geetestObject = new JSONObject();
				geetestObject.put("code", jo.getString("code"));
				geetestObject.put("geetest", true);				
				geetestObject.put("geetestSuccess",geetestResult.getString("geetestSuccess"));
				geetestObject.put("geetestGt",geetestResult.getString("geetestGt"));
				geetestObject.put("geetestChallenge",geetestResult.getString("geetestChallenge"));
				return geetestObject;
			} else {
				//请求登录失败
				result.put("success", false);
				result.put("errorText",jo.getString("message") != null ? jo.getString("message") : "绑定失败，请稍后再试。");
			}
		}catch(CustomerException e){
			result.put("success", false);
			result.put("errorText", e.getMessage() !=null ? e.getMessage() : "系统错误，请稍后再试。");
		}catch(Exception e){
			e.printStackTrace();
			result.put("success", false);
			result.put("errorText", "系统错误，请稍后再试。");
		}						
		return result;
	}

}

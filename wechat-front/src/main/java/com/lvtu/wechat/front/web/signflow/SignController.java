package com.lvtu.wechat.front.web.signflow;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvtu.wechat.common.annotation.FreqRequestLimit;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.model.activity.signflow.Flow;
import com.lvtu.wechat.common.model.advertising.Advertising;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowService;
import com.lvtu.wechat.common.service.activity.signflow.IWxSignInService;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.AdvertisingUtils;

/**
 * 微信签到送流量
 * @author xuyao
 *
 */
@Controller("signController")
@RequestMapping("/sign")
@NeedOauth
public class SignController extends BaseController {
	
	@Autowired
	private IWxSignInService sigInService;
	
	@Autowired
	private IWxFlowService flowService;
	
	/**
	 * 签到首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/index")
	@FreqRequestLimit(setToken=true)
	public String index(HttpServletRequest request, HttpServletResponse response) {
		// 获得微信用户信息
		WechatUser userInfo = getWechatUser(request);
		request.setAttribute("wxUser", userInfo);
		// 获得用户流量信息
		Flow flow = flowService.getFlow(userInfo.getOpenid());
		request.setAttribute("flow", flow);
		// 获得用户签到信息
		Map<String, Object> signInfoResult = sigInService.signInfo(userInfo.getOpenid());
		if (signInfoResult != null) {
			request.setAttribute("signInfo", signInfoResult);
		}
		//获得广告位信息
		List<Advertising> advertisingList=AdvertisingUtils.getAdvertisingInfo("signFlow","sign");
		request.setAttribute("advertisingInfo", advertisingList);

		return "signflow/signIndex";
	}

	/**
	 * 签到
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/signIn")
	@FreqRequestLimit
	public Object signIn(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = getResultMap();
		try {
			WechatUser userInfo = getWechatUser(request);
			boolean isSuccess = sigInService.signIn(userInfo.getOpenid());
			if (!isSuccess) {
				result.put("code", "-1");
				result.put("msg", "签到失败");
			}
		} catch (CustomerException e) {
			result.put("code", "-1");
			result.put("msg", e.getMessage());
		}
		return result;
	}
}

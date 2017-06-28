package com.lvtu.wechat.front.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lvmama.com.lvtu.common.utils.StringUtil;
import com.lvtu.wechat.common.annotation.NeedFllow;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 微信关注拦截器
 * 
 * @author zhengchongxiang
 *
 */
public class FllowInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 未关注跳到提示关注页面
	 */
	private static final String FLOW_SHARE_URL = "https://weixin.lvmama.com/flow/beforeShare";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			NeedFllow methodNeedFllow = handlerMethod.getMethod().getAnnotation(NeedFllow.class);
			NeedFllow classNeedFllow = handlerMethod.getBeanType().getAnnotation(NeedFllow.class);
			// 需要关注的情况
			if (methodNeedFllow != null || classNeedFllow != null){
				String openid = CookieUtils.getCookie(request, Constants.WX_USER_COOKIE);
				if(StringUtils.isNotBlank(openid)) {
					openid = WebchatUtil.decyptOpenid(openid);
				} else {
					openid = request.getParameter("openid");					
				}
				if(StringUtil.isNotBlank(openid)){
					if(!WebchatUtil.isFollow(openid)){
						//未关注用户访问跳转指引绑定页面
						response.sendRedirect(FLOW_SHARE_URL);
						return false;
					}
				}
			}			
		}
		return true;
	}

}

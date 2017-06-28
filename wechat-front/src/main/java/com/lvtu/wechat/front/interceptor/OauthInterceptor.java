package com.lvtu.wechat.front.interceptor;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.annotation.NoNeedOauth;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 微信认证拦截器
 * 
 * @author xuyao
 *
 */
public class OauthInterceptor extends HandlerInterceptorAdapter {

	/**
	 * 微信网页授权发起地址
	 */
	private static final String WECHAT_OAUTH_API_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope={2}&state=STATE#wechat_redirect";

	/**
	 * 网页授权回调地址
	 */
	private static final String OAUTH_CALLBACK_URL = "https://weixin.lvmama.com/wechat/oauthCallback?callback=";
	
	 /**
     * 网页显示授权回调地址
     */
    private static final String BASE_OAUTH_CALLBACK_URL = "https://weixin.lvmama.com/wechat/baseOauthCallback?callback=";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			NoNeedOauth noNeedOauth = handlerMethod.getMethod().getAnnotation(NoNeedOauth.class);
			if (noNeedOauth != null)
				return true;
			NeedOauth classNeedOauth = handlerMethod.getBeanType().getAnnotation(NeedOauth.class);
			NeedOauth methodNeedOauth = handlerMethod.getMethod().getAnnotation(NeedOauth.class);
			if (methodNeedOauth == null && classNeedOauth == null)
				return true;
			// 判断是否需要强制授权，如果非强制授权，则在非微信浏览器打开不需要授权
			if (!WebchatUtil.isWechatBrowser(request)) {
				if (methodNeedOauth == null && classNeedOauth != null && !classNeedOauth.isForceOauth())
					return true;
				if (methodNeedOauth != null && !methodNeedOauth.isForceOauth())
					return true;
			}
			// 需要授权的情况
			String encryptedOpenid = CookieUtils.getCookie(request, Constants.WX_USER_COOKIE);
			String realOpenid = WebchatUtil.decyptOpenid(encryptedOpenid);
			if (StringUtils.isBlank(realOpenid)) {
				String appid = Constants.getConfig("wechat.appID");
				String code = request.getParameter("code");// 调用微信授权后获得的code码
				if (StringUtils.isBlank(code)) {
					String requestUrl = this.genRequesturl(request);
					//使用显示授权,不然无法获取用户的名称信息
					String targetUrl = null;
					//显示授权
					if (methodNeedOauth != null && !methodNeedOauth.isSnsapiOauth()) {
					    //隐式授权，给WAP站提供
						String callback = URLEncoder.encode(BASE_OAUTH_CALLBACK_URL + new String(Base64.encodeBase64(requestUrl.getBytes())), "UTF-8");
					    targetUrl = MessageFormat.format(WECHAT_OAUTH_API_URL, appid, callback, "snsapi_base");
					}
					else {
					    String callback = URLEncoder.encode(OAUTH_CALLBACK_URL + new String(Base64.encodeBase64(requestUrl.getBytes())), "UTF-8");
					    targetUrl = MessageFormat.format(WECHAT_OAUTH_API_URL, appid, callback, "snsapi_userinfo");
					}
					response.sendRedirect(targetUrl);
					return false;
				}
			}
		} 
		return true;
	}

	/**
	 * 生成callback地址
	 * 
	 * @param request
	 * @return
	 */
	private String genRequesturl(HttpServletRequest request) {
		String url = request.getRequestURI().indexOf("http") == -1
				? (request.getScheme() + "://" + request.getHeader("host") + request.getRequestURI())
				: request.getRequestURI();
		StringBuilder urlBuilder = new StringBuilder(url);
		Map<?, ?> parameterMap = request.getParameterMap();
		Set<?> keys = parameterMap.keySet();
		for (Object key : keys.toArray()) {
			if (urlBuilder.toString().contains("?")) {
				urlBuilder.append("&").append(key.toString()).append("=").append(((String[]) parameterMap.get(key))[0]);
			} else {
				urlBuilder.append("?").append(key.toString()).append("=").append(((String[]) parameterMap.get(key))[0]);
			}
		}
		return urlBuilder.toString();
	}

}

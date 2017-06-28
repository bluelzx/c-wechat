package com.lvtu.wechat.front.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lvtu.wechat.common.utils.Constants;

/**
 * 跨域请求拦截器，实现api接口跨域访问
 * 
 * @author xuyao
 *
 */
public class CORSInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (Constants.isDeugMode()) {
			response.setHeader("Access-Control-Allow-Origin", "https://localhost");
		} else {
			String origin = request.getHeader("Origin");
			if (StringUtils.isNotBlank(origin) && origin.endsWith(".lvmama.com"))
				response.setHeader("Access-Control-Allow-Origin", origin);
			else
				response.setHeader("Access-Control-Allow-Origin", "https://m.lvmama.com");
		}
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		return true;
	}
}

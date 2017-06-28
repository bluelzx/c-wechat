package com.lvtu.wechat.back.interceptor;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lvtu.wechat.back.security.SystemAuthorizingRealm.Principal;
import com.lvtu.wechat.common.model.sys.Log;
import com.lvtu.wechat.common.service.sys.LogService;

/**
 * 日志拦截器
 * @author xuyao
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private LogService logService;

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		@SuppressWarnings("rawtypes")
		Map paramsMap = request.getParameterMap();
		//不记录没有参数的访问
		if(paramsMap == null || paramsMap.size() == 0)
			return;
		//不记录列表页查询
		if(request.getRequestURI().endsWith("list"))
			return;
		//只记录登录后的操作
		Subject subject = SecurityUtils.getSubject();
		Principal principal = (Principal) subject.getPrincipal();
		if (principal != null && principal.getLoginName() != null) {
			Log log = new Log();
			log.setTitle("");
			log.setRemoteAddr(getRemoteAddr(request));
			log.setRequestUri(request.getRequestURI());
			log.setParams(request.getParameterMap());
			log.setOperator(principal.getLoginName());
			log.setCreateDate(new Date());
			logService.saveLog(log);
		}
	}

	/**
	 * 获取用户真实IP地址
	 * 
	 * @param request
	 * @return
	 */
	public String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Real-IP");
		if (StringUtils.isBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		} else if (StringUtils.isBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		} else if (StringUtils.isBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
}
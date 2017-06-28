package com.lvtu.wechat.common.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * 解决不打印异常日志的问题
 * @author xuyao
 *
 */
public class MyExceptionResolver extends SimpleMappingExceptionResolver {

	protected Logger logger = Logger.getLogger(this.getClass());

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		logger.error("Error: ", ex);
		return super.doResolveException(request, response, handler, ex);
	}

}

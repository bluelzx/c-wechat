package com.lvtu.wechat.front.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lvtu.wechat.common.annotation.FreqRequestLimit;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.common.utils.JedisTemplate;
import com.lvtu.wechat.front.utils.FreqRequestHelper;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 频繁请求拦截器
 * 
 * @author xuyao
 * 
 */
public class FreqReqInterceptor extends HandlerInterceptorAdapter {

	Logger logger = Logger.getLogger(FreqReqInterceptor.class);
	
	private static JedisTemplate writeTemplate = JedisTemplate.getWriterInstance();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			FreqRequestLimit freqReqLimit = method.getAnnotation(FreqRequestLimit.class);
			if (freqReqLimit == null) {
			    return true;
			}
			// 获取openid
            String openid = CookieUtils.getCookie(request, Constants.WX_USER_COOKIE);
            openid = WebchatUtil.decyptOpenid(openid);
			if (freqReqLimit.setToken()) {
			    //在request里设置token
			    return setToken(request, response, openid);
			}
			else {
			    return judgeIsFreqReq(request, response, openid);
			}
		}

		return true;
	}

	/**
	 * 设置token
	 * @param request
	 * @param response 
	 * @param openid
	 */
	private boolean setToken(HttpServletRequest request, HttpServletResponse response, String openid) {
	    String token = UUID.randomUUID().toString().replaceAll("-", "");
	    //失效时间为30分钟
	    writeTemplate.setex("wechat_" + openid, token, 1800);
	    CookieUtils.setCookie(response, "kento", token);
	    logger.info("设置token----openid:" + openid + "------token:" + token);
	    return true;
    }

    /**
	 * 判断是否是频繁请求
	 * @param request
	 * @param response
	 * @param openid
	 * @return
	 * @throws IOException
	 */
    private boolean judgeIsFreqReq(HttpServletRequest request, HttpServletResponse response, String openid)
        throws IOException {
        String token = CookieUtils.getCookie(request, "kento");
        if (StringUtils.isBlank(openid)) {
        	openid = request.getParameter("openid");
        	if (StringUtils.isBlank(openid)) {
        		response.setContentType("application/json;charset=UTF-8");
        		response.getWriter().write("{\"code\":-1,\"msg\":\"非法请求！\"}");
        		return false;
        	}
        }
        if (StringUtils.isBlank(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":-1,\"msg\":\"非法请求！\"}");
            return false;
        }
        // 判断是否频繁请求
        if (StringUtils.isNotBlank(openid) && FreqRequestHelper.getInstance().isFreqRequest(openid)) {
            logger.info("Warming---拦截到频繁请求-----------ip:" + request.getRemoteAddr());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":-1,\"msg\":\"请不要频繁请求！\"}");
            return false;
        }
        String redisToken = writeTemplate.getSet("wechat_" + openid, "");
        logger.info("缓存获取token为:" + redisToken);
        if (!token.equals(redisToken)) {
            logger.info("Warming---拦截到频繁请求-----------ip:" + request.getRemoteAddr());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":-1,\"msg\":\"操作失败，请刷新页面重试！\"}");
            return false;
        }
        return true;
        
    }
}

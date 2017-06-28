package com.lvtu.wechat.front.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ApiUtils {

    private static Logger logger = Logger.getLogger(ApiUtils.class);

    /**
     * 判断访问地址的IP是否为内网IP
     * 
     * @param request
     * @return
     */
    public static boolean isLanAccess(HttpServletRequest request) {
        String remoteAddr = getRemortIP(request);
        logger.info("访问ip为：" + remoteAddr);
        // 请求仅限内网发起
        if (!remoteAddr.startsWith("192.168.") && !remoteAddr.startsWith("10.") && !remoteAddr.startsWith("172.")) {
            return false;
        }
        return true;
    }

    public static String getRemortIP(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
}

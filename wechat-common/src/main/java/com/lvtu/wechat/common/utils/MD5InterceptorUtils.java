package com.lvtu.wechat.common.utils;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;

public class MD5InterceptorUtils {
	
	private static final Log Log = LogFactory.getLog(MD5InterceptorUtils.class);
	
    private static final String MD5_SALT = "Vkd0U1UxSkdWbFZYYldoYVZtMDRlRmt5TVhOTmF6VllVbXhTYVdGc1dtRldhazVPVFZkSmVsSnVUazlXYlhoM1ZUTndWazFYU2xWWmVrWlBWMFZhUjFSclVsTlNSazQyVm14d2JGWnRZM2hXUldoSFpXczFWVlp1YUZOV1JscG9Wa1JLTTAxV2JGZGFSV3hQWWtkNFNWVXdWVFZOVmxZMlZtNWFWVTFyV2tOYVJrNHdWa1UxV1ZGcVJtaFhSbG8yVmtSR2EyRXlSWGRQVmxKUFVqSjNlRlpZY0dGaU1sSllVbXR3YUUxc1duTlhWbVJ2VlVkR1ZXSklaR3RXVkd4RFdWWk9NR0ZHVFhwUmJYaHBWbFZ3ZVZONlJrNU5SMUpaVm01Q1dHSlVSWGhXYWtKTFlqQnplVkpzYUdwTlJHd3dWakJvVjA1V1NraGpSelZVWVRKb01GbHJhRXRTVjBwSVkwWndUMDFxVlhsV01uUnZUVzFTY21OSVdrNVNSbG96V2xaV2MxUnNXa2hqU0ZKcFVrWndXbGt3YUVOa01rcDBUVlJHVkdWVWJIcFpNRll3WWxkS1NHRkhhR3hpVkVaM1dURmFiMk15UmxaaVNHeHBZbFJHY0ZwSE1ERmtSMGw0Vlc1R1lVMUhlRFZaYTJSM1V6RndkR1JFUm1wWFNFSXhXV3hqTlZaWFNraGpla3BZVWpOb00xWXhaR0ZrTVc5NFlrY3hhMkpzY0V4Wk1qRXdUVlpzVmxWdVVtRk5TR2Q1V1ZST1YyRkdiSFJQV0d4cVlURktlbGx0ZUhkU1YwVjZWbXR3YW1KWVVYaFpha3BMWkVkR1ZtTkZiR2xpVkVZeVZtdGpOVTFzYkZoVGJrWmhUVWQzZVZsVVRsZFRiRXBJVFZoT2FVMXNiM2xhUjNSelRtMUtkVlpVUWsxTmFrWXlWa1ZrZDJNeVRuVlJWRTVyWW14d1MxcFhNSGhsVm14WFZXNVNhRmRGV2xwWk1HUnJXVlpXU0dSRVJsVlRSWEF5V1d4YWQyVlhTa2hhUm5CaFlsUkdlbGt5ZEZOa01rcEZXa1JLYUdKWFVrdFZNVkYzVUZFOVBRPT0=";

	
    /**
     * 校验SIGN签名
     */
    @SuppressWarnings("unchecked")
	public static boolean check(HttpServletRequest request) {
        String sign = request.getParameter("lvtukey");
        if (StringUtils.isBlank(sign)) {
            return false;
        }

        Map<String, Object> parameterMap = request.getParameterMap();
        String originSign = getSign(parameterMap);
        String expectSign = DigestUtils.md5Hex(originSign + MD5_SALT);
        boolean checkResult = sign.equals(expectSign);
        if(!checkResult) {
        	Log.info("md5签名校验失败, 接收参数为->"+JSONObject.toJSON(parameterMap).toString());
        }
        return checkResult;
    }
    
    @SuppressWarnings("unchecked")
	public static String getSign(Map<String, Object> parameterMap) {
        List<String> keys = new ArrayList<String>(parameterMap.keySet());
        keys.remove("IS_DEBUG"); //剔除IS_DEBUG、sign两个参数
        keys.remove("lvtukey");

        Collections.sort(keys); //键值ASCII码递增排序

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            Object value = parameterMap.get(key);
            if (value == null) {
                continue;
            }
            if (value.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(value); i++) {
                    String item = Array.get(value, i).toString();
                    sb.append(key).append('=').append(item).append('&');
                }
            } else if(value instanceof List) {
                List<Object> items = (List<Object>) value;
                for (Object item : items) {
                    sb.append(key).append('=').append(item.toString()).append('&');
                }
            } else {
                String str = value.toString();
                sb.append(key).append('=').append(str).append('&');
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}

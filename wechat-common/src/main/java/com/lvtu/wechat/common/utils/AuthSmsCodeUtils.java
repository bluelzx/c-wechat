package com.lvtu.wechat.common.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.lvtu.wechat.common.enums.SmsTemplate;
import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.utils.HttpsUtil;
import com.lvtu.wechat.common.utils.JSONUtil;
import com.lvtu.wechat.common.utils.MD5;
import com.lvtu.wechat.common.utils.StringUtil;


/** 
* @ClassName: AuthSmsCodeUtils 
* @Description: 微信发送短信验证码以及校验接口
* @author zhengchongxiang
* @date 2016-12-20 下午2:52:07  
*/
public class AuthSmsCodeUtils {
	
	private static final Log Log = LogFactory.getLog(AuthSmsCodeUtils.class);

	/**
	 * 发送短信验证码
	 * @param request
	 * @param mobile 手机号
	 * @return
	 */		
	public static String sendAuthSMS(HttpServletRequest request, String mobile) {
		// 手机号校验
		String result = null;
		try {
			if (!StringUtil.validMobileNumber(mobile)) {
				throw new CustomerException("请输入正确的手机号");
			}
			String lvsessionid = CookieUtils.getCookie(request, "lvsessionid");
			String ip = request.getLocalAddr();
			String timestamp = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss.S");// 时间戳
			String clientIp = request.getRemoteAddr();// 客户端ip
			String signKey = Constants.getConfig("mobile.sign.key");
			String sign = MD5.md5(lvsessionid + signKey + timestamp + mobile + clientIp);// 签名
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("method", "api.com.user.activity.sendAuthCodeMsg");
			paraMap.put("version", "1.0.0");
			paraMap.put("lvversion", "7.8.3");
			paraMap.put("mobile", mobile);
			paraMap.put("ip", ip);
			paraMap.put("templateId",SmsTemplate.SMS_MOBILE_AUTHENTICATION_CODE.name());
			paraMap.put("lvsessionid", lvsessionid);
			paraMap.put("clientIP", clientIp);
			paraMap.put("sign", sign);
			paraMap.put("stamptime", timestamp);
			String url = genLoginUrl("http://api3g2.lvmama.com/usso/router/rest.do?", paraMap);
			url = url.replaceAll(" ", "%20");
			Log.info("发送短信验证码url=" + url);
			result = HttpsUtil.requestGet(url);
			Log.info("发送短信验证码result=" + result);
			JSONObject jo = JSONUtil.getObject(result);
			if (null != jo && jo.get("code").equals("1")) {
				Map<String, Object> result0 = new HashMap<String, Object>();
				result0.put("code", 1);
				result0.put("msg", "短信验证码发送成功！");
			} else {
				Map<String, Object> result1 = new HashMap<String, Object>();
				result1.put("code", -1);
				result1.put("msg", jo.get("message") != null ? jo.get("message") : "短信验证码发送失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/** 
	* @Description: 生成url 
	* @author 郑崇祥
	* @date 2016-11-16  
	*/
	public static String genLoginUrl(String RequestUrl,Map<String, Object> paraMap) {
		String sign = getSignFromMap(paraMap);
		String signKey = Constants.getConfig("mobile.sign.key");
		String lvtukey = DigestUtils.md5Hex(sign+signKey);
		paraMap.put("lvtukey", lvtukey);
		RequestUrl += getSignFromMap(paraMap);
		return RequestUrl;
	}
	
	/** 
	* @Description: 以a=1&b=2 形式拼接参数串 
	* @author 郑崇祥
	* @date 2016-11-16  
	*/
	@SuppressWarnings("unchecked")
	public static String getSignFromMap(Map<String, Object> paraMap) {
		List<String> keys = new ArrayList<String>(paraMap.keySet());
	    Collections.sort(keys); //键值ASCII码递增排序	
	    StringBuilder sb = new StringBuilder();
	    for (String key : keys) {
	        Object value = paraMap.get(key);
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
	
	public static Map<String,String> commonGenRegUrl(Map<String, Object> paraMap) {
		String sign = getSignFromMap(paraMap);
		String signKey = Constants.getConfig("mobile.sign.key");
		String lvtukey = DigestUtils.md5Hex(sign+signKey);
		
		Iterator<Map.Entry<String, Object>> entries = paraMap.entrySet().iterator();
		Map<String,String> postParaMap = new HashMap<String, String>();
		while (entries.hasNext()) {
			Map.Entry<String, Object> entry = entries.next();
			postParaMap.put(entry.getKey(), (String)entry.getValue());
		}
		postParaMap.put("lvtukey", lvtukey);
        
		return postParaMap;
	}
	
	
	/**
     * check输入的验证码在redies中是否正确
     * @param mobile
     * @param smsCode
     * @return
     */	
	public static boolean checkSMSCodeRedis(String mobile, String smsCode) {
		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(smsCode))
			return false;
		boolean flag = false;
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("method", "api.com.user.activity.validateSmsCode");
			paramMap.put("version", "1.0.0");
			paramMap.put("lvversion", "7.8.3");
			paramMap.put("mobile", mobile);
			paramMap.put("msgAuthCode", smsCode);
			String url = genLoginUrl(
					"http://api3g2.lvmama.com/usso/router/rest.do?", paramMap);
			Log.info("验证短信验证码url=" + url);
			String result = HttpsUtil.requestGet(url);
			Log.info("验证短信验证码result=" + result);
			JSONObject jo = JSONUtil.getObject(result);
			if (null != result && jo.get("code").equals("1")) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/** 
	* @Description:  
	* @author 周天琪 
	* @date 2016-11-16  
	*/
	public static Map<String,String> getMapParamsForHttpPost(Map<String, Object> paraMap) {
		String sign = getSignFromMap(paraMap);
		String signKey = Constants.getConfig("mobile.sign.key");
		String lvtukey = DigestUtils.md5Hex(sign+signKey);
		
		Iterator<Map.Entry<String, Object>> entries = paraMap.entrySet().iterator();
		Map<String,String> postParaMap = new HashMap<String, String>();
		while (entries.hasNext()) {
			Map.Entry<String, Object> entry = entries.next();
			postParaMap.put(entry.getKey(), (String)entry.getValue());
		}
		postParaMap.put("lvtukey", lvtukey);
        
		return postParaMap;
	}
}

package com.lvtu.wechat.front.base;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lvmama.comm.pet.po.user.UserUser;
import com.lvmama.comm.utils.MemcachedUtil;
import com.lvmama.comm.utils.ServletUtil;
import com.lvmama.comm.vo.Constant;
import com.lvtu.wechat.common.base.BeanValidators;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.share.ShareTemplate;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.share.IShareTemplateService;
import com.lvtu.wechat.common.service.weixin.IWechatUserService;
import com.lvtu.wechat.common.spring.SpringBeanProxy;
import com.lvtu.wechat.common.utils.AuthSmsCodeUtils;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.utils.EmojiUtils;
import com.lvtu.wechat.common.utils.HttpsPayUtils;
import com.lvtu.wechat.common.utils.HttpsUtil;
import com.lvtu.wechat.common.utils.JSONUtil;
import com.lvtu.wechat.common.utils.JedisTemplate;
import com.lvtu.wechat.common.utils.JsonMapper;
import com.lvtu.wechat.front.utils.WebchatUtil;

public class BaseController {
	/**
	 * 日志
	 */
	protected static Logger logger = Logger.getLogger(BaseController.class);
	
	/**
	 * request对象
	 */
	protected ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();  
	
	
	/**
	 * response对象
	 */
	protected ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();
    
    /**
     * 联合登录接口
     */
    public static final String COOP_LOGIN_URL = "https://login.lvmama.com/nsso/mobileAjax/cooperationUserRegisterLogin.do";
    
    
    /**
     * 联合登录新接口
     * @author zhengchongxiang
     * @date 20161229 
     */
    public static final String NEW_COOP_LOGIN_URL = "http://api3g2.lvmama.com/usso/router/rest.do";

    
    private static final String MD5_SALT = "Vkd0U1UxSkdWbFZYYldoYVZtMDRlRmt5TVhOTmF6VllVbXhTYVdGc1dtRldhazVPVFZkSmVsSnVUazlXYlhoM1ZUTndWazFYU2xWWmVrWlBWMFZhUjFSclVsTlNSazQyVm14d2JGWnRZM2hXUldoSFpXczFWVlp1YUZOV1JscG9Wa1JLTTAxV2JGZGFSV3hQWWtkNFNWVXdWVFZOVmxZMlZtNWFWVTFyV2tOYVJrNHdWa1UxV1ZGcVJtaFhSbG8yVmtSR2EyRXlSWGRQVmxKUFVqSjNlRlpZY0dGaU1sSllVbXR3YUUxc1duTlhWbVJ2VlVkR1ZXSklaR3RXVkd4RFdWWk9NR0ZHVFhwUmJYaHBWbFZ3ZVZONlJrNU5SMUpaVm01Q1dHSlVSWGhXYWtKTFlqQnplVkpzYUdwTlJHd3dWakJvVjA1V1NraGpSelZVWVRKb01GbHJhRXRTVjBwSVkwWndUMDFxVlhsV01uUnZUVzFTY21OSVdrNVNSbG96V2xaV2MxUnNXa2hqU0ZKcFVrWndXbGt3YUVOa01rcDBUVlJHVkdWVWJIcFpNRll3WWxkS1NHRkhhR3hpVkVaM1dURmFiMk15UmxaaVNHeHBZbFJHY0ZwSE1ERmtSMGw0Vlc1R1lVMUhlRFZaYTJSM1V6RndkR1JFUm1wWFNFSXhXV3hqTlZaWFNraGpla3BZVWpOb00xWXhaR0ZrTVc5NFlrY3hhMkpzY0V4Wk1qRXdUVlpzVmxWdVVtRk5TR2Q1V1ZST1YyRkdiSFJQV0d4cVlURktlbGx0ZUhkU1YwVjZWbXR3YW1KWVVYaFpha3BMWkVkR1ZtTkZiR2xpVkVZeVZtdGpOVTFzYkZoVGJrWmhUVWQzZVZsVVRsZFRiRXBJVFZoT2FVMXNiM2xhUjNSelRtMUtkVlpVUWsxTmFrWXlWa1ZrZDJNeVRuVlJWRTVyWW14d1MxcFhNSGhsVm14WFZXNVNhRmRGV2xwWk1HUnJXVlpXU0dSRVJsVlRSWEF5V1d4YWQyVlhTa2hhUm5CaFlsUkdlbGt5ZEZOa01rcEZXa1JLYUdKWFVrdFZNVkYzVUZFOVBRPT0=";
   
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;
	
	@Autowired
	private IShareTemplateService shareTemplateService;
	
	
	private static JedisTemplate writeTemplate = JedisTemplate.getWriterInstance();
	
	@ModelAttribute  
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response){  
	    requestThreadLocal.set(request);
	    responseThreadLocal.set(response);
    }
	
	/**
	 * 获取当前登录的驴妈妈账户
	 * @param request
	 * @param response
	 * @return
	 */
	public UserUser getLvUser() {
		return (UserUser) ServletUtil.getSession(requestThreadLocal.get(), responseThreadLocal.get(), Constant.SESSION_FRONT_USER);
	}
	
	/**
	 * 获取当前微信用户
	 * @return
	 */
	public WechatUser getWechatUser() {
		HttpServletRequest request = getRequest();
		String openid = CookieUtils.getCookie(request, Constants.WX_USER_COOKIE);
		if(StringUtils.isNotBlank(openid)) {
			openid = WebchatUtil.decyptOpenid(openid);
		} else {
			openid = request.getParameter("openid");
		}
		return WebchatUtil.getUserInfo(openid);
	}

	/**
	 * 获取request对象
	 * @return
	 */
	public HttpServletRequest getRequest(){
		//return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return requestThreadLocal.get();
	}
	
	   /**
     * 获取response对象
     * @return
     */
    public HttpServletResponse getResponse(){
        //return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return responseThreadLocal.get();
    }
	/**
	 * 获取用户IP地址
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
	
	   /**
     * 获取用户IP地址
     * @param request
     * @return
     */
    public String getRemoteAddr() {
       return getRemoteAddr(requestThreadLocal.get());
    }
    
	/**
	 * 生成ajax返回map
	 * @return
	 */
	public Map<String, Object> getResultMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", 1);
		return resultMap;
	}
	
	/**
	 * 服务端参数有效性验证
	 * 
	 * @param object
	 *            验证的实体对象
	 * @param groups
	 *            验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object,
			Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(
					ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 服务端参数有效性验证
	 * 
	 * @param object
	 *            验证的实体对象
	 * @param groups
	 *            验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes,
			Object object, Class<?>... groups) {
		try {
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(
					ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[] {}));
			return false;
		}
		return true;
	}

	/**
	 * 服务端参数有效性验证
	 * 
	 * @param object
	 *            验证的实体对象
	 * @param groups
	 *            验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}

	/**
	 * 添加Model消息
	 * 
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 添加Flash消息
	 * 
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes,
			String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}

	
	 /**
     * 客户端返回JSON字符串
     * 
     * @param response
     * @param object
     * @return
     */
    protected String renderString(Object object) {
        return renderString(responseThreadLocal.get(), JsonMapper.toJsonString(object),
                "application/json");
    }
	
	/**
	 * 客户端返回JSON字符串
	 * 
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object),
				"application/json");
	}

	/**
	 * 客户端返回字符串
	 * 
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string,
			String type) {
		try {
			response.reset();
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 异常
	 */
	@ExceptionHandler({ Exception.class })
	public String onException(Exception e) {
		logger.error("系统出错.", e);
		return "error/500";
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils
						.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}
	
    /**
     * 实现合作登录
     * @param lvsessionid
     * @param wechatUser
     * @param response
     * @return
     */
    public static String coopLogin(String lvsessionid, WechatUser wechatUser, HttpServletResponse response) {
        //如果不存在lvsessionid，则自动获取一个lvsessionid并做登录和写入cookie
        if (StringUtils.isBlank(lvsessionid)) {
            lvsessionid = WebchatUtil.getLvsessionId();
            CookieUtils.setCookie(response, "lvsessionid", lvsessionid);
        }
        return coopLogin(lvsessionid, wechatUser);
    }
    
	
    /**
     * 实现合作登录
     * @param lvsessionid
     * @param wechatUser
     * @return
     */
    public static String coopLogin(String lvsessionid, WechatUser wechatUser) {
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("lvsessionid", lvsessionid);
        if (!StringUtils.isBlank(wechatUser.getUnionid())) {
            paras.put("cooperationUserId", wechatUser.getUnionid());
        }
        if (!StringUtils.isBlank(wechatUser.getNickname())) {
            paras.put("cooperationUserName", EmojiUtils.filterName(wechatUser.getNickname()));
        }
        if (!StringUtils.isBlank(wechatUser.getHeadimgurl())) {
            paras.put("profileImageUrl", wechatUser.getHeadimgurl());
        }
        paras.put("cooperationChannel", "WEIXIN");
        paras.put("loginType", "WEIXIN");
        paras.put("lvversion", "7.6.0");
        paras.put("version", "1.0.0");
        paras.put("firstChannel", "TOUCH");
        paras.put("method", "api.com.sso.deprecated.unionRegAndLogin");
        //用户第三方登录注册渠道增加可自定义功能！！！
        if (wechatUser.getChannel() != null) {
            paras.put("secondChannel", wechatUser.getChannel());   
        }
        else {
            paras.put("secondChannel", "LVMM");   
        }
        String url = AuthSmsCodeUtils.genLoginUrl("http://api3g2.lvmama.com/usso/router/rest.do?", paras);
        logger.info("自动登录url="+url);
        String result =  HttpsUtil.requestGet(url);
        logger.info("调用自动登录" + "lvsessionid:" + lvsessionid + ",cooperationUid：" + wechatUser.getUnionid() + ",cooperationUserName：" + paras.get("cooperationUserName")
         + ",profileImageUrl：" + paras.get("profileImageUrl"));   
        logger.info("自动登录result="+result);
        if(null != result){
        	try{
        	JSONObject jo =JSONUtil.getObject(result);
        	
        	if(jo.get("code").equals("1")){
        		saveWechatBind(lvsessionid, wechatUser);
        	}
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }        
        return result;
    }
    
    /**
     * 绑定微信openId与userId
     * @param lvsessionid
     * @param wechatUser 
     */
    private static void saveWechatBind(String lvsessionid, WechatUser wechatUser) {
        IWechatUserService wechatUserService = SpringBeanProxy.getBean(IWechatUserService.class, "remoteWechatUserService");
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>)MemcachedUtil.getInstance().get(lvsessionid,true);
        if (map == null) {
            return;
        }
        UserUser user = (UserUser) map.get("SESSION_FRONT_USER");
        if (user == null) {
            return;
        }
        wechatUser.setUserId(user.getId());
        wechatUser.setChannel("0");
        wechatUserService.insertIntoWechatUserBind(wechatUser);
    }

    /**
     * 校验SIGN签名
     */
    private static String getLvtuey(Map<String, String> parameterMap) {
        String originSign = getSign(parameterMap);
        String expectSign = DigestUtils.md5Hex(originSign + MD5_SALT);
        return expectSign;
    }

    public static String getSign(Map<String, String> parameterMap) {
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
                @SuppressWarnings("unchecked")
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
    
    /**
     * 获取微信页面分享内容
     * @param shareTemplateId
     * @param model
     */
    public void getShareContent(String shareTemplateId, Model model) {
        ShareTemplate shareTemplate = shareTemplateService.getShareTemplate(shareTemplateId, CommonType.SHARE_CHANNEL_WX.getValue());
        if (shareTemplate != null && shareTemplate.getWxShareContent() != null) {
            model.addAttribute("shareContent", shareTemplate.getWxShareContent());
            if (shareTemplate.getWxShareContent().imageList.size() != 0 && !shareTemplate.getWxShareContent().imageList.isEmpty()) {
                model.addAttribute("imageList", shareTemplate.getWxShareContent().imageList.get(0));
            }
        }
    }
    
    /**
     * 获取当前微信用户
     * @return
     */
    public WechatUser getWechatUser(HttpServletRequest req) {
        String openid = CookieUtils.getCookie(req, Constants.WX_USER_COOKIE);
        if(StringUtils.isNotBlank(openid)) {
            openid = WebchatUtil.decyptOpenid(openid);
        } else {
            openid = req.getParameter("openid");
        }
        return WebchatUtil.getUserInfo(openid);
    }
    
    /**
     * 获取当前登录的驴妈妈账户
     * @param request
     * @param response
     * @return
     */
    public UserUser getLvUser(HttpServletRequest req, HttpServletResponse resp) {
        return (UserUser) ServletUtil.getSession(req, resp, Constant.SESSION_FRONT_USER);
    }
    
    /**
     * 重置redis缓存中的token
     */
    
    public void resetToken(HttpServletRequest req, String openid) {
        String token = CookieUtils.getCookie(req, "kento");
        writeTemplate.setex("wechat_" + openid, token, 1800);
        
    }
}

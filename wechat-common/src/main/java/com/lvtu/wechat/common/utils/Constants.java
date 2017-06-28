package com.lvtu.wechat.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName: Constants
 * @Description: 全局常量
 * 
 */
public final class Constants {

	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("const.properties");

	/**
	 * 微信公众号appid
	 */
	public static final String WX_APP_ID = "wx8d97e09f8f2c3323";

	/**
	 * 中国移动代码
	 */
	public static final String CMCC_CODE = "CMCC";

	/**
	 * 中国联通代码
	 */
	public static final String CUCC_CODE = "CUCC";

	/**
	 * 中国电信代码
	 */
	public static final String CTCC_CODE = "CTCC";

	/**
	 * 缓存用户绑定关系时key使用的前缀
	 */
	public static final String WX_BIND_CATCH_KEY_PREF = "wx_bind_";

	/**
	 * 微信cookie名称
	 */
	public static final String WX_USER_COOKIE = "session_id";

	/**
	 * 微信cookie名称
	 */
	public static final String WX_OAUTH_TRY_TIME_NAME = "oauth_try_times";

	/**
	 * 微信js_apiticket缓存key
	 */
	public static final String WX_JS_API_TICKET_CACHE_KEY = "jsapi_ticket";
	
	/**
	 * 第三方平台缓存前缀
	 */
	public static final String THIRD_PLATFORMS_CACHE_KEY_PREF = "wx_3rd_platforms_";
	
	/**
	 * 匹配域名的正则表达式
	 */
	public static final String DOMAIN_REG = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})$";

	/**
	 * 匹配URL的正则表达式
	 */
	public static final String URL_REG = "[a-zA-z]+://[^\\s]*";

	/**
	 * 微信签到每次增加的流量
	 */
	public static final Integer WX_SIGNIN_FLOW_ADD = 1;

	/**
	 * 微信绑定第一次赠送流量值
	 */
	public static final Integer WX_1ST_BIND_FLOW_ADD = 20;
	
	/**
	 * 接口版本号
	 */
	public static final String TOUCHVERSION="1.0.0";
	
	
    /**
     * 一级渠道
     */
    public static final String FIRSTCHANNEL="TOUCH";
    
    /**
     * 二级渠道
     */
    public static final String SECONDCHANNEL="LVMM";
    
    /**
     * 微信h5优惠券活动的losc
     */
    public static final String H5LOSC="losc=058571";
	
	/**
	 * 微信活动图形验证码缓存key前缀
	 */
	public static final String WX_IMG_AUTH_CODE_CACHE_KEY_PREF = "wx_img_auth_code_";
	
	/**
	 * 加密session_id用到的秘钥
	 */
	public static final String SECRET_KEY = "TL5EkMridqqQ5vmc";

	/**
	 * 缓存瑞冀流量手机号RSA加密公匙信息
	 */
	public static final String WX_RSA_PUBLIC_KEY = "wx_rsa_public_key";
	
	/**
	 * 微信流量兑换合作商选择缓存
	 */
	public static final String WX_FLOW_PARTNER_KEY = "wx_flow_partner_key";
	
	/** 
	* 微信裂变流量活动二次转发量key值
	*/ 
	public static final String WX_FLOW_FROWARD_NUM = "wx_flow_forward_num";
	
	
	/** 
	* 微信裂变流量活动参与人数限制
	*/ 
	public static final String WX_FLOW_TEAM_NUM = "wx_flow_team_num";
	
	/** 
	* 微信裂变流量活动正在请求中
	*/ 
	public static final String WX_FLOW_TEAM_ING = "wx_flow_team_ing";
	
	/** 
	* 甩尾订阅获取用户实际地理位置key
	*/ 
	public static final String WX_ORDER_AREAS_KEY = "wx_order_areas_key";
	
	public static final String LV_SESSION_ID = "lvsessionid";
	
	/**
	 * 获取配置
	 * 
	 * @param key
	 * @return
	 */
	public static String getConfig(String key) {
		return loader.getProperty(key);
	}
	
	/**
	 * 程序是否运行在debug模式下
	 * @return
	 */
	public static boolean isDeugMode() {
		String mode = getConfig("mode");
		return StringUtils.equals("debug", mode);
	}
}

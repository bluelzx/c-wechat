package com.lvtu.wechat.front.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.comm.pet.po.user.UserCooperationUser;
import com.lvmama.comm.pet.po.user.UserUser;
import com.lvmama.comm.pet.service.user.UserCooperationUserService;
import com.lvmama.comm.pet.service.user.UserUserProxy;
import com.lvmama.comm.pet.service.user.UserUserProxy.USER_IDENTITY_TYPE;
import com.lvmama.comm.utils.StringUtil;
import com.lvtu.wechat.common.model.weixin.WechatReqParams;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.weixin.IWechatUserService;
import com.lvtu.wechat.common.spring.SpringBeanProxy;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.Crypto;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.utils.HttpsPayUtils;
import com.lvtu.wechat.common.utils.MemcachedUtil;

/**
 * 微信工具类
 * 
 * @author xuyao
 */
public class WebchatUtil {

    private static Logger log = Logger.getLogger(WebchatUtil.class);

    /**
     * cookie中的openid过期天数
     */
    public static final double COOKIE_OPENID_DAY_OF_EXPIRY = 7;

    /**
     * 主站获取全局access_token接口
     */
    public static String ACCESS_TOKEN_API_URL = Constants.getConfig("weixin.accessTokenUrl");

    /**
     * 全局access_token获取微信用户信息接口
     */
    public static final String WECHAT_USERINFO_API_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang=zh_CN";

    /**
     * 网页授权获取用户信息接口
     */
    public static final String WECHAT_OAUTH_USERINFO_API_URL = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN";

    private static UserUserProxy userUserProxy = SpringBeanProxy.getBean(UserUserProxy.class, "userUserProxy");

    private static UserCooperationUserService userCooperationUserService = SpringBeanProxy
        .getBean(UserCooperationUserService.class, "userCooperationUserService");

    static {
        if (StringUtils.isBlank(ACCESS_TOKEN_API_URL))
            ACCESS_TOKEN_API_URL = "https://pay.lvmama.com/payment/pay/api/weixinPublicAccessToken.do";
    }

    /**
     * WAP站获取LvsessionId URL
     */
    private static final String GET_LVSESSIONID_URL = "https://api3g.lvmama.com/clutter/client/getSessionId.do";
    /**
     * 检查签名是否正确
     * 
     * @return
     */
    public static boolean check(WechatReqParams req) {
        String token = Constants.getConfig("wechat.token");
        if (StringUtils.isNotBlank(token)) {
            String signature = genSignature(token, req.getTimestamp(), req.getNonce());
            if (signature.equals(req.getSignature()))
                return true;
        }
        return false;
    }

    /**
     * 生成微信
     * 
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String genSignature(String token, String timestamp, String nonce) {
        String[] sa = {
            token, timestamp, nonce
        };
        Arrays.sort(sa);
        String sortStr = sa[0] + sa[1] + sa[2];
        String signature = DigestUtils.sha1Hex(sortStr);

        return signature;
    }

    /**
     * 动态加密openid，避免明文传送
     * 
     * @param openid
     * @return
     */
    public static String encyptOpenid(String openid) {
        if (StringUtils.isBlank(openid))
            return null;
        // 添加当前时间戳字符后再进行AES加密
        String timestampStr = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        String randomOpenid = openid + timestampStr;
        return Crypto.encryptAES(randomOpenid, Constants.SECRET_KEY);
    }

    /**
     * 解密openid
     * 
     * @param cyptStr
     * @return
     */
    public static String decyptOpenid(String cyptStr) {
        if (StringUtils.isBlank(cyptStr))
            return null;

        String timestampStr = "";
        try {
            // 先执行AES解密
            int timestampLength = 14;
            String randomOpenid = Crypto.decryptAES(cyptStr, Constants.SECRET_KEY);
            timestampStr = randomOpenid.substring(randomOpenid.length() - timestampLength);
            // 检验时间戳是否正确
            Long.parseLong(timestampStr);
            // 检查cookie是否过期
            Date genDate = DateUtils.parseDate(timestampStr, "yyyyMMddHHmmss");
            double distanceDays = DateUtils.getDistanceOfTwoDate(genDate, new Date());
            if (distanceDays <= COOKIE_OPENID_DAY_OF_EXPIRY) {
                String openid = randomOpenid.substring(0, randomOpenid.length() - timestampLength);
                return openid;
            }
        }
        catch (NumberFormatException e) {
            log.error("解密openid失败。错误的时间戳。" + timestampStr);
        }
        catch (ParseException e) {
            log.error("解密openid失败。错误的时间戳。" + timestampStr);
        }
        catch (Exception e) {
            log.error("解密openid失败。", e);
        }
        return null;
    }

    /**
     * 判断该微信用户是否已经绑定过驴妈妈账户
     * 
     * @param openid
     * @return
     */
    public static boolean isBind(WechatUser wechatUser) {
        if (wechatUser == null || StringUtils.isBlank(wechatUser.getOpenid())) {
            return false;
        }
        //通过第三方和做登录的关联关系查询用户
        UserUser bindUser = null;
        if (StringUtil.isNotEmptyString(wechatUser.getUnionid())) {
            String unionid = wechatUser.getUnionid();
            bindUser = getWechatCoopUser(unionid);
            if (bindUser != null) {
                if (bindUser.getWechatId() == null) {
                    bindUser.setWechatId(wechatUser.getOpenid());
                    bindUser.setSubScribe(wechatUser.getSubscribe());
                    userUserProxy.update(bindUser);
                }
                log.info("用户的userId为：" + bindUser.getUserId());
                return true;
            }
        }
        //如果仍然为空，则去user_user表查询并更新第三方登录表
        if (bindUser == null) {
            bindUser = userUserProxy.getUsersByIdentity(wechatUser.getOpenid(), USER_IDENTITY_TYPE.WECHAT_ID);
            if (bindUser != null) {
                bindUser.setWechatUnionId(wechatUser.getUnionid());
                userUserProxy.update(bindUser);
                // 绑定关系，插入记录到第三方联合登录表
                UserCooperationUser coopUser = new UserCooperationUser();
                coopUser.setUserId(Long.valueOf(bindUser.getId()));
                coopUser.setCooperation("WEIXIN");
                coopUser.setCooperationUserAccount(wechatUser.getUnionid());
                userCooperationUserService.save(coopUser);
                return true;
            }
        }
        
        return false;
    }

    /**
     * 判断该微信用户是否已经绑定过驴妈妈账户
     * 
     * @param openid
     * @return
     */
    public static void upadteSubscribe(WechatUser wechatUser, String subscribe) {
        // 先从UserUser表获取
        UserUser bindUser = null;
        // 再从第三方联合登录中查询用户信息
        if (StringUtil.isNotEmptyString(wechatUser.getUnionid())) {
            String unionid = wechatUser.getUnionid();
            bindUser = getWechatCoopUser(unionid);
            if (bindUser != null) {
                // 更新UserUser数据，方便消息推送
                bindUser.setWechatId(wechatUser.getOpenid());
                bindUser.setSubScribe(subscribe);
                userUserProxy.update(bindUser);
            }
        }
    }

    /**
     * 微信绑定驴妈妈账户
     * 
     * @param wechatUser
     * @param lvUser
     */
    public static boolean bindWxLvmama(WechatUser wechatUser, UserUser lvUser) {
        if (lvUser != null && lvUser.getId() != null && getWechatCoopUserByUid(lvUser.getId()) != null)
            return false;
        if (StringUtil.isNotEmptyString(wechatUser.getOpenid()) && getWechatCoopUser(wechatUser.getUnionid()) != null)
            return false;

        // 更新用户表，1.为了区分第三方联合登录和公众号绑定。 2.消息推送
        lvUser.setWechatId(wechatUser.getOpenid());
        lvUser.setWechatUnionId(wechatUser.getUnionid());
        lvUser.setSubScribe(wechatUser.getSubscribe());
        userUserProxy.update(lvUser);
        // 绑定关系，插入记录到第三方联合登录表
        UserCooperationUser coopUser = new UserCooperationUser();
        coopUser.setUserId(Long.valueOf(lvUser.getId()));
        coopUser.setCooperation("WEIXIN");
        coopUser.setCooperationUserAccount(wechatUser.getUnionid());
        userCooperationUserService.save(coopUser);
        return true;
    }

    /**
     * 根据unionid获取合作登录用户信息
     * 
     * @param unionid
     * @return
     */
    public static UserUser getWechatCoopUser(String unionid) {
        UserUser bindUser = null;
        if (StringUtil.isNotEmptyString(unionid)) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("cooperationUserAccount", unionid);
            parameters.put("cooperation", "WEIXIN");
            UserCooperationUserService userCooperationUserService = SpringBeanProxy
                .getBean(UserCooperationUserService.class, "userCooperationUserService");
            List<UserCooperationUser> cooperationUseres = userCooperationUserService.getCooperationUsers(parameters);
            if (cooperationUseres != null && cooperationUseres.size() > 0) {
                UserCooperationUser copUser = cooperationUseres.get(0);
                log.info("用户的userId为：" + copUser.getUserId());
                bindUser = userUserProxy.getUserUserByPk(copUser.getUserId());
            }
        }
        return bindUser;
    }

    /**
     * 根据userid获取合作登录用户信息
     * 
     * @param userId0
     * @return
     */
    public static UserUser getWechatCoopUserByUid(Long userId) {
        UserUser bindUser = null;
        if (userId != null) {
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("userId", userId);
            parameters.put("cooperation", "WEIXIN");
            List<UserCooperationUser> cooperationUseres = userCooperationUserService.getCooperationUsers(parameters);
            if (cooperationUseres != null && cooperationUseres.size() > 0) {
                UserCooperationUser copUser = cooperationUseres.get(0);
                bindUser = userUserProxy.getUserUserByPk(copUser.getUserId());
            }
        }
        return bindUser;
    }

    /**
     * 快速注册驴妈妈用户,手机号已验证
     * 
     * @param mobibe 手机号
     * @return
     */
    public static UserUser regUserByMobile(String mobibe) {
        if (StringUtils.isBlank(mobibe))
            return null;
        // 生成用户信息
        UserUser newUser = userUserProxy.genDefaultUserByMobile(mobibe);
        // 将手机号码设置为已验证
        newUser.setIsMobileChecked("Y");
        newUser.setGroupId(com.lvmama.comm.vo.Constant.CHANNEL.WEIXIN.name()); // 一级渠道
        newUser.setChannel(com.lvmama.comm.vo.Constant.CHANNEL.WEIXIN + "_LVMM");// 渠道
        // 注册用户
        newUser = userUserProxy.register(newUser);
        return newUser;
    }

    /**
     * 获取微信access token
     * 
     * @param clientIp
     * @return
     */
    public static String getAccessToken() {
        // 获取本机IP
        String clientIp = "";
        try {
            clientIp = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            log.error("获取本机IP失败！");
            clientIp = "127.0.0.1";
        }
        // 调用主站接口获取accesstoken
        String accessDate = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        String tmp = "create_date=" + accessDate + "&input_charset=UTF-8&ip=" + clientIp
            + "&partner=201411201161&key=0b64548d87759b7b0d7db5175b7f07fab99d5b2f";
        String sign = DigestUtils.md5Hex(tmp).toUpperCase();
        String url = ACCESS_TOKEN_API_URL + "?" + "create_date=" + accessDate + "&input_charset=UTF-8&ip=" + clientIp
            + "&partner=201411201161&sign=" + sign + "&isRefresh=1";
        String result = HttpsPayUtils.requestGet(url);
        log.info("urls="+url);
        log.info("results="+result);
        if (result != null) {
            JSONObject obj = (JSONObject) JSONObject.parse(result);
            return obj.getString("access_token");
        }
        return "";
    }

    /**
     * 获取微信ticket
     * 
     * @return
     */
    public static String getJsApiTicket() {
        MemcachedUtil mem = MemcachedUtil.getInstance();
        Object jsApiTicket = mem.get(Constants.WX_JS_API_TICKET_CACHE_KEY);
        if (jsApiTicket != null) {
            return jsApiTicket.toString();
        }
        else {
            String accessToken = getAccessToken();
//            String accessToken = "sXGDBxLW79rbnea06dFhXnUzp-wiN0QlrkFWiC_PQCnkaYKrnhWcPHokY7v2L7bXPpd22IzWSLJnKPc7pUkfJDd6TXI-5cr2KhyiYJOjNg5BePPRFndgtEwQNpQPlLWSQDQaAFAGTG";
            String getJsTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken
                + "&type=jsapi";
            String jsapi_ticket = HttpsPayUtils.requestGet(getJsTicketUrl);
            JSONObject jsapi_ticketJson = JSON.parseObject(jsapi_ticket);
            String ticket = jsapi_ticketJson.getString("ticket");
            if (StringUtils.isNotBlank(ticket) && !ticket.contains("\"errcode\":")) {
                mem.set(Constants.WX_JS_API_TICKET_CACHE_KEY, 60 * 110, ticket);
            }
            else {
                log.error("获取微信js_apiticket失败：" + ticket);
            }
            return ticket;
        }
    }

    /**
     * @Description: 缓存或更新缓存微信用户信息
     * @author liuzhiqiang
     * @date 2014-12-19 上午11:36:02
     */
    public static void cacheWeixinUserInfo(WechatUser userInfo, int expireSeconds) {
        if (userInfo == null || userInfo.getOpenid() == null) {
            return;
        }
        MemcachedUtil mem = MemcachedUtil.getInstance();
        WechatUser cachedUserInfo = getCachedUserInfo(userInfo.getOpenid());
        if (cachedUserInfo != null) {
            // 修改缓存用户信息
            mem.replace(userInfo.getOpenid(), expireSeconds, JSONObject.toJSONString(userInfo));
        }
        else {
            // 缓存微信用户信息
            boolean isAddSuccess = mem.set(userInfo.getOpenid(), expireSeconds, JSONObject.toJSONString(userInfo));
            log.info("add cache success=" + isAddSuccess);
        }
    }

    /**
     * 从缓存中读取微信用户信息
     * 
     * @param userId
     * @return
     */
    private static WechatUser getCachedUserInfo(String userId) {
        MemcachedUtil mem = MemcachedUtil.getInstance();
        Object userObject = mem.get(userId);
        if (userObject != null) {
            return JSONObject.parseObject(userObject.toString(), WechatUser.class);
        }
        return null;
    }

    /**
     * 获取微信用户信息
     * 
     * @param clientIp
     * @param openid
     * @return
     */
    public static WechatUser getUserInfo(String openid) {
        log.info("获取用户信息openid:" + openid);
        if (StringUtils.isBlank(openid))
            return null;

        // 先从缓存中获取
        WechatUser userInfo = getCachedUserInfo(openid);
        if (userInfo != null && userInfo.getOpenid() != null){
            log.info("缓存获取用户信息userId:" + userInfo.getId());
            return userInfo;
        }

        // 从数据库中读取

        IWechatUserService wxuserService = SpringBeanProxy.getBean(IWechatUserService.class, "remoteWechatUserService");
        userInfo = wxuserService.getByOpenid(openid);
        if (userInfo != null && userInfo.getOpenid() != null) {
            cacheWeixinUserInfo(userInfo, 24 * 60 * 60);
            return userInfo;
        }

        // 调用微信API获取用户信息，并加入緩存
        userInfo = getRealUserInfo(openid);
        if (userInfo != null) {
            wxuserService.save(userInfo);
            cacheWeixinUserInfo(userInfo, 24 * 60 * 60);
        }
        return userInfo;
    }
    
    /**
     * 获取微信用户信息不通过缓存
     * 
     * @param clientIp
     * @param openid
     * @return
     */
    public static WechatUser getUserInfoWithoutCache(String openid) {
        
        if (StringUtils.isBlank(openid))
            return null;
        
        // 从数据库中读取
        IWechatUserService wxuserService = SpringBeanProxy.getBean(IWechatUserService.class, "remoteWechatUserService");
        WechatUser userInfo = wxuserService.getByOpenid(openid);
        if (userInfo != null && userInfo.getOpenid() != null) {
            cacheWeixinUserInfo(userInfo, 24 * 60 * 60);
            return userInfo;
        }

        // 调用微信API获取用户信息，并加入緩存
        userInfo = getRealUserInfo(openid);
        if (userInfo != null) {
            wxuserService.save(userInfo);
            cacheWeixinUserInfo(userInfo, 24 * 60 * 60);
        }
        return userInfo;
    }
    /**
     * 获取微信用户信息
     * 
     * @param clientIp
     * @param openid
     * @return
     */
    public static WechatUser subscribe(String openid, String subscribe) {
        IWechatUserService wxuserService = SpringBeanProxy.getBean(IWechatUserService.class, "remoteWechatUserService");
        if (StringUtils.isBlank(openid)) {
            return null;
        }

        // 先从缓存中获取
        WechatUser userInfo = getCachedUserInfo(openid);
        if (userInfo != null && userInfo.getOpenid() != null) {
            if (!subscribe.equals(userInfo.getSubscribe())) {
                userInfo.setSubscribe(subscribe);
                wxuserService.update(userInfo);
                cacheWeixinUserInfo(userInfo, 24 * 60 * 60);
            }
            return userInfo;
        }

        // 从数据库中读取
        userInfo = wxuserService.getByOpenid(openid);
        if (userInfo != null && userInfo.getOpenid() != null) {
            if (!subscribe.equals(userInfo.getSubscribe())) {
                userInfo.setSubscribe(subscribe);
                wxuserService.update(userInfo);
            }
            cacheWeixinUserInfo(userInfo, 24 * 60 * 60);
            return userInfo;
        }

        // 调用微信API获取用户信息，并加入緩存
        userInfo = getRealUserInfo(openid);
        if (userInfo != null) {
            userInfo.setSubscribe(subscribe);
            wxuserService.save(userInfo);
            cacheWeixinUserInfo(userInfo, 24 * 60 * 60);
        }
        return userInfo;
    }

    /**
     * 获取实时的微信用户信息
     * 
     * @param openid
     * @return
     */
    public static WechatUser getRealUserInfo(String openid) {
        if (StringUtils.isBlank(openid))
            return null;
        String accessToken = getAccessToken();
        // String accessToken =
        // "tMnSZP8o6Qi2n_5vMv5D3AkTVkKQ5Z4JHn_eAJb4ywOd1XbKAao-NHY1PfOu2Q504yg9qP4t6ld9xkf-j2fQsevxx43_IAtZiV4P_21hgFprHQbrDOKhtXmHWpphRhDNKEIjAHASMQ";
        if (StringUtils.isNotBlank(accessToken)) {
            String url = MessageFormat.format(WECHAT_USERINFO_API_URL, accessToken, openid);
            String json = HttpsPayUtils.requestGet(url);
            if (json != null && !json.contains("\"errcode\":")) {
                return JSON.parseObject(json, WechatUser.class);
            }
            else {
                log.error("获取微信用户信息失败！" + json);
            }
        }
        else {
            log.error("获取access_token失败！");
        }
        return null;
    }

    /**
     * 通过网页授权的方式获取用户信息
     * 
     * @param openid
     * @param code
     * @return
     */
    public static WechatUser getUserInfo(String openid, String accessToken) {
        if (StringUtils.isBlank(openid) || StringUtils.isBlank(accessToken))
            return null;

        String apiUrl = MessageFormat.format(WECHAT_OAUTH_USERINFO_API_URL, accessToken, openid);
        String json = HttpsPayUtils.requestGet(apiUrl);
        if (json != null && !json.contains("\"errcode\":")) {
            return JSON.parseObject(json, WechatUser.class);
        }
        else {
            return null;
        }
    }

    /**
     * 判断请求是否来着微信浏览器
     * 
     * @param request
     * @return
     */
    public static boolean isWechatBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        if (StringUtils.isNotBlank(userAgent) && userAgent.toLowerCase().indexOf("micromessenger") > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户是否关注公众号
     * 
     * @param openid
     * @return
     */
    public static boolean isFollow(String openid) {
        if (StringUtils.isBlank(openid))
            return false;
        // 获取用户信息
        WechatUser user = getRealUserInfo(openid);
        if (user != null)
            return "1".equals(user.getSubscribe());

        return false;
    }
    
    
    public static String getLvsessionId() {
        String result = HttpsPayUtils.requestGet(GET_LVSESSIONID_URL);
        if (result != null) {
            JSONObject obj = (JSONObject) JSONObject.parse(result);
            if ("1".equals(obj.getString("code"))) {
                return obj.getJSONObject("data").getString("lvsessionid");
            }
        }
        return "";
    }
}
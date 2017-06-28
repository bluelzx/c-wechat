package com.lvtu.wechat.front.web.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.xml.XMLSerializer;

import org.apache.commons.lang3.StringUtils;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import com.lvmama.comm.pet.po.user.UserCooperationUser;
import com.lvmama.comm.pet.po.user.UserUser;
import com.lvmama.comm.pet.service.user.UserCooperationUserService;
import com.lvmama.comm.pet.service.user.UserUserProxy;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.model.activity.signflow.Flow;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowService;
import com.lvtu.wechat.common.service.weixin.IWechatUserService;
import com.lvtu.wechat.common.spring.SpringBeanProxy;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.common.utils.HttpsPayUtils;
import com.lvtu.wechat.common.utils.MemcachedUtil;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.FlowChargeUtils;
import com.lvtu.wechat.front.utils.FlowExchangeUtil;
import com.lvtu.wechat.front.utils.WebchatUtil;

@Controller
@RequestMapping("/wechat")
public class WechatController extends BaseController {

    /**
     * 网页授权获取access_token接口
     */
    public static final String WECHAT_OAUTH_ACCESS_TOKEN_API_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    /**
     * 联合登录接口
     */
    public static final String COOP_LOGIN_URL = "https://login.lvmama.com/nsso/mobileAjax/cooperationUserRegisterLogin.do";

    /**
     * 微信绑定驴妈妈账户
     */
    public static final String M_LVMAMA_BINDING = "https://m.lvmama.com/weixinAccountControl_Before.htm?openid={0}&unionid={1}&callback={2}";
 
   
    @Autowired
    private IWxFlowService flowService;

    @Autowired
    private IWechatUserService wxUserService;

    @Autowired
    private Producer captchaProducer;
    
    @Autowired
    private IWechatUserService wechatUserService;

    /**
     * 微信绑定回调。 在clutter中完成绑定后回调该链接，完成业务逻辑。
     * 
     * @param openid
     * @return
     */
    @ResponseBody
    @RequestMapping("/bindCallback/{openid}/{userid}/{channel}")
    public boolean bindCallback(@PathVariable String openid, @PathVariable Long userid, @PathVariable String channel) {
        if (StringUtils.isBlank(openid) || userid == null || StringUtils.isBlank(channel)) {
            return false;
        }
        // 判断是否已经领取过首次赠送的流量
        try {
            Flow flow = flowService.getFlow(openid);
            if (flow != null && !flow.isGotFirstFlow()) {
                flow.addFlow(Constants.WX_1ST_BIND_FLOW_ADD);
                flow.setGotFirstFlow(true);
                flowService.update(flow);
            }
            WechatUser wechatUser = wechatUserService.getByOpenid(openid);
            if (wechatUser != null) {
                wechatUser.setUserId(userid);
                //用于区分绑定的渠道
                wechatUser.setChannel(channel);
                wechatUserService.insertIntoWechatUserBind(wechatUser);
            }
            
        }catch (Exception e) {
            logger.error("绑定赠送20M流量失败.", e);
            return false;
        }
        return true;
    }

    /**
     * 提供第三方流量提供商回调（西城）
     * 
     * @param reqXml
     * @param response
     */
    @RequestMapping(value = "/flowChargeCallback", method = {RequestMethod.POST})
    public void chargeCallback(@RequestBody String reqXml, HttpServletResponse response) {

        StringBuffer respBuf = new StringBuffer();
        try {
            // 获得请求数据
            String decodedXML = URLDecoder.decode(reqXml, "UTF-8");
            String jsonReq = new XMLSerializer().read(decodedXML).toString();
            logger.info("收到第三方充值回调信息：" + jsonReq);
            JSONObject req = JSONObject.parseObject(jsonReq);
            JSONObject head = req.getJSONObject("head");
            String echo = head.getString("echo");
            String timestamp = head.getString("timestamp");
            String chargeSign = head.getString("chargeSign");
            if (FlowChargeUtils.validateReq(echo, timestamp, chargeSign)) {
                JSONArray body = req.getJSONArray("body");
                int size = body.size();
                for (int i = 0; i < size; i++) {
                    JSONObject item = body.getJSONObject(i);
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("exchangeId", item.getString("orderId"));
                    params.put("returnCode", item.getString("result"));
                    if(StringUtils.isNotBlank(item.getString("desc")) && item.getString("desc").length()>100){
                    	params.put("returnDesc", item.getString("desc").substring(0, 99));
                    }else{
                    	params.put("returnDesc", item.getString("desc"));	
                    }                   
                    flowService.updateStatus(params);
                }
                respBuf.append("<response>");
                respBuf.append("<result>0000</result>");
                respBuf.append("<desc></desc>");
                respBuf.append("</response>");
            }
            else {
                respBuf.append("<response>");
                respBuf.append("<result>0001</result>");
                respBuf.append("<desc>校验失败！</desc>");
                respBuf.append("</response>");
            }
        }
        catch (Exception e) {
            logger.error("充值回调失败，回调XML数据：\n" + reqXml, e);
            respBuf.append("<response>");
            respBuf.append("<result>0001</result>");
            respBuf.append("<desc>服务器异常！</desc>");
            respBuf.append("</response>");
        }

        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(respBuf.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三方流量兑换回调(瑞翼)
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/flowExchangeCallback", method = {
        RequestMethod.POST
    })
    public void flowExchangeCallback(HttpServletRequest request, HttpServletResponse response) {
        boolean flag = false;
        String result = "success";
        String orderNoRuiYi = request.getParameter("orderNo");
        String partnerOrderNo = request.getParameter("partnerOrderNo");
        String status = request.getParameter("status");
        String desc = request.getParameter("desc");
        logger.info("收到第三方充值回调请求----status=" + status + ",desc=" + desc);
        if (StringUtils.isNotBlank(orderNoRuiYi) && StringUtils.isNotBlank(partnerOrderNo)
            && StringUtils.isNotBlank(status)) {
            // status=="1" 成功
            if (status.equals("1")) {
                // 根据订单号查询确认订单是否成功
                flag = FlowExchangeUtil.isCommitedOrder(partnerOrderNo);
                if (!flag) {
                    status = "2";// 状态改为 失败
                }
            }
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("exchangeId", partnerOrderNo);
            params.put("returnCode", status);
            params.put("returnDesc", desc);
            // 更新记录状态
            flowService.updateStatus(params);
        }
        else {
            result = "fail";
        }
        try {
            response.getWriter().write(result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 网页授权回调地址
     * 
     * @param code
     * @throws Base64DecodingException
     */
    @RequestMapping("/oauthCallback")
    public String oauthCallback(HttpServletResponse response, String code, String callback)
        throws Base64DecodingException {
        String appid = Constants.getConfig("wechat.appID");
        String secret = Constants.getConfig("wechat.appsecret");
        String realCallback = new String(Base64.decode(callback));
        // 调用微信接口获取用户信息
        String apiUrl = MessageFormat.format(WECHAT_OAUTH_ACCESS_TOKEN_API_URL, appid, secret, code);
        String token = HttpsPayUtils.requestGet(apiUrl);
        if (StringUtils.isNotBlank(token) && !token.contains("\"errcode\":")) {
            JSONObject obj = (JSONObject) JSONObject.parse(token);
            String accessToken = obj.getString("access_token");
            String openid = obj.getString("openid");
            // 获取用户信息
            WechatUser nowUser = WebchatUtil.getUserInfo(openid, accessToken);
            nowUser.setSubscribe(WebchatUtil.isFollow(openid) ? "1" : "0");
            logger.info("用户昵称" + nowUser.getNickname());
            // 查询已由记录
            WechatUser existUser = wxUserService.getByOpenid(openid);
            if (existUser != null) { // 如果已存在，更新用户信息
                nowUser.setId(existUser.getId());
                wxUserService.update(nowUser);
            }
            else {
                wxUserService.save(nowUser);
            }
            // 加密后再存入cookie
            String encryptedOpenid = WebchatUtil.encyptOpenid(openid);
            CookieUtils.setCookie(response, Constants.WX_USER_COOKIE, encryptedOpenid);
            realCallback = realCallback.replace("http://", "https://");
            return "redirect:" + realCallback;
        }
        else {
            renderString(response, "网页授权获取用户信息失败！", "text/html;charset=UTF-8");
            return null;
        }
    }

    /**
     * 显示网页授权回调地址
     * 
     * @param code
     * @throws Base64DecodingException
     */
    @RequestMapping("/baseOauthCallback")
    public String baseOauthCallback(HttpServletResponse response, String code, String callback)
        throws Base64DecodingException {
        String appid = Constants.getConfig("wechat.appID");
        String secret = Constants.getConfig("wechat.appsecret");
        String realCallback = new String(Base64.decode(callback));
        // 调用微信接口获取用户信息
        String apiUrl = MessageFormat.format(WECHAT_OAUTH_ACCESS_TOKEN_API_URL, appid, secret, code);
        String token = HttpsPayUtils.requestGet(apiUrl);
        if (StringUtils.isNotBlank(token) && !token.contains("\"errcode\":")) {
            JSONObject obj = (JSONObject) JSONObject.parse(token);
            String openid = obj.getString("openid");
            // 加密后再存入cookie
            String encryptedOpenid = WebchatUtil.encyptOpenid(openid);
            CookieUtils.setCookie(response, Constants.WX_USER_COOKIE, encryptedOpenid);
            realCallback = realCallback.replace("http://", "https://");
            return "redirect:" + realCallback;
        }
        else {
            renderString(response, "网页授权获取用户信息失败！", "text/html;charset=UTF-8");
            return null;
        }
    }

    /**
     * 用于在公众号中打开M站或H5链接时实现自动登录
     * 
     * @param target 目标url地址
     * @throws Base64DecodingException 
     */
    @RequestMapping("/preVisit")
    @NeedOauth
    public String preVisit(Model model, HttpServletResponse response, String target, HttpServletRequest request) throws Base64DecodingException {
        String lvsessionid = CookieUtils.getCookie(request, "lvsessionid");
        target = new String(Base64.decode(target));
        //Https改造, 暂时不用
      /*  target = target.replace("http://", "https://");
        target = target.replace("webapp/", "");*/
        WechatUser wechatUser = getWechatUser(request);
        if (WebchatUtil.isBind(wechatUser)) {
            UserUser user = getLvUser(request, response);
            if (user != null) // 如果已经登录直接跳转
                return "redirect:" + target;
            if (StringUtils.isBlank(lvsessionid)) {
                lvsessionid = WebchatUtil.getLvsessionId();
                CookieUtils.setCookie(response, "lvsessionid", lvsessionid);
            }
            String result = coopLogin(lvsessionid, wechatUser);
            logger.info("自动登录结果：" + result);
            return "redirect:" + target;
        }
        else {//跳转到指引绑定页面
            String newUri = Base64.encode(target.getBytes());
            model.addAttribute("wechatUser", wechatUser);
            model.addAttribute("callbackUrl", newUri);//绑定后回调链接
            return "/wechat/accountbing";
        }
    }

    /**
     * 自动登录的同时并且在cookie存入openId
     * 
     * @return
     * @throws Base64DecodingException 
     * @throws IOException 
     */
    @RequestMapping("/preLogin")
    @NeedOauth
    public String preLogin(HttpServletResponse response, String target, HttpServletRequest request) throws Base64DecodingException, IOException {
        WechatUser wechatUser = getWechatUser(request);
        target = new String(Base64.decode(target));
         //Https改造, 暂时不用
       /* target = target.replace("http://", "https://");
        target = target.replace("webapp/", "");*/
        String lvsessionid = CookieUtils.getCookie(request, "lvsessionid");
        CookieUtils.setCookie(response, "oipdne", wechatUser.getOpenid());
        //如果用户的微信已经和驴妈妈帐号绑定，则自动登录
        if (WebchatUtil.isBind(wechatUser)) {
            UserUser user = getLvUser(request, response);
            if (user != null) // 如果已经登录直接跳转
                return "redirect:" + target;
            logger.info("调用自动登录：" + COOP_LOGIN_URL);
            if (StringUtils.isBlank(lvsessionid)) {
                lvsessionid = WebchatUtil.getLvsessionId();
                CookieUtils.setCookie(response, "lvsessionid", lvsessionid);
            }
            String result = coopLogin(lvsessionid, wechatUser);
            logger.info("自动登录结果：" + result);
            return "redirect:" + target;
        }
        else {
            return "redirect:" + target;
        }
    }
    
    /**
     * 自动登录的同时并且在cookie存入openId
     * 
     * @return
     * @throws Base64DecodingException 
     * @throws IOException 
     */
    @RequestMapping("/getOpenid")
    @NeedOauth(isSnsapiOauth=false)
    public String getOpenid(HttpServletResponse response, String target, HttpServletRequest request) throws Base64DecodingException, IOException {
        WechatUser wechatUser = getWechatUser(request);
        target = new String(Base64.decode(target));
        CookieUtils.setCookie(response, "oipdne", wechatUser.getOpenid());
        return "redirect:" + target;
    }
    
    
    /** 
     * @Description: 用谷歌api生成校验码
     * @author wxxuyuan
     * @date 2015-9-9 下午3:34:07
     */
    @RequestMapping("/imgAuthCode")
    public void genImgAuthCode(HttpServletRequest request, HttpServletResponse response) {
        OutputStream out = null;
        try {
            String lvsessionid = CookieUtils.getCookie(request, "lvsessionid");
            String authCode = captchaProducer.createText();
            Boolean isSuccess = MemcachedUtil.getInstance().set(Constants.WX_IMG_AUTH_CODE_CACHE_KEY_PREF + lvsessionid,
                600, authCode);
            logger.info("Gen LoginVerifyCode google:" + authCode + " success:" + isSuccess);
            response.setContentType("images/jpeg");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            out = response.getOutputStream();
            BufferedImage image = captchaProducer.createImage(authCode);
            ImageIO.write(image, "jpg", out);
            out.flush();
        }
        catch (Exception e) {
            logger.error("生成图形校验码失败!", e);
        }
        finally {
            try {
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                out = null;
            }
        }
    }
    
    /**
     * 跳转到解绑页面
     * @return
     */
    @RequestMapping("toUnbind")
    public String index(Model model,HttpServletRequest request){   	
    	WechatUser wechatUser = getWechatUser(request);
    	model.addAttribute("isBind", "false");
    	if(WebchatUtil.isBind(wechatUser)){
    		model.addAttribute("isBind", "true");
    	}
    	return "wechat/unbind";
    }
    
	/**
	 * 微信解绑
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value="weixinUnbind")
	public Object weixinUnbind(HttpServletRequest request) {
		WechatUser wechatUser = getWechatUser(request);
		JSONObject jsonObj = new JSONObject();		
		if(WebchatUtil.isBind(wechatUser)){
			UserUserProxy userUserProxy = SpringBeanProxy.getBean(UserUserProxy.class, "userUserProxy");
			UserUser user1=userUserProxy.getUsersByMobOrNameOrEmailOrCard(wechatUser.getOpenid());
			//通过账号管理解除绑定
			if(null!=user1){
				user1.setWechatId("none");
				userUserProxy.update(user1);
				logger.info(user1.getUserName()+" bind wechat id:"+wechatUser.getOpenid()+"-->null");				
				//注册之前解除微信绑定
			}
			Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("cooperationUserAccount", wechatUser.getUnionid());
            parameters.put("cooperation", "WEIXIN");
            UserCooperationUserService userCooperationUserService = SpringBeanProxy
                .getBean(UserCooperationUserService.class, "userCooperationUserService");
            List<UserCooperationUser> cooperationUseres = userCooperationUserService.getCooperationUsers(parameters);
            if(cooperationUseres!=null && cooperationUseres.size()>0){
            	UserCooperationUser copUser = cooperationUseres.get(0);
            	copUser.setCooperationUserAccount("none");
            	userCooperationUserService.update(copUser);
            	jsonObj.put("success", true);
            	logger.info(copUser.getUserId()+" bind wechat unionid:"+wechatUser.getUnionid()+"-->null");          	
            }			
		}
		logger.info("微信解绑结果:"+jsonObj);
		return jsonObj;		
	}
}
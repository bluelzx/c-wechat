package com.lvtu.wechat.front.web.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lvmama.comm.utils.HttpsUtil;
import com.lvtu.utils.StringUtil;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.comm.ApiResponse;
import com.lvtu.wechat.common.model.template.Template;
import com.lvtu.wechat.common.model.template.TemplateRecords;
import com.lvtu.wechat.common.model.weixin.template.TemplateData;
import com.lvtu.wechat.common.model.weixin.template.WxTemplate;
import com.lvtu.wechat.common.service.template.ITemplateService;
import com.lvtu.wechat.common.utils.AuthSmsCodeUtils;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.utils.Http;
import com.lvtu.wechat.common.utils.MD5InterceptorUtils;
import com.lvtu.wechat.common.utils.TemplatesUtils;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 微信相关api封装
 * 
 * @author xuyao
 *
 */
@Controller
@RequestMapping("/api")
public class WechatApi extends BaseController {
	
	/**
	 * 微信网页授权发起地址
	 */
	private static final String WECHAT_OAUTH_API_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope={2}&state=STATE#wechat_redirect";

	/**
	 * 网页授权回调地址
	 */
	private static final String OAUTH_CALLBACK_URL = "https://weixin.lvmama.com/wechat/oauthCallback?callback=";
	
	private static final String TEMPLATE_COLOR = "#173177";
	
	@Autowired
	private ITemplateService templateService;
	
	
	/**
	 * 获取jsapi_ticket
	 * @return
	 */
	@RequestMapping("/jsapi_ticket")
	@ResponseBody
	public ApiResponse<String> jsApiTicket() {
		String jsApiTicket = WebchatUtil.getJsApiTicket();
		if (StringUtils.isBlank(jsApiTicket)) {
			return new ApiResponse<String>("-1", "获取jsapi_ticket失败！");
		}
		ApiResponse<String> resp = new ApiResponse<String>();
		resp.setData(jsApiTicket);
		return resp;
	}

	/**
	 * 获取微信jsapi签名相关信息
	 * @param shareUrl 调用接口的当前页面地址，不包含#后面的部分
	 * @return
	 */
	@RequestMapping("/jsApiSign")
	@ResponseBody
	public ApiResponse<Map<String, Object>> jsApiSign(String shareUrl) {
		if (StringUtils.isBlank(shareUrl)) {
			return new ApiResponse<Map<String, Object>>("-1", "shareUrl为空！");
		}
		//decode url
		try {
			String shateUrlTmp = new String(shareUrl.getBytes("ISO-8859-1"), "ISO-8859-1");
			if (shareUrl.equals(shateUrlTmp)) {
				shareUrl = new String(shareUrl.getBytes("ISO-8859-1"), "UTF-8");
			}
			shareUrl = java.net.URLDecoder.decode(shareUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("解析url出错:" + shareUrl, e);
			return new ApiResponse<Map<String, Object>>("-1", "解析url出错，获取jsapi_ticket失败！");
		}
		// 从缓存中获取ticket
		String ticket = WebchatUtil.getJsApiTicket();
		if(StringUtils.isBlank(ticket)) {
			return new ApiResponse<Map<String, Object>>("-1", "获取jsapi_ticket失败！");
		}
		//生成签名
		Long timestampMil = System.currentTimeMillis() / 1000;
		String noncestr = UUID.randomUUID().toString();
		String sign = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestampMil + "&url="
				+ shareUrl;
		String signature = DigestUtils.sha1Hex(sign);
		//组装返回数据
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("appId", Constants.WX_APP_ID);
		result.put("timestamp", timestampMil);
		result.put("nonceStr", noncestr);
		result.put("signature", signature);
		ApiResponse<Map<String, Object>> resp = new ApiResponse<Map<String, Object>>();
		resp.setData(result);
		return resp;
	}
	
	/**
	 * 对外授权接口
	 * @param callback 回调地址(base64编码)
	 * @throws IOException 
	 */
	@RequestMapping("/oAuthProxy")
	public void oAuthProxy(String callback, HttpServletResponse response, HttpServletRequest request) throws IOException {
		// 非空校验
		if (StringUtils.isBlank(callback)) {
			renderString(response, new ApiResponse<Map<String, Object>>("-1", "回调地址不能为空！"));
			return;
		}
		// 回调地址校验
		String realCallbackUrl = new String(Base64.decodeBase64(callback), "UTF-8");
		if (!realCallbackUrl.matches("[a-zA-z]+://[^\\s]*")) {
			renderString(response, new ApiResponse<Map<String, Object>>("-1", "错误的回调地址！"));
			return;
		}
		// 校验回调地址是否在lvmama.com域名下
		String domain = realCallbackUrl.contains("?") ? realCallbackUrl.substring(0, realCallbackUrl.indexOf("?"))
				: realCallbackUrl;
		if (!domain.contains(".lvmama.com")) {
			renderString(response, new ApiResponse<Map<String, Object>>("-1", "只能在 lvmama.com 域名下调用！"));
			return;
		}
		// 查询cookie中的session_id
		String encryptedOpenid = CookieUtils.getCookie(request, Constants.WX_USER_COOKIE);
		String realOpenid = WebchatUtil.decyptOpenid(encryptedOpenid);
		// 如果cookie中不存在session_id或者已过期，进行授权操作
		if (StringUtils.isBlank(realOpenid)) {
			String appid = Constants.getConfig("wechat.appID");
			String callbackUrl = URLEncoder.encode(OAUTH_CALLBACK_URL + callback, "UTF-8");
			String targetUrl = MessageFormat.format(WECHAT_OAUTH_API_URL, appid, callbackUrl, "snsapi_userinfo");
			response.sendRedirect(targetUrl);
		} else {
			response.sendRedirect(realCallbackUrl);
		}
	}
	
	/** 
	* @Title: templateMsg 
	* @Description: 发送模板消息对外接口
	* @param @param sender 发送方
	* @param @param templateId 模板ID
	* @param @param openid
	* @param @param url 跳转URL
	* @throws 
	*/
	@RequestMapping("sendMsg")
	@ResponseBody
	public Object templateMsg(String sender,String templateId, String openid, String url,
 HttpServletRequest request) {
		ApiResponse<Object> resp = new ApiResponse<Object>();
		if (!MD5InterceptorUtils.check(request)) {
			resp.setCode("-1");
			resp.setMsg("抱歉，签名校验失败！!");
			return resp;
		}
		if (StringUtil.isEmpty(templateId) || StringUtil.isEmpty(openid)) {
			resp.setCode("-1");
			resp.setMsg("必填参数不能为空!");
			return resp;
		}
		Template template = templateService.selectByTemplateId(templateId);
		if (template == null) {
			resp.setCode("-1");
			resp.setMsg("此模板配置不存在!");
			return resp;
		}
		TemplateRecords templateRecords = new TemplateRecords();//发送记录
		templateRecords.setSender(sender);
		templateRecords.setTemplateId(templateId);
		templateRecords.setOpenid(openid);
		templateRecords.setRetryCount(template.getRetryCount());
		WxTemplate wxTemplate = new WxTemplate();
		wxTemplate.setTemplate_id(templateId);
		wxTemplate.setTopcolor(TEMPLATE_COLOR);
		wxTemplate.setTouser(openid);
		wxTemplate.setUrl(url);
		String[] values = template.getFields().split(",");
		for (int i = 0; i < values.length; i++) {
			TemplateData tmdata = new TemplateData();
			tmdata.setValue(request.getParameter(values[i]));
			wxTemplate.getData().put(values[i], tmdata);
		}
		String result = TemplatesUtils.sendMessage(wxTemplate);
		Log.info("推送"+openid+"=result=" + result);
		JSONObject obj = (JSONObject) JSONObject.parse(result);
		templateRecords.setContent(JSONObject.toJSON(wxTemplate).toString());
		if (obj != null && "0".equals(obj.getString("errcode"))) {
			templateRecords.setSuccess(CommonType.SEND_SUCCESS.getValue());
			resp.setMsg("消息推送成功!");
		} else {
			templateRecords.setSuccess(CommonType.SEND_FAILED.getValue());
			resp.setCode("-1");
			resp.setMsg("消息推送失败!");
		}
		templateService.save(templateRecords);// 保存消息发送记录
		return resp;
	}
	
	@RequestMapping("test")
	@ResponseBody
	public Object test(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("templateId", "Syt8VqAcIjlJQ8ZEidx995zoJrzhC6GW9QlMVti-6xc");
		paramMap.put("openid", "okA7Sjhg7QPXOOxdxd1EGj4Y1WCk");
		paramMap.put("url", "http://baidu.com");
		paramMap.put("first", "标题");
		paramMap.put("sender", "WEIXIN");
		paramMap.put("order", "12333");		
		paramMap.put("Name", "name");
		paramMap.put("datein", "datein");
		paramMap.put("dateout", "dateout");
		paramMap.put("room type", "豪华");
		paramMap.put("number", "100");
		paramMap.put("pay", "1000元");
		String url = AuthSmsCodeUtils.genLoginUrl("http://weixin.lvmama.com/api/sendMsg?", paramMap);
		url = url.replaceAll(" ", "%20");
		String result = HttpsUtil.requestGet(url);
		return result;
	}
}

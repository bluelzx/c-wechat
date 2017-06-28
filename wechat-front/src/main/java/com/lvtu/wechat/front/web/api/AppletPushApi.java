package com.lvtu.wechat.front.web.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lvmama.com.lvtu.common.utils.HttpsUtil;
import com.lvtu.wechat.common.model.weixin.template.TemplateData;
import com.lvtu.wechat.common.model.weixin.template.WxTemplate;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.utils.Http;
import com.lvtu.wechat.common.utils.HttpsPayUtils;
import com.lvtu.wechat.common.utils.JSONUtil;
import com.lvtu.wechat.common.utils.StringUtil;
import com.lvtu.wechat.front.base.BaseController;
/**
 * 微信小程序订单购买和订单支付推送接口
 * @author majun
 *
 */
@Controller
@RequestMapping("api/applet")
public class AppletPushApi extends BaseController {
	
	private static final Log LOG = LogFactory.getLog(AppletPushApi.class);

    /** 
    * @Fields TEMPLATE_URL : 微信模板消息接口URL 
    */ 
    private static final String TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";
    
    /** 
    * @Fields TEMPLATE_ID : 订单支付成功消息模板ID
    */ 
    private static final String TEMPLATE_ID_PAY = "7a6vHEVQ03PWIM-wOSQNz4nmEWRMYd4QYrokTt4Q01c";
    
    /** 
    * @Fields TEMPLATE_ID : 订单购买成功消息模板ID
    */ 
    private static final String TEMPLATE_ID_BUY = "0MJhoqJ2cGjw9jthP30oZCi35uJ_sL9b8po_jRTxMc0";
    
    /** 
    * @Fields TEMPLATE_COLOR : TODO 
    */ 
    private static final String TEMPLATE_COLOR = "#173177";
    
    public static String ACCESS_TOKEN_API_URL = "https://pay.lvmama.com/payment/pay/weixinPublicAccessTokenForApplet.do";
	
	/** 
	* @Title: sendMessage 
	* @Description: 发送模板消息接口 
	* @param @param request
	* @param @param response
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("send")
	@ResponseBody
	public Object sendMessage(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = getResultMap();
		result.put("code", -1);
		result.put("msg", "消息推送失败");
		if (!validate(request)) {
			result.put("msg", "参数缺失！");
			return result;
		}
		try {
			String accessToken = getAccessToken();
			WxTemplate wxTemplate = new WxTemplate();
			String messageType = request.getParameter("messageType");
			if (StringUtil.isEmptyString(messageType)) {
				result.put("code", -1);
				result.put("msg", "推送消息类型不能为空！");
				return result;
			} else if (messageType.equals("1")) {
				//用1或者2区别是订单支付还是订单购买
				wxTemplate = makeWxTemplatePay(request);
			} else if (messageType.equals("2")) {
				wxTemplate = makeWxTemplateBuy(request);
			}
			String results = Http.Send("POST", TEMPLATE_URL + accessToken,JSONObject.toJSON(wxTemplate).toString());
			System.out.println(JSONObject.toJSON(wxTemplate).toString());
			net.sf.json.JSONObject jo = JSONUtil.getObject(results);
			if (null != jo && jo.get("errcode").equals("0")) {
				result.put("code", 1);
				result.put("msg", "消息推送成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * @throws IOException  
	* @Title: validate 
	* @Description: 验证请求信息 
	* @param @param request
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws  
	*/
	public boolean validate(HttpServletRequest request){
		String messageType = request.getParameter("messageType");
		String openid = request.getParameter("openid");	
		String formId = request.getParameter("formId");	
		if(StringUtil.isEmptyString(openid) || StringUtil.isEmptyString(formId) || StringUtil.isEmptyString(messageType)){
			return false;
		}
		return true;
	}
	
	public static String getAccessToken(){
        // 获取本机IP
        String clientIp = "";
        try {
            clientIp = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            clientIp = "127.0.0.1";
        }
        // 调用主站接口获取accesstoken
        String accessDate = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        String tmp = "create_date=" + accessDate + "&input_charset=UTF-8&ip=" + clientIp
            + "&partner=1402585202&key=e4fe5b9c97b2d7e1a68e14163e48ac8b";
        String sign = DigestUtils.md5Hex(tmp).toUpperCase();
        String url = ACCESS_TOKEN_API_URL + "?" + "create_date=" + accessDate + "&input_charset=UTF-8&ip=" + clientIp
            + "&partner=1402585202&sign=" + sign + "&isRefresh=1";

        String result = HttpsPayUtils.requestGet(url);
        LOG.info("tokenurl="+url);
        LOG.info("result"+result);
        if (result != null) {
            JSONObject obj = (JSONObject) JSONObject.parse(result);
            return obj.getString("access_token");
        }
        return "";
	}
	
	public static WxTemplate makeWxTemplatePay(HttpServletRequest request){
		WxTemplate wxTemplate = new WxTemplate();
    	wxTemplate.setTemplate_id(TEMPLATE_ID_PAY);
    	wxTemplate.setTopcolor(TEMPLATE_COLOR);
        wxTemplate.setTouser(request.getParameter("openid"));
        wxTemplate.setPage(request.getParameter("page"));
        wxTemplate.setForm_id(request.getParameter("formId"));
        //订单号码
        TemplateData templateData = new TemplateData();
        templateData.setValue(request.getParameter("orderId"));
        wxTemplate.getData().put("keyword1", templateData);
        
        //订单金额
        templateData = new TemplateData();
        templateData.setValue(request.getParameter("orderAmount"));
        wxTemplate.getData().put("keyword2", templateData);
        
        //订单状态
        templateData = new TemplateData();
        templateData.setValue(request.getParameter("orderStatus"));
        wxTemplate.getData().put("keyword3", templateData);
        
        //物品名称
        templateData = new TemplateData();
        templateData.setValue(request.getParameter("prodName"));
        wxTemplate.getData().put("keyword4", templateData);
        return wxTemplate;
	}
	
	public static WxTemplate makeWxTemplateBuy(HttpServletRequest request){
		WxTemplate wxTemplate = new WxTemplate();
    	wxTemplate.setTemplate_id(TEMPLATE_ID_BUY);
    	wxTemplate.setTopcolor(TEMPLATE_COLOR);
        wxTemplate.setTouser(request.getParameter("openid"));
        wxTemplate.setPage(request.getParameter("page"));
        wxTemplate.setForm_id(request.getParameter("formId"));
        //物品名称
        TemplateData templateData = new TemplateData();
        templateData.setValue(request.getParameter("prodName"));
        wxTemplate.getData().put("keyword1", templateData);
        
        //取票时间
        templateData = new TemplateData();
        templateData.setValue(request.getParameter("fetchTime"));
        wxTemplate.getData().put("keyword2", templateData);
        
        //入园凭证
        templateData = new TemplateData();
        templateData.setValue(request.getParameter("passCode"));
        wxTemplate.getData().put("keyword3", templateData);
        
        //交易单号
        templateData = new TemplateData();
        templateData.setValue(request.getParameter("dealId"));
        wxTemplate.getData().put("keyword4", templateData);
        
        //游玩日
        templateData = new TemplateData();
        templateData.setValue(request.getParameter("playTime"));
        wxTemplate.getData().put("keyword5", templateData);
        return wxTemplate;
	}
	
	@RequestMapping("test")
	public void test(){
		Map<String,String> paramMap =  new HashMap<String, String>();
		paramMap.put("messageType", "1");
		paramMap.put("openid", "okA7SjlUkGlcdkjo5QCsVQLLAq8A");
		paramMap.put("prodName", "train");
		paramMap.put("orderId", "201706218879");
		paramMap.put("orderAmount", "50");
		paramMap.put("orderStatus", "1");
		paramMap.put("formId", "15454");
		paramMap.put("page", "1534543");
		String value = HttpsUtil.requestPostForm("https://weixin.lvmama.com/api/applet/send", paramMap);
		LOG.info("resultsss="+value);
	}
	

}

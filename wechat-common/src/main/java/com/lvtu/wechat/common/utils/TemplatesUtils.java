package com.lvtu.wechat.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.lvtu.wechat.common.model.productorder.PushTemplate;
import com.lvtu.wechat.common.model.weixin.template.TemplateData;
import com.lvtu.wechat.common.model.weixin.template.WxTemplate;

public class TemplatesUtils {
	
	private static final Log LOG = LogFactory.getLog(TemplatesUtils.class);

    private static final String TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    private static final String TEMPLATE_ID = "qCz45lZrFpAFtPFJttW0NGiboDoDUZ5-4hpSaXhUPCk";
    //private static final String TEMPLATE_ID = "J-vK8IuRsDA2Qu16x1ClByOApMHI33rVOfs66IuKEkU";
    private static final String TEMPLATE_COLOR = "#173177";
    
    /**
     * 主站获取全局access_token接口
     */
    public static String ACCESS_TOKEN_API_URL = Constants.getConfig("weixin.accessTokenUrl");
    
    static {
        if (StringUtils.isBlank(ACCESS_TOKEN_API_URL))
            ACCESS_TOKEN_API_URL = "https://pay.lvmama.com/payment/pay/api/weixinPublicAccessToken.do";
    }
    
    /** 
    * @Title: sendMessage 
    * @Description: 推送消息
    * @param @param wxTemplate
    * @param @return    设定文件 
    * @return Object    返回类型 
    * @throws 
    */
    public static String sendMessage(WxTemplate wxTemplate){
    	String accessToken = getAccessToken();
    	String result = Http.Send("POST", TEMPLATE_URL + accessToken, JSONObject.toJSON(wxTemplate).toString());
    	return result;
    }
    
    public static String sendMessage(String data){
    	String accessToken = getAccessToken();
    	String result = Http.Send("POST", TEMPLATE_URL + accessToken, data);
    	return result;
    }
    

    /** 
    * @Title: getAccessToken 
    * @Description: 获取微信access token 
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws 
    */
    public static String getAccessToken() {
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
            + "&partner=201411201161&key=0b64548d87759b7b0d7db5175b7f07fab99d5b2f";
        String sign = DigestUtils.md5Hex(tmp).toUpperCase();
        String url = ACCESS_TOKEN_API_URL + "?" + "create_date=" + accessDate + "&input_charset=UTF-8&ip=" + clientIp
            + "&partner=201411201161&sign=" + sign + "&isRefresh=1";
        String result = HttpsPayUtils.requestGet(url);
        LOG.info("tokenurl="+url);
        LOG.info("result"+result);
        if (result != null) {
            JSONObject obj = (JSONObject) JSONObject.parse(result);
            return obj.getString("access_token");
        }
        return "";
    }
    
    /** 
    * @Title: makeWxTemplate 
    * @Description: 消息模板信息 
    * @param @param pushtemplate
    * @param @param openid
    * @param @return    设定文件 
    * @return WxTemplate    返回类型 
    * @throws 
    */
    public static WxTemplate makeWxTemplate(PushTemplate pushtemplate,String openid){
    	WxTemplate wxTemplate = new WxTemplate();
    	wxTemplate.setTemplate_id(TEMPLATE_ID);
        wxTemplate.setTopcolor(TEMPLATE_COLOR);
        wxTemplate.setTouser(openid);
        wxTemplate.setUrl(pushtemplate.getUrl());
        //活动标题
        TemplateData templateData = new TemplateData();
        templateData.setValue(pushtemplate.getTitle());
        wxTemplate.getData().put("first", templateData);
        //来源
        templateData = new TemplateData();
        templateData.setValue(pushtemplate.getSource());
        wxTemplate.getData().put("keyword1", templateData);
        //内容
        templateData = new TemplateData();
        templateData.setValue(pushtemplate.getContent());
        wxTemplate.getData().put("keyword2", templateData);
        //时间
        templateData = new TemplateData();
        String date = DateUtils.getDate("yyyy年MM月dd日 HH:mm:ss");
        templateData.setValue(date);
        wxTemplate.getData().put("keyword3", templateData);
        //备注
        templateData = new TemplateData();
        templateData.setValue(pushtemplate.getBack());
        wxTemplate.getData().put("remark", templateData);
    	return wxTemplate;
    }
    
  


}

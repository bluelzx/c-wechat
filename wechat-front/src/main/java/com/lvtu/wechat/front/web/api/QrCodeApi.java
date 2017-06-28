package com.lvtu.wechat.front.web.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.comm.ApiResponse;
import com.lvtu.wechat.common.utils.Http;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.Constants;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 获取二维码图片相关接口
 * 
 * @author qianqingchen
 *
 */
@Controller
@RequestMapping("/api")
public class QrCodeApi extends BaseController  {
    
    //二维码展示
    private final String WX_QRCODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    
    //获取二维码接口
    private final String WX_GET_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    
    //普通二维码失效时间
    private final int EXPIRE_SECONDS = 2592000;
    
    private static String qrcode_token = Constants.getConfig("QRCODE_TOKEN");
   
    static {
        if (StringUtils.isBlank(qrcode_token))
            qrcode_token = "eGlhbnNoYW5naHVhbmppbmc=";
    }
    /**
     * 获取二维码
     */
    @RequestMapping(value = "getQrCode", method=RequestMethod.POST)
    @ResponseBody
    public ApiResponse<String> getQrCode(HttpServletRequest request) {
        JSONObject scene = new JSONObject();
        JSONObject qrcodeJson = new JSONObject();
        JSONObject actionInfo = new JSONObject();
        actionInfo.put("scene", scene);
        qrcodeJson.put("action_info", actionInfo);
        String accessToken = WebchatUtil.getAccessToken();
        String sceneType = request.getParameter("SCENE_TYPE");
        String token = request.getParameter("TOKEN");
        //二维码请求token线上环境和测试环境不一致，避免测试环境调用线上的接口
        if (StringUtils.isBlank(token)) {
            logger.info("请求的token为：" + token);
            logger.info("配置的token为：" + qrcode_token);
            return new ApiResponse<String>("-1", "请求的token为空");
        }
        if (!token.equals(qrcode_token)) {
            logger.info("请求的token为：" + token);
            logger.info("配置的token为：" + qrcode_token);
            return new ApiResponse<String>("-1", "请求的token不正确");
        }
        //二维码请求 开启关闭接口，如果为0，则为关闭，获取的二维码永远是临时二维码
        String env = Constants.getConfig("QrCode.env");
        logger.info("env为：" + env);
        if ("0".equals(env)) {
            sceneType = CommonType.QR_SCENE.getStringValue();
        }
        if (CommonType.QR_LIMIT_STR_SCENE.getStringValue().equals(sceneType)) {
            String sceneStr = request.getParameter("SCENE_STR");
            if (StringUtils.isBlank(sceneStr)) {
                return new ApiResponse<String>("-1", "参数不正确");
            }
            scene.put("scene_str", sceneStr);
            qrcodeJson.put("action_name", "QR_LIMIT_STR_SCENE");
        }
        else if (CommonType.QR_LIMIT_SCENE.getStringValue().equals(sceneType)) {
            String sceneId = request.getParameter("SCENE_ID");
            if (StringUtils.isBlank(sceneId)) {
                return new ApiResponse<String>("-1", "参数不正确");
            }
            scene.put("scene_id", sceneId);
            qrcodeJson.put("action_name", "QR_LIMIT_SCENE");
        }
        else if (CommonType.QR_SCENE.getStringValue().equals(sceneType)){
            logger.info("生成临时二维码");
            String sceneId = request.getParameter("SCENE_ID");
            if (StringUtils.isBlank(sceneId)) {
                return new ApiResponse<String>("-1", "参数不正确");
            }
            scene.put("scene_id", sceneId);
            qrcodeJson.put("action_name", "QR_SCENE");
            qrcodeJson.put("expire_seconds", EXPIRE_SECONDS);
        }
        else {
            return new ApiResponse<String>("-1", "二维码类型错误");
        }
        String result = Http.Send("POST", WX_GET_QRCODE_URL + accessToken, qrcodeJson.toString());
        JSONObject resultJson = JSONObject.parseObject(result);
        if (resultJson.getString("ticket") == null) {
            return new ApiResponse<String>("-1", "生成二维码失败:" + resultJson.getString("errmsg"));
        }
        ApiResponse<String> resp = new ApiResponse<String>();
        resp.setData(WX_QRCODE_URL + resultJson.getString("ticket"));
        return resp;
    }
}

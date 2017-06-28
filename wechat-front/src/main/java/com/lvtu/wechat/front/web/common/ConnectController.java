package com.lvtu.wechat.front.web.common;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvtu.wechat.common.model.weixin.WechatReqParams;
import com.lvtu.wechat.common.utils.MessageUtil;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.EventDispatcher;
import com.lvtu.wechat.front.utils.MsgDispatcher;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 与微信后台对接的controller
 * 
 * @author xuyao
 */
@Controller
@RequestMapping("/connect")
public class ConnectController extends BaseController {

//    @Autowired
//    private ThirdPlatformService platformService;

    /**
     * 接收微信get验证请求
     * 
     * @param params 校验参数
     * @return
     */
    @RequestMapping(value = "/msg", method = RequestMethod.GET)
    @ResponseBody
    public Object recevieGetMsg(WechatReqParams params) {
        if (WebchatUtil.check(params)) {
            logger.info("-------------微信接入成功！-------------");
            return params.getEchostr();
        }
        return "error";
    }

    /**
     * 接收微信消息时间推送
     * 
     * @param params 校验参数
     * @param reqXml 消息体
     * @return
     */
    @RequestMapping(value = "/msg", method = RequestMethod.POST)
    @ResponseBody
    public Object receviePostMsg(WechatReqParams params, @RequestBody String reqXml) {
        try {
            if(!WebchatUtil.check(params)) {
                logger.error("身份校验失败");
                return "error";
            }
            Map<String, String> map = MessageUtil.parseXml(reqXml);
            String resp = null;
            String msgtype = map.get("MsgType");
            if (MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgtype)) {
                resp = EventDispatcher.processEvent(map); // 进入事件处理
            }
            else {
                resp = MsgDispatcher.processMessage(map); // 进入消息处理 PrintWriter out = response.getWriter();
            }
            return resp;
        } 
        catch (Exception e) {
            logger.error(e);
        }
        return "success";
    }

//    /**
//     * 转发xml消息
//     *
//     * @param platform
//     * @param msgXml
//     * @return
//     */
//    private String transmitMsg(String msgXml) {
//        ThirdPlatform platform = getTargetPlatform(msgXml);
//        if (platform != null) {
//            String url = genMsgTransmitUrl(platform);
//            String result = HttpsUtil.requestPostXml(url, msgXml);
//            return result;
//        }
//        else {
//            logger.warn("没有找到第三方目标转发平台，消息丢失！");
//            return "success";
//        }
//    }
//
//    /**
//     * 生成对应平台的消息请求url
//     *
//     * @param platform
//     * @return
//     */
//    private String genMsgTransmitUrl(ThirdPlatform platform) {
//        String apiUrl = platform.getApiUrl();
//        String token = platform.getApiToken();
//        String timestamp = DateUtils.getDate("yyyyMMddHHmmss");
//        String nonce = "lvmama";
//        // 生成签名和请求url
//        String signature = WebchatUtil.genSignature(token, timestamp, nonce);
//        String parmes = "?signature=" + signature + "&timestamp=" + timestamp + "&nonce=" + nonce;
//        return apiUrl + parmes;
//    }
//
//    /**
//     * 获取消息转发目标平台
//     *
//     * @param msgXml
//     * @return
//     */
//    private ThirdPlatform getTargetPlatform(String msgXml) {
//        WechatReqMsgBody msg = XmlUtil.xmlToObject(msgXml, WechatReqMsgBody.class);
//        MsgType msgType = MsgType.valueOf(msg.getMsgType());
//        // 先匹配关键字转发
//        if (msgType == MsgType.text) {
//            List<ThirdPlatform> platforms = getPlatformsByType(ForwardType.KEYWORD);
//            for (ThirdPlatform platform : platforms) {
//                String[] words = platform.getForwardKeyword().split(",");
//                for (String word : words) {
//                    if (msg.getContent().startsWith(word))
//                        return platform;
//                }
//            }
//        }
//        // 如果没有关键字匹配，查找全部转发
//        List<ThirdPlatform> platforms = getPlatformsByType(ForwardType.ALL);
//        if (platforms != null && platforms.size() > 0)
//            return platforms.get(0);
//
//        return null;
//    }
//
//    /**
//     * 根据类型获取第三方平台
//     *
//     * @param type
//     * @return
//     */
//    private List<ThirdPlatform> getPlatformsByType(ForwardType type) {
//        List<ThirdPlatform> platforms = null;
//        if (type != null) {
//            platforms = getCachedPlatforms(type);
//            if (platforms == null) {
//                Map<String, Object> parames = new HashMap<String, Object>();
//                parames.put("forwardType", type);
//                parames.put("useable", true);
//                platforms = platformService.findPlatforms(parames);
//                if (platforms != null) {
//                    cachePlatforms(platforms, type, 24 * 60 * 60);
//                }
//            }
//        }
//        return platforms;
//    }
//
//    /**
//     * 根据转发类型缓存第三方平台
//     * 
//     * @param platforms
//     * @param type
//     * @param expireSeconds
//     */
//    public void cachePlatforms(List<ThirdPlatform> platforms, ForwardType type, int expireSeconds) {
//        if (platforms == null || platforms.size() == 0) {
//            return;
//        }
//        MemcachedUtil mem = MemcachedUtil.getInstance();
//        List<ThirdPlatform> _platforms = getCachedPlatforms(type);
//        if (_platforms != null) {
//            mem.replace("" + type.name(), expireSeconds, JSONObject.toJSONString(platforms));
//        }
//        else {
//            String cacheKey = Constants.THIRD_PLATFORMS_CACHE_KEY_PREF + type.name();
//            boolean isAddSuccess = mem.set(cacheKey, expireSeconds, JSONObject.toJSONString(platforms));
//            logger.info("add platforms cache success=" + isAddSuccess);
//        }
//    }
//
//    /**
//     * 获取缓存的第三方平台列表
//     * 
//     * @param type
//     * @return
//     */
//    private List<ThirdPlatform> getCachedPlatforms(ForwardType type) {
//        MemcachedUtil mem = MemcachedUtil.getInstance();
//        String cacheKey = Constants.THIRD_PLATFORMS_CACHE_KEY_PREF + type.name();
//        Object userObject = mem.get(cacheKey);
//        if (userObject != null) {
//            return JSONObject.parseArray(userObject.toString(), ThirdPlatform.class);
//        }
//        return null;
//    }

    /**
     * 接收微信get验证请求
     * 
     * @param params 校验参数
     * @return
     */
    @RequestMapping(value = "/checkToken", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkToken(WechatReqParams params) {
        if (WebchatUtil.check(params)) {
            logger.info("-------------微信接入成功！-------------");
            return true;
        }
        return false;
    }
}

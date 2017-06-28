package com.lvtu.wechat.front.utils;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.lvtu.wechat.common.enums.ChannelType;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.utils.MessageUtil;
import com.lvtu.wechat.front.web.common.WechatController;
import com.lvtu.wechat.front.web.qrcode.WxQrCode;
import com.lvtu.wechat.front.web.tnt.GetTicketInfoByQrcode;

public class EventDispatcher {
    public static String processEvent(Map<String, String> map) {
        String eventKey = map.get("EventKey");
        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) { // 关注事件
            if (!StringUtils.isBlank(eventKey) && eventKey.indexOf("qrscene_") != -1) {
                eventKey = eventKey.replace("qrscene_", "");
                return processEventByEventKey(map, eventKey);
            }
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) { // 取消关注事件
            String openId = map.get("FromUserName");
            //设置用户为取消关注！！！
            WechatUser wechatUser = WebchatUtil.subscribe(openId, CommonType.UNSUBSCRIBE.getStringValue());
            //取消关注的同时，设置会员系统的用户表，微信为未关注
            WebchatUtil.upadteSubscribe(wechatUser, CommonType.UNSUBSCRIBE.getStringValue());
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_SCAN)) { // 扫描二维码事件
            if (!StringUtils.isBlank(eventKey)) {
                return processEventByEventKey(map, eventKey);
            }
        }
        if(map.get("Event").equals(MessageUtil.EVENT_TYPE_TEMPLATE)){//发送模板消息的回调事件
        	String msgId = map.get("MsgID");
        	String openid = map.get("FromUserName");
        	String status = map.get("Status");
        	if(StringUtils.isNotBlank(msgId)){
        		return TemplateUtil.updatePushRecords(openid, msgId, status);
        	}       	
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_LOCATION)) { // 位置上报事件
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_CLICK)) { // 自定义菜单点击事件
        }

        if (map.get("Event").equals(MessageUtil.EVENT_TYPE_VIEW)) { // 自定义菜单 View 事件
        }
        return "success";
    } 
    
    /**
     * 根据二维码参数的识别做对应的处理
     * @param map
     * @param eventKey
     * @return
     */
    private static String processEventByEventKey(Map<String, String> map, String eventKey) {
        if (eventKey.startsWith("tnt_")) {
            return tntScan(map, eventKey);
        }
        if (eventKey.startsWith("weixin_")) {
            return WxQrCode.getPushMessageByQrCode(eventKey, map);
        }
        return "success";
    }


    /**
     * 分销商扫码用户，扫码进来注册驴妈妈帐号并且绑定
     * @param map
     * @param eventKey
     * @return
     */
    private static String tntScan(Map<String, String> map, String eventKey) {
        String openId = map.get("FromUserName");
        //设置用户为关注！！！
        WechatUser wechatUser = WebchatUtil.subscribe(openId, CommonType.SUBSCRIBE.getStringValue());
        //关注 并且没有绑定驴驴妈妈帐号则直接注册
        if (!WebchatUtil.isBind(wechatUser)) {
            String lvsessionid = UUID.randomUUID().toString();
            wechatUser.setChannel(ChannelType.TIAN_NIU.getValue());
            WechatController.coopLogin(lvsessionid, wechatUser);
        }
        //修改用户对应的主站数据为关注状态
        WebchatUtil.upadteSubscribe(wechatUser, CommonType.SUBSCRIBE.getStringValue());
        //调用分销的接口获取推送数据
        return GetTicketInfoByQrcode.getProductInfoFromTnt(eventKey, map);
    }
}

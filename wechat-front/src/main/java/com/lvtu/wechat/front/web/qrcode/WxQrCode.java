package com.lvtu.wechat.front.web.qrcode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lvtu.wechat.common.model.qrcode.QRCode;
import com.lvtu.wechat.common.model.qrcode.UseQRCode;
import com.lvtu.wechat.common.model.weixin.message.reply.Article;
import com.lvtu.wechat.common.model.weixin.message.reply.NewsMessage;
import com.lvtu.wechat.common.service.qrcode.IQRCodeService;
import com.lvtu.wechat.common.spring.SpringBeanProxy;
import com.lvtu.wechat.common.utils.MessageUtil;
import com.lvtu.wechat.front.base.BaseController;

public class WxQrCode extends BaseController {
    private static IQRCodeService qrCodeService = SpringBeanProxy.getBean(IQRCodeService.class, "qRCodeService");
    
    public static String getPushMessageByQrCode(String eventKey, Map<String, String> map) {
        QRCode qrCode = new QRCode();
        qrCode.setParam(eventKey);
        UseQRCode useQrCode = qrCodeService.queryUsedQrCode(qrCode);
        
        String openid = map.get("FromUserName"); // 用户 openid
        String mpid = map.get("ToUserName"); // 公众号原始 ID
        NewsMessage newmsg = new NewsMessage();
        newmsg.setToUserName(openid);
        newmsg.setFromUserName(mpid);
        newmsg.setCreateTime(new Date().getTime());
        newmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
        Article article = new Article();
        article.setDescription(useQrCode.getDescription()); // 图文消息的描述
        article.setPicUrl(useQrCode.getPicUrl()); // 图文消息图片地址
        article.setTitle(useQrCode.getTitle()); // 图文消息标题
        article.setUrl(useQrCode.getUrl()); // 图文 url 链接
        List<Article> list = new ArrayList<Article>();
        list.add(article); // 这里发送的是单图文，如果需要发送多图文则在这里 list 中加入多个 Article 即可！
        newmsg.setArticleCount(list.size());
        newmsg.setArticles(list);
        return MessageUtil.newsMessageToXml(newmsg);
    }

}

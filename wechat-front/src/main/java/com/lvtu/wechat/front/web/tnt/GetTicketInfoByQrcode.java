package com.lvtu.wechat.front.web.tnt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.xml.security.utils.Base64;
import org.mortbay.log.Log;

import com.alibaba.fastjson.JSONObject;
import com.lvtu.wechat.common.model.weixin.message.reply.Article;
import com.lvtu.wechat.common.model.weixin.message.reply.NewsMessage;
import com.lvtu.wechat.common.utils.HttpsPayUtils;
import com.lvtu.wechat.common.utils.MessageUtil;

public class GetTicketInfoByQrcode {
    // userId=623$productId=91119
    private final static String tntGetProductUrl = "https://appf.lvmama.com/api/1.0/client/prod/pushToWeChat?appKey=LVMM_ANDROID&timestamp=#TIMESTAMP#&sceneStr=";

    private final static String preVisitUrl = "https://weixin.lvmama.com/wechat/preLogin?target=";

    /**
     * 二维码扫描获取的参数进行get请求从分销处获取图文信息
     * 
     * @param param
     * @param map
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getProductInfoFromTnt(String param, Map<String, String> map) {
        String openid = map.get("FromUserName"); // 用户 openid
        String mpid = map.get("ToUserName"); // 公众号原始 ID
        NewsMessage newmsg = new NewsMessage();
        newmsg.setToUserName(openid);
        newmsg.setFromUserName(mpid);
        newmsg.setCreateTime(new Date().getTime());
        newmsg.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
        Long timestamp = new Date().getTime();
        String response = HttpsPayUtils.requestGet(tntGetProductUrl.replace("#TIMESTAMP#", timestamp.toString()) + param);
        Log.info("tntresult="+response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (jsonObject == null) {
            return null;
        }
        if (StringUtils.isBlank(jsonObject.getString("productName"))
            || StringUtils.isBlank(jsonObject.getString("imgUrl"))
            || StringUtils.isBlank(jsonObject.getString("prodUrl"))) {
            return null;
        }
        String linkUrl = jsonObject.getString("prodUrl");
        Article article = new Article();
        if (StringUtils.isNotBlank(jsonObject.getString("productDesc"))) {
            article.setDescription(jsonObject.getString("productDesc")); // 图文消息的描述
        }
        else {
            article.setDescription(null);
        }
        article.setPicUrl(jsonObject.getString("imgUrl")); // 图文消息图片地址
        article.setTitle(jsonObject.getString("productName")); // 图文消息标题
        article.setUrl(preVisitUrl + Base64.encode(linkUrl.getBytes())); // 图文 url 链接
        List<Article> list = new ArrayList<Article>();
        list.add(article); // 这里发送的是单图文，如果需要发送多图文则在这里 list 中加入多个 Article 即可！
        newmsg.setArticleCount(list.size());
        newmsg.setArticles(list);
        return MessageUtil.newsMessageToXml(newmsg);
    }
}

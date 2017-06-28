package com.lvtu.wechat.common.utils;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.lvtu.wechat.common.model.weixin.message.reply.Article;
import com.lvtu.wechat.common.model.weixin.message.reply.ImageMessage;
import com.lvtu.wechat.common.model.weixin.message.reply.MusicMessage;
import com.lvtu.wechat.common.model.weixin.message.reply.NewsMessage;
import com.lvtu.wechat.common.model.weixin.message.reply.TextMessage;
import com.lvtu.wechat.common.model.weixin.message.reply.VideoMessage;
import com.lvtu.wechat.common.model.weixin.message.reply.VoiceMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * ClassName: MessageUtil
 * 
 * @Description: 消息工具类
 * @author dapengniao
 * @date 2016 年 3 月 7 日 上午 10:05:04
 */
public class MessageUtil {

	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * 用户已关注的事件推送
	 */
	public static final String EVENT_TYPE_SCAN = "SCAN";

	/**
	 * 地理位置上报事件
	 */
	public static final String EVENT_TYPE_LOCATION = "LOCATION";

	/**
	 * 自定义菜单 View 事件
	 */
	public static final String EVENT_TYPE_VIEW = "VIEW";

	/**
	 * 视频消息
	 */
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
	
	/**
	 * 模板消息回调
	 */
	public static final String EVENT_TYPE_TEMPLATE = "TEMPLATESENDJOBFINISH";

	/**
	 * @Description: 解析微信发来的请求（XML）
	 * @param @param request
	 * @param @return
	 * @param @throws Exception
	 * @author dapengniao
	 * @date 2016 年 3 月 7 日 上午 10:04:02
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(String reqXml)
			throws Exception {
		// 将解析结果存储在 HashMap 中
		Map<String, String> map = new HashMap<String, String>();
		Document document = DocumentHelper.parseText(reqXml);
		// 得到 xml 根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());
		return map;
	}

	/**
     * @Description: 文本消息对象转换成 xml
     * @param @param textMessage
     * @param @return
     * @author dapengniao
     * @date 2016 年 3 月 8 日 下午 4:13:22
     */
    public static String textMessageToXml(TextMessage textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

	/**
	 * @Description: 图文消息对象转换成 xml
	 * @param @param newsMessage
	 * @param @return
	 * @author dapengniao
	 * @date 2016 年 3 月 8 日 下午 4:14:09
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * @Description: 图片消息对象转换成 xml
	 * @param @param imageMessage
	 * @param @return
	 * @author dapengniao
	 * @date 2016 年 3 月 9 日 上午 9:25:51
	 */
	public static String imageMessageToXml(ImageMessage imageMessage) {
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}

	/**
	 * @Description: 语音消息对象转换成 xml
	 * @param @param voiceMessage
	 * @param @return
	 * @author dapengniao
	 * @date 2016 年 3 月 9 日 上午 9:27:26
	 */
	public static String voiceMessageToXml(VoiceMessage voiceMessage) {
		xstream.alias("xml", voiceMessage.getClass());
		return xstream.toXML(voiceMessage);
	}

	/**
	 * @Description: 视频消息对象转换成 xml
	 * @param @param videoMessage
	 * @param @return
	 * @author dapengniao
	 * @date 2016 年 3 月 9 日 上午 9:31:09
	 */
	public static String videoMessageToXml(VideoMessage videoMessage) {
		xstream.alias("xml", videoMessage.getClass());
		return xstream.toXML(videoMessage);
	}

	/**
	 * @Description: 音乐消息对象转换成 xml
	 * @param @param musicMessage
	 * @param @return
	 * @author dapengniao
	 * @date 2016 年 3 月 8 日 下午 4:13:36
	 */
	public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有 xml 节点的转换都增加 CDATA 标记
				boolean cdata = true;
				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					if ("CreateTime".equals(name) || "ArticleCount".equals(name)) {
						cdata = false;
					}
					else {
						cdata = true;
					}
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
package com.lvtu.wechat.common.utils;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;

/**
 * xml工具类
 * @author xuyao
 *
 */
public class XmlUtil {
	
	/**
	 * XML转对象
	 * @param <T>
	 * @param xml
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xmlToObject(String xml, Class<T> clazz) {
		if(StringUtils.isBlank(xml))
			return null;

		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (T)unmarshaller.unmarshal(new StringReader(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}
}

package com.lvtu.wechat.back.spring;

import org.springframework.core.convert.converter.Converter;

import com.lvtu.wechat.common.enums.ForwardType;


/**
 * 第三方平台转发类型枚举转换
 * @author xuyao
 *
 */
public class ForwardTypeConverter implements Converter<String, ForwardType> {

	@Override
	public ForwardType convert(String source) {
		String value = source.trim();
		if ("".equals(value)) {
			return null;
		}
		return ForwardType.get(Integer.parseInt(source));
	}
}
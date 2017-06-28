package com.lvtu.wechat.common.enums;

import com.lvtu.wechat.common.mybatis.ValuedEnum;

/**
 * 第三方消息转发类型枚举
 * 
 * @author xuyao
 * 
 */
public enum ForwardType implements ValuedEnum {
	NO(0, "不转发"),
	ALL(1, "全部转发"), //全部转发
	KEYWORD(2, "关键字转发"); //关键字转发

	private int value;
	private String showName;

	private ForwardType(int value, String showName) {
		this.value = value;
		this.showName = showName;
	}

	@Override
	public int getValue() {
		return value;
	}

	public String getShowName(){
		return showName;
	}

	/**
	 * 根据value获取
	 * @param value
	 * @return
	 */
	public static ForwardType get(int value) {
		for(ForwardType type : ForwardType.values()) {
			if(type.value == value)
				return type;
		}
		return null;
	}
}
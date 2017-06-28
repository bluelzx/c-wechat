package com.lvtu.wechat.front.utils;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 请求容器，存放哪些微信用户在做请求。 避免并发重复请求和恶意刷流量
 * 
 * @author xuyao
 * 
 */
public class FreqRequestHelper {

	/**
	 * 最低请求间隔，避免恶意刷流量
	 */
	public static final int MIN_REQ_INTERVAL_SECOND = 1;

	private volatile static FreqRequestHelper freqReqHelper = null;

	/**
	 * 记录请求的openid和请求时间毫秒
	 */
	private Map<String, Long> reqs = null;

	private FreqRequestHelper() {
		reqs = new WeakHashMap<String, Long>();
	}

	/**
	 * 双重检查获取单例对象
	 * @return
	 */
	public static FreqRequestHelper getInstance() {
		if (freqReqHelper == null) {
			synchronized (FreqRequestHelper.class) {
				if (freqReqHelper == null) {
					freqReqHelper = new FreqRequestHelper();
				}
			}
		}
		return freqReqHelper;
	}

	/**
	 * 判断该用户是否能做请求
	 * 
	 * @param openid
	 * @return
	 */
	public synchronized boolean isFreqRequest(String openid) {
		if (reqs.containsKey(openid)) {
			Long preReqTime = reqs.get(openid);
			// 更新最后一次访问时间
			reqs.put(openid, System.currentTimeMillis());
			// 判断是否频繁访问
			Long nowTimeMill = System.currentTimeMillis();
			Long reqIntervalSecond = (nowTimeMill - preReqTime) / 300;
			if (reqIntervalSecond > MIN_REQ_INTERVAL_SECOND) {
				return false;
			} else {
				return true;
			}
		} else {
			reqs.put(openid, System.currentTimeMillis());
			return false;
		}
	}
}

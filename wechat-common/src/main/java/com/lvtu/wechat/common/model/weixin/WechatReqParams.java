package com.lvtu.wechat.common.model.weixin;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 微信请求通信参数
 * 
 * @author xuyao
 * 
 */
public class WechatReqParams {
	/**
	 * 签名
	 */
	@NotBlank(message="签名为空")
	private String signature;
	/**
	 * 时间戳
	 */
	@NotBlank(message="时间戳为空")
	private String timestamp;
	/**
	 * 随机数
	 */
	@NotBlank(message="随机数为空")
	private String nonce;
	/**
	 * 随机字符
	 */
	private String echostr;
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}
}

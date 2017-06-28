package com.lvtu.wechat.common.model.template;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class TemplateRecords extends BaseModel{

	/** 
	* @Fields serialVersionUID : TODO 
	*/ 
	private static final long serialVersionUID = -2305847495171372439L;
	
	private String openid;
	
	private String templateId;
	
	private String content;
	
	private int success;
	
	private String msgId;
	
	private Date createTime;
	
	private Integer retryCount;
	
	private Integer nowRetryCount;
	
	private String sender;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public Integer getNowRetryCount() {
		return nowRetryCount;
	}

	public void setNowRetryCount(Integer nowRetryCount) {
		this.nowRetryCount = nowRetryCount;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	
	
	

}

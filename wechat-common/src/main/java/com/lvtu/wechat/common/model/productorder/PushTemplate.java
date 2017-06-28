package com.lvtu.wechat.common.model.productorder;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

/** 
* @ClassName: PushTemplate 
* @Description: 推送消息模板信息
* @author zhengchongxiang
* @date 2016-8-29 上午11:02:55  
*/
public class PushTemplate extends BaseModel{

	/** 
	*  
	*/ 
	private static final long serialVersionUID = 5772567459264333816L;

	/** 
	* 消息标题 
	*/ 
	private String title;
	
	/** 
	* 类型(1，出境游 2，国内游 3，特价机票) 
	*/ 
	private String type;
	
	/** 
	* 消息来源 
	*/ 
	private String source;
	
	/** 
	* 消息内容 
	*/ 
	private String content;
	
	/** 
	* 消息url 
	*/ 
	private String url;
	
	/** 
	* 发送时间
	*/ 
	private Date SendTime;
	
	/** 
	* 创建时间
	*/ 
	private Date createTime;
	
	/** 
	* 是否可用
	*/ 
	private String useable;
	
	/** 
	* 地区分组ID 
	*/ 
	private String areasId;
	
	/** 
	* 备注
	*/ 
	private String back;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getSendTime() {
		return SendTime;
	}

	public void setSendTime(Date sendTime) {
		SendTime = sendTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public String getAreasId() {
		return areasId;
	}

	public void setAreasId(String areasId) {
		this.areasId = areasId;
	}

	
}

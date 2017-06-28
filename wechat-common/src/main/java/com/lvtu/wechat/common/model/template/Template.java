package com.lvtu.wechat.common.model.template;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class Template extends BaseModel{

	/** 
	* @Fields serialVersionUID : TODO 
	*/ 
	private static final long serialVersionUID = -7821215048289113934L;
	
	private String templateId;
	
	private String name;
	
	private String fields;
	
	private Integer retryCount;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String back;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
	
	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}
	
	
	

}

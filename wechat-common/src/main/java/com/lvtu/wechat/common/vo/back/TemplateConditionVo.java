package com.lvtu.wechat.common.vo.back;

import com.lvtu.wechat.common.base.BaseCondition;

public class TemplateConditionVo extends BaseCondition{

	/** 
	* @Fields serialVersionUID : TODO 
	*/ 
	private static final long serialVersionUID = -7795704687115854444L;
	
	private String templateId;
	
	private String openid;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}


	
	
	

}

package com.lvtu.wechat.common.model.weixin.template;

import java.util.HashMap;
import java.util.Map;

public class WxTemplate {

    /**
     * 模板消息id
     */
    private String template_id;

    /**
     * 用户openId
     */
    private String touser;
    
    /**
     * 标题颜色
     */
    private String topcolor;

    /**
     * URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）
     */
    private String url;
    
    /** 
    * @Fields form_id : 小程序模板消息来源ID 
    */ 
    private String form_id;
    
    
    /** 
    * @Fields page : 小程序跳转路径
    */ 
    private String page;
    

    /**
     * 详细内容
     */
    private Map<String, TemplateData> data = new HashMap<String, TemplateData>();


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

	public String getForm_id() {
		return form_id;
	}

	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

}

package com.lvtu.wechat.common.vo.back;

import com.lvtu.wechat.common.base.BaseCondition;

public class ShareTemplateVo extends BaseCondition {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 分享模版名称
     */
    private String name;
    
    /**
     * 分享模版ID
     */
    private String templateId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    
}

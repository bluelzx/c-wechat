package com.lvtu.wechat.common.model.weixin.template;

public class TemplateData {
    /**
     * 内容
     */
    private String value;

    /**
     * 颜色
     */
    private String color;
    
    
    public TemplateData() {
        this.color = "#173177";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
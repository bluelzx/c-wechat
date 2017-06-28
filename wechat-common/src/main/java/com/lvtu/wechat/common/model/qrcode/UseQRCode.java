package com.lvtu.wechat.common.model.qrcode;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 已使用的二维码
 * @author wxlizhi 
 *
 */
public class UseQRCode extends BaseModel{

    /**
     * 
     */
    private static final long serialVersionUID = -2188990476882911376L;

    /**
     * 微信二维码表id
     */
    private String wxQRCodeId;
    
    /**
     * 二维码推送标题
     */
    private String title;
    
    /**
     * 链接
     */
    private String url;
    
    /**
     * 推送图文描述
     */
    private String description;
    
    /**
     * 推送纯文字内容
     */
    private String content;
    
    /**
     * 图片链接
     */
    private String picUrl;
    
    /**
     * 状态：1-使用，2-废弃
     */
    private String state;
    
    /**
     * 活动id
     */
    private String actId;
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getWxQRCodeId() {
        return wxQRCodeId;
    }

    public void setWxQRCodeId(String wxQRCodeId) {
        this.wxQRCodeId = wxQRCodeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }
    
    
}

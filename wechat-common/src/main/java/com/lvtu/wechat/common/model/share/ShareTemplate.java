package com.lvtu.wechat.common.model.share;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 分享模版主表
 * @author qianqc
 *
 */
public class ShareTemplate extends BaseModel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 分享模版名称
     */
    private String name;
    
    /**
     * 模版ID
     */
    private String templateId;
    
    /**
     * 是否微信分享 0-否，1-是
     */
    private String weixin;
    
    /**
     * 是否支持qq分享 0-否，1-是
     */
    private String qq;
    
    /**
     * 是否支持微博文学 0-否，1-是
     */
    private String weibo;
    
    /**
     * 状态 0-关闭，1-开启
     */
    private String state;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 微信分享内容
     */
    private ShareContent wxShareContent;

    /**
     * qq分享内容
     */
    private ShareContent qqShareContent;
    
    /**
     * 微博分享内容
     */
    private ShareContent wbShareContent;
    
    /**
     * 备注
     */
    private String remark;


    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShareContent getWxShareContent() {
        return wxShareContent;
    }

    public void setWxShareContent(ShareContent wxShareContent) {
        this.wxShareContent = wxShareContent;
    }

    public ShareContent getQqShareContent() {
        return qqShareContent;
    }

    public void setQqShareContent(ShareContent qqShareContent) {
        this.qqShareContent = qqShareContent;
    }

    public ShareContent getWbShareContent() {
        return wbShareContent;
    }

    public void setWbShareContent(ShareContent wbShareContent) {
        this.wbShareContent = wbShareContent;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


}

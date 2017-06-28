package com.lvtu.wechat.common.model.share;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 分享通用模版图片表
 * @author qianqc
 *
 */
public class ShareImage extends BaseModel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 分享模版表ID
     */
    private String shareId;
    
    /**
     * 渠道，1-微信，2-QQ，3-微博
     */
    private String channel;
    
    /**
     * 图片链接URL
     */
    private String url;

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
    
}

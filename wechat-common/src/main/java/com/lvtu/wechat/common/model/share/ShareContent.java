package com.lvtu.wechat.common.model.share;

import java.util.ArrayList;
import java.util.List;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 分享模版相关内容表，针对QQ，微信，微博
 * @author qianqc
 *
 */
public class ShareContent extends BaseModel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 分享模版表ID
     */
    private String shareId;
    
    /**
     * 分享渠道  1-微信，2-QQ，3-微博
     */
    private String channel;
    
    /**
     * 分享标题-QQ，微信  
     */
    private String title;
    
    /**
     * 分享描述-QQ，微信      分享内容-微博
     */
    private String shareDesc;
    
    /**
     * 分享链接URL
     */
    private String url;
    
    /**
     * 提示文案  -微博
     */
    private String tips;
    
    /**
     * 分享图片集合
     */
    public List<ShareImage> imageList = new ArrayList<ShareImage>();
    
    /**
     * 备注
     */
    private String reamrk;

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReamrk() {
        return reamrk;
    }

    public void setReamrk(String reamrk) {
        this.reamrk = reamrk;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }
    
}

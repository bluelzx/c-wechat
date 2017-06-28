package com.lvtu.wechat.common.model.qrcode;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 二维码
 * @author wxlizhi
 *
 */
public class QRCode extends BaseModel{
    /**
     * 
     */
    private static final long serialVersionUID = 5425427266759765409L;

    /**
     * 二维码生成参数
     */
    private String param;
    
    /**
     * 二维码的链接Url
     */
    private String url;
    
    /**
     * 二维码使用状态：0-可用，1-正在使用
     */
    private String state;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    
}

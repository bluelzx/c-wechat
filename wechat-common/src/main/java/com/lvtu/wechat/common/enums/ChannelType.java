package com.lvtu.wechat.common.enums;

/**
 * 用户关注渠道  对应wx_user表的channel字段
* @ClassName: ChannelType 
* @Description: TODO
* @author qianqc
* @date 2016年8月26日 下午2:10:39
 */
public enum ChannelType {
    TIAN_NIU("B2B_QRCODE","天牛");
    
    private String value;
    private String showName;
    
    private ChannelType(String value, String showName) {
        this.value = value;
        this.showName = showName;
    }
    
    public String getShowName() {
        return showName;
    }
    
    public String getValue() {
        return value;
    }
    
}

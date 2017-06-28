package com.lvtu.wechat.common.enums;

/**
 * enum通用类，主要针对数据库里的状态(0,1..)
 * @author qianqc
 *
 */
public enum CommonType {
    
    /**
     * 新，老   新用户，老用户...
     */
    NEW(0, "NEW"),
    OLD(1, "OLD"),
    
    /**
     * 使用，未使用
     */
    USED(1, "USED"),
    UNUSED(0, "UNUSED"),
    
    /**
     * 是否已经加入缓存
     */
    CACHE(1, "CACHE"),
    NOCACHE(0, "NOCACHE"),
    
    /**
     * 是否可用
     */
    ENABLE(1, "ENABLE"),
    DISABLE(0, "DISABLE"),
    
    /**
     * 分享渠道  QQ，微信，微博
     */
    SHARE_CHANNEL_WX(1, "微信"),
    SHARE_CHANNEL_QQ(2, "QQ"),
    SHARE_CHANNEL_WB(3, "微博"),
    
    
    /**
     * 用户是否关注微信号 1-关注，0-未关注
     */
    FOLLOW(1, "关注"),
    UN_FOLLOW(2, "未关注"),
    
    /**
     * 是否分享
     */
    SHARE(1,"分享"),
    NO_SHARE(0,"不分享"),
    
    /**
     * 永久二维码，和临时二维码
     */
    QR_LIMIT_SCENE(2, "永久二维码"),
    QR_LIMIT_STR_SCENE(1, "永久字符串二维码"),
    QR_SCENE(0, "临时二维码"),
    
    
    /**
     * 用户订阅和取消订阅
     */
    SUBSCRIBE(1, "订阅"),
    UNSUBSCRIBE(0, "取消订阅"),
    
    /**
     * 发送状态
     */
    NO_SEND(0, "没发送"),
    SENDING(1, "发送中"),
    SEND_SUCCESS(2, "发送成功"),
    SEND_FAILED(3, "发送失败");
    
    private Integer value;
    private String showName;
    private CommonType(Integer value, String showName) {
        this.value = value;
        this.showName = showName;
    }
    public String getShowName() {
        return showName;
    }
    public Integer getValue() {
        return value;
    }
    
    public String getStringValue() {
        return String.valueOf(value);
    }

}

package com.lvtu.wechat.common.enums;

/**
 * @author wxlizhi
 * 活动的公众号平台类型 
 */
public enum PublicPlatformType {
    
    SUBSCRIBE(1,"订阅号"),
    SERVICE(2,"服务号");
    
    private Integer value;
    private String showName;
    
    private PublicPlatformType(Integer value, String showName){
        this.value=value;
        this.showName=showName;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }
    
}

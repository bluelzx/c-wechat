package com.lvtu.wechat.common.enums;

/**
 * 活动人数
 * @author wxlizhi
 *
 */
public enum TeamNumberType {
    ONE(1,"1"),
    TWO(2,"2"),
    THREE(3,"3"),
    FOUR(4,"4"),
    FIVE(5,"5"),
    SIX(6,"6"),
    SEVEN(7,"7"),
    EIGHT(8,"8"),
    NINE(9,"9"),
    TEN(10,"10");
    
    private Integer value;
    private String showName;
    
    private TeamNumberType(Integer value, String showName) {
        this.value = value;
        this.showName = showName;
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

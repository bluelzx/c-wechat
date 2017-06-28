package com.lvtu.wechat.common.enums;

/**
 * 是否显示
 * @author wxlizhi
 *
 */
public enum IsShowType {
    SHOW(1,"显示",false),
    NOTSHOW(0,"不显示",false);
    
    private Integer value;
    private String showName;
    private boolean checked;
    
    private IsShowType(Integer value, String showName, boolean checked) {
        this.value = value;
        this.showName = showName;
        this.checked = checked;
    }
    
    public String getShowName() {
        return showName;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public String getStringValue() {
        return value.toString();
    }
    
    public boolean getChecked() {
        return checked;
    }
    
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}

package com.lvtu.wechat.common.model.activity.group;

public class UserInfo {
    private Integer value;
    
    private String showName;
    
    private boolean checked;

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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public UserInfo(Integer value, String showName, boolean checked) {
        super();
        this.value = value;
        this.showName = showName;
        this.checked = checked;
    }
    
    
}

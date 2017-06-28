package com.lvtu.wechat.common.enums;

public enum StatusType {
    ALL(-1 ,"全部"),
    OPEN(1 ,"开启"),
    CLOSE(0 ,"关闭");
    
    private Integer value;
    private String showName;

    private StatusType(Integer value, String showName) {
        this.value = value;
        this.showName = showName;
    }
    public String getShowName() {
        return showName;
    }
    public String getValue() {
        return value.toString();
    }
    
    
    /**
     * 获取开启和关闭状态
     * @return
     */
    public static StatusType[] getStatusType() {
        StatusType[] statusTypeArr = new StatusType[2];
        statusTypeArr[0] = StatusType.OPEN;
        statusTypeArr[1] = StatusType.CLOSE;
        return statusTypeArr;
    }
}

package com.lvtu.wechat.common.enums;

/**
 * 团组员的排序类型
 * @author wxlizhi
 *
 */
public enum GroupSortType {
    NULL(-1 ,"排序"),
    COMPLETETIME(1 ,"成团时间"),
    STARTTIME(0 ,"开团时间");
    
    private Integer value;
    private String showName;

    private GroupSortType(Integer value, String showName) {
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
    public static GroupSortType[] getStatusType() {
        GroupSortType[] statusTypeArr = new GroupSortType[2];
        statusTypeArr[0] = GroupSortType.COMPLETETIME;
        statusTypeArr[1] = GroupSortType.STARTTIME;
        return statusTypeArr;
    }
}

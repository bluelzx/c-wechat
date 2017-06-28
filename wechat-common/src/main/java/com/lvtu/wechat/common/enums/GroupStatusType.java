package com.lvtu.wechat.common.enums;

/**
 * 团状态
 * @author wxlizhi
 *
 */
public enum GroupStatusType {
    ALL(-1 ,"全部"),
    OPEN(1 ,"已成团"),
    CLOSE(0 ,"开团中");
    
    private Integer value;
    private String showName;

    private GroupStatusType(Integer value, String showName) {
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
    public static GroupStatusType[] getStatusType() {
        GroupStatusType[] statusTypeArr = new GroupStatusType[2];
        statusTypeArr[0] = GroupStatusType.OPEN;
        statusTypeArr[1] = GroupStatusType.CLOSE;
        return statusTypeArr;
    }
    
    
    public static String getGroupStatusShowName(String value){
        String showName="";
        if(value!=null){
            GroupStatusType[] groupStatusTypes = GroupStatusType.values();
            for(GroupStatusType groupStatusType : groupStatusTypes){
                if(groupStatusType.getValue().equals(value)){
                    showName=groupStatusType.getShowName();
                    break;
                }
            } 
        }
        return showName;
    }
}

package com.lvtu.wechat.common.enums;

/**
 * 用户身份
 * @author wxlizhi
 *
 */
public enum GroupIsLeaderType {
    LEADER(1 ,"开团者"),
    NOTLEADER(0 ,"参团者");
    
    private Integer value;
    private String showName;

    private GroupIsLeaderType(Integer value, String showName) {
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
    public static GroupIsLeaderType[] getStatusType() {
        GroupIsLeaderType[] statusTypeArr = new GroupIsLeaderType[2];
        statusTypeArr[0] = GroupIsLeaderType.LEADER;
        statusTypeArr[1] = GroupIsLeaderType.NOTLEADER;
        return statusTypeArr;
    }
    
    public static String getGroupIsLeaderShowName(String value){
        String showName="";
        if(value!=null){
            GroupIsLeaderType[] groupIsLeaderTypes = GroupIsLeaderType.values();
            for(GroupIsLeaderType groupIsLeaderType : groupIsLeaderTypes){
                if(groupIsLeaderType.getValue().equals(value)){
                    showName=groupIsLeaderType.getShowName();
                    break;
                }
            } 
        }
        return showName;
    }
    
    public String getStringValue() {
        return value.toString();
    }
    
}

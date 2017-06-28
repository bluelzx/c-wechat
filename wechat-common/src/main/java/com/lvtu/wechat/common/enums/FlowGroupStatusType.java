package com.lvtu.wechat.common.enums;


/** 
* @ClassName: FlowGroupStatusType 
* @Description: 裂变流量状态
* @author zhengchongxiang
* @date 2016-11-4 下午3:10:21  
*/
public enum FlowGroupStatusType {
    ING(0 ,"进行中"),
    DONE(1 ,"已完成");
    
    private Integer value;
    private String showName;

    private FlowGroupStatusType(Integer value, String showName) {
        this.value = value;
        this.showName = showName;
    }
    public String getShowName() {
        return showName;
    }
    public Integer getValue() {
        return value;
    }
}

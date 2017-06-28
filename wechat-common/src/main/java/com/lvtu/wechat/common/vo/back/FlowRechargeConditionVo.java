package com.lvtu.wechat.common.vo.back;

import com.lvtu.wechat.common.base.BaseCondition;

public class FlowRechargeConditionVo extends BaseCondition{
	
	 /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5547232995565616561L;

    /**
     * id
     */
    private String id;
    
    /**
     * name
     */
    private String name;
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

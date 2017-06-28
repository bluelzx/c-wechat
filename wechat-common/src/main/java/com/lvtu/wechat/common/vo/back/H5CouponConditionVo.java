package com.lvtu.wechat.common.vo.back;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseCondition;

public class H5CouponConditionVo extends BaseCondition {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5547232997775616561L;

    /**
     * id
     */
    private String id;
    
    /**
     * name
     */
    private String name;
    
    /**
     * startDate
     */
    private Date startDate;
    
    /**
     * endDate
     */
    private Date endDate;
    
    /**
     * state
     */
    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}

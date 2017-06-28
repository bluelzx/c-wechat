package com.lvtu.wechat.common.vo.back;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseCondition;

public class CouponActConditionVo extends BaseCondition {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3485841739299714915L;

    /**
     * 活动名称
     */
    private String name;
    
    /**
     * 邀请码
     */
    private String inviteCode;
    
    /**
     * 开始时间
     */
    private Date beginDate;
    
    /**
     * 结束时间
     */
    private Date endDate;
    
    /**
     * 状态
     */
    private String status;
    
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
}

package com.lvtu.wechat.common.model.activity.group;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class Group extends BaseModel{

    /**
     * 
     */
    private static final long serialVersionUID = 2969268655903586320L;
    
    /**
     * 活动ID
     */
    private String groupActivityId;
    
    /**
     * 参与人数
     */
    private Integer participantsNum;
    
    /**
     * 总人数
     */
    private Integer totalNum;
    
    /**
     * 开团时间
     */
    private Date startTime;
    
    /**
     * 成团时间
     */
    private Date completeTime;
    
    /**
     * 是否已经发送推送消息
     */
    private String isSend;
    
    /**
     * 删除时间
     */
    private Date deleteDate;
    
    /**
     * 扩展字段
     */
    private String back;

    public String getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(String groupActivityId) {
        this.groupActivityId = groupActivityId;
    }

    public Integer getParticipantsNum() {
        return participantsNum;
    }

    public void setParticipantsNum(Integer participantsNum) {
        this.participantsNum = participantsNum;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

    
}

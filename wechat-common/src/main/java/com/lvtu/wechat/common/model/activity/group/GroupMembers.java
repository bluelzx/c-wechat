package com.lvtu.wechat.common.model.activity.group;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class GroupMembers extends BaseModel{

    /**
     * 
     */
    private static final long serialVersionUID = 3863736341256120686L;
    
    /**
     * 组团表ID
     */
    private String groupId;
    
    /**
     * wx_user_id
     */
    private String wxUserId;
    
    /**
     * 是否为团长0-否，1-是
     */
    private String isLeader;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 手机号
     */
    private String telephone;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 年龄
     */
    private Integer age;
    
    /**
     * 星座
     */
    private String constellation;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 微信名
     */
    private String nickname;
    
    /**
     * 团状态
     */
    private String state;
    
    /**
     * 目前参团人数
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
     * 活动ID
     */
    private String groupActivityId;
    
    /**
     * 微信openid
     */
    private String openid;
    
    /**
     * 是否已经发送推送消息
     */
    private String isSend;
    
    /**
     * 头像
     */
    private String headImgUrl;
    
    /**
     * 删除时间
     */
    private Date deleteDate;
    
    /**
     * 扩展字段
     */
    private String back;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    public String getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(String isLeader) {
        this.isLeader = isLeader;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
    
    public String getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(String groupActivityId) {
        this.groupActivityId = groupActivityId;
    }
    
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
    
    
    

}

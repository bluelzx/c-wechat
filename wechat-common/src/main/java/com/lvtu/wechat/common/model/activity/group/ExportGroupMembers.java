package com.lvtu.wechat.common.model.activity.group;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;
import com.lvtu.wechat.common.model.excel.ExcelCell;

public class ExportGroupMembers extends BaseModel{

     
    private static final long serialVersionUID = 1L;

    /**
     * 微信名
     */
    @ExcelCell(index = 0)
    private String nickname;
    
    /**
     * 姓名
     */
    @ExcelCell(index = 1)
    private String name;
    
    /**
     * 手机号
     */
    @ExcelCell(index = 2)
    private String telephone;
    
    /**
     * 年龄
     */
    @ExcelCell(index = 3)
    private Integer age;
    
    /**
     * 星座
     */
    @ExcelCell(index = 4)
    private String constellation;
    
    /**
     * 省份
     */
    @ExcelCell(index = 5)
    private String province;
    
    /**
     * 城市
     */
    @ExcelCell(index = 6)
    private String city;
    
    /**
     * 是否为团长0-否，1-是
     */
    @ExcelCell(index = 7)
    private String isLeader;
    
    /**
     * 组团表ID
     */
    @ExcelCell(index = 8)
    private String groupId;
    
    /**
     * 团状态
     */
    @ExcelCell(index = 9)
    private String state;
    
    /**
     * 目前参团人数
     */
    @ExcelCell(index = 10)
    private Integer participantsNum;
    
    /**
     * 开团时间
     */
    @ExcelCell(index = 11)
    private Date startTime;
    
    /**
     * 成团时间
     */
    @ExcelCell(index = 12)
    private Date completeTime;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(String isLeader) {
        this.isLeader = isLeader;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
    
}

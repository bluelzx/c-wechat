package com.lvtu.wechat.common.vo.back;

import com.lvtu.wechat.common.base.BaseCondition;

public class GroupMembersConditionVo extends BaseCondition{

    /**
     * 
     */
    private static final long serialVersionUID = -8303673136263299477L;
    
    /**
     * 活动ID
     */
    private String groupActId;
    
    /**
     * 用户微信名
     */
    private String nickname;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 手机号
     */
    private String telephone;
    
    /**
     * 星座
     */
    private String constellation;
    
    /**
     * 团ID
     */
    private String groupId;
    
    /**
     * 团状态
     */
    private String groupState;
    
    /**
     * 排序
     */
    private String sort;

    public String getGroupActId() {
        return groupActId;
    }

    public void setGroupActId(String groupActId) {
        this.groupActId = groupActId;
    }
    
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupState() {
        return groupState;
    }

    public void setGroupState(String groupState) {
        this.groupState = groupState;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    
}

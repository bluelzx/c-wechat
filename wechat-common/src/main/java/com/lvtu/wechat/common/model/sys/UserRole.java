package com.lvtu.wechat.common.model.sys;

import com.lvtu.wechat.common.base.BaseModel;

public class UserRole extends BaseModel {
    
    /** 
    * @Fields serialVersionUID : TODO 
    */ 
    private static final long serialVersionUID = 1L;

    private String UserId;
    
    private String RoleId;
    
    private String delFlag;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

}

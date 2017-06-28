package com.lvtu.wechat.common.model.activity.discountnew;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class DiscountActivityNew extends BaseModel{
	
	/**
     * 
     */
    private static final long serialVersionUID = -3325941454792895444L;
    
    /**
     * 活动名称
     */
    private String name;
    
    /**
     * banner图片url
     * 
     */
    private String banner;
    
    /**
     * 
     * 周期
     */
    private String period;
    
    /**
     * 
     * 
     */
    private String isable;

	/**
     * 创建时间
     */
    private Date updateTime;
    
    /**
     * 扩展字段
     * 
     */
    private String back;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public String getIsable() {
		return isable;
	}

	public void setIsable(String isable) {
		this.isable = isable;
	}
	
	
}

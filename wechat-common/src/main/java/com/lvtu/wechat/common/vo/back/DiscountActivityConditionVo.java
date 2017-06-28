package com.lvtu.wechat.common.vo.back;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseCondition;

public class DiscountActivityConditionVo extends BaseCondition {
	
	 /**
     * 
     */
    private static final long serialVersionUID = 935523320209148998L;
    
    /**
     * 活动ID
     */
    private String id;

    /**
     * 活动名称
     */
    private String name;
    
    /**
     * 
     * 活动banner
     */
    private String banner;
    

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }


}

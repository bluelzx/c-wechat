package com.lvtu.wechat.common.vo.back;

import java.util.Date;
import com.lvtu.wechat.common.base.BaseCondition;

/**
 * @author zhengchongxiang
 */
public class AdvertisingClicksConditionVo extends BaseCondition {


    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3059329553938806148L;
	
	/**
	 * 广告位Id
	 */
	private String advertisingId;
	
	/**
	 * 开始时间
	 */
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 点击时间
	 */
	private Date clickDate;

	public String getAdvertisingId() {
		return advertisingId;
	}

	public void setAdvertisingId(String advertisingId) {
		this.advertisingId = advertisingId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getClickDate() {
		return clickDate;
	}

	public void setClickDate(Date clickDate) {
		this.clickDate = clickDate;
	}
    
}

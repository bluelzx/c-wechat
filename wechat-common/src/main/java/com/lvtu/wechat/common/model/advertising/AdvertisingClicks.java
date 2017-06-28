package com.lvtu.wechat.common.model.advertising;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * @author zhengchongxiang
 *
 */
public class AdvertisingClicks extends BaseModel{


	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8101174537737981782L;

	/**
	 * 广告编号
	 */
	private String advertisingId;
	
	/**
	 * pv
	 */
	private Integer pageViews;
	
	/**
	 * uv
	 */
	private Integer uniqueVisitors;
	
	/**
	 * 点击日期
	 */
	private Date clickDate;
	
	/**
	 * 备用
	 */
	private String back;


	public String getAdvertisingId() {
		return advertisingId;
	}

	public void setAdvertisingId(String advertisingId) {
		this.advertisingId = advertisingId;
	}

	public Integer getPageViews() {
		return pageViews;
	}

	public void setPageViews(Integer pageViews) {
		this.pageViews = pageViews;
	}

	public Integer getUniqueVisitors() {
		return uniqueVisitors;
	}

	public void setUniqueVisitors(Integer uniqueVisitors) {
		this.uniqueVisitors = uniqueVisitors;
	}

	public Date getClickDate() {
		return clickDate;
	}

	public void setClickDate(Date clickDate) {
		this.clickDate = clickDate;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}
	
	
}

package com.lvtu.wechat.common.vo.back;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseCondition;

/**
 * @ClassName: PushTemplateCondition
 * @Description: 甩尾消息推送模板条件vo
 * @author zhengchongxiang
 * @date 2016-8-26 下午1:54:33
 */
public class PushTemplateConditionVo extends BaseCondition {

 
	private static final long serialVersionUID = 4817690469510289004L;
	
	
	/** 
	* 活动类型
	*/ 
	private String type;
	
	/** 
	* 开始时间
	*/ 
	private Date startTime;
	
	/** 
	* 结束时间 
	*/ 
	private Date endTime;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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



}

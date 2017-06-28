package com.lvtu.wechat.common.model.productorder;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

/** 
* @ClassName: PushRecords 
* @Description: 推送记录
* @author zhengchongxiang
* @date 2016-8-29 上午11:02:55  
*/
public class PushRecords extends BaseModel{

	/** 
	* 
	*/ 
	private static final long serialVersionUID = 8635623816848678580L;

	/** 
	* 消息模板ID
	*/ 
	private String activityId;
	
	/** 
	* openid 
	*/ 
	private String openid;
	
	/** 
	*  成功标示
	*/ 
	private Integer success;
	
	/** 
	* 消息ID（微信返回）
	*/ 
	private String msgId;
	
	/** 
	* 状态
	*/ 
	private String status;

	/** 
	* 创建时间
	*/ 
	private Date createTime;
	
	private String title;
	
	private Integer numCount;
	
	private Integer numSuccess;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNumCount() {
		return numCount;
	}

	public void setNumCount(Integer numCount) {
		this.numCount = numCount;
	}

	public Integer getNumSuccess() {
		return numSuccess;
	}

	public void setNumSuccess(Integer numSuccess) {
		this.numSuccess = numSuccess;
	}


}

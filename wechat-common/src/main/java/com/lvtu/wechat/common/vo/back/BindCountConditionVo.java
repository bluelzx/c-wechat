package com.lvtu.wechat.common.vo.back;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseCondition;

public class BindCountConditionVo extends BaseCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3941282678036083678L;
	
	 /**
     * id
     */
    private String id;
    
    
	/**
	 * 用户的openid
	 */
	private String openid;
	
	
	/**
     * 开始时间
     */
    private Date createDate;
    
    
    /**
     * 结束时间
     */
    private Date endDate;
    

    /**
     *  关注渠道
     */
    private String channel;
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}

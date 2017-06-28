package com.lvtu.wechat.common.model.weixin;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;
import com.lvtu.wechat.common.model.excel.ExcelCell;

public class ExportBindCount extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4281583977501302056L;
	
	
	/**
	 * 用户的openid
	 */
	@ExcelCell(index=0)
	private String openid;
	
	
	/**
     * 开始时间
     */
	@ExcelCell(index=1)
    private Date createDate;
    

	/**
     *  关注渠道
     */
	@ExcelCell(index=2)
    private String channel;
    
       
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


	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}

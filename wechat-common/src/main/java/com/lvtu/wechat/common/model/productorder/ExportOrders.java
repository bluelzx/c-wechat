package com.lvtu.wechat.common.model.productorder;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;
import com.lvtu.wechat.common.model.excel.ExcelCell;


/** 
* @ClassName: ExportOrders 
* @Description: 甩尾产品订阅导出
* @author zhengchongxiang
* @date 2016-9-2 下午5:30:28  
*/
public class ExportOrders extends BaseModel {

	private static final long serialVersionUID = 8571032393821223414L;
	
	@ExcelCell(index=0)
	private String nickName;
	
	@ExcelCell(index=1)
	private String openid;
	
	@ExcelCell(index=2)
	private String outboundTourism;
	
	@ExcelCell(index=3)
	private String inboundTourism;
	
	@ExcelCell(index=4)
	private String specialTicket;
	
	@ExcelCell(index=5)
	private String homeTourism;
	
	@ExcelCell(index=6)
	private String hotTicket;
	
	@ExcelCell(index=7)
	private Date updateTime;

	public String getHomeTourism() {
		return homeTourism;
	}

	public void setHomeTourism(String homeTourism) {
		this.homeTourism = homeTourism;
	}

	public String getHotTicket() {
		return hotTicket;
	}

	public void setHotTicket(String hotTicket) {
		this.hotTicket = hotTicket;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOutboundTourism() {
		return outboundTourism;
	}

	public void setOutboundTourism(String outboundTourism) {
		this.outboundTourism = outboundTourism;
	}

	public String getInboundTourism() {
		return inboundTourism;
	}

	public void setInboundTourism(String inboundTourism) {
		this.inboundTourism = inboundTourism;
	}

	public String getSpecialTicket() {
		return specialTicket;
	}

	public void setSpecialTicket(String specialTicket) {
		this.specialTicket = specialTicket;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}

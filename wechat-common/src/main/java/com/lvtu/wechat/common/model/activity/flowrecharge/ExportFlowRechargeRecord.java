package com.lvtu.wechat.common.model.activity.flowrecharge;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;
import com.lvtu.wechat.common.model.excel.ExcelCell;

public class ExportFlowRechargeRecord extends BaseModel {
	
    
    /** 
	* @Fields serialVersionUID : TODO 
	*/ 
	private static final long serialVersionUID = 1L;


	/**
     * 充值活动名称
     */
    @ExcelCell(index = 0)
    private String name;
    
    
    /**
	 * 用户昵称
	 */
    @ExcelCell(index = 1)
	private String nickname;
    
    
    /**
     * 用户的openid
     */
    @ExcelCell(index = 2)
    private String openid;
    
    
    /**
     * 充值流量
     */
    @ExcelCell(index = 3)
    private Integer rechargeFlow;
    
    /**
     * 操作人
     */
    @ExcelCell(index = 4)
    private String operator;
    
    
    /**
     * 备注
     */
    @ExcelCell(index = 5)
    private String remark;
    
    /**
     * 创建时间
     */
    @ExcelCell(index = 6)
    private Date createDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getRechargeFlow() {
		return rechargeFlow;
	}

	public void setRechargeFlow(Integer rechargeFlow) {
		this.rechargeFlow = rechargeFlow;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
    
}

package com.lvtu.wechat.common.base;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Length;


/**
 * 基础实体类
 * @author xuyao
 */
public abstract class BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 删除标记（0：正常；1：删除；2：审核；）
	 */
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";

	/**
	 * 实体编号（唯一标识）
	 */
	protected String id;
	
	/**
	 * 备注
	 */
	@Length(min=0, max=255)
	protected String remarks;
	
	/**
	 * 删除标记
	 */
	@Length(min=1, max=1)
	protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	
	public BaseModel() {
		this.delFlag = DEL_FLAG_NORMAL;
	}

	public BaseModel(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	/**
	 * 插入之前执行方法，需要手动调用
	 */
	public void preInsert(){
		if (StringUtils.isBlank(this.id)){
			setId(UUID.randomUUID().toString().replaceAll("-", ""));
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		BaseModel that = (BaseModel) obj;
		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}

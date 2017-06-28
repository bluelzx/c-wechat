package com.lvtu.wechat.dao.activity.signflow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.signflow.FlowPartner;


/**
 * @author zhengchongxiang
 *
 */
@Repository
public class FlowPartnerDAO extends BaseIbatisDAO {

	public FlowPartnerDAO() {
		super("T_WX_FLOW_PARTNER");
	}
	/**
	 * 查询所有流量合作商信息
	 * @return
	 */
	public List<FlowPartner> selectAllFlowPartners() {
		return getList("selectAllFlowPartners");
	}

	/**
	 * 更改选择流量合作商信息
	 * @return
	 */
	public Integer updateChoicedPartner(FlowPartner flowPartner) {
		return update("updateChoicedPartner", flowPartner);
	}
	
	/**
	 * 更改非流量合作商信息
	 * @return
	 */
	public Integer updateNotChoicedPartner(FlowPartner flowPartner) {
		return update("updateNotChoicedPartner", flowPartner);
	}

	/**
	 * 查询被使用流量合作商信息
	 * @return
	 */
	public List<FlowPartner> selectUsedPartner() {
		return getList("selectOneUsedPartner");
	}
}
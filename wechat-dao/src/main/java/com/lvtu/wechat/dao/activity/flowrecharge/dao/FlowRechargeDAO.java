package com.lvtu.wechat.dao.activity.flowrecharge.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRecharge;
import com.lvtu.wechat.common.vo.back.FlowRechargeConditionVo;


@Repository
public class FlowRechargeDAO extends BaseIbatisDAO {
	
	 public FlowRechargeDAO() {
	        super("FLOWRECHARGE");
	    }

	 
	/**
	 * 
	* @Title: selectList 
	* @Description: TODO(手动充值流量首页面，分页) 
	* @param @param flowRechargeConditionVo
	* @param @return    设定文件 
	* @return List<FlowRecharge>    返回类型 
	* @throws
	 */
	public List<FlowRecharge> selectList(FlowRechargeConditionVo flowRechargeConditionVo) {
		 return super.getList("selectList", flowRechargeConditionVo);
	}

	/**
	 * 
	* @Title: queryOpenid 
	* @Description: TODO(添加流量充值活动) 
	* @param @param flowopenids
	* @param @return    设定文件 
	* @return List<String>    返回类型 
	* @throws
	 */
	public FlowRecharge addfloeRecharge(FlowRecharge flowRecharge) {
		return (FlowRecharge) insert("addfloeRecharge", flowRecharge);
	}

}

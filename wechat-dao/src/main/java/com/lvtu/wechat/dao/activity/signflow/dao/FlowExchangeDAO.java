package com.lvtu.wechat.dao.activity.signflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.signflow.FlowExchange;
import com.lvtu.wechat.common.vo.back.FlowRecordConditionVo;


@Repository
public class FlowExchangeDAO extends BaseIbatisDAO {

	public FlowExchangeDAO() {
		super("T_WX_FLOW_EXCHANGE");
	}

	/**
	 * 根据openid获取兑换记录
	 * @param openid
	 * @return
	 */
	public FlowExchange selectByOpenid(String openid) {
		return get("selectByOpenid", openid);
	}

	/**
	 * 根据主键获得兑换信息
	 * @param id
	 * @return
	 */
	public FlowExchange selectByPrimaryKey(String id) {
		return get("selectByPrimaryKey", id);
	}

	/**
	 * 插入一条流量兑换数据
	 * @param flow
	 * @return
	 */
	public FlowExchange insertSelective(FlowExchange flowExchange) {
		return (FlowExchange) insert("insertSelective", flowExchange);
	}

	/**
	 * 更新兑换记录
	 * @param flow
	 * @return
	 */
	public Integer updateByPrimaryKey(FlowExchange flowExchange) {
		return update("updateByPrimaryKey", flowExchange);
	}

	/**
	 * 根据日期月份(2015-06)获取兑换记录
	 * 
	 * @param date
	 * @return
	 */
	public List<FlowExchange> selectByDate(String date, String openid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("date", date);
		params.put("openid", openid);
		return getList("selectByDate", params);
	}

	/**
	 * 
	* @Title: selectListByOpenid 
	* @Description: TODO(查询t_wx_flow_exchange表的记录) 
	* @param @param flowRecordConditionVo
	* @param @return    设定文件 
	* @return List<FlowExchange>    返回类型 
	* @throws
	 */
    public List<FlowExchange> selectListByOpenid(FlowRecordConditionVo flowRecordConditionVo) {
        return super.getList("selectListByOpenid", flowRecordConditionVo);
    }
}
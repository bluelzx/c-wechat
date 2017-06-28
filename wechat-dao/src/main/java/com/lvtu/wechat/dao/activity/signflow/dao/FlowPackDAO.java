package com.lvtu.wechat.dao.activity.signflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.signflow.FlowPack;


@Repository
public class FlowPackDAO extends BaseIbatisDAO {

	public FlowPackDAO() {
		super("T_WX_FLOW_PACK");
	}
	
	/**
	 * 获得所有流量包信息
	 * @return
	 */
	public List<FlowPack> selectAll() {
		return getList("selectAll");
	}

	/**
	 * 根据运营商获取支持的流量
	 * @param operator 运营商
	 * @return
	 */
	public List<FlowPack> selectByOperator(String operator) {
		return getList("selectByOperator", operator);
	}

	/**
	 * 根据code和运营商获得流量包信息
	 * @param operator 运营商
	 * @param flowCode 流量编码
	 * @return
	 */
	public FlowPack selectByOpAndCode(String operator, String flowCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("operator", operator);
		params.put("flowCode", flowCode);
		return get("selectByOpAndCode", params);
	}

	/**
	 * 通过id删除记录
	 * @param id
	 * @return
	 */
	public Integer deleteByPK(String id) {
		return delete("deleteByPrimaryKey", id);
	}

	/**
	 * 保存流量包信息
	 * @param pack
	 */
	public void save(FlowPack pack) {
		insert("insertSelective", pack);
	}
}
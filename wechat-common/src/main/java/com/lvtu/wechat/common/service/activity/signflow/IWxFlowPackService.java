package com.lvtu.wechat.common.service.activity.signflow;

import java.util.List;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.signflow.FlowPack;


/**
 * 服务商支持的流量包服务
 * @author xuyao
 *
 */
@RemoteService("flowPackService")
public interface IWxFlowPackService {
	
	/**
	 * 获取所有流量包信息
	 * @return
	 */
	public List<FlowPack> getAllFlowPacks();
	
	/**
	 * 根据运营商获取支持的流量包
	 * @param operator 运营商代码 CMCC CMTC CMUC
	 * @return
	 */
	public List<FlowPack> getFlowPacks(String operator);

	/**
	 * 更具运营商和流量包代码查询流量包信息
	 * @param operator
	 * @param flowCode
	 * @return
	 */
	public FlowPack getFlowPackByOpAndCode(String operator, String flowCode);

	/**
	 * 删除流量包
	 * @param id
	 */
	public void delete(String id);

	/**
	 * 保存流量包信息
	 * @param pack
	 */
	public void save(FlowPack pack);
}

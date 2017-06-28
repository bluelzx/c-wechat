package com.lvtu.wechat.common.service.activity.signflow;

import java.util.List;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.signflow.FlowPartner;


/**
 * 服务商服务
 * @author zhengchongxiang
 *
 */
@RemoteService("flowPartnerService")
public interface IWxFlowPartnerService {
	
	/**
	 * 获取所有流量包信息
	 * @return
	 */
	public List<FlowPartner> getAllFlowPartners();
	
	/**
	 * 修改流量合作商信息
	 * @param id
	 * @return
	 */
	public boolean updatePartner(String id);
	
	/**
	 * 修改流量合作商信息
	 * @param id
	 * @return
	 */
	public FlowPartner selectUsedPartner();
	
}

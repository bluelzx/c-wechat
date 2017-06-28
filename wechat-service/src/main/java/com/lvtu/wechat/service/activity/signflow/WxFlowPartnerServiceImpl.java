package com.lvtu.wechat.service.activity.signflow;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.signflow.FlowPartner;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowPartnerService;
import com.lvtu.wechat.dao.activity.signflow.dao.FlowPartnerDAO;

/**
 * 流量服务商支服务实现
 * 
 * @author zhengchongxiang
 * 
 */
@HessianService("flowPartnerService")
@Service("flowPartnerService")
public class WxFlowPartnerServiceImpl implements IWxFlowPartnerService {
	
	Logger logger = Logger.getLogger(WxFlowPartnerServiceImpl.class);
	
	@Autowired
	private FlowPartnerDAO flowPartnerDao;

	@Override
	public List<FlowPartner> getAllFlowPartners() {
		
		return flowPartnerDao.selectAllFlowPartners();
	}

	@Override
	@Transactional
	public boolean updatePartner(String id) {
		boolean flag =false;
		FlowPartner flowPartner = new FlowPartner();
		flowPartner.setId(id);
		flowPartner.setUsedFlag(1);
		Integer temp = flowPartnerDao.updateChoicedPartner(flowPartner);
		if(temp>0){
			flowPartner.setUsedFlag(0);
			Integer temp2 = flowPartnerDao.updateNotChoicedPartner(flowPartner);
			if(temp2>0){
				flag = true;
			}
		}
		
		return flag;
	}

	@Override
	public FlowPartner selectUsedPartner() {
		FlowPartner flowPartner = new FlowPartner();
		List<FlowPartner> lists = flowPartnerDao.selectUsedPartner();
		if(lists != null && lists.size() > 0){
			flowPartner = lists.get(0);
			return flowPartner;
		}
		return null;
	}
}

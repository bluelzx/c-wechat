package com.lvtu.wechat.service.activity.signflow;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.signflow.FlowPack;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowPackService;
import com.lvtu.wechat.dao.activity.signflow.dao.FlowPackDAO;

/**
 * 服务商支持的流量包服务实现
 * 
 * @author xuyao
 * 
 */
@HessianService("flowPackService")
@Service("flowPackService")
public class WxFlowPackServiceImpl implements IWxFlowPackService {
	
	Logger logger = Logger.getLogger(WxFlowPackServiceImpl.class);
	
	@Autowired
	private FlowPackDAO flowPackDao;
	
	@Override
	public List<FlowPack> getAllFlowPacks() {
		return flowPackDao.selectAll();
	}

	@Override
	public List<FlowPack> getFlowPacks(String operator) {
		
		if(StringUtils.isBlank(operator))
			return null;
		
		return flowPackDao.selectByOperator(operator);
	}

	@Override
	public FlowPack getFlowPackByOpAndCode(String operator, String flowCode){
		if(StringUtils.isBlank(operator) || StringUtils.isBlank(flowCode))
			return null;
		
		return flowPackDao.selectByOpAndCode(operator, flowCode);
	}

	@Override
	public void delete(String id) {
		if(StringUtils.isBlank(id))
			return;
		flowPackDao.deleteByPK(id);
	}

	@Override
	public void save(FlowPack pack) {
		if(StringUtils.isBlank(pack.getFlowCode()))
			return;
		if(pack.getFlow() == null || pack.getFlow() <= 0)
			return;
		if(StringUtils.isBlank(pack.getOperator()))
			return;
		
		pack.preInsert();
		flowPackDao.save(pack);
	}
}

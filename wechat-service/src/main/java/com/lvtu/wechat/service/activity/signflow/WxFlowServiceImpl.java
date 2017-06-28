package com.lvtu.wechat.service.activity.signflow;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.signflow.Flow;
import com.lvtu.wechat.common.model.activity.signflow.FlowExchange;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowService;
import com.lvtu.wechat.dao.activity.signflow.dao.FlowDAO;
import com.lvtu.wechat.dao.activity.signflow.dao.FlowExchangeDAO;

/**
 * 微信绑定服务实现
 * 
 * @author xuyao
 * 
 */
@HessianService("wechatFlowService")
@Service("wechatFlowService")
public class WxFlowServiceImpl implements IWxFlowService {
	
	Logger logger = Logger.getLogger(WxFlowServiceImpl.class);
	
	@Autowired
	private FlowDAO flowDao;
	
	@Autowired
	private FlowExchangeDAO flowExchangeDao;

	@Override
	public Flow getFlow(String openid) {
		if (StringUtils.isBlank(openid))
			return null;

		Flow flow = flowDao.selectByOpenid(openid);
		if (flow == null) {
			flow = new Flow();
			flow.setOpenid(openid);
			flow.setTotalFlow(0);
			flow.setSurplusFlow(0);
			flow.preInsert();
			flow = flowDao.insertSelective(flow);
		}
		return flow;
	}

	@Override
	@Transactional
	public FlowExchange exchange(int flow, String openid, String phoneNum, String operator) throws CustomerException {
		if (flow == 0 || StringUtils.isEmpty(openid) || StringUtils.isEmpty(phoneNum)) {
			throw new CustomerException("参数不正确");
		}

		//判断本月是否已经兑换过流量
		String thisMonth = DateUtil.formatDate(new Date(), "yyyy-MM");
		List<FlowExchange> exchangeRecords = flowExchangeDao.selectByDate(thisMonth, openid);
		if(exchangeRecords != null) {
			boolean hasExchangedThisMon = false;
			for(FlowExchange exchange : exchangeRecords) {
				Boolean isSucceed = exchange.getSucceed();
				if(isSucceed == null || isSucceed == true) {
					hasExchangedThisMon = true;
					break;
				}
			}
			if(hasExchangedThisMon) {
				throw new CustomerException("本月已经领取过流量");
			}
		}
		
		
		Flow flows = flowDao.selectByOpenid(openid);
		int surplusFlow = flows.getSurplusFlow();
		if(flows == null || surplusFlow == 0 || surplusFlow < flow) {
			throw new CustomerException("剩余流量不足");
		} else {
			//更新用户流量
			flows.setSurplusFlow(surplusFlow - flow);
			flowDao.updateByPrimaryKey(flows);
			
			//增加兑换记录
			FlowExchange exchange = new FlowExchange();
			exchange.setOpenid(openid);
			exchange.setExchangeFlow(flow);
			exchange.setExchangeTime(new Date());
			exchange.setPhoneNum(phoneNum);
			exchange.setOperator(operator);
			exchange.preInsert();
			exchange = flowExchangeDao.insertSelective(exchange);
			
			return exchange;
		}
	}

	@Override
	@Transactional
	public boolean rollbackExchange(String exchangeId) {
		if(StringUtils.isBlank(exchangeId))
			return false;
		
		FlowExchange flowExchange = flowExchangeDao.selectByPrimaryKey(exchangeId);
		if(flowExchange != null) {
			Flow flows = flowDao.selectByOpenid(flowExchange.getOpenid());
			if(flows != null) {
				//回滚扣除的流量
				int surplusFlow = flows.getSurplusFlow();
				flows.setSurplusFlow(surplusFlow + flowExchange.getExchangeFlow());
				flowDao.updateByPrimaryKey(flows);
				//更新兑换状态
				flowExchange.setSucceed(false);
				flowExchange.setRemarks("流量充值失败，扣除流量已回退。");
				flowExchangeDao.updateByPrimaryKey(flowExchange);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateStatus(Map<String, Object> params) {
		String exchangeId = (String) params.get("exchangeId");
		String returnCode = (String) params.get("returnCode");
		String returnDesc = (String) params.get("returnDesc");
		
		if(StringUtils.isBlank(exchangeId) || StringUtils.isBlank(returnCode))
			return false;

		FlowExchange flowExchange = flowExchangeDao.selectByPrimaryKey(exchangeId);
		//判断是否有兑换记录，并且是否已经更新过状态。默认succed为null
		if (flowExchange != null && flowExchange.getSucceed() == null) {
			flowExchange.setSucceed(returnCode.equals("0000") || returnCode.equals("1"));
			flowExchange.setReturnCode(returnCode);
			flowExchange.setReturnTime(new Date());
			flowExchange.setReturnDesc(returnDesc);
			flowExchangeDao.updateByPrimaryKey(flowExchange);
			//未成功回退扣除的流量
			if(!flowExchange.getSucceed()) {
				rollbackExchange(exchangeId);
			}
		}

		return false;
	}

	@Override
	public void update(Flow flow) {
		flowDao.updateByPrimaryKey(flow);
	}
	
	/**
     * 没有openid时, 插入记录, got_first_flow状态为0
     */
    @Override
    @Transactional
    public void addwxFlow(Flow flow) {
        if (StringUtils.isBlank(flow.getId())) {
            flow.preInsert();
            flowDao.addwxFlow(flow);
        }
        
    }

    /**
     * 根据openid, 查询t_wx_flow用户
     */
    @Override
    @Transactional
    public List<Flow> selectFlow(String openid) {
       return flowDao.selectFlow(openid);
    }

    /**
     * openid存在时, 在total_flow 和 surplus_flow 增加流量
     */
    @Override
    @Transactional
    public void changewxFlow(Flow flow) {
        flowDao.changewxFlow(flow);
        
    }

}

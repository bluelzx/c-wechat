package com.lvtu.wechat.service.activity.flowrecharge;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.flowrecharge.ExportFlowRechargeRecord;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRecharge;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRechargeRecord;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRechargeRecordInfo;
import com.lvtu.wechat.common.model.activity.signflow.Flow;
import com.lvtu.wechat.common.model.activity.signflow.FlowExchange;
import com.lvtu.wechat.common.service.activity.flowrecharge.IFlowRechargeService;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowService;
import com.lvtu.wechat.common.vo.back.FlowRechargeConditionVo;
import com.lvtu.wechat.common.vo.back.FlowRecordConditionVo;
import com.lvtu.wechat.dao.activity.flowrecharge.dao.FlowRechargeDAO;
import com.lvtu.wechat.dao.activity.flowrecharge.dao.FlowRechargeRecordDAO;
import com.lvtu.wechat.dao.activity.signflow.dao.FlowExchangeDAO;


@HessianService("flowRechargeService")
@Service("flowRechargeService")
@Transactional(readOnly = true)
public class FlowRechargeServiceImpl implements IFlowRechargeService {
	
	
	@Autowired
	 private FlowRechargeDAO flowRechargeDAO;
	
	@Autowired
	private FlowRechargeRecordDAO flowRechargeRecordDAO;
	
	@Autowired
	private IWxFlowService wechatFlowService;
	
	@Autowired
    private FlowExchangeDAO flowExchangeDAO;

	 
	/**
	 * 首页面, 分页
	 */
	@Override
	@Transactional(readOnly = false)
	public PageInfo<FlowRecharge> queryFlowRechargeList(FlowRechargeConditionVo flowRechargeConditionVo) {
		
		PageHelper.startPage(flowRechargeConditionVo.getPage(), flowRechargeConditionVo.getPageSize());
		List<FlowRecharge> flowRechargeList = flowRechargeDAO.selectList(flowRechargeConditionVo);
	    PageInfo<FlowRecharge> pageInfo = new PageInfo<FlowRecharge>(flowRechargeList);
	       
	    return pageInfo;
		
	}

	
	/**
	 * 添加流量充值活动, 向flow_recharge_record表中插入充值记录
	 */
    @Override
    @Transactional(readOnly = false)
    public void addflowRechargeRecord(FlowRecharge flowRecharge, List<FlowRechargeRecord> flowRechargeRecordList) {
        
           Date now = new Date();
           flowRecharge.setCreateDate(now);
           flowRechargeDAO.addfloeRecharge(flowRecharge);
         
          for (FlowRechargeRecord flowRechargeRecord : flowRechargeRecordList) {
              
                flowRechargeRecord.setCreateDate(now);
                flowRechargeRecordDAO.addflowRechargeRecord(flowRechargeRecord);
                
                String openid = flowRechargeRecord.getOpenid();
                //根据openid，查询t_wx_flow用户
                List<Flow> flowList = wechatFlowService.selectFlow(openid);
                Flow flow = new Flow();
                if (flowList == null || flowList.isEmpty()) {
                    flow.setOpenid(openid);
                    flow.setTotalFlow(flowRechargeRecord.getRechargeFlow());
                    flow.setSurplusFlow(flowRechargeRecord.getRechargeFlow());
                    wechatFlowService.addwxFlow(flow);
                } else {
                    //根据openid, 查询t_wx_flow用户
                    flowList.get(0).setRechargeFlow(flowRechargeRecord.getRechargeFlow());
                    //openid存在时, 在total_flow 和 surplus_flow 增加流量
                    wechatFlowService.changewxFlow(flowList.get(0));
                }
          }
      
    }
	
	
	/**
	 * 根据flow_recharge表的id, 查询相应的充值记录
	 */
	@Override
	@Transactional(readOnly = false)
	public PageInfo<FlowRechargeRecordInfo> queryFlowRechargeRecordListById(FlowRecordConditionVo flowRecordConditionVo) {
		PageHelper.startPage(flowRecordConditionVo.getPage(), flowRecordConditionVo.getPageSize());
		List<FlowRechargeRecordInfo> FlowRechargeRecordList = flowRechargeRecordDAO.queryFlowRechargeRecordListById(flowRecordConditionVo);
	    PageInfo<FlowRechargeRecordInfo> pageInfo = new PageInfo<FlowRechargeRecordInfo>(FlowRechargeRecordList);
	    return pageInfo;
	}
	

	/**
	 * 查询需要导出的充值流量记录
	 */
	@Override
	@Transactional(readOnly = false)
	public PageInfo<ExportFlowRechargeRecord> queryExportFlowRechargeRecord(FlowRecordConditionVo flowRecordConditionVo) {
		 PageHelper.startPage(flowRecordConditionVo.getPage(), 0);
	     List<ExportFlowRechargeRecord> flowRechargeRecordList = flowRechargeRecordDAO.queryExportFlowRechargeRecord(flowRecordConditionVo);
	     PageInfo<ExportFlowRechargeRecord> pageInfo = new PageInfo<ExportFlowRechargeRecord>(flowRechargeRecordList);
	     return pageInfo;
	}


	/**
	 * 查询t_wx_flow_exchange表的记录
	 */
    @Override
    @Transactional(readOnly = false)
    public PageInfo<FlowExchange> queryWxFlowExchange(FlowRecordConditionVo flowRecordConditionVo) {
        PageHelper.startPage(flowRecordConditionVo.getPage(), flowRecordConditionVo.getPageSize());
        List<FlowExchange> flowExchangeList = flowExchangeDAO.selectListByOpenid(flowRecordConditionVo);
        PageInfo<FlowExchange> pageInfo = new PageInfo<FlowExchange>(flowExchangeList);
           
        return pageInfo;
    }

}

package com.lvtu.wechat.common.service.activity.flowrecharge;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.flowrecharge.ExportFlowRechargeRecord;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRecharge;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRechargeRecord;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRechargeRecordInfo;
import com.lvtu.wechat.common.model.activity.signflow.FlowExchange;
import com.lvtu.wechat.common.vo.back.FlowRechargeConditionVo;
import com.lvtu.wechat.common.vo.back.FlowRecordConditionVo;



@RemoteService("flowRechargeService")
public interface IFlowRechargeService {
	

	/**
	 * 
	* @Title: queryFlowRechargeList 
	* @Description: TODO(手动充值流量首页面，分页) 
	* @param @param flowRechargeConditionVo
	* @param @return    设定文件 
	* @return PageInfo<FlowRecharge>    返回类型 
	* @throws
	 */
	PageInfo<FlowRecharge> queryFlowRechargeList(FlowRechargeConditionVo flowRechargeConditionVo);


    /**
     * 
    * @Title: queryFlowRechargeRecordListById 
    * @Description: TODO(根据flow_recharge表的id, 查询相应的充值记录) 
    * @param @param flowRecordConditionVo
    * @param @return    设定文件 
    * @return PageInfo<FlowRechargeRecordInfo>    返回类型 
    * @throws
     */
	PageInfo<FlowRechargeRecordInfo> queryFlowRechargeRecordListById(FlowRecordConditionVo flowRecordConditionVo);



	/**
	 * 
	* @Title: queryExportFlowRechargeRecord 
	* @Description: TODO(查询需要导出的充值流量记录) 
	* @param @param flowRecordConditionVo
	* @param @return    设定文件 
	* @return PageInfo<ExportFlowRechargeRecord>    返回类型 
	* @throws
	 */
	PageInfo<ExportFlowRechargeRecord> queryExportFlowRechargeRecord(FlowRecordConditionVo flowRecordConditionVo);



	/**
	 * 
	* @Title: addflowRechargeRecord 
	* @Description: TODO(添加流量充值活动, 向flow_recharge_record表中插入充值记录) 
	* @param @param flowRecharge
	* @param @param flowRechargeRecordList    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void addflowRechargeRecord(FlowRecharge flowRecharge, List<FlowRechargeRecord> flowRechargeRecordList);


	/**
	 * 
	* @Title: queryWxFlowExchange 
	* @Description: TODO(查询t_wx_flow_exchange表的记录) 
	* @param @param flowRecordConditionVo
	* @param @return    设定文件 
	* @return PageInfo<FlowRecharge>    返回类型 
	* @throws
	 */
    PageInfo<FlowExchange> queryWxFlowExchange(FlowRecordConditionVo flowRecordConditionVo);

}

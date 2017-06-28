package com.lvtu.wechat.dao.activity.flowrecharge.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.flowrecharge.ExportFlowRechargeRecord;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRechargeRecord;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRechargeRecordInfo;
import com.lvtu.wechat.common.vo.back.FlowRecordConditionVo;


@Repository
public class FlowRechargeRecordDAO extends BaseIbatisDAO {
	
	 public FlowRechargeRecordDAO() {
	        super("FLOWRECHARGERECORD");
	    }


	/**
	 * 
	* @Title: addflowRechargeRecord 
	* @Description: TODO(向flow_recharge_record表中插入充值记录) 
	* @param @param flowRechargeRecord
	* @param @return    设定文件 
	* @return FlowRechargeRecord    返回类型 
	* @throws
	 */
	public FlowRechargeRecord addflowRechargeRecord(FlowRechargeRecord flowRechargeRecord) {
		return (FlowRechargeRecord) insert("addflowRechargeRecord", flowRechargeRecord);
		
	}

    /**
     * 
    * @Title: queryFlowRechargeRecordListById 
    * @Description: TODO(根据flow_recharge表的id, 查询相应的充值记录) 
    * @param @param flowRecordConditionVo
    * @param @return    设定文件 
    * @return List<FlowRechargeRecordInfo>    返回类型 
    * @throws
     */
	public List<FlowRechargeRecordInfo> queryFlowRechargeRecordListById(FlowRecordConditionVo flowRecordConditionVo) {
		 return super.getList("queryFlowRechargeRecordListById", flowRecordConditionVo);
	}


	/**
	 * 
	* @Title: queryExportFlowRechargeRecord 
	* @Description: TODO(查询需要导出的充值流量记录) 
	* @param @param flowRecordConditionVo
	* @param @return    设定文件 
	* @return List<ExportFlowRechargeRecord>    返回类型 
	* @throws
	 */
	public List<ExportFlowRechargeRecord> queryExportFlowRechargeRecord(FlowRecordConditionVo flowRecordConditionVo) {
		 return super.getList("queryExportFlowRechargeRecord", flowRecordConditionVo);
	}

}

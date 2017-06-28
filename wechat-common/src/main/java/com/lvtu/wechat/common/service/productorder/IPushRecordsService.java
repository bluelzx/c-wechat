package com.lvtu.wechat.common.service.productorder;

import java.util.List;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.productorder.PushRecords;


/**
 * @ClassName: IPushRecordsService
 * @Description: 推送消息记录
 * @author zhengchongxiang
 * @date 2016-8-26 下午2:10:20
 */
@RemoteService("pushRecordsService")
public interface IPushRecordsService {
	
	/** 
	* @Title: save 
	* @Description: 新增推送记录 
	* @param @param pushRecords
	* @return boolean    返回类型 
	*/
	public boolean save(PushRecords pushRecords);
		
	/** 
	* @Title: selectByMsgId 
	* @Description: 根据消息id查询推送记录 
	* @param @param msgId
	* @return PushRecords    返回类型  
	*/
	public PushRecords selectByMsgId(String msgId);
	
	/** 
	* @Title: update 
	* @Description: 修改推送记录信息 
	* @return void    返回类型 
	*/
	public void update(PushRecords pushRecords);
	
	/** 
	* @Title: selectBySuccess 
	* @Description: 统计发送量与成功量 
	* @param @param id
	* @return List<PushRecords>    返回类型 
	* @throws 
	*/
	public List<PushRecords> selectBySuccess(String id);
	
}

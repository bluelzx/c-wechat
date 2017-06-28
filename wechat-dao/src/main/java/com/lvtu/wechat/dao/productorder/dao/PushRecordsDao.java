package com.lvtu.wechat.dao.productorder.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.productorder.PushRecords;

/** 
* @ClassName: PushRecordsDao 
* @Description: 推送记录
* @author zhengchongxiang
* @date 2016-8-29 下午1:44:59  
*/
@Repository
public class PushRecordsDao extends BaseIbatisDAO {
	
	public PushRecordsDao(){
		super("T_WX_PUSH_RECORDS");
	}
	
	public void save(PushRecords pushRecords){		
		super.insert("insert", pushRecords);
	}
	
	public List<PushRecords> selectByMsgId(String msgId){
		
		return super.getList("selectByMsgId", msgId);
	}
	
	public void update(PushRecords pushRecords){
		super.update("update", pushRecords);
	}
	
	public List<PushRecords> selectBySuccess(String id){
		return super.getList("selectBySuccess", id);
	}
	
	
}

package com.lvtu.wechat.service.activity.productorder;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.productorder.PushRecords;
import com.lvtu.wechat.common.service.productorder.IPushRecordsService;
import com.lvtu.wechat.dao.productorder.dao.PushRecordsDao;

/** 
* @ClassName: PushRecordsServiceImpl 
* @Description: 推送记录
* @author zhengchongxiang
* @date 2016-8-29 下午1:39:35  
*/
@HessianService("pushRecordsService")
@Service("pushRecordsService")
@Transactional(readOnly = true)
public class PushRecordsServiceImpl implements IPushRecordsService {

	Logger logger = Logger.getLogger(PushRecordsServiceImpl.class);
	
	@Autowired
	private PushRecordsDao pushRecordsDao;

	@Override
	@Transactional(readOnly = false)
	public boolean save(PushRecords pushRecords) {
		pushRecords.preInsert();
		pushRecordsDao.save(pushRecords);
		return false;
	}

	@Override
	public PushRecords selectByMsgId(String msgId) {		
		List<PushRecords> list = pushRecordsDao.selectByMsgId(msgId);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public void update(PushRecords pushRecords) {		
		pushRecordsDao.update(pushRecords);
	}

	@Override
	public List<PushRecords> selectBySuccess(String id) {
		List<PushRecords> list = pushRecordsDao.selectBySuccess(id);
		return list;
	}


}

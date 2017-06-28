package com.lvtu.wechat.service.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.sys.Log;
import com.lvtu.wechat.common.service.sys.LogService;
import com.lvtu.wechat.common.vo.back.LogConditionVO;
import com.lvtu.wechat.dao.sys.dao.LogDao;

/**
 * 日志服务实现类
 * 
 * @author xuyao
 *
 */
@HessianService("remoteLogService")
@Service("logService")
@Transactional(readOnly = true)
public class LogServiceImpl implements LogService {

	@Autowired
	private LogDao logDao;

	@Override
	@Transactional(readOnly = false)
	public void saveLog(Log log) {
		if (log != null) {
			log.preInsert();
			logDao.save(log);
		}
	}

	@Override
	public PageInfo<Log> findList(LogConditionVO params) {
		PageHelper.startPage(params.getPage(), params.getPageSize());
		List<Log> logs = logDao.findList(params);
		PageInfo<Log> page = new PageInfo<Log>(logs);
		return page;
	}

}

package com.lvtu.wechat.common.service.sys;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.sys.Log;
import com.lvtu.wechat.common.vo.back.LogConditionVO;

/**
 * 系统日志服务
 * 
 * @author xuyao
 *
 */
@RemoteService("remoteLogService")
public interface LogService {
	/**
	 * 保存日志信息
	 * 
	 * @param log
	 */
	public void saveLog(Log log);

	/**
	 * 查询日志
	 * @param params
	 * @return
	 */
	public PageInfo<Log> findList(LogConditionVO params);
}

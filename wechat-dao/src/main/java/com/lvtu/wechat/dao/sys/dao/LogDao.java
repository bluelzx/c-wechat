package com.lvtu.wechat.dao.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.sys.Log;
import com.lvtu.wechat.common.vo.back.LogConditionVO;

/**
 * 系统日志DAO
 * @author xuyao
 *
 */
@Repository
public class LogDao extends BaseIbatisDAO {

	public LogDao() {
		super("sys_log");
	}

	public List<Log> findList(LogConditionVO params) {
		return super.getList("findList", params);
	}

	public void save(Log log) {
		super.insert("insert", log);
	}
}

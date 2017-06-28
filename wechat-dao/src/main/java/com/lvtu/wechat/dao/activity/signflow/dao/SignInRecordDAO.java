package com.lvtu.wechat.dao.activity.signflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.signflow.SignInRecord;


@Repository
public class SignInRecordDAO extends BaseIbatisDAO {

	public SignInRecordDAO() {
		super("T_WX_SIGNIN");
	}

	/**
	 * 插入一条签到记录
	 * 
	 * @param signInRecord
	 * @return
	 */
	public SignInRecord insertSelective(SignInRecord signInRecord) {
		return (SignInRecord) insert("insertSelective", signInRecord);
	}

	/**
	 * 根据日期获取签到记录
	 * 
	 * @param date
	 * @return
	 */
	public List<SignInRecord> selectByDate(String date, String openid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("date", date);
		params.put("openid", openid);
		return getList("selectByDate", params);
	}
}
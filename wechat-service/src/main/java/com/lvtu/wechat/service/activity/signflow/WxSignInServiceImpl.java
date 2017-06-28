package com.lvtu.wechat.service.activity.signflow;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.activity.signflow.Flow;
import com.lvtu.wechat.common.model.activity.signflow.SignInRecord;
import com.lvtu.wechat.common.service.activity.signflow.IWxSignInService;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.dao.activity.signflow.dao.FlowDAO;
import com.lvtu.wechat.dao.activity.signflow.dao.SignInRecordDAO;

/**
 * 微信绑定服务实现
 * 
 * @author xuyao
 * 
 */
@HessianService("wechatSignInService")
@Service("wechatSignInService")
public class WxSignInServiceImpl implements IWxSignInService {
	
	Logger logger = Logger.getLogger(WxSignInServiceImpl.class);
	
	@Autowired
	private SignInRecordDAO signInDao;
	
	@Autowired
	private FlowDAO flowDao;

	@Override
	@Transactional
	public boolean signIn(String openid) throws CustomerException {
		if (StringUtils.isBlank(openid))
			return false;
		//获得绑定关系
		Date nowDate = new Date();
		String nowDateStr = DateUtils.formatDate(nowDate, "yyyy-MM-dd");
		List<SignInRecord> existRecord = signInDao.selectByDate(nowDateStr, openid);
		if(existRecord == null || existRecord.size() == 0) {
			Date yesterday = DateUtils.addDays(nowDate, -1);
			String yesterdayDateStr = DateUtils.formatDate(yesterday, "yyyy-MM-dd");
			List<SignInRecord> yesterdayRecord = signInDao.selectByDate(yesterdayDateStr, openid);
			//构建签到记录
			SignInRecord signInRecord = new SignInRecord();
			if(yesterdayRecord != null && yesterdayRecord.size() > 0) {
				signInRecord.setContinuousSignCount(yesterdayRecord.get(0).getContinuousSignCount() + 1);
			} else {
				signInRecord.setContinuousSignCount(1);
			}
			signInRecord.setOpenid(openid);
			signInRecord.setAddedFlow(Constants.WX_SIGNIN_FLOW_ADD);
			signInRecord.setSignTime(nowDate);
			signInRecord.setAdditionalAward(false);
			signInRecord.setRemark("普通签到");
			signInRecord.preInsert();
			signInRecord = signInDao.insertSelective(signInRecord);
			
			//为用户增加流量
			Flow flow = flowDao.selectByOpenid(openid);
			if(flow == null) {
				flow = new Flow();
				flow.setOpenid(openid);
				flow.setTotalFlow(Constants.WX_SIGNIN_FLOW_ADD);
				flow.setSurplusFlow(Constants.WX_SIGNIN_FLOW_ADD);
				flow.preInsert();
				flowDao.insertSelective(flow);
			} else {
				flow.setTotalFlow(flow.getTotalFlow() + Constants.WX_SIGNIN_FLOW_ADD);
				flow.setSurplusFlow(flow.getSurplusFlow() + Constants.WX_SIGNIN_FLOW_ADD);
				flowDao.updateByPrimaryKey(flow);
			}
			return true;
		} else {
			throw new CustomerException("今天已经签到过");
		}
	}

	@Override
	public Map<String, Object> signInfo(String openid) {
		if (StringUtils.isEmpty(openid)) {
			return null;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(1);
		Date todayDate = new Date();
		String todayDateStr = DateUtils.formatDate(todayDate, "yyyy-MM-dd");
		List<SignInRecord> lastSign = signInDao.selectByDate(todayDateStr, openid);
		if(lastSign == null || lastSign.size() == 0) {
			resultMap.put("signed", "false");
			Date yesterday = DateUtils.addDays(todayDate, -1);
			String yesterdayDateStr = DateUtils.formatDate(yesterday, "yyyy-MM-dd");
			lastSign = signInDao.selectByDate(yesterdayDateStr, openid);
		} else {
			resultMap.put("signed", "true");
		}
		
		if(lastSign == null || lastSign.size() == 0) {
			resultMap.put("continuousSignCount", 0);
		} else {
			resultMap.put("continuousSignCount", lastSign.get(0).getContinuousSignCount());
		}
		resultMap.put("code", 1);
		resultMap.put("msg", "success");
		
		return resultMap;
	}

}

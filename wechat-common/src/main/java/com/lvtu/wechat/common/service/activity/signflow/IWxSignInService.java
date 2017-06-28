package com.lvtu.wechat.common.service.activity.signflow;

import java.util.Map;

import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.hessian.RemoteService;

/**
 * 微信签到相关服务
 * @author xuyao
 *
 */
@RemoteService("wechatSignInService")
public interface IWxSignInService {

	/**
	 * 微信用户签到
	 * @param openid
	 * @return
	 * @throws Exception 
	 */
	public boolean signIn(String openid) throws CustomerException;
	
	/**
	 * 得到用户签到信息
	 * @param openid
	 * @return
	 */
	public Map<String, Object> signInfo(String openid);

}

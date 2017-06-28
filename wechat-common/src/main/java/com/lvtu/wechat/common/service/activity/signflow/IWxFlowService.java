package com.lvtu.wechat.common.service.activity.signflow;

import java.util.List;
import java.util.Map;

import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.activity.signflow.Flow;
import com.lvtu.wechat.common.model.activity.signflow.FlowExchange;

/**
 * 微信流量相关服务
 * @author xuyao
 *
 */
@RemoteService("wechatFlowService")
public interface IWxFlowService {

	/**
	 * 根据openid获取流量信息
	 * @param openid
	 * @return
	 */
	public Flow getFlow(String openid);
	
	/**
	 * 微信流量兑换
	 * @param flow
	 * @param openid
	 * @param phoneNum
	 * @param operator
	 * @return
	 * @throws CustomerException 
	 */
	public FlowExchange exchange(int flow, String openid, String phoneNum, String operator) throws CustomerException;

	/**
	 * 兑换失败时回滚
	 * @param exchangeId
	 * @return
	 */
	public boolean rollbackExchange(String exchangeId);
	
	/**
	 * 更新流量兑换状态
	 * 必要参数exchangeId returnCode returnDesc
	 * @return
	 */
	public boolean updateStatus(Map<String, Object> params);

	/**
	 * 更新流量信息
	 * @param flow
	 */
	public void update(Flow flow);
	
	/**
     * 
    * @Title: addwxFlow 
    * @Description: TODO(没有openid时, 插入记录, got_first_flow状态为0) 
    * @param @param flow    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void addwxFlow(Flow flow);

    /**
     * 
    * @Title: changewxFlow 
    * @Description: TODO(openid存在时, 在total_flow 和 surplus_flow 增加流量) 
    * @param @param flow    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public void changewxFlow(Flow flow);

    /**
     * 
    * @Title: selectFlow 
    * @Description: TODO(根据openid, 查询t_wx_flow用户) 
    * @param @param openid
    * @param @return    设定文件 
    * @return List<Flow>    返回类型 
    * @throws
     */
    public List<Flow> selectFlow(String openid);
}

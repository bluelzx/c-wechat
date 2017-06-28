package com.lvtu.wechat.dao.activity.signflow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.activity.signflow.Flow;


@Repository
public class FlowDAO extends BaseIbatisDAO {

	public FlowDAO() {
		super("T_WX_FLOW");
	}

	/**
	 * 根据openid获取用户流量信息
	 * @param openid
	 * @return
	 */
	public Flow selectByOpenid(String openid) {
		return get("selectByOpenid", openid);
	}

	/**
	 * 插入一条流量数据
	 * @param flow
	 * @return
	 */
	public Flow insertSelective(Flow flow) {
		return (Flow) insert("insertSelective", flow);
	}

	/**
	 * 更新流量信息
	 * @param flow
	 * @return
	 */
	public Integer updateByPrimaryKey(Flow flow) {
		return update("updateByPrimaryKey", flow);
	}
	
	  /**
     * 
    * @Title: addwxFlow 
    * @Description: TODO( 没有openid时, 插入记录, got_first_flow状态为0) 
    * @param @param flow    设定文件 
    * @return void    返回类型 
    * @throws
     */
    public Flow addwxFlow(Flow flow) {
        
        return (Flow) insert("addwxFlow", flow);
    }

    /**
     * 
    * @Title: selectFlow 
    * @Description: TODO(根据openid, 查询t_wx_flow用户) 
    * @param @param openid
    * @param @return    设定文件 
    * @return List<Flow>    返回类型 
    * @throws
     */
    public List<Flow> selectFlow(String openid) {
        
        return super.getList("selectFlow", openid);
    }

    /**
     * 
    * @Title: changewxFlow 
    * @Description: TODO(openid存在时, 在total_flow 和 surplus_flow 增加流量) 
    * @param @param flow
    * @param @return    设定文件 
    * @return Integer    返回类型 
    * @throws
     */
    public Integer changewxFlow(Flow flow) {
        
        return update("changewxFlow", flow);
    }

}
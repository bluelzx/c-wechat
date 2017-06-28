package com.lvtu.wechat.dao.productorder.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.productorder.PushTemplate;
import com.lvtu.wechat.common.vo.back.PushTemplateConditionVo;

/** 
* @ClassName: PushTemplateDao 
* @Description: 推送模板信息dao
* @author zhengchongxiang
* @date 2016-8-29 下午1:44:40  
*/
@Repository
public class PushTemplateDao extends BaseIbatisDAO {
	
	public PushTemplateDao() {
		super("T_WX_PUSH_TEMPLATE");
	}
	
	public List<PushTemplate> get(PushTemplateConditionVo vo){
		
		return super.getList("getPushTemplate", vo);
	}
	
	public PushTemplate getByPrimary(String id){
		return super.get("getByPrimary", id);
	}
	
	public PushTemplate save(PushTemplate pushTemplate){
		return (PushTemplate) super.insert("insertPushTemplate", pushTemplate);
		
	}
	
	public Integer update(PushTemplate pushTemplate){		
		return super.update("updatePushTemplate", pushTemplate);
		
	}
	
}

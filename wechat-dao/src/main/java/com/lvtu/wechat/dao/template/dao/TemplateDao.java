package com.lvtu.wechat.dao.template.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.template.Template;
import com.lvtu.wechat.common.vo.back.TemplateConditionVo;

/** 
* @ClassName: TemplateDao 
* @Description: 模板消息配置信息dao
* @author zhengchongxiang
* @date 2017-2-21 下午4:52:03  
*/
@Repository
public class TemplateDao extends BaseIbatisDAO{
	
	public TemplateDao(){
		super("TEMPLATE");
	}
	
	public Template insert(Template template){
		return (Template) super.insert("insert", template);
	}
	
	public Integer update(Template template){
		return super.update("update", template);
	}
	
	public List<Template> select(TemplateConditionVo vo){
		return super.getList("select",vo);
	}
	
	public Template selectById(String id){
		return super.get("selectById", id);
	}
	
	public Template selectByTemplateId(String templateId){
		return super.get("selectByTemplateId", templateId);
	}

}

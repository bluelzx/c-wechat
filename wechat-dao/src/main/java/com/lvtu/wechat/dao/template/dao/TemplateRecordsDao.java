package com.lvtu.wechat.dao.template.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lvtu.wechat.common.base.BaseIbatisDAO;
import com.lvtu.wechat.common.model.template.TemplateRecords;
import com.lvtu.wechat.common.vo.back.TemplateConditionVo;

/** 
* @ClassName: TemplateRecordsDao 
* @Description: 模板消息发送记录dao
* @author zhengchongxiang
* @date 2017-2-21 下午4:52:24  
*/
@Repository
public class TemplateRecordsDao extends BaseIbatisDAO{
	public TemplateRecordsDao(){
		super("TEMPLATE_RECORDS");
	}
	
	public TemplateRecords insert(TemplateRecords templateRecords){
		return (TemplateRecords) super.insert("insert", templateRecords);
	}
	
	public List<TemplateRecords> select(TemplateConditionVo templateConditionVo){
		return super.getList("select", templateConditionVo);
	}
	
	public List<TemplateRecords> selectByRetry(){
		return super.getList("selectRetry");
	}
	
	public void update(TemplateRecords templateRecords){
		super.update("update", templateRecords);
	}
	
	public TemplateRecords selectById(String id){
		return super.get("selectById", id);
	}

}

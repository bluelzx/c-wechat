package com.lvtu.wechat.common.service.template;


import java.util.List;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.template.Template;
import com.lvtu.wechat.common.model.template.TemplateRecords;
import com.lvtu.wechat.common.vo.back.TemplateConditionVo;

/** 
* @ClassName: ITemplateService 
* @Description: TODO
* @author zhengchongxiang
* @date 2017-2-21 下午4:48:20  
*/
@RemoteService("templateService")
public interface ITemplateService {
	
	/** 
	* @Title: saveOrUpdateTemplate 
	* @Description: 新增或修改模板消息 
	* @param @param template
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws 
	*/
	public boolean saveOrUpdateTemplate(Template template);
		
	/** 
	* @Title: selectTemplates 
	* @Description: 查询后台配置的模板消息 
	* @param @param vo
	* @param @return    设定文件 
	* @return PageInfo<Template>    返回类型 
	* @throws 
	*/
	public PageInfo<Template> selectTemplates(TemplateConditionVo vo);
	
	/** 
	* @Title: selectByPrimary 
	* @Description: 主键查询模板消息 
	* @param @param id
	* @param @return    设定文件 
	* @return Template    返回类型 
	* @throws 
	*/
	public Template selectByPrimary(String id);
	
	/** 
	* @Title: selectByTemplateId 
	* @Description: 根据模板Id产讯模板消息 
	* @param @param templateId
	* @param @return    设定文件 
	* @return Template    返回类型 
	* @throws 
	*/
	public Template selectByTemplateId(String templateId);
	
	/** 
	* @Title: save 
	* @Description: 保存模板消息发送记录 
	* @param @param templateRecords
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws 
	*/
	public boolean save(TemplateRecords templateRecords);
	
	/** 
	* @Title: selectTemplateRecords 
	* @Description: 查询消息发送记录 
	* @param @param vo
	* @param @return    设定文件 
	* @return PageInfo<TemplateRecords>    返回类型 
	* @throws 
	*/
	public PageInfo<TemplateRecords> selectTemplateRecords(TemplateConditionVo vo);
	
	/** 
	* @Title: selectByRetry 
	* @Description: 查询失败后需要重试机智的消息 
	* @param @return    设定文件 
	* @return List<TemplateRecords>    返回类型 
	* @throws 
	*/
	public List<TemplateRecords> selectByRetry();
	
	/** 
	* @Title: updateTemplateRecords 
	* @Description: 修改模板消息发送记录 
	* @param @param templateRecords    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void updateTemplateRecords(TemplateRecords templateRecords);
	
	public TemplateRecords selectById(String id);

}

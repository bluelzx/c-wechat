package com.lvtu.wechat.common.service.productorder;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.productorder.PushTemplate;
import com.lvtu.wechat.common.vo.back.PushTemplateConditionVo;


/**
 * @ClassName: IPushTemplateService
 * @Description: 推送消息模板service
 * @author zhengchongxiang
 * @date 2016-8-26 下午2:10:20
 */
@RemoteService("pushTemplateService")
public interface IPushTemplateService {
	
	/** 
	* @Title: get 
	* @Description: 获取所有的活动模板信息 
	* @param @param vo
	* @return PageInfo<PushTemplate>    返回类型 
	*/
	public PageInfo<PushTemplate> get(PushTemplateConditionVo vo);
	
	/** 
	* @Title: getByPrimary 
	* @Description: 很据主键id查询活动模板信息
	* @param @param id
	* @return PushTemplate    返回类型 
	*/
	public PushTemplate getByPrimary(String id);
	
	/** 
	* @Title: saveOrUpdate 
	* @Description: 新增或修改活动模板信息 
	* @param @param pushTemplate
	* @return boolean    返回类型 
	*/
	public boolean saveOrUpdate(PushTemplate pushTemplate);
	
	/** 
	* @Title: update 
	* @Description: 更新活动模板信息 
	* @param @param pushTemplate
	* @return boolean    返回类型 
	*/
	public boolean update(PushTemplate pushTemplate);
	
}

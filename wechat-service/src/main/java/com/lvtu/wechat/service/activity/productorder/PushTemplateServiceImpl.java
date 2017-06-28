package com.lvtu.wechat.service.activity.productorder;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.productorder.PushTemplate;
import com.lvtu.wechat.common.service.productorder.IPushTemplateService;
import com.lvtu.wechat.common.vo.back.PushTemplateConditionVo;
import com.lvtu.wechat.dao.productorder.dao.PushTemplateDao;
import com.mysql.jdbc.StringUtils;

/** 
* @ClassName: PushTemplateServiceImpl 
* @Description: 推送模板
* @author zhengchongxiang
* @date 2016-8-29 下午1:41:50  
*/
@HessianService("pushTemplateService")
@Service("pushTemplateService")
@Transactional(readOnly = true)
public class PushTemplateServiceImpl implements IPushTemplateService {
	
	Logger logger = Logger.getLogger(PushTemplateServiceImpl.class);
	
	@Autowired
	private PushTemplateDao pushTemplateDao;

	@Override
	@Transactional(readOnly = false)
	public boolean saveOrUpdate(PushTemplate pushTemplate) {
		if(StringUtils.isNullOrEmpty(pushTemplate.getId())){
			pushTemplate.preInsert();
			pushTemplate.setCreateTime(new Date());
			pushTemplate.setSendTime(new Date());
			pushTemplate = pushTemplateDao.save(pushTemplate);
			if(pushTemplate != null){
				return true;
			}
		}else{
			//更新操作
			Integer num = pushTemplateDao.update(pushTemplate);
			if(num>0){
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean update(PushTemplate pushTemplate) {
		Integer num = pushTemplateDao.update(pushTemplate);
		if(num > 0){
			return true;
		}
		return false;
	}

	@Override
	public PageInfo<PushTemplate> get(PushTemplateConditionVo vo) {
		PageHelper.startPage(vo.getPage(), vo.getPageSize());
		List<PushTemplate> list = pushTemplateDao.get(vo);
		PageInfo<PushTemplate> pageInfo = new PageInfo<PushTemplate>(list);
		return pageInfo;
	}

	@Override
	public PushTemplate getByPrimary(String id) {		
		return pushTemplateDao.getByPrimary(id);
	}
}

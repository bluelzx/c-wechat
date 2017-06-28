package com.lvtu.wechat.service.template;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.template.Template;
import com.lvtu.wechat.common.model.template.TemplateRecords;
import com.lvtu.wechat.common.service.template.ITemplateService;
import com.lvtu.wechat.common.utils.StringUtil;
import com.lvtu.wechat.common.vo.back.TemplateConditionVo;
import com.lvtu.wechat.dao.template.dao.TemplateDao;
import com.lvtu.wechat.dao.template.dao.TemplateRecordsDao;

@HessianService("templateService")
@Service("templateService")
@Transactional(readOnly = true)
public class TemplateServiceImpl implements ITemplateService {

	@Autowired
	private TemplateDao templateDao;
	
	@Autowired
	private TemplateRecordsDao templateRecordsDao;

	@Override
	@Transactional(readOnly = false)
	public boolean saveOrUpdateTemplate(Template template) {
		if (StringUtils.isEmpty(template.getId())) {
			template.preInsert();
			template.setCreateTime(new Date());
			template = templateDao.insert(template);
			if (!StringUtils.isEmpty(template.getId())) {
				return true;
			}
		} else {
			Integer temp = templateDao.update(template);
			if (temp > 0) {
				return true;
			}
		}

		return false;
	}

	@Override
	public PageInfo<Template> selectTemplates(TemplateConditionVo params) {
		PageHelper.startPage(params.getPage(), params.getPageSize());
		List<Template> list = templateDao.select(params);
		PageInfo<Template> page = new PageInfo<Template>(list);
		return page;
	}

	@Override
	public Template selectByPrimary(String id) {
		Template template = templateDao.selectById(id);
		if (template != null && StringUtil.isNotEmptyString(template.getId())) {
			return template;
		}
		return null;
	}

	@Override
	public Template selectByTemplateId(String templateId) {
		Template template = templateDao.selectByTemplateId(templateId);
		if (template != null && StringUtil.isNotEmptyString(template.getId())) {
			return template;
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public boolean save(TemplateRecords templateRecords) {
		templateRecords.preInsert();
		templateRecords.setNowRetryCount(0);
		templateRecords.setCreateTime(new Date());
		templateRecords = templateRecordsDao.insert(templateRecords);
		if(templateRecords != null && StringUtil.isNotEmptyString(templateRecords.getId())){
			return true;
		}
		return false;
	}

	@Override
	public PageInfo<TemplateRecords> selectTemplateRecords(TemplateConditionVo params) {		
		PageHelper.startPage(params.getPage(), params.getPageSize());
		List<TemplateRecords> list = templateRecordsDao.select(params);
		PageInfo<TemplateRecords> page = new PageInfo<TemplateRecords>(list);
		return page;
	}

	@Override
	public List<TemplateRecords> selectByRetry() {
		return templateRecordsDao.selectByRetry();
	}

	@Override
	@Transactional(readOnly = false)
	public void updateTemplateRecords(TemplateRecords templateRecords) {
		templateRecordsDao.update(templateRecords);
	}

	@Override
	public TemplateRecords selectById(String id) {
		if(StringUtils.isNotBlank(id)){
			return templateRecordsDao.selectById(id);
		}
		return null;
	}

}

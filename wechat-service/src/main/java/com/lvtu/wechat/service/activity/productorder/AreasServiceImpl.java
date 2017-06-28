package com.lvtu.wechat.service.activity.productorder;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.productorder.AreaGroup;
import com.lvtu.wechat.common.model.productorder.City;
import com.lvtu.wechat.common.model.productorder.Province;
import com.lvtu.wechat.common.service.productorder.IAreasService;
import com.lvtu.wechat.common.utils.StringUtil;
import com.lvtu.wechat.dao.productorder.dao.AreaGroupDao;
import com.lvtu.wechat.dao.productorder.dao.CityDao;
import com.lvtu.wechat.dao.productorder.dao.ProvinceDao;

@HessianService("areasService")
@Service("areasService")
@Transactional(readOnly = true)
public class AreasServiceImpl implements IAreasService {

	@Autowired
	private CityDao cityDao;

	@Autowired
	private AreaGroupDao areaGroupDao;

	@Autowired
	private ProvinceDao provinceDao;

	@Override
	public City selectByName(String name) {
		if (StringUtil.isNotEmptyString(name)) {
			return cityDao.selectByName(name);
		}
		return null;
	}

	@Override
	public List<AreaGroup> selectAreaGroups() {
		return areaGroupDao.selectAll();
	}

	@Override
	@Transactional(readOnly = false)
	public void addAreaGroup(AreaGroup areaGroup) {
		areaGroup.preInsert();
		areaGroup.setCreateTime(new Date());
		areaGroupDao.save(areaGroup);

	}

	@Override
	public List<Province> selectProvinces() {
		return provinceDao.selectAll();
	}

	@Override
	public List<City> selectCityByParentId(Integer id) {
		return cityDao.selectByParentId(id);
	}

	@Override
	public Integer selectProvinceByName(String name) {
		if (StringUtil.isNotEmptyString(name)) {
			return provinceDao.selectByName(name);
		}
		return null;
	}

	@Override
	public City selectByLikeName(String name, Integer provinceId) {
		City city = new City();
		city.setProvinceId(provinceId);
		city.setName(name);
		return cityDao.selectByLikeName(city);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(AreaGroup areaGroup) {
		areaGroupDao.update(areaGroup);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(String id) {
		if (StringUtil.isNotEmptyString(id)) {
			areaGroupDao.delete(id);
		}
	}

	@Override
	public AreaGroup selectAreaGroupById(String id) {
		if (StringUtil.isNotEmptyString(id)) {
			return areaGroupDao.selectById(id);
		}
		return null;
	}

	@Override
	public List<AreaGroup> selectAreaGroupByIds(String ids) {
		if (StringUtil.isNotEmptyString(ids)) {
			return areaGroupDao.selectByIds(ids);
		}
		return null;
	}

}

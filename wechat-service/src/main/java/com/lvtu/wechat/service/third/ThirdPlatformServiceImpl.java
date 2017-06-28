package com.lvtu.wechat.service.third;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lvtu.wechat.common.hessian.HessianService;
import com.lvtu.wechat.common.model.third.ThirdPlatform;
import com.lvtu.wechat.common.service.third.ThirdPlatformService;
import com.lvtu.wechat.dao.third.dao.ThirdPlatformDao;

@HessianService("remoteThirdPlatformService")
@Service("thirdPlatformService")
@Transactional(readOnly = true)
public class ThirdPlatformServiceImpl implements ThirdPlatformService {

	@Autowired
	private ThirdPlatformDao platformDao;
	
	@Override
	//@Cacheable(value = "3rdCache", key = "#id")
	public ThirdPlatform findPlatformById(String id) {
		return platformDao.getByPrimaryKey(id);
	}

	@Override
	//@Cacheable(value = "3rdCache", key = "#apiUrl")
	public ThirdPlatform findPlatformByUrl(String apiUrl) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("apiUrl", apiUrl);
		List<ThirdPlatform> platforms = platformDao.findList(params);
		if (platforms == null || platforms.size() == 0)
			return null;
		else
			return platforms.get(0);
	}

	@Override
	//@Cacheable(value = "3rdCache", key = "#platform.toString()")
	public List<ThirdPlatform> findPlatforms(Map<String,Object> params) {
		List<ThirdPlatform> platforms = platformDao.findList(params);
		return platforms;
	}

	@Override
	//@CacheEvict(value = "3rdCache", allEntries = true)
	@Transactional(readOnly = false)
	public void delete(String id) {
		platformDao.deleteById(id);
	}

	@Override
	//@CacheEvict(value = "3rdCache", allEntries = true)
	@Transactional(readOnly = false)
	public void updateUseable(ThirdPlatform platform) {
		platformDao.update(platform);
	}

	@Override
	//@CacheEvict(value = "3rdCache", allEntries = true)
	@Transactional(readOnly = false)
	public void savePlatform(ThirdPlatform platform) {
		if(StringUtils.isBlank(platform.getId())) {
			platform.preInsert();
			platformDao.insert(platform);
		} else {
			platformDao.update(platform);
		}
	}
}

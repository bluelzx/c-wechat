package com.lvtu.wechat.common.service.third;

import java.util.List;
import java.util.Map;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.third.ThirdPlatform;


/**
 * 第三方平台服务类接口
 * @author xuyao
 *
 */
@RemoteService("remoteThirdPlatformService")
public interface ThirdPlatformService {
	
	/**
	 * 根据主键ID获取
	 * @param platform
	 * @return
	 */
	public ThirdPlatform findPlatformById(String id);
	
	/**
	 * 根据url获取
	 * @param platform
	 * @return
	 */
	public ThirdPlatform findPlatformByUrl(String apiUrl);
	
	/**
	 * 查询第三方平台列表
	 * @param platform
	 * @return
	 */
	public List<ThirdPlatform> findPlatforms(Map<String,Object> params);
	
	/**
	 * 删除第三方平台
	 * @param platform
	 */
	public void delete(String id);
	
	/**
	 * 更新平台是否可用
	 * @param platform
	 */
	public void updateUseable(ThirdPlatform platform);
	
	/**
	 * 保存第三方平台
	 * @param platform
	 */
	public void savePlatform(ThirdPlatform platform);
}

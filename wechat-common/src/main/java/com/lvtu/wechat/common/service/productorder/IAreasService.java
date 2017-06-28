package com.lvtu.wechat.common.service.productorder;

import java.util.List;

import com.lvtu.wechat.common.hessian.RemoteService;
import com.lvtu.wechat.common.model.productorder.AreaGroup;
import com.lvtu.wechat.common.model.productorder.City;
import com.lvtu.wechat.common.model.productorder.Province;

/** 
* @ClassName: IAreasService 
* @Description: 所属地区操作service
* @author zhengchongxiang
* @date 2017-3-13 下午3:53:29  
*/
@RemoteService("areasService")
public interface IAreasService {
	
	/** 
	* @Title: selectByName 
	* @Description: 根据城市名称查询所属城市信息 
	* @param @param name
	* @param @return    设定文件 
	* @return City    返回类型 
	* @throws 
	*/
	public City selectByName(String name);
	
	/** 
	* @Title: selectAreaGroups 
	* @Description: 查询所有分组信息 
	* @param @return    设定文件 
	* @return List<AreaGroup>    返回类型 
	* @throws 
	*/
	public List<AreaGroup> selectAreaGroups();
	
	/** 
	* @Title: addAreaGroup 
	* @Description: 新增分组 
	* @param @param areaGroup    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void addAreaGroup(AreaGroup areaGroup);
	
	/** 
	* @Title: selectProvinces 
	* @Description: 查询所有省份信息 
	* @param @return    设定文件 
	* @return List<Province>    返回类型 
	* @throws 
	*/
	public List<Province> selectProvinces(); 
	
	/** 
	* @Title: selectCityByParentId 
	* @Description: 查询指定省份下的所有城市信息 
	* @param @param id
	* @param @return    设定文件 
	* @return List<City>    返回类型 
	* @throws 
	*/
	public List<City> selectCityByParentId(Integer id);
	
	/** 
	* @Title: selectProvinceByName 
	* @Description: 根据名称模糊查询省份信息 
	* @param @param name
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @throws 
	*/
	public Integer selectProvinceByName(String name);
	
	/** 
	* @Title: selectByLikeName 
	* @Description: 根据名称模糊查询城市信息 
	* @param @param name
	* @param @param provinceId
	* @param @return    设定文件 
	* @return City    返回类型 
	* @throws 
	*/
	public City selectByLikeName(String name,Integer provinceId);
	
	/** 
	* @Title: update 
	* @Description: 编辑地区分组 
	* @param @param areaGroup    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void update(AreaGroup areaGroup);
	
	/** 
	* @Title: delete 
	* @Description: 删除地区分组
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void delete(String id);
	
	/** 
	* @Title: selectAreaGroupById 
	* @Description: 根据主键id查询地区分组
	* @param @param id
	* @param @return    设定文件 
	* @return AreaGroup    返回类型 
	* @throws 
	*/
	public AreaGroup selectAreaGroupById(String id);
	
	/** 
	* @Title: selectAreaGroupByIds 
	* @Description: 批量查询地区分组
	* @param @param ids
	* @param @return    设定文件 
	* @return List<AreaGroup>    返回类型 
	* @throws 
	*/
	public List<AreaGroup> selectAreaGroupByIds(String ids);

}

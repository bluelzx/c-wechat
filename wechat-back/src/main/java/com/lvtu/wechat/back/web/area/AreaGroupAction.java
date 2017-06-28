package com.lvtu.wechat.back.web.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.productorder.AreaGroup;
import com.lvtu.wechat.common.model.productorder.City;
import com.lvtu.wechat.common.model.productorder.Province;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.productorder.IAreasService;
import com.lvtu.wechat.common.service.productorder.IProductOrderService;
import com.lvtu.wechat.common.service.weixin.IWechatUserService;
import com.lvtu.wechat.common.utils.StringUtil;
import com.lvtu.wechat.common.vo.back.ProductOrderConditionVo;

/** 
* @ClassName: AreaAction 
* @Description: 甩尾2.0新增地区分组
* @author zhengchongxiang
* @date 2017-3-13 下午4:01:00  
*/
@Controller
@RequestMapping("${adminPath}/areas")
public class AreaGroupAction extends BaseActionSupport {

	@Autowired
	private IAreasService areasService;
	
	@Autowired
	private IProductOrderService productOrderService;
	
	@Autowired
	private IWechatUserService wechatUserService;
	
	@Autowired
	private IAreasService cityService;

	/** 
	* @Title: index 
	* @Description: 地区分组首页 
	* @param @param model
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("index")
	public String index(Model model) {
		List<AreaGroup> list = areasService.selectAreaGroups();
		model.addAttribute("groups", list);
		List<Province> list1 = areasService.selectProvinces();
		model.addAttribute("provinces", list1);
		return "productorder/areas";
	}
	
	/** 
	* @Title: addGroup 
	* @Description: 新增分组 
	* @param @param areaGroup
	* @param @param redirectAttrs
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("add")
	public String addGroup(AreaGroup areaGroup,RedirectAttributes redirectAttrs) {
		if (beanValidator(redirectAttrs, areaGroup)) {
			areasService.addAreaGroup(areaGroup);
			addMessage(redirectAttrs, "分组添加成功！");
		}
		return "redirect:" + adminPath + "/areas/index";
	}
	
	/** 
	* @Title: edit 
	* @Description: 地区分组编辑页面 
	* @param @param model
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("edit")
	public String edit(Model model){
		List<Province> list = areasService.selectProvinces();
		model.addAttribute("provinces", list);
		return "productorder/edit";
	}
	
	/** 
	* @Title: getCity 
	* @Description: 根据省份Id查询下面所有城市信息 
	* @param @param id
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("getCity")
	@ResponseBody
	public Object getCity(Integer id,String groupId){
		Map<String,Object> result = new HashMap<String,Object>();		
		if (null != id){
			 List<City> citys = areasService.selectCityByParentId(id);	
			 result.put("citys", citys);
			 AreaGroup areaGroup = areasService.selectAreaGroupById(groupId);
			 result.put("areaGroup", areaGroup);
		}
		return result;
	}
	
	/** 
	* @Title: saveEdit 
	* @Description: 保存编辑 
	* @param @param areaGroup
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("saveEdit")
	public String saveEdit(AreaGroup areaGroup){
		areasService.update(areaGroup);
		return "redirect:" + adminPath + "/areas/index";
	}
	
	/** 
	* @Title: delete 
	* @Description: 删除地区分组 
	* @param @param id
	* @param @param redirectAttrs
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws 
	*/
	@RequestMapping("delete")
	public String delete(String id,RedirectAttributes redirectAttrs){
		if(StringUtil.isNotEmptyString(id)){
			areasService.delete(id);
			addMessage(redirectAttrs, "删除成功！");
		}
		return "redirect:" + adminPath + "/areas/index";
	}
	
	/** 
	* @Title: change 
	* @Description: 已订阅用户信息同步 
	* @param @return    设定文件 
	* @return Object    返回类型 
	* @throws 
	*/
	@RequestMapping("change")
	@ResponseBody
	public Object change() {
		List<String> list = productOrderService.selectAllOpenids();
		List<ProductOrderConditionVo> listVo = new ArrayList<ProductOrderConditionVo>();
		for (String openid : list) {
			WechatUser user = wechatUserService.getByOpenid(openid);
			if (null != user && user.getCity() != null) {
				if (user.getProvince().equals("上海")
						|| user.getProvince().equals("北京")
						|| user.getProvince().equals("天津")
						|| user.getProvince().equals("重庆")
						|| user.getProvince().equals("香港")
						|| user.getProvince().equals("澳门")
						|| user.getProvince().equals("台湾")) {
					City city = cityService.selectByName(user.getProvince());
					ProductOrderConditionVo newVo = new ProductOrderConditionVo();
					newVo.setCityId(city.getCode());
					newVo.setProvinceId(city.getProvinceId());
					newVo.setOpenid(openid);
					listVo.add(newVo);
				} else {
					City city = cityService.selectByName(user.getCity());
					if (null != city) {
						ProductOrderConditionVo newVo = new ProductOrderConditionVo();
						newVo.setCityId(city.getCode());
						newVo.setProvinceId(city.getProvinceId());
						newVo.setOpenid(openid);
						listVo.add(newVo);
					} else {
						ProductOrderConditionVo newVo = new ProductOrderConditionVo();
						newVo.setCityId(392);
						newVo.setProvinceId(35);
						newVo.setOpenid(openid);
						listVo.add(newVo);
					}
				}
			}
		}
		productOrderService.updateList(listVo);
		return "success";
	}

}

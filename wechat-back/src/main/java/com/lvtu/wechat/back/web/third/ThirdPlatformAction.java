package com.lvtu.wechat.back.web.third;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lvtu.wechat.common.enums.ForwardType;
import com.lvtu.wechat.common.model.third.ThirdPlatform;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.service.third.ThirdPlatformService;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.MemcachedUtil;

/**
 * 第三方平台管理Controller
 * @author xuyao
 *
 */
@Controller
public class ThirdPlatformAction extends BaseActionSupport {
	
	@Autowired
	private ThirdPlatformService platformService;
	
	/**
	 * 清除第三方平台缓存，
	 * 缓存按转发类型进行缓存
	 * @return
	 */
	@RequestMapping("${adminPath}/3rd/clearCache")
	public String clearCache(RedirectAttributes redirectAttrs) {
		MemcachedUtil mem = MemcachedUtil.getInstance();
		for(ForwardType type : ForwardType.values()) {
			mem.remove(Constants.THIRD_PLATFORMS_CACHE_KEY_PREF + type.name());
		}
		addMessage(redirectAttrs, "清除第三方平台缓存成功!");

		return "redirect:" + adminPath + "/3rd/list";
	}
	
	@RequiresPermissions("3rd:platform:view")
	@RequestMapping("${adminPath}/3rd/list")
	public String list(Model model) {
		List<ThirdPlatform> platforms = platformService.findPlatforms(new HashMap<String, Object>());
		model.addAttribute("platforms", platforms);
		
		return "third/3rdPlatformList";
	}

	@RequiresPermissions("3rd:platform:edit")
	@RequestMapping("${adminPath}/3rd/delete")
	public String deletePlatform(String id, RedirectAttributes redirectAttrs) {
		platformService.delete(id);
		addMessage(redirectAttrs, "删除第三方平台成功！");

		return "redirect:" + adminPath + "/3rd/list";
	}

	@RequiresPermissions("3rd:platform:edit")
	@RequestMapping("${adminPath}/3rd/usable")
	public String useable(ThirdPlatform platform, RedirectAttributes redirectAttrs) {
		platformService.updateUseable(platform);
		if(platform.isUseable()) {
			addMessage(redirectAttrs, "启用第三方平台成功");
		} else {
			addMessage(redirectAttrs, "禁用第三方平台成功");
		}
		
		return "redirect:" + adminPath + "/3rd/list";
	}
	
	@RequestMapping("${adminPath}/3rd/new")
	public String newPlatform(Model model) {
		model.addAttribute("types", ForwardType.values());

		return "/third/PlatformForm";
	}

	@RequestMapping("${adminPath}/3rd/edit")
	public String editPlatform(String id, Model model) {
		ThirdPlatform platform = platformService.findPlatformById(id);
		model.addAttribute("platform", platform);
		model.addAttribute("types", ForwardType.values());

		return "/third/PlatformForm";
	}

	/**
	 * 保存第三方平台
	 * @return
	 */
	@RequiresPermissions("3rd:platform:edit")
	@RequestMapping("${adminPath}/3rd/save")
	public String save(ThirdPlatform platform, Model model, RedirectAttributes redirectAttrs) {
		//数据合法性校验
		boolean hasError = false;
		if (!beanValidator(model, platform)) {
			hasError = true;
		} else if (!platform.getDomain().matches(Constants.DOMAIN_REG)) {
			hasError = true;
			addMessage(model, "保存第三方平台'" + platform.getName() + "'失败，安全域名格式不正确");
		} else if (!platform.getApiUrl().matches(Constants.URL_REG)) {
			hasError = true;
			addMessage(model, "保存第三方平台'" + platform.getName() + "'失败，服务器URL格式不正确");
		} else if (platform.getForwardType() == ForwardType.KEYWORD
				&& StringUtils.isBlank(platform.getForwardKeyword())) {
			hasError = true;
			addMessage(model, "保存第三方平台'" + platform.getName() + "'失败，转发类型为关键字转发时，关键字不能为空");
		}
		//校验数据是否重复
		if(!hasError) { 
			ThirdPlatform existPlatform = platformService.findPlatformByUrl(platform.getApiUrl());
			if ((StringUtils.isBlank(platform.getId()) && existPlatform != null) 
					|| (StringUtils.isNotBlank(platform.getId()) && existPlatform != null && !platform.getId().equals(existPlatform.getId()))) {
				hasError = true;
				addMessage(model, "保存第三方平台'" + platform.getName() + "'失败，该第三方平台已经存在");
			} else if(platform.getForwardType() == ForwardType.ALL) {
				Map<String, Object> parames = new HashMap<String, Object>();
				parames.put("forwardType", ForwardType.ALL);
				List<ThirdPlatform> platforms = platformService.findPlatforms(parames);
				if (platforms != null && platforms.size() > 0) {
					if (StringUtils.isBlank(platform.getId()) || (StringUtils.isNotBlank(platform.getId())
							&& !platforms.get(0).getId().equals(platform.getId()))) {
						hasError = true;
						addMessage(model, "保存第三方平台'" + platform.getName() + "'失败，只能有一个平台转发类型为‘全部转发’。");
					}
				}
			} else if(platform.getForwardType() == ForwardType.KEYWORD && !checkKeyword(platform)) {
				hasError = true;
				addMessage(model, "保存第三方平台'" + platform.getName() + "'失败，与已有关键字重复。");
			}
		}
		//数据校验全部通过
		if(!hasError) {
			platformService.savePlatform(platform);
			addMessage(redirectAttrs, "保存第三方平台'" + platform.getName() + "'成功");
			return "redirect:" + adminPath + "/3rd/list";
		}
		//编辑和添加跳到不同页面
		if(StringUtils.isBlank(platform.getId())) {
			model.addAttribute("platform", platform);
			return newPlatform(model);
		} else {
			return editPlatform(platform.getId(), model);
		}
	}
	
	/**
	 * 检查关键字是否重复
	 * @param keywords
	 * @return
	 */
	private boolean checkKeyword(ThirdPlatform platform) {
		Set<String> exsitWords = new HashSet<String>();
		// 读取已经存在的关键字
		Map<String, Object> parames = new HashMap<String, Object>();
		parames.put("forwardType", ForwardType.KEYWORD);
		List<ThirdPlatform> platforms = platformService.findPlatforms(parames);
		for (ThirdPlatform _platform : platforms) {
			if (_platform.getId().equals(platform.getId()))
				continue;
			String[] words = _platform.getForwardKeyword().split(",");
			for (String word : words) {
				exsitWords.add(word);
			}
		}
		// 检查是否有重复关键字
		String[] newKeywords = platform.getForwardKeyword().split(",");
		for (String keyword : newKeywords) {
			if (exsitWords.contains(keyword))
				return false;
		}

		return true;
	}
}

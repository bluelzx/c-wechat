package com.lvtu.wechat.front.web.advertising;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvmama.comm.utils.MemcachedUtil;
import com.lvtu.wechat.common.model.advertising.AdvertisingClicks;
import com.lvtu.wechat.common.service.advertising.IAdvertisingClicksService;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.vo.back.AdvertisingClicksConditionVo;
import com.lvtu.wechat.front.base.BaseController;

/**
 * 广告位点击量
 * @author zhengchongxiang
 */
@Controller("advertisingClicksCOntroller")
@RequestMapping("/advertising")
public class AdvertisingClicksController extends BaseController {
	
	@Autowired
	private IAdvertisingClicksService advertisingClicksService;
	
	/**
	 * 新增点击量事件
	 */
	@ResponseBody
	@RequestMapping("/click")
	public Object advertisingClick(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> result = getResultMap();
		String advertisingId = request.getParameter("advertisingId");
		if (StringUtils.isBlank(advertisingId)) {
			result.put("code", -1);
			result.put("msg", "参数错误！");
			return result;
		}
		Integer pageViews = 0;
		Integer uniqueVisitors = 0;
		Date date = new Date();
		AdvertisingClicks advertisingClicks = new AdvertisingClicks();
		AdvertisingClicksConditionVo advertisingClicksConditionVo = new AdvertisingClicksConditionVo();
		advertisingClicksConditionVo.setAdvertisingId(advertisingId);
		advertisingClicksConditionVo.setClickDate(date);
		List<AdvertisingClicks> list = advertisingClicksService
				.queryAdvertisingClicksList(advertisingClicksConditionVo);
		if (list != null && list.size() > 0) {
			advertisingClicks = list.get(0);
			pageViews = advertisingClicks.getPageViews();
			uniqueVisitors = advertisingClicks.getUniqueVisitors();
		}
		String userIp = getRemoteAddr(request);// 得到ip地址
		String key = advertisingId + userIp;// 广告编号和ip地址做为uv标示
		MemcachedUtil mem = MemcachedUtil.getInstance();
		String value = (String) mem.get(key);
		if (StringUtils.isBlank(value)) {
			mem.set(key, (int) DateUtils.getTodayOverSecond(), date.toString());
			++uniqueVisitors;// uv指数+1
		}
		++pageViews;// pv指数+1
		advertisingClicks.setAdvertisingId(advertisingId);
		advertisingClicks.setPageViews(pageViews);
		advertisingClicks.setUniqueVisitors(uniqueVisitors);
		advertisingClicks.setClickDate(date);
		advertisingClicksService.saveAdvertisingClicks(advertisingClicks);
		return result;
	}

}

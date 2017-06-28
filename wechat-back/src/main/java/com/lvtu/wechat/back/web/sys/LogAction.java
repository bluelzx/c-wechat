package com.lvtu.wechat.back.web.sys;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.sys.Log;
import com.lvtu.wechat.common.service.sys.LogService;
import com.lvtu.wechat.common.vo.back.LogConditionVO;

/**
 * 日志查询action
 * 
 * @author xuyao
 *
 */
@Controller
public class LogAction extends BaseActionSupport {

	@Autowired
	private LogService logService;

	@RequiresPermissions("sys:log:view")
	@RequestMapping("${adminPath}/log/list")
	public String logList(LogConditionVO condition, Model model) {
		PageInfo<Log> page = logService.findList(condition);
		model.addAttribute("page", page);
		model.addAttribute("logs", page.getList());
		model.addAttribute("condition", condition);

		return "sys/logList";
	}
}

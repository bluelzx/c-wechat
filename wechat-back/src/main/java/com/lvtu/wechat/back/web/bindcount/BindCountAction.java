package com.lvtu.wechat.back.web.bindcount;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.ExcelUtil;
import com.lvtu.wechat.common.enums.BindCountType;
import com.lvtu.wechat.common.model.productorder.ExportOrders;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.productorder.IProductOrderService;
import com.lvtu.wechat.common.service.weixin.IWechatUserService;
import com.lvtu.wechat.common.vo.back.BindCountConditionVo;

/**
 * 微信绑定统计
 * @author majun
 *
 */
@Controller
@RequestMapping("${adminPath}/bindCount")
public class BindCountAction extends BaseActionSupport {

	@Autowired
	private IWechatUserService wechatUserService;
	
	@Autowired
	private IProductOrderService productOrderService;
	
	/**
	 * 绑定统计初始主页面
	 * @param vo
	 * @param model
	 * @param productOrderVo
	 * @param state
	 * @param tab
	 * @return
	 */
	@RequestMapping("index")
	public String getBindCountIndex(BindCountConditionVo bindCountConditionVo, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		
		BindCountType[] statusTypeArr = BindCountType.values();
		List<BindCountType> statusList = Arrays.asList(statusTypeArr);
		if (BindCountType.SUM.getValue().equals(bindCountConditionVo.getChannel())) {
			 bindCountConditionVo.setChannel(null);
	    }
		
		PageInfo<WechatUser> pageInfo = wechatUserService.queryBindUserList(bindCountConditionVo);
	    model.addAttribute("conditionManage", bindCountConditionVo);
		model.addAttribute("bindUserList", pageInfo.getList());
		model.addAttribute("pageManage", pageInfo);
		model.addAttribute("statusList", statusList);
		
		return "/bindcount/index";
	}
	
	
	/**
	 * 导出绑定数据
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("export")
	public void exportExcel(BindCountConditionVo bindCountConditionVo, String channel, HttpServletRequest request, HttpServletResponse response){
		bindCountConditionVo.setChannel(channel);
		//获取所有统计数据
		List<ExportOrders> list = wechatUserService.selectByExport(bindCountConditionVo);
        String[] headers = { "用户openid", "绑定时间", "绑定渠道[0表示微信,1表示客服]"};
        String codedFileName = null;  
        OutputStream fOut = null; 
        try {
         // 生成提示信息，  
            response.setContentType("application/vnd.ms-excel");  
         // 进行转码，使其支持中文文件名  
            codedFileName = java.net.URLEncoder.encode("绑定统计用户信息", "UTF-8");  
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            logger.info("导出数据条数：" + list.size());
            ExcelUtil.exportExcel(headers, list, fOut, "yyyy-MM-dd HH:mm:ss");
        }catch(Exception e){
        	e.printStackTrace();  
        	logger.info("导出数据异常");
        }				
	}

}

package com.lvtu.wechat.back.web.record;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.common.model.activity.signflow.Flow;
import com.lvtu.wechat.common.model.activity.signflow.FlowExchange;
import com.lvtu.wechat.common.service.activity.flowrecharge.IFlowRechargeService;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowService;
import com.lvtu.wechat.common.vo.back.FlowRecordConditionVo;

@Controller
@RequestMapping("${adminPath}/flowRecord")
public class FlowRecordAction extends BaseActionSupport {


    @Autowired
    private IFlowRechargeService flowRechargeService;
    
    @Autowired
    private IWxFlowService wxFlowService;

    /**
     * @Title: index
     * @Description: TODO(查询流量充值页面，分页)
     * @param @param flowRechargeConditionVo
     * @param @param model
     * @param @param request
     * @param @param response
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    @RequestMapping("/index")
    public String index(FlowRecordConditionVo flowRecordConditionVo, Model model, HttpServletRequest request,
        HttpServletResponse response) {
        model.addAttribute("conditionManage", flowRecordConditionVo);
        PageInfo<FlowExchange> pageInfo = flowRechargeService.queryWxFlowExchange(flowRecordConditionVo);
        model.addAttribute("flowExchangeList", pageInfo.getList());
        model.addAttribute("pageManage", pageInfo);
        if (!StringUtils.isBlank(flowRecordConditionVo.getOpenid())) {
            Flow flow = wxFlowService.getFlow(flowRecordConditionVo.getOpenid());
            model.addAttribute("flow", flow);
        }
        return "/record/flowExchangeRecord";
    }
}

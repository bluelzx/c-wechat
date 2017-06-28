package com.lvtu.wechat.back.web.activity.flowrecharge;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.Constants;
import com.lvtu.wechat.back.utils.ExcelUtil;
import com.lvtu.wechat.common.model.activity.flowrecharge.ExportFlowRechargeRecord;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRecharge;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRechargeRecord;
import com.lvtu.wechat.common.model.activity.flowrecharge.FlowRechargeRecordInfo;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.flowrecharge.IFlowRechargeService;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowService;
import com.lvtu.wechat.common.service.weixin.IWechatUserService;
import com.lvtu.wechat.common.utils.AuthSmsCodeUtils;
import com.lvtu.wechat.common.utils.CookieUtils;
import com.lvtu.wechat.common.utils.JSONUtil;
import com.lvtu.wechat.common.vo.back.FlowRechargeConditionVo;
import com.lvtu.wechat.common.vo.back.FlowRecordConditionVo;

@Controller
@RequestMapping("${adminPath}/activity")
public class FlowRechargeAction extends BaseActionSupport {

    @Autowired
    private IWechatUserService wechatUserService;

    @Autowired
    private IFlowRechargeService flowRechargeService;

    @Autowired
    private IWxFlowService wechatFlowService;

    /**
     * @Title: index
     * @Description: TODO(手动充值流量首页面，分页)
     * @param @param flowRechargeConditionVo
     * @param @param model
     * @param @param request
     * @param @param response
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    @RequestMapping("/flowrecharge")
    public String index(FlowRechargeConditionVo flowRechargeConditionVo, Model model, HttpServletRequest request,
        HttpServletResponse response) {
        model.addAttribute("conditionManage", flowRechargeConditionVo);
        PageInfo<FlowRecharge> pageInfo = flowRechargeService.queryFlowRechargeList(flowRechargeConditionVo);
        model.addAttribute("FlowRechargeList", pageInfo.getList());
        model.addAttribute("pageManage", pageInfo);
        return "/activity/flowrecharge/flowRechargeManage";
    }

    /**
     * @Title: newflow
     * @Description: TODO(显示系统当前使用者)
     * @param @param model
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    @RequestMapping("/flowrecharge/newflow")
    public String newflow(Model model) {
        String mobile = Constants.getConfig("telephone");

        model.addAttribute("currentUser", getUser());
        model.addAttribute("telephonenum", mobile);

        return "/activity/flowrecharge/flowRechargeAdd";
    }

    /**
     * 
    * @Title: addflow 
    * @Description: TODO(这里用一句话描述这个方法的作用) 
    * @param @param request
    * @param @param response
    * @param @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @throws
     */
    @RequestMapping("/flowRecharge/addFlow")
    @ResponseBody
    public Map<String, Object> addflow(HttpServletRequest request, HttpServletResponse response, String name, String openids, String operator, String flows, String code, String remark) {
        Map<String, Object> result = getResult();
            //运营主管(陈梁)手机号telephone
            String mobile = Constants.getConfig("telephone");
            String smsCode = code;
            logger.info("输入的短信验证码smsCode为：" + smsCode);

         if (AuthSmsCodeUtils.checkSMSCodeRedis(mobile, smsCode)) {
             logger.info("校验验证码成功！验证码为" + smsCode);
             String flowopenids = openids;
             String str = dealString(flowopenids);
             String[] openidArry = str.split(",");
             FlowRecharge flowRecharge = new FlowRecharge();
             flowRecharge.setName(name);
             flowRecharge.setOperator(operator);
             flowRecharge.setRechargeFlow(Integer.parseInt(flows));
             flowRecharge.setRemark(remark);
             flowRecharge.preInsert();
             logger.info("充值流量活动表赋值成功！" + flowRecharge);
             List<FlowRechargeRecord> flowRechargeRecordList = new ArrayList<FlowRechargeRecord>();
             FlowRechargeRecord flowRechargeRecord = null;

             for (int i = 0; i < openidArry.length; i++) {
                 String openid = openidArry[i];
                 logger.info("查找手动添加流量的openid: " + openid );
                 WechatUser wechatUser = wechatUserService.getByOpenid(openid);
                 if (wechatUser == null) {
                     logger.info("手动添加流量的openid: " + openid + "在对应的用户不存在！");
                     result.put("code", "-1");
                     result.put("msg", "手动添加流量的openid: " + openid + "在对应的用户不存在！");
                     return result;
                 }
                 else {
                     flowRechargeRecord = new FlowRechargeRecord();
                     flowRechargeRecord.preInsert();
                     flowRechargeRecord.setFlowRechargeId(flowRecharge.getId());
                     flowRechargeRecord.setOpenid(openid);
                     flowRechargeRecord.setRechargeFlow(Integer.parseInt(flows));
                     flowRechargeRecordList.add(flowRechargeRecord);
                     logger.info("添加充值流量记录表成功！");
                 }
             }
             logger.info("循环查找openid结束!");
             flowRechargeService.addflowRechargeRecord(flowRecharge, flowRechargeRecordList);
             logger.info("手动充值流量成功!");
             result.put("code", "1");
             result.put("msg", "添加流量成功");
             return result;
        }
        else {
            logger.info("验证码错误！");
            result.put("code", "-1");
            result.put("msg", "验证码错误");
            return result;
        }
            
    }

    /**
     * 发送短信验证码
     * 
     * @param mobile
     * @return
     */
    @RequestMapping("/flowrecharge/sendAuthSms")
    @ResponseBody
    public Map<String, Object> sendAuthSMS(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = getResult();
        //运营主管(陈梁)手机号telephone
        String mobile = Constants.getConfig("telephone");
        logger.info("telephone为：" + mobile);
        
        String lvsessionid = CookieUtils.getCookie(request, "lvsessionid");
        if (StringUtils.isBlank(lvsessionid)) {
            lvsessionid = UUID.randomUUID().toString().replaceAll("-", "");
            CookieUtils.setCookie(response, "lvsessionid", lvsessionid);
        }
        
		try {
			// 发送短信
			String resutls = AuthSmsCodeUtils.sendAuthSMS(request, mobile);
			JSONObject jo = JSONUtil.getObject(resutls);
			if (jo != null && jo.get("code").equals("1")) {
				result.put("msg", "验证码已发送到手机");
			} else {
				result.put("code", "-1");
				result.put("msg",jo.get("message") != null ? jo.get("message") : "验证码发送失败");
			}
		} catch (Exception e) {
			result.put("code", "-1");
			result.put("msg", "验证码发送失败");
		}

        return result;

    }

    /**
     * @Title: record
     * @Description: TODO(点击充值记录, 根据flow_recharge表的id, 查询相应的充值记录)
     * @param @param name
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    @RequestMapping("/flowrecharge/record/{id}")
    public String record(FlowRecordConditionVo flowRecordConditionVo, Model model, @PathVariable String id) {
        flowRecordConditionVo.setFlowRechargeId(id);
        PageInfo<FlowRechargeRecordInfo> pageInfo = flowRechargeService
            .queryFlowRechargeRecordListById(flowRecordConditionVo);
        model.addAttribute("FlowRechargeRecordList", pageInfo.getList());
        model.addAttribute("pageManage", pageInfo);
        model.addAttribute("flowRecordCondition", flowRecordConditionVo);
        model.addAttribute("id", id);

        return "/activity/flowrecharge/flowRechargeRecord";
    }

    /**
     * @Title: export
     * @Description: TODO(导出充值流量记录)
     * @param @param request
     * @param @param response
     * @param @param name
     * @param @param openid
     * @param @param rechargeFlow
     * @param @param operator
     * @param @param remark 设定文件
     * @return void 返回类型
     * @throws
     */
    @RequestMapping("/flowrecharge/export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response,
        FlowRecordConditionVo flowRecordConditionVo, String flowRechargeId) {
        flowRecordConditionVo.setFlowRechargeId(flowRechargeId);

        logger.info("查询需要导出的数据：" + flowRecordConditionVo.toString());

        PageInfo<ExportFlowRechargeRecord> pageInfo = flowRechargeService
            .queryExportFlowRechargeRecord(flowRecordConditionVo);
        List<ExportFlowRechargeRecord> flowRechargeRecordList = pageInfo.getList();
        logger.info("导出数据条数：" + flowRechargeRecordList.size());
        String[] headers = {
            "充值活动名", "用户昵称", "openid", "充值流量(M)", "操作人", "备注"
        };
        String codedFileName = null;
        OutputStream fOut = null;
        try {
            // 生成提示信息，
            response.setContentType("application/vnd.ms-excel");
            // 进行转码，使其支持中文文件名
            codedFileName = java.net.URLEncoder.encode("手动充值流量记录", "UTF-8");
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            logger.info("导出数据条数：" + flowRechargeRecordList.size());
            ExcelUtil.exportExcel(headers, flowRechargeRecordList, fOut, "yyyy-MM-dd HH:mm:ss");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: dealString
     * @Description: TODO(去除openid中的回车)
     * @param @param s
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    private String dealString(String s) {
        String[] array = s.trim().split("\r\n");

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
        }

        return sb.toString();
    }
    
    
    public Map<String, Object> getResult() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "1");
        result.put("msg", "操作成功");
        return result;
    }

}

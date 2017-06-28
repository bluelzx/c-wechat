package com.lvtu.wechat.front.web.signflow;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lvmama.comm.utils.MemcachedUtil;
import com.lvtu.wechat.common.annotation.FreqRequestLimit;
import com.lvtu.wechat.common.annotation.NeedFllow;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.annotation.NoNeedOauth;
import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.model.activity.signflow.Flow;
import com.lvtu.wechat.common.model.activity.signflow.FlowExchange;
import com.lvtu.wechat.common.model.activity.signflow.FlowPack;
import com.lvtu.wechat.common.model.advertising.Advertising;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowPackService;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowPartnerService;
import com.lvtu.wechat.common.service.activity.signflow.IWxFlowService;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.AdvertisingUtils;
import com.lvtu.wechat.front.utils.FlowChargeUtils;
import com.lvtu.wechat.front.utils.FlowExchangeUtil;
import com.lvtu.wechat.front.utils.PhoneUtil;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 微信流量
 * 
 * @author majun
 * 
 */
@Controller("FlowKeFuController")
@RequestMapping("/flow/kefu")
@NeedOauth
public class FlowKeFuController extends BaseController {

	@Autowired
	private IWxFlowService flowService;

	@Autowired
	private IWxFlowPackService flowPackService;

	@Autowired
	private IWxFlowPartnerService flowPartnerService;
	
	/**
	 * 客服渠道链接
	 * 返回url为 https://weixin.lvmama.com/flow/kefu/index 的Base64编码
	 */
	private static String callbackUrl = "aHR0cHM6Ly93ZWl4aW4ubHZtYW1hLmNvbS9mbG93L2tlZnUvaW5kZXg=";
	
	@RequestMapping("/index")
	@NeedFllow
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) {
        // 判断用户是否已经绑定了驴妈妈账户
        WechatUser wechatUser = getWechatUser(request);
        if(!WebchatUtil.isBind(wechatUser)) {
            model.addAttribute("wechatUser", wechatUser);
            model.addAttribute("callbackUrl", callbackUrl);
            
            return "forward:/flow/kefu/toBind";
        } else {
            //判断是否已经领取过首次赠送的流量
            Flow flow = flowService.getFlow(wechatUser.getOpenid());
            if (flow != null && !flow.isGotFirstFlow()) {
                flow.addFlow(Constants.WX_1ST_BIND_FLOW_ADD);
                flow.setGotFirstFlow(true);
                flowService.update(flow);
            }
            return "signflow/flowIndex";
        }
	}

	/**
	 * 去绑定页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/toBind")
	public String toBind(Model model,HttpServletRequest request, HttpServletResponse response) {
		WechatUser nowUser = getWechatUser(request);
		boolean isBind = WebchatUtil.isBind(nowUser);

		model.addAttribute("isBind", isBind);
		model.addAttribute("openid", nowUser.getOpenid());
		model.addAttribute("channel", "1");

		return "signflow/toBind";
	}

	/**
	 * 流量兑换页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getFlow")
	@FreqRequestLimit(setToken=true)
	@NeedFllow
	public String getFlow(HttpServletRequest request, HttpServletResponse response) {
		// 校验手机号码
		String phone = request.getParameter("phone");
		if (StringUtils.isBlank(phone))
			return "redirect:/flow/kefu/index";

		String phoneOperator = PhoneUtil.getPhoneOperator(phone);
		request.setAttribute("phoneOperator", phoneOperator);
		request.setAttribute("phone", phone);
		// 获得微信用户信息
		WechatUser userInfo = getWechatUser(request);
		request.setAttribute("wxUser", userInfo);
		// 获得用户流量信息
		Flow flow = flowService.getFlow(userInfo.getOpenid());
		request.setAttribute("flow", flow);
		// 获得支持的流量包
		List<FlowPack> flowPacks = flowPackService.getFlowPacks(phoneOperator);
		request.setAttribute("flowPacks", flowPacks);
		// 获得广告位信息
		List<Advertising> advertisingList = AdvertisingUtils
				.getAdvertisingInfo("signFlow", "receive");
		request.setAttribute("advertisingInfo", advertisingList);
		// 获得流量兑换合作商url
		MemcachedUtil mem = MemcachedUtil.getInstance();
		String partnerUrl = (String) mem.get(Constants.WX_FLOW_PARTNER_KEY);
		if (StringUtils.isBlank(partnerUrl)) {
			partnerUrl = flowPartnerService.selectUsedPartner().getPartnerUrl();
			mem.set(Constants.WX_FLOW_PARTNER_KEY, 60 * 60 * 24 * 7, partnerUrl);
		}
		request.setAttribute("flowExchangeUrl", partnerUrl);
		return "signflow/getFlow";
	}

	/**
	 * 用户兑换流量
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/exchange")
	@FreqRequestLimit
	public Object exchange(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> result = getResultMap();
		String phone = request.getParameter("phone");
		String flowCode = request.getParameter("flowCode");
		WechatUser wechatUser = getWechatUser(request);
		if (!WebchatUtil.isBind(wechatUser)) {
			result.put("code", -1);
			result.put("msg", "请先绑定驴妈妈账户后再兑换流量！");
		} else if (!PhoneUtil.isValide(phone)) {
			result.put("code", -1);
			result.put("msg", "号码输入有误或不支持该号码兑换流量！");
		} else if (StringUtils.isBlank(flowCode) || !flowCode.matches("\\d+")) {
			result.put("code", -2);
			result.put("msg", "流量包参数不正确！");
		} else if (!FlowChargeUtils.isOutOfBlance()) {
			result.put("code", -1);
			result.put("msg", "本月流量已全部领完，请您下个月再来领。");
		} else {
			// 获得微信用户信息
			WechatUser userInfo = getWechatUser(request);
			// 通过号码获得运营商信息
			String operator = PhoneUtil.getPhoneOperator(phone);
			// 得到流量包信息
			FlowPack flowPack = flowPackService.getFlowPackByOpAndCode(operator, flowCode);
			if (flowPack == null || flowPack.getFlow() == null || flowPack.getFlow() <= 0) {
				result.put("code", -3);
				result.put("msg", "不存在的流量包！");
			} else {
				logger.info("用户'" + userInfo.getOpenid() + "'兑换流量" + flowPack.getFlow() + "M到手机" + phone);
				try {
					FlowExchange flowExchange = flowService.exchange(flowPack.getFlow(), userInfo.getOpenid(), phone,operator);
					if (flowExchange != null) {
						boolean chargeSuccess = FlowChargeUtils.chargeFlow(phone, flowPack.getFlowCode(),flowExchange.getId());
						if (!chargeSuccess) {
							// 回滚扣除的流量
							Boolean rollbackSucceed = flowService.rollbackExchange(flowExchange.getId());
							logger.error("流量兑换失败， exchangeId:"+ flowExchange.getId() + " 流量回退是否成功:"+ rollbackSucceed);
							result.put("code", -1);
							result.put("msg", "流量兑换失败！");
						}
					}
				} catch (CustomerException e) {
					logger.info("流量兑换失败，" + e.getMessage());
					result.put("code", -1);
					result.put("msg",StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "流量兑换失败！");
				}
			}
		}
		
		resetToken(request, wechatUser.getOpenid());
		return result;
	}

	/**
	 * 领流量分享页面,所有动态信息通过参数传递
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/share")
	@NoNeedOauth
	public String sharePage(HttpServletRequest request,
			HttpServletResponse response) {
		// 领取了多少流量
		String exchangedFlow = request.getParameter("exchangedFlow");
		String totalFlow = request.getParameter("totalFlow");
		request.setAttribute("exchangedFlow", exchangedFlow);
		request.setAttribute("totalFlow", totalFlow);
		// 获得微信用户信息
		String openid = request.getParameter("openid");
		if (StringUtils.isNotBlank(openid)) {
			WechatUser wechatUser = WebchatUtil.getUserInfo(openid);
			request.setAttribute("headImg", wechatUser.getHeadimgurl());
			request.setAttribute("nickName", wechatUser.getNickname());
		} else {
			// 兼容以前的分享
			String headImg = request.getParameter("headImg");
			String nickName = request.getParameter("nickName");
			try {
				headImg = URLDecoder.decode(headImg, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			request.setAttribute("headImg", headImg);
			request.setAttribute("nickName", nickName);
		}
		return "signflow/share";
	}

	/**
	 * 领流量之前分享页面,提示给未关注公众号用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/beforeShare")
	@NoNeedOauth
	public String beforeSharePage(HttpServletRequest request,
			HttpServletResponse response) {

		return "signflow/beforeShare";
	}

	/**
	 * 用戶流量兌換(瑞翼)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/newExchange")
	@FreqRequestLimit
	public Object exchangeTemp(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> result = getResultMap();
		String phone = request.getParameter("phone");
		String flowCode = request.getParameter("flowCode");
		boolean flag = false;
		FlowExchange flowExchange = null;
		WechatUser wechatUser = getWechatUser(request);
		if (!WebchatUtil.isBind(wechatUser)) {
			result.put("code", -1);
			result.put("msg", "请先绑定驴妈妈账户后再兑换流量！");
		} else if (!PhoneUtil.isValide(phone)) {
			result.put("code", -1);
			result.put("msg", "号码输入有误或不支持该号码兑换流量！");
		} else if (StringUtils.isBlank(flowCode) || !flowCode.matches("\\d+")) {
			result.put("code", -2);
			result.put("msg", "流量包参数不正确！");
		} else if (!FlowExchangeUtil.isOutOfBlance()) {
			result.put("code", -1);
			result.put("msg", "本月流量已全部领完，请您下个月再来领。");
		} else {
			WechatUser userInfo = getWechatUser(request);// 获得微信用户信息
			String operator = PhoneUtil.getPhoneOperator(phone);// 通过号码获得运营商信息
			// 得到流量包信息 拿到flowSize值
			FlowPack flowPack = flowPackService.getFlowPackByOpAndCode(operator, flowCode);
			if (flowPack == null || flowPack.getFlow() == null || flowPack.getFlow() <= 0) {
				result.put("code", -3);
				result.put("msg", "不存在的流量包！");
			} else {
				try {
					// 由于增加省份产品信息 需要通过手机号从合作商调取接口获取流量包信息
					String flowSize = flowPack.getFlow() + "";
					String productId = FlowExchangeUtil.getProductListByMobile(phone, flowSize);
					if (productId == null || productId.equals("")) {
						result.put("code", -3);
						result.put("msg", "不存在的流量包！");
					} else {
						logger.info("用户'" + userInfo.getOpenid() + "'兑换流量" + flowSize + "M到手机" + phone);
						// 本地流量兑换操作
						flowExchange = flowService.exchange(flowPack.getFlow(),userInfo.getOpenid(), phone, operator);
						if (flowExchange != null) {
							// 调用合作方接口兑换流量
							logger.info("本地生成订单号exchanggeId="+ flowExchange.getId());
							flag = FlowExchangeUtil.buyFlow(phone, productId,flowExchange.getId());
						} else {
							result.put("code", -1);
							result.put("msg", "流量兑换失败！");
						}
					}
				} catch (CustomerException e) {
					logger.info("流量兑换失败，" + e.getMessage());
					result.put("code", -1);
					result.put("msg",StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : "流量兑换失败！");
				} finally {
					if (!flag && flowExchange != null) {
						Boolean rollbackSucceed = flowService.rollbackExchange(flowExchange.getId());// 回滚扣除的流量
						logger.error("流量兑换失败， exchangeId:"+ flowExchange.getId() + " 流量回退是否成功:"+ rollbackSucceed);
					}
				}
			}
		}
		resetToken(request, wechatUser.getOpenid());
		return result;
	}

}

package com.lvtu.wechat.front.web.flowactivity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.mortbay.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.JedisCluster;

import com.lvmama.com.lvtu.common.utils.DateUtil;
import com.lvmama.com.lvtu.common.utils.StringUtil;
import com.lvmama.comm.utils.MemcachedUtil;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.enums.FlowGroupStatusType;
import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowActivity;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowGroups;
import com.lvtu.wechat.common.model.activity.flowActivity.FlowMembers;
import com.lvtu.wechat.common.model.qrcode.QRCode;
import com.lvtu.wechat.common.model.share.ShareTemplate;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.flow.IFlowActivityService;
import com.lvtu.wechat.common.service.activity.flow.IFlowMembersService;
import com.lvtu.wechat.common.service.qrcode.IQRCodeService;
import com.lvtu.wechat.common.service.share.IShareTemplateService;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.vo.back.FlowGroupsConditionVo;
import com.lvtu.wechat.common.vo.back.FlowMembersConditionVo;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.WebchatUtil;

@Controller
@RequestMapping("flowActivity")
@NeedOauth
public class FlowActivityController extends BaseController {

	@Autowired
	private IFlowActivityService flowActvityService;

	@Autowired
	private IFlowMembersService flowMembersService;

	@Autowired
	private IQRCodeService qRCodeService;

	@Autowired
	JedisCluster jedisCluster;
	
	@Autowired
	private IShareTemplateService shareTemplateService;

	/**
	 * @Title: index
	 * @Description: 活动首页
	 * @param @param id 活动ID
	 * @param @param request
	 * @param @param response
	 * @param @param groupId 团ID
	 * @param @param model
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("index/{activityId}")
	public String index(@PathVariable String activityId,
			HttpServletRequest request, HttpServletResponse response,
			String groupId, Model model) {
		MemcachedUtil mem = MemcachedUtil.getInstance();
		FlowActivity flowActivity = (FlowActivity) mem.get(activityId);
		if (null == flowActivity) {
			flowActivity = flowActvityService.queryFLowActivityById(activityId);
			mem.set(activityId, flowActivity);
		}
		if (null == flowActivity) {
			model.addAttribute("msg", "活动信息不存在！更多精彩活动");
			return "flowActivity/scanCode";
			// 活动不存在
		}
		model.addAttribute("flowActivity", flowActivity);
		if (flowActivity.getState().equals("0")) {
			model.addAttribute("msg", "活动还未开启！更多精彩活动");
			return "flowActivity/scanCode";
			// 活动状态未开启
		} else if (DateUtil.inAdvance(flowActivity.getEndDate(), new Date())) {
			model.addAttribute("msg", "活动已经结束啦！更多精彩活动");
			return "flowActivity/scanCode";
			// 活动已经结束
		} else if (DateUtil.inAdvance(new Date(), flowActivity.getStartDate())) {
			model.addAttribute("msg", "活动还未开始！更多精彩活动");
			return "flowActivity/scanCode";
			// 活动还未开始
		}
		getShareContents(flowActivity.getShareTemplateId(), model);
		WechatUser wecahtUser = getWechatUser(request);
		model.addAttribute("wecahtUser", wecahtUser);
		String openid = wecahtUser.getOpenid();
		String target = "index";
		if (StringUtil.isBlank(groupId)) {
			// 活动推送进入
			target = checkActivity(openid, activityId, model);
			return "flowActivity/" + target;
		} else {
			// 朋友圈分享链接进入
			checkActivity(wecahtUser, flowActivity, groupId, model);
			FlowGroups flowGroups = flowActvityService.queryGroupsById(groupId);
			if (flowGroups.getOpenid().equals(wecahtUser.getOpenid())
					&& flowGroups.getState().equals(1)
					&& flowGroups.getIsShow().equals("0")) {
				flowGroups.setIsShow("1");
				flowActvityService.updateFlowGroups(flowGroups);
				model.addAttribute("isShow", true);
			}
			model.addAttribute("flowGroups", flowGroups);
			return "flowActivity/detail";
		}
	}

	/**
	 * @Title: openGroup
	 * @Description: 点击：我要开团
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("openGroup/{activityId}")
	public Object openGroup(@PathVariable String activityId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Map<String, Object> resultMap = getResultMap();
		resultMap.put("code", -1);
		try {
			WechatUser userInfo = getWechatUser();
			MemcachedUtil mem = MemcachedUtil.getInstance();
			FlowActivity flowActivity = (FlowActivity) mem.get(activityId);
			if (null == flowActivity) {
				flowActivity = flowActvityService.queryFLowActivityById(activityId);
				mem.set(activityId, flowActivity);
			}
			if (null == flowActivity) {
				resultMap.put("msg", "活动不存在");
				return resultMap;
				// 活动不存在
			} else if (flowActivity.getState().equals("0")) {
				resultMap.put("msg", "活动状态未开启");
				return resultMap;
				// 活动状态未开启
			} else if (DateUtil.inAdvance(flowActivity.getEndDate(), new Date())) {
				resultMap.put("msg", "活动已经结束了");
				return resultMap;
				// 活动已经结束
			} else if (DateUtil.inAdvance(new Date(),flowActivity.getStartDate())) {
				resultMap.put("msg", "活动还未开始");
				return resultMap;
				// 活动还未开始
			} else {
				FlowGroups flowGroups = flowActvityService.openGroup(flowActivity, userInfo);
				Log.info("开团成功:" + userInfo.getOpenid());
				resultMap.put("code", 1);
				resultMap.put("msg", flowGroups.getId());
			}
			model.addAttribute("flowActivity", flowActivity);
		} catch (Exception e) {
			resultMap.put("code", -1);
			resultMap.put("msg", e.getMessage() != null ? e.getMessage()
					: "系统异常");
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * @Title: success
	 * @Description: 开团成功
	 * @param @param groupId
	 * @param @param model
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("success")
	public String success(String groupId, Model model) {
		FlowGroups flowGroups = flowActvityService.queryGroupsById(groupId);
		MemcachedUtil mem = MemcachedUtil.getInstance();
		FlowActivity flowActivity = (FlowActivity) mem.get(flowGroups.getFlowActivityId());
		if (null == flowActivity) {
			flowActivity = flowActvityService.queryFLowActivityById(flowGroups.getFlowActivityId());
			mem.set(flowGroups.getFlowActivityId(), flowActivity);
		}
		getShareContents(flowActivity.getShareTemplateId(), model);
		model.addAttribute("flowGroups", flowGroups);
		model.addAttribute("flowActivity", flowActivity);
		return "flowActivity/success";
	}

	/**
	 * @Title: qrcode
	 * @Description: 显示活动二维码信息
	 * @param @param activityId
	 * @param @param model
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping("qrcode/{activityId}")
	public String qrcode(@PathVariable String activityId, Model model) {
		MemcachedUtil mem = MemcachedUtil.getInstance();
		FlowActivity flowActivityQrCode = (FlowActivity) mem.get(activityId);
		if (null == flowActivityQrCode) {
			flowActivityQrCode = flowActvityService.queryFLowActivityById(activityId);
			mem.set(activityId, flowActivityQrCode);
		}
		QRCode qRCode = qRCodeService.queryQRCodeByActId(activityId);
		model.addAttribute("flowActivity", flowActivityQrCode);
		model.addAttribute("qRCode", qRCode);
		return "flowActivity/scanCode";
	}

	public String checkActivity(String openid, String activityId, Model model) {
		FlowGroupsConditionVo vo = new FlowGroupsConditionVo();
		vo.setOpenid(openid);
		vo.setFlowActivityId(activityId);
		FlowGroups flowGroups = flowActvityService.queryGroupsByCondition(vo);
		if (flowGroups != null) {
			List<FlowMembers> list = flowMembersService.queryByGroupId(flowGroups.getId());
			model.addAttribute("listMembers", list);
			model.addAttribute("flowGroups", flowGroups);
			if (flowGroups.getOpenid().equals(openid)
					&& flowGroups.getState().equals(1)
					&& flowGroups.getIsShow().equals("0")) {
				flowGroups.setIsShow("1");
				flowActvityService.updateFlowGroups(flowGroups);
				model.addAttribute("isShow", true);
			}
			return "alreadyJoin";// 已经发起过活动
		}
		return "index";
	}

	public void checkActivity(WechatUser userInfo, FlowActivity flowActivity,
			String groupId, Model model) {
		FlowGroups flowGroups = flowActvityService.queryGroupsById(groupId);
		if (flowGroups.getOpenid().equals(userInfo.getOpenid())) {
			// 发起者再次打开自己分享的链接
			model.addAttribute("step", 0);
			List<FlowMembers> listMembers = flowMembersService.queryByGroupId(groupId);
			model.addAttribute("listMembers", listMembers);
			model.addAttribute("openUser", userInfo);
			return;
		}
		FlowMembersConditionVo vo = new FlowMembersConditionVo();
		vo.setActivityId(flowActivity.getId());
		vo.setOpenid(userInfo.getOpenid());
		List<FlowMembers> list = flowMembersService.queryMembersByOpenid(vo);
		try {
			if (null != list && list.size() > 1) {

				List<FlowMembers> listMembers = flowMembersService.queryByGroupId(groupId);
				model.addAttribute("listMembers", listMembers);
				model.addAttribute("step", 1);// 你已经参加过活动，把机会留给别人吧
				model.addAttribute("IWant", 1);
				for (FlowMembers flowMembers : list) {
					if (flowMembers.getIsLeader().equals(0)) {
						if (flowMembers.getGroupId().equals(groupId)) {// 再次打开自己的参加过的页面
							model.addAttribute("openUser", WebchatUtil.getUserInfo(flowGroups.getOpenid()));
							list = flowMembersService.queryByGroupId(groupId);
							model.addAttribute("listMembers", list);
							groupId = flowMembers.getGroupId();
							model.addAttribute("groupId", groupId);
							model.addAttribute("flowMembers", flowMembers);
							model.addAttribute("step", 2);
							return;
						}
						groupId = flowMembers.getGroupId();
						model.addAttribute("groupId", groupId);
					}
				}
				return;
			} else if (null != list && list.size() == 1) {
				FlowMembers flowMembers = list.get(0);
				if (flowMembers.getIsLeader().equals(0)) {
					if (flowMembers.getGroupId().equals(groupId)) {// 再次打开自己的参加过的页面
						model.addAttribute("openUser",
								WebchatUtil.getUserInfo(flowGroups.getOpenid()));
						list = flowMembersService.queryByGroupId(groupId);
						model.addAttribute("listMembers", list);
						groupId = flowMembers.getGroupId();
						model.addAttribute("groupId", groupId);
						model.addAttribute("flowMembers", flowMembers);
						model.addAttribute("step", 2);
						return;
					}

					list = flowMembersService.queryByGroupId(groupId);
					model.addAttribute("listMembers", list);
					groupId = flowMembers.getGroupId();
					model.addAttribute("groupId", groupId);
					model.addAttribute("step", 1);// 参加过其他人发起的活动
					return;
				} else {
					model.addAttribute("IWant", 1);
				}
			}
			if (flowGroups.getTotalFlow().equals(flowGroups.getNowNum())
					|| flowGroups.getState().equals(FlowGroupStatusType.DONE.getValue())) {
				list = flowMembersService.queryByGroupId(groupId);
				model.addAttribute("listMembers", list);
				model.addAttribute("step", 3);// 流量已经被领取完了
				return;
			}
			boolean flag = false;
			synchronized (this) {
				if ("1".equals(jedisCluster.get(Constants.WX_FLOW_TEAM_ING + groupId))) {
					Log.info("有请求正在处理中" + groupId);
					flag = true;
				} else {
					Log.info("没有请求处理中" + groupId);
					jedisCluster.setex(Constants.WX_FLOW_TEAM_ING + groupId,60, "1");
				}
			}
			if (flag) {
				list = flowMembersService.queryByGroupId(groupId);
				model.addAttribute("listMembers", list);
				model.addAttribute("step", 5);// 流量领取的人太多了
				return;
			}
			// 参与活动
			FlowMembers flowMembers = flowActvityService.joinGroup(flowGroups,
					flowActivity, userInfo);
			jedisCluster.setex(Constants.WX_FLOW_TEAM_ING + groupId, 60, "0");
			Log.info("参团成功:" + userInfo.getOpenid());
			model.addAttribute("flowMembers", flowMembers);
			model.addAttribute("step", 4);// 参团成功
			model.addAttribute("openUser",WebchatUtil.getUserInfo(flowGroups.getOpenid()));
		} catch (CustomerException e) {
			Log.info(e.getMessage());
			model.addAttribute("step", 1);
			jedisCluster.setex(Constants.WX_FLOW_TEAM_ING + groupId, 60, "0");
		} catch (Exception e) {
			jedisCluster.setex(Constants.WX_FLOW_TEAM_ING + groupId, 60, "0");
		}
		list = flowMembersService.queryByGroupId(groupId);
		model.addAttribute("listMembers", list);
		return;
	}

	/**
	 * @Title: addFlowForwardNum
	 * @Description: 活动二次转发量统计
	 * @param @param activityId 活动ID
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping("addForwardNum/{activityId}")
	public void addFlowForwardNum(@PathVariable String activityId) {
		String nums = jedisCluster.get(Constants.WX_FLOW_FROWARD_NUM + activityId);
		// 转发量放到redis缓存里，半小时跑一次定时任务持久化到数据库
		if (!StringUtils.isBlank(nums)) {
			Integer num = Integer.parseInt(nums);
			++num;
			jedisCluster.set(Constants.WX_FLOW_FROWARD_NUM + activityId, num + "");
		} else {
			jedisCluster.set(Constants.WX_FLOW_FROWARD_NUM + activityId, "1");
		}
	}

	@RequestMapping("record/{groupId}")
	public String record(@PathVariable String groupId,
			HttpServletRequest request, HttpServletResponse response,
			Model model, String param) {
		if (param != null) {
			model.addAttribute("step", 1);
		}
		FlowGroups flowGroups = flowActvityService.queryGroupsById(groupId);
		List<FlowMembers> listMembers = flowMembersService.queryByGroupId(groupId);
		FlowMembers flowMembers = null;
		WechatUser wecahtUser = getWechatUser(request);
		String openid = wecahtUser.getOpenid();
		for (FlowMembers flowMember : listMembers) {
			if (flowMember.getOpenid().equals(openid)) {
				flowMembers = flowMember;
				break;
			}
		}
		MemcachedUtil mem = MemcachedUtil.getInstance();
		FlowActivity flowActivity = (FlowActivity) mem.get(flowMembers.getActivityId());
		if (null == flowActivity) {
			flowActivity = flowActvityService.queryFLowActivityById(flowMembers.getActivityId());
			mem.set("flowActivity", flowActivity);
		}
		getShareContents(flowActivity.getShareTemplateId(), model);
		model.addAttribute("listMembers", listMembers);
		model.addAttribute("flowMembers", flowMembers);
		model.addAttribute("flowActivity", flowActivity);
		model.addAttribute("flowGroups", flowGroups);
		model.addAttribute("openUser",WebchatUtil.getUserInfo(flowGroups.getOpenid()));
		if (isOpen(flowActivity.getId(), openid)) {
			model.addAttribute("IWant", 1);
		}
		return "flowActivity/record";
	}

	/**
	 * @Title: isJoin
	 * @Description: 判断某个用户是否发起过某个活动
	 * @param @param activityId
	 * @param @param openid
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean isOpen(String activityId, String openid) {
		FlowGroupsConditionVo vo = new FlowGroupsConditionVo();
		vo.setFlowActivityId(activityId);
		vo.setOpenid(openid);
		FlowGroups flowGroups = flowActvityService.queryGroupsByCondition(vo);
		if (null != flowGroups && StringUtil.isNotEmptyString(flowGroups.getId())) {
			return true;
		}
		return false;
	}
	
    /**
     * 获取微信页面分享内容
     * @param shareTemplateId
     * @param model
     */
	public void getShareContents(String shareTemplateId, Model model) {
		MemcachedUtil mem = MemcachedUtil.getInstance();
		ShareTemplate shareTemplate = (ShareTemplate) mem.get(shareTemplateId);
		if (null == shareTemplate) {
			shareTemplate = shareTemplateService.getShareTemplate(
					shareTemplateId, CommonType.SHARE_CHANNEL_WX.getValue());
			if (null != shareTemplate) {
				mem.set(shareTemplateId, shareTemplate);
			}
		}
		if (shareTemplate != null && shareTemplate.getWxShareContent() != null) {
			model.addAttribute("shareContent",
					shareTemplate.getWxShareContent());
			if (shareTemplate.getWxShareContent().imageList.size() != 0
					&& !shareTemplate.getWxShareContent().imageList.isEmpty()) {
				model.addAttribute("imageList",
						shareTemplate.getWxShareContent().imageList.get(0));
			}
		}
	}

}

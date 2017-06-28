package com.lvtu.wechat.front.web.groupActivity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lvmama.com.lvtu.common.utils.DateUtil;
import com.lvtu.wechat.common.annotation.NeedOauth;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.enums.ConstellationsType;
import com.lvtu.wechat.common.enums.StatusType;
import com.lvtu.wechat.common.model.activity.group.GroupActivity;
import com.lvtu.wechat.common.model.activity.group.GroupMembers;
import com.lvtu.wechat.common.model.qrcode.QRCode;
import com.lvtu.wechat.common.model.qrcode.UseQRCode;
import com.lvtu.wechat.common.model.weixin.WechatUser;
import com.lvtu.wechat.common.service.activity.group.IGroupActivityService;
import com.lvtu.wechat.common.service.qrcode.IQRCodeService;
import com.lvtu.wechat.front.base.BaseController;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 组团活动，集齐送神龙
 * 
 * @NeedOauth
 * @author qianqc
 */
@Controller
@RequestMapping("/groupActivity")
@NeedOauth
public class GroupActivityController extends BaseController {

    @Autowired
    IGroupActivityService groupActivityService;
    
    @Autowired
    private IQRCodeService qRCodeService;

    /**
     * 首页
     * 
     * @param groupActId
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping("/{groupActId}")
    public Object index(@PathVariable String groupActId, Model model, HttpServletResponse resp, String groupId, HttpServletRequest request) {
        // 当前团队活动进行到第几步了
        int step = 1;
        GroupActivity groupActivity = groupActivityService.queryGroupActivitiesById(groupActId);
        if (groupActivity == null) {
            return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
        }
        if (groupActivity != null) {
            getShareContent(groupActivity, model);
        }
        model.addAttribute("groupActivity", groupActivity);
        if (!StatusType.OPEN.getValue().equals(groupActivity.getState())) {
            // 二维码界面
            model.addAttribute("finish", "1");
            return "/group/result";
        }
        // 日期校验
        Date now = new Date();
        if (groupActivity.getEndDate() != null && now.after(groupActivity.getEndDate())) {
            // 二维码界面
            model.addAttribute("finish", "1");
            return "/group/result";
        }
        if (groupActivity.getDeadlineTime() != null && now.after(groupActivity.getDeadlineTime())) {
            step = 4;
        }


        // 判断用户是否已经参加过该活动
        judgeUserAlardyJoin(groupActId, step, model, groupId, groupActivity, request);
        model.addAttribute("banners", groupActivity.bannerList);
        model.addAttribute("prizes", groupActivity.prizeList);
        model.addAttribute("deadlineTime", DateUtil.formatDate(groupActivity.getDeadlineTime(), "yyyy-MM-dd HH:mm:ss"));
        model.addAttribute("logostate", groupActivity.getLogoButton());
        model.addAttribute("showstate", groupActivity.getShowButton());
        model.addAttribute("groupActId", groupActId);
        return "/group/index";
    }

    /**
     * 判断用户是否已经参加过该活动
     * 
     * @param groupActId
     * @param step
     * @param model
     * @param groupId
     * @param groupActivity
     */
    private void judgeUserAlardyJoin(String groupActId, int step, Model model, String groupId,
        GroupActivity groupActivity, HttpServletRequest request) {
        // 0-用户已经参加团队 1-用户参团中
        int join = 0;
        WechatUser wechatUser = getWechatUser(request);
        // 1.通过活动id和用户的wx_user_id查询是否已经参加过团队
        GroupMembers groupMember = new GroupMembers();
        String wxUserId = wechatUser.getId();
        if (StringUtils.isBlank(wechatUser.getId())) {
            wechatUser = WebchatUtil.getUserInfoWithoutCache(wechatUser.getOpenid());
            wxUserId = wechatUser.getId();
        }
        groupMember.setWxUserId(wxUserId);
        groupMember.setGroupActivityId(groupActId);
        logger.info("查询团队成员:wxUserId=" + wxUserId);
        List<GroupMembers> groupMembersList = groupActivityService.queryGroupMembers(groupMember);
        // 2.如果用户没有参加过，并且groupId不为空，查询团队成员信息
        if (!StringUtils.isBlank(groupId) && (groupMembersList == null || groupMembersList.isEmpty())) {
            join = 1;
            groupMember = new GroupMembers();
            groupMember.setGroupId(groupId);
            groupMember.setGroupActivityId(groupActId);
            logger.info("查询团队成员:groupId=" + groupId);
            groupMembersList = groupActivityService.queryGroupMembers(groupMember);
        }

        if (groupMembersList != null && !(groupMembersList.isEmpty())) {
            if (groupMembersList.size() == groupActivity.getTeamNumber()) {
                step = step == 4 ? step : 3;
                model.addAttribute("remainNum", 0);
            }
            else {
                step = step == 4 ? step : 2;
                model.addAttribute("remainNum", groupActivity.getTeamNumber() - groupMembersList.size());
            }
            model.addAttribute("groupId", groupMembersList.get(0).getGroupId());
        }
        model.addAttribute("join", join);
        model.addAttribute("step", step);
        model.addAttribute("groupMembersList", groupMembersList);
    }

    /**
     * 获取团队成员信息
     * 
     * @param groupActId
     * @param model
     * @param resp
     * @param groupId
     * @return
     */
    @RequestMapping("/getGroupMembers/{groupActId}")
    public Object getGroupMembers(@PathVariable String groupActId, Model model, HttpServletResponse resp, HttpServletRequest request,
        String groupId) {
        GroupActivity groupActivity = groupActivityService.queryGroupActivitiesById(groupActId);
        if (groupActivity != null) {
            getShareContent(groupActivity, model);
        }
        WechatUser wechatUser = getWechatUser(request);
        GroupMembers groupMember = new GroupMembers();
        if (StringUtils.isBlank(groupId)) {
            groupMember.setWxUserId(wechatUser.getId());
        }
        else {
            groupMember.setGroupId(groupId);
        }
        groupMember.setGroupActivityId(groupActId);
        List<GroupMembers> groupMembersList = groupActivityService.queryGroupMembers(groupMember);
        model.addAttribute("groupMembersList", groupMembersList);
        model.addAttribute("groupActivity", groupActivity);
        model.addAttribute("groupActId", groupActId);
        if (!StringUtils.isBlank(groupId)) {
            model.addAttribute("groupId", groupId);
        }
        return "/group/alreadyjoin";
    }

    /**
     * 获取规则信息
     * 
     * @param groupActId
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping("/getRule/{groupActId}")
    public Object getRule(@PathVariable String groupActId, Model model, HttpServletResponse resp, String groupId) {
        GroupActivity groupActivity = groupActivityService.queryGroupActivitiesById(groupActId);
        if (groupActivity != null) {
            getShareContent(groupActivity, model);
        }
        groupActivity.setRuleCopy(StringEscapeUtils.unescapeHtml4((groupActivity.getRuleCopy())));
        model.addAttribute("groupActivity", groupActivity);
        model.addAttribute("groupActId", groupActId);
        if (!StringUtils.isBlank(groupId)) {
            model.addAttribute("groupId", groupId);
        }
        return "/group/rule";
    }

    /**
     * 进入登录填写信息页面
     * 
     * @param groupActId
     * @param model
     * @param resp
     * @return
     */
    @RequestMapping("/iWantJoin/{groupActId}")
    public Object iWantJoin(@PathVariable String groupActId, Model model, HttpServletResponse resp, String groupId) {
        GroupActivity groupActivity = groupActivityService.queryGroupActivitiesById(groupActId);
        if (groupActivity != null) {
            getShareContent(groupActivity, model);
        }
        model.addAttribute("groupActivity", groupActivity);
        model.addAttribute("banners", groupActivity.bannerList);
        model.addAttribute("logostate", groupActivity.getLogoButton());
        model.addAttribute("groupActId", groupActId);
        if (!StringUtils.isBlank(groupId)) {
            model.addAttribute("groupId", groupId);
        }
        model.addAttribute("constellationsList", Arrays.asList(ConstellationsType.values()));
        return "/group/join";
    }

    /**
     * 报名参加信息提交
     */
    @RequestMapping("/join")
    public Object join(Model model, HttpServletRequest request, GroupMembers groupMembers, HttpServletResponse resp) {
        GroupActivity groupActivity = groupActivityService.queryGroupActivitiesById(groupMembers.getGroupActivityId());
        if (groupActivity == null) {
            return renderString(resp, "活动不存在!", "text/html;charset=UTF-8");
        }

        // 日期校验
        Date now = new Date();
        if ((groupActivity.getEndDate() != null && now.after(groupActivity.getEndDate()))
            || (groupActivity.getDeadlineTime() != null && now.after(groupActivity.getDeadlineTime()))) {
            // 二维码界面
            getShareContent(groupActivity, model);
            model.addAttribute("groupActId", groupMembers.getGroupActivityId());
            model.addAttribute("groupId", groupMembers.getGroupId());
            model.addAttribute("finish", "1");
            return "/group/result";
        }
        WechatUser wechatUser = getWechatUser(request);
        String wxUserId = wechatUser.getId();
        if (StringUtils.isBlank(wechatUser.getId())) {
            wechatUser = WebchatUtil.getUserInfoWithoutCache(wechatUser.getOpenid());
            wxUserId = wechatUser.getId();
        }
        groupMembers.setWxUserId(wxUserId);
        List<GroupMembers> groupMembersList = groupActivityService.queryGroupMembers(groupMembers);
        if (groupMembersList != null && !(groupMembersList.isEmpty())) {
            for (GroupMembers gourpMember : groupMembersList) {
                if (wechatUser.getId().equals(gourpMember.getWxUserId())) {
                    // 如果用户已经参加过该活动
                    return "redirect:" + groupActivity.getId();
                }
            }
        }
        int insertResult = groupActivityService.insertGroupMembers(groupMembers, groupActivity);
        if (insertResult == 0) {
            model.addAttribute("msg", "1");
            return index(groupMembers.getGroupActivityId(), model, resp, groupMembers.getGroupId(), request);
        }
        if (CommonType.SUBSCRIBE.getStringValue().equals(wechatUser.getSubscribe())) {
            logger.info("====================" + Thread.currentThread().getName() + "=============url:" + groupActivity.getId() + "?groupId=" +  groupMembers.getGroupId());
            return "redirect:" + groupActivity.getId() + "?groupId=" +  groupMembers.getGroupId();
        }
        else {
            getQrCode(model, groupMembers);
            // 二维码界面
            model.addAttribute("groupActId", groupMembers.getGroupActivityId());
            model.addAttribute("groupId", groupMembers.getGroupId());
            model.addAttribute("groupActivity", groupActivity);
            model.addAttribute("banners", groupActivity.bannerList);
            model.addAttribute("logostate", groupActivity.getLogoButton());
            return "/group/result";
        }
    }

    /**
     * 获取活动对应的二维码
     * @param model
     * @param groupMembers
     */
    private void getQrCode(Model model, GroupMembers groupMembers) {
        UseQRCode useQRCode = new UseQRCode();
        useQRCode.setActId(groupMembers.getGroupActivityId());
        useQRCode = qRCodeService.queryUseQRCode(useQRCode);
        QRCode qRCode= new QRCode();
        if(useQRCode != null){
            qRCode.setId(useQRCode.getWxQRCodeId());
            qRCode=qRCodeService.queryQRCode(qRCode);
        }
        model.addAttribute("qRCode", qRCode);
    }

    /**
     * 将分享内容封装调用
     * 
     * @param groupActivity
     * @param model
     */
    public void getShareContent(GroupActivity groupActivity, Model model) {
        getShareContent(groupActivity.getShareTemplateId(), model);
    }

}

package com.lvtu.wechat.front.quartz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.model.activity.group.Group;
import com.lvtu.wechat.common.model.activity.group.GroupActivity;
import com.lvtu.wechat.common.model.activity.group.GroupMembers;
import com.lvtu.wechat.common.model.weixin.template.TemplateData;
import com.lvtu.wechat.common.model.weixin.template.WxTemplate;
import com.lvtu.wechat.common.quartz.BaseQuartzJobBean;
import com.lvtu.wechat.common.service.activity.group.IGroupActivityService;
import com.lvtu.wechat.common.utils.Http;
import com.lvtu.wechat.front.utils.WebchatUtil;

/**
 * 组团活动定时任务 组团成功之后，给用户微信推送组团成功的消息
 * 
 * @author qianqc
 */
public class GroupActivityTask extends BaseQuartzJobBean {

    @Autowired
    private IGroupActivityService groupActivityService;
    
    private final String TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    
    private final String TEMPLATE_ID = "7rbR_4uI4vWyu58BX2qdU2U-rvd5voD4FFhwn9Cva3A";
    
    private final String TEMPLATE_COLOR = "#173177";
    
    private final String GROUP_ACTIVITY_URL = "https://weixin.lvmama.com/groupActivity/";

    @Override
    public void invoke() {
        // 查询组团成功，需要发送组团提醒的团队
        List<Group> groupList = groupActivityService.queryNeedSendMessageGroup();
        if (groupList == null || groupList.isEmpty()) {
            return;
        }
        //设置查询到的团队为正在发送组团通知状态，涉及字段is_send为2  对应CommonType.SENDING
        for (Group group : groupList) {
            group.setIsSend(CommonType.SENDING.getStringValue());
        }
        groupActivityService.updateGroupState(groupList);
        // 获取对应的团队成员，并且发送组团成功通知
        GroupMembers groupMember = null;
        GroupActivity groupActivity = null;
        for (Group group : groupList) {
            try {
                groupMember = new GroupMembers();
                groupMember.setGroupId(group.getId());
                groupMember.setGroupActivityId(group.getGroupActivityId());
                List<GroupMembers> groupMembersList = groupActivityService.queryGroupMembers(groupMember);
                if (groupMembersList != null && !groupMembersList.isEmpty()) {
                    groupActivity = groupActivityService.queryGroupActivitiesById(group.getGroupActivityId());
                    sendGroupSuccessMessage(groupMembersList, groupActivity);
                }
                group.setIsSend(CommonType.SEND_SUCCESS.getStringValue());
                groupActivityService.updateGroupState(group);
            }
            catch (Exception e) {
                group.setIsSend(CommonType.SEND_FAILED.getStringValue());
                groupActivityService.updateGroupState(group);
                logger.error(e);
                continue;
            }
        }
    }
    
    /**
     * 发送组团成功提醒
     * 
     * @param groupMembersList
     * @param groupActivity
     * @throws Exception
     */
    private void sendGroupSuccessMessage(List<GroupMembers> groupMembersList, GroupActivity groupActivity)
        throws Exception {
        WxTemplate wxTemplate = null;
        for (GroupMembers groupMembers : groupMembersList) {
            // 如果没有发送就发送
            if (CommonType.NO_SEND.getStringValue().equals(groupMembers.getIsSend())
                || CommonType.SEND_FAILED.getStringValue().equals(groupMembers.getIsSend())) {
                try {
                    wxTemplate = new WxTemplate();
                    sendMeassge(groupMembers, groupActivity, wxTemplate, groupMembersList.get(0));
                }
                catch (Exception e) {
                    groupMembers.setIsSend(CommonType.SEND_FAILED.getStringValue());
                    groupActivityService.saveGroupMemberForm(groupMembers);
                    throw e;
                }
            }
        }
    }

    /**
     * 发送消息
     * @param groupMembers
     * @param groupActivity
     * @param wxTemplate
     * @param groupMemberLeader
     */
    private void sendMeassge(GroupMembers groupMembers, GroupActivity groupActivity, WxTemplate wxTemplate, GroupMembers groupMemberLeader) {
        wxTemplate.setTemplate_id(TEMPLATE_ID);
        wxTemplate.setTopcolor(TEMPLATE_COLOR);
        wxTemplate.setTouser(groupMembers.getOpenid());
        wxTemplate.setUrl(GROUP_ACTIVITY_URL + groupActivity.getId());
        
        TemplateData first = new TemplateData();
        first.setValue("恭喜您组团成功");
        wxTemplate.getData().put("first", first);
        //商品名称
        TemplateData templateData = new TemplateData();
        templateData.setValue(groupActivity.getName());
        wxTemplate.getData().put("keyword1", templateData);
        //团长
        templateData = new TemplateData();
        templateData.setValue(groupMemberLeader.getNickname());
        wxTemplate.getData().put("keyword2", templateData);
        //成团人数
        templateData = new TemplateData();
        templateData.setValue(groupActivity.getTeamNumber().toString());
        wxTemplate.getData().put("keyword3", templateData);
        
        templateData = new TemplateData();
        templateData.setValue("您已成功组团，请等待抽奖结果");
        wxTemplate.getData().put("remark", templateData);
        
        String accessToken = WebchatUtil.getAccessToken();    
        String result = Http.Send("POST", TEMPLATE_URL + accessToken, JSONObject.toJSON(wxTemplate).toString());
        //根据发送结果更新状态
        String errcode = JSONObject.parseObject(result).getString("errcode");
        if ("0".equals(errcode)) {
            groupMembers.setIsSend(CommonType.SEND_SUCCESS.getStringValue());
            groupActivityService.saveGroupMemberForm(groupMembers);
        }
        else {
            groupMembers.setIsSend(CommonType.SEND_FAILED.getStringValue());
            groupMembers.setBack(JSONObject.parseObject(result).getString("errmsg"));
            groupActivityService.saveGroupMemberForm(groupMembers);
        }
    }
}

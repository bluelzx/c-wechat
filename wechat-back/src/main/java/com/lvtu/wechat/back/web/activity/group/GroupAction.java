package com.lvtu.wechat.back.web.activity.group;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lvtu.wechat.back.base.BaseActionSupport;
import com.lvtu.wechat.back.utils.ExcelUtil;
import com.lvtu.wechat.back.utils.UploadImg;
import com.lvtu.wechat.common.enums.CommonType;
import com.lvtu.wechat.common.enums.ConstellationsType;
import com.lvtu.wechat.common.enums.GroupIsLeaderType;
import com.lvtu.wechat.common.enums.GroupSortType;
import com.lvtu.wechat.common.enums.GroupStatusType;
import com.lvtu.wechat.common.enums.IsShowType;
import com.lvtu.wechat.common.enums.IsShowLogoType;
import com.lvtu.wechat.common.enums.StatusType;
import com.lvtu.wechat.common.enums.TeamNumberType;
import com.lvtu.wechat.common.model.activity.group.ExportGroupMembers;
import com.lvtu.wechat.common.model.activity.group.GroupActivity;
import com.lvtu.wechat.common.model.activity.group.GroupActivityBanner;
import com.lvtu.wechat.common.model.activity.group.GroupMembers;
import com.lvtu.wechat.common.model.activity.group.UserInfo;
import com.lvtu.wechat.common.model.activity.group.Group;
import com.lvtu.wechat.common.model.qrcode.QRCode;
import com.lvtu.wechat.common.model.qrcode.UseQRCode;
import com.lvtu.wechat.common.service.activity.group.IGroupActivityService;
import com.lvtu.wechat.common.service.qrcode.IQRCodeService;
import com.lvtu.wechat.common.vo.back.GroupActivitiesConditionVo;
import com.lvtu.wechat.common.vo.back.GroupMembersConditionVo;

@Controller
@RequestMapping("${adminPath}/activity")
public class GroupAction extends BaseActionSupport{

    @Autowired
    private IGroupActivityService groupActivitiesService;
    
    @Autowired
    private IQRCodeService qRCodeService;
    
    /**
     * 集齐召唤神龙初始页面
     * @param model
     * @return
     */
    @RequestMapping("/group")
    public String group(GroupActivitiesConditionVo groupActivitiesConditionVo, Model model,HttpServletRequest request,
        HttpServletResponse response) {
        String tab = request.getParameter("tab");
        if (tab == null) {
            tabManage(groupActivitiesConditionVo, model);
            tabStatistical(groupActivitiesConditionVo, model);
            model.addAttribute("tab", "tabManage");
        }
        else if (tab.equals("tabManage")) {
            tabManage(groupActivitiesConditionVo, model);
            tabStatistical(groupActNullVo(), model);
            model.addAttribute("tab", "tabManage");
        }
        else if (tab.equals("tabStatistical")) {
            tabStatistical(groupActivitiesConditionVo, model);
            tabManage(groupActNullVo(), model);
            model.addAttribute("tab", "tabStatistical");
        }
        return "/activity/group/groupManager";
    }
    
    
    /**
     * 活动管理
     * 
     * @param groupActConditionVo
     * @param model
     */
    public void tabManage(GroupActivitiesConditionVo groupActivitiesConditionVo, Model model) {
        StatusType[] statusTypeArr = StatusType.values();
        List<StatusType> statusList = Arrays.asList(statusTypeArr);
        model.addAttribute("conditionManage", groupActivitiesConditionVo);
        PageInfo<GroupActivity> pageInfo = groupActivitiesService.queryGroupActivitiesList(groupActivitiesConditionVo);
        model.addAttribute("groupActListManage", pageInfo.getList());
        model.addAttribute("pageManage", pageInfo);
        model.addAttribute("statusList", statusList);
    }
    
    
    /**
     * 活动统计
     * 
     * @param groupActConditionVo
     * @param model
     */
    private void tabStatistical(GroupActivitiesConditionVo groupActivitiesConditionVo, Model model) {
        StatusType[] statusTypeArr = StatusType.values();
        List<StatusType> statusList = Arrays.asList(statusTypeArr);
        model.addAttribute("conditionStatistical", groupActivitiesConditionVo);
        PageInfo<GroupActivity> pageInfo = groupActivitiesService.queryGroupActivitiesList(groupActivitiesConditionVo);
        model.addAttribute("groupActListStatistical", pageInfo.getList());
        model.addAttribute("pageStatistical", pageInfo);
        model.addAttribute("statusList", statusList);
    }
    
    
    /**
     * 生成空的查询条件
     * 
     * @return
     */
    public GroupActivitiesConditionVo groupActNullVo() {
        GroupActivitiesConditionVo groupActNullVo = new GroupActivitiesConditionVo();
        groupActNullVo.setId(null);
        groupActNullVo.setName(null);
        groupActNullVo.setStartDate(null);
        groupActNullVo.setEndDate(null);
        groupActNullVo.setState(null);
        return groupActNullVo;
    }
    
    
    /**
     * 添加活动
     * @param model
     * @return
     */
    @RequestMapping("/group/new")
    public String newGroupAct(Model model) {
        //获取状态数组
        StatusType[] statusTypeArr = StatusType.getStatusType();
        List<StatusType> statusList = Arrays.asList(statusTypeArr);
        //获取团队人员数量数组
        TeamNumberType[] teamNumberTypeArr = TeamNumberType.values();
        List<TeamNumberType> teamNumberList = Arrays.asList(teamNumberTypeArr);
        //获取用户资料数组
        List<UserInfo> userInfoList = getUserInfoList();
        //获取是否显示数组
        IsShowType[] isShowTypeArr = IsShowType.values();
        List<IsShowType> isShowList = Arrays.asList(isShowTypeArr);
        IsShowLogoType[] isShowLogoTypeArr = IsShowLogoType.values();
        List<IsShowLogoType> isShowLogoList = Arrays.asList(isShowLogoTypeArr);
        
        model.addAttribute("statusList", statusList);
        model.addAttribute("teamNumberList", teamNumberList);
        model.addAttribute("userInfoList", userInfoList);
        model.addAttribute("isShowList", isShowList);
        model.addAttribute("isShowLogoList", isShowLogoList);

        return "/activity/group/groupForm";
    }
    
    /**
     * 编辑活动
     * @param model
     * @return
     */
    @RequestMapping("/group/edit")
    public String editGroupAct(Model model,String id){
        //查询活动
        GroupActivity groupActivitiesform = groupActivitiesService.queryGroupActivitiesById(id);
        //查询活动对应的二维码
        UseQRCode useQRCode = new UseQRCode();
        useQRCode.setActId(id);
        useQRCode = qRCodeService.queryUseQRCode(useQRCode);
        QRCode qRCode= new QRCode();
        if(useQRCode != null){
            qRCode.setId(useQRCode.getWxQRCodeId());
            qRCode=qRCodeService.queryQRCode(qRCode);
        }
        //获取状态数组
        StatusType[] statusTypeArr = StatusType.getStatusType();
        List<StatusType> statusList = Arrays.asList(statusTypeArr);
        //获取团队成员数组
        TeamNumberType[] teamNumberTypeArr = TeamNumberType.values();
        List<TeamNumberType> teamNumberList = Arrays.asList(teamNumberTypeArr);
        //获取用户资料数组
        List<UserInfo> userInfoList =getUserInfoList(groupActivitiesform.getSignParam());
        //获取是否显示数组
        IsShowType[] isShowTypeArr = IsShowType.values();
        List<IsShowType> isShowList = Arrays.asList(isShowTypeArr);
        IsShowLogoType[] isShowLogoTypeArr = IsShowLogoType.values();
        List<IsShowLogoType> isShowLogoList = Arrays.asList(isShowLogoTypeArr);
        model.addAttribute("groupform", groupActivitiesform);
        model.addAttribute("useQRCode", useQRCode);
        model.addAttribute("qRCode", qRCode);
        model.addAttribute("bannerUrlList", groupActivitiesform.bannerList);
        model.addAttribute("prizeList", groupActivitiesform.prizeList);
        model.addAttribute("statusList", statusList);
        model.addAttribute("teamNumberList", teamNumberList);
        model.addAttribute("userInfoList", userInfoList);
        model.addAttribute("isShowList", isShowList);
        model.addAttribute("isShowLogoList", isShowLogoList);

        return "/activity/group/groupForm";
    }
    
    /**
     * 删除分享图片
     * @param redirectAttrs
     * @param key
     * @return
     */
    @RequestMapping("/group/deleteImg")
    @ResponseBody
    public Object delete(RedirectAttributes redirectAttrs, String key) {
        GroupActivityBanner bannerImage = new GroupActivityBanner();
        bannerImage.setId(key);
        groupActivitiesService.deleteBannerImage(bannerImage);
        return getResult();
    }
    
    
    /**
     * 保存添加或者修改活动
     * 
     * @param model
     * @param couponActivityform
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/group/save")
    public String saveGroupAct(Model model, GroupActivity groupActivitiesform) {
        String bannerUrls=StringEscapeUtils.unescapeXml(groupActivitiesform.getBannerUrls());
        JSONObject jsonBannerUrls = JSONObject.parseObject(bannerUrls);
        String prizes=StringEscapeUtils.unescapeXml(groupActivitiesform.getPrizes());
        JSONObject jsonPrizes = JSONObject.parseObject(prizes);
        groupActivitiesService.saveGroupActivityform(groupActivitiesform,jsonBannerUrls,jsonPrizes);
        return "redirect:" + adminPath + "/activity/group";
    }
    
    /**
     * 活动统计详情
     * @param model
     * @param groupActivitiesform
     * @return
     */
    @RequestMapping("/group/view")
    public String groupStatistical(Model model,GroupMembersConditionVo groupMembersConditionVo,GroupActivitiesConditionVo groupActivitiesConditionVo,String actName){
        groupActivitiesConditionVo.setName(actName);
        //用户身份，团长  or 团员
        GroupIsLeaderType[] isLeaderTypeArr = GroupIsLeaderType.values();
        List<GroupIsLeaderType> isLeaderList = Arrays.asList(isLeaderTypeArr);
        //状态数组
        GroupStatusType[] statusTypeArr = GroupStatusType.values();
        List<GroupStatusType> statusList = Arrays.asList(statusTypeArr);
        //排序规则
        GroupSortType[] sortTypeArr = GroupSortType.values();
        List<GroupSortType> sortList = Arrays.asList(sortTypeArr);
        model.addAttribute("conditionStatistical", groupMembersConditionVo);
        //查询对应的活动
        GroupActivity groupActivities=groupActivitiesService.queryGroupActivitiesById(groupMembersConditionVo.getGroupActId());
        //根据活动里的报名参数，构造对应的 用户信息list
        List<UserInfo> userInfoList = getUserInfoList(groupActivities.getSignParam());
        //查询活动对应的团成员
        PageInfo<GroupMembers> pageInfo=groupActivitiesService.queryGroupMembersByGroupId(groupMembersConditionVo);
        //12星座
        ConstellationsType[] constellationsTypeArr = ConstellationsType.values();
        List<ConstellationsType> constellationsList = Arrays.asList(constellationsTypeArr);
        model.addAttribute("userInfoList", userInfoList);
        model.addAttribute("groupMembersList", pageInfo.getList());
        model.addAttribute("pageStatistical", pageInfo);
        model.addAttribute("statusList", statusList);
        model.addAttribute("isLeaderList", isLeaderList);
        model.addAttribute("sortList", sortList);
        model.addAttribute("groupActivitiesConditionVo", groupActivitiesConditionVo);
        model.addAttribute("constellationsList", constellationsList);
        return "/activity/group/groupStatisticalInfo";
    }
    
    
    /**
     * 编辑成员信息
     * @param model
     * @param groupMemberId
     * @return
     */
    @RequestMapping("/group/membersEdit")
    public String membersEdit(Model model,String groupMemberId,String groupActivityId){
        //查询对应的成员信息
        GroupMembers groupMember=groupActivitiesService.queryMemberInfoById(groupMemberId);
        //状态 启用，关闭
        GroupStatusType[] statusTypeArr = GroupStatusType.getStatusType();
        List<GroupStatusType> statusList = Arrays.asList(statusTypeArr);
        //用户身份  开团者 参团者
        GroupIsLeaderType[] isLeaderTypeArr = GroupIsLeaderType.values();
        List<GroupIsLeaderType> isLeaderList = Arrays.asList(isLeaderTypeArr);
        //查询对应的活动
        GroupActivity groupActivities=groupActivitiesService.queryGroupActivitiesById(groupActivityId);
        //12星座
        ConstellationsType[] constellationsTypeArr = ConstellationsType.values();
        List<ConstellationsType> constellationsList = Arrays.asList(constellationsTypeArr);
        model.addAttribute("member", groupMember);
        model.addAttribute("statusList", statusList);
        model.addAttribute("isLeaderStatusList", isLeaderList);
        model.addAttribute("userInfoList", groupActivities.getSignParam());
        model.addAttribute("constellationsList", constellationsList);
        return "/activity/group/groupMembersForm";
    }
    

    /**
     * 
     * 删除团成员信息
     */
    @RequestMapping("/group/membersDelete")
    @ResponseBody
    public Map<String, Object> deleteMember(String groupMemberId, String groupId,RedirectAttributes redirectAttrs) {
    	Map<String, Object> result = getResult();
    	GroupMembers groupMember=groupActivitiesService.queryMemberInfoById(groupMemberId);
    	Group group = groupActivitiesService.queryGroupsByGroupId(groupId);
    	if(GroupIsLeaderType.LEADER.getStringValue().equals(groupMember.getIsLeader()))
    	{ 
    		if (group.getParticipantsNum() > 1) {
    			 result.put("code", "-1");
    		     result.put("msg", "抱歉,该团下面还有其他成员,不能删除团长!");
    		     return result;
    		}
    	}
    	groupActivitiesService.delete(groupMember,group);
    	
    	return result;
	}
    



	/**
     * 保存成员信息
     * @param model
     * @param groupMembersForm
     * @return
     */
    @RequestMapping("/group/saveMember")
    public String saveMember(Model model,GroupMembers groupMembersForm){
        groupActivitiesService.saveGroupMemberForm(groupMembersForm);
        return "redirect:" + adminPath + "/activity/group/view?groupActId="+groupMembersForm.getGroupActivityId();
    }
    /**
     * 切换活动状态（启用或禁用）
     * 
     * @param groupActivities
     * @param redirectAttrs
     * @return
     */
    @RequestMapping("/group/usstate")
    public String state(GroupActivity groupActivities, RedirectAttributes redirectAttrs) {
        groupActivitiesService.updateState(groupActivities);
        if (groupActivities.getState().equals(CommonType.ENABLE.getStringValue())) {
            addMessage(redirectAttrs, "启用状态成功");
        }
        else {
            addMessage(redirectAttrs, "禁用状态成功");
        }

        return "redirect:" + adminPath + "/activity/group";
    }
    
    
    /**
     * 点击生成二维码
     * @param groupId
     * @return
     */
    @RequestMapping("/group/createQRcode")
    @ResponseBody
    public Map<String,Object> createQRcode(String groupId){
        GroupActivity groupActivity = groupActivitiesService.queryGroupActivitiesById(groupId);
        Map<String, Object> result = getResult();
        synchronized (this) {
            result=qRCodeService.aquireQRCode(groupActivity,result);
        }
        return result;
    } 
    
    /**
     * 上传单个图片
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/group/imageUpload")
    @ResponseBody
    public Map<String, Object> imageUpload(Model model, MultipartHttpServletRequest request,String fileElementId,int width,int height,
        HttpServletResponse response) {
        String fileDiv = "_group";
        boolean isFixed = true;// 尺寸固定
        MultipartFile image = request.getFile(fileElementId);
        UploadImg uploadImg = new UploadImg();
        return uploadImg.imageUpload(width, height, isFixed, fileDiv, image);
    }
    
    
    /**
     * 导出数据
     * @param request
     * @param response
     * @param nickname
     * @param name
     * @param telephone
     * @param groupId
     * @param groupState
     * @param sort
     */
    @RequestMapping("/group/export")
    @ResponseBody
    public void export(HttpServletRequest request,HttpServletResponse response,String nickname,String name,String telephone,String groupId,String groupState,String sort,String groupActId) {
        GroupMembersConditionVo groupMembersConditionVo=new GroupMembersConditionVo();
        groupMembersConditionVo.setGroupActId(groupActId);
        groupMembersConditionVo.setNickname(nickname);
        groupMembersConditionVo.setName(name);
        groupMembersConditionVo.setTelephone(telephone);
        groupMembersConditionVo.setGroupId(groupId);
        groupMembersConditionVo.setGroupState(groupState);
        groupMembersConditionVo.setSort(sort);
        logger.info("查询需要导出的数据：" + groupMembersConditionVo.toString());
        PageInfo<ExportGroupMembers> pageInfo=groupActivitiesService.queryExportGroupMembersByGroupId(groupMembersConditionVo);
        List<ExportGroupMembers> groupMembersList=pageInfo.getList();
        for(ExportGroupMembers groupMember:groupMembersList){
            String constellation = ConstellationsType.getConstellationsShowName(groupMember.getConstellation());
            String isLeader = GroupIsLeaderType.getGroupIsLeaderShowName(groupMember.getIsLeader());
            String state = GroupStatusType.getGroupStatusShowName(groupMember.getState());
            groupMember.setConstellation(constellation);
            groupMember.setIsLeader(isLeader);
            groupMember.setState(state);
        }
        logger.info("导出数据条数：" + groupMembersList.size());
        String[] headers = { "微信名", "姓名", "手机号", "年龄", "星座", "省份", "城市" , "身份", "团ID","团状态","目前参团人数","开团时间","成团时间" };
        String codedFileName = null;  
        OutputStream fOut = null; 
        try {
         // 生成提示信息，  
            response.setContentType("application/vnd.ms-excel");  
         // 进行转码，使其支持中文文件名  
            codedFileName = java.net.URLEncoder.encode("活动团组员信息", "UTF-8");  
            response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
            fOut = response.getOutputStream();
            logger.info("导出数据条数：" + groupMembersList.size());
            ExcelUtil.exportExcel(headers, groupMembersList, fOut, "yyyy-MM-dd HH:mm:ss");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 上传多个图片
     * 
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/group/imagesUpload")
    @ResponseBody
    public Map<String, Object> imageUpload(Model model, MultipartHttpServletRequest request,
        HttpServletResponse response) {
        String fileDiv = "_shareTemplate";
        Map<String, Object> result = getResult();
        int width = 750;// 宽度固定750
        int height = 328;// 高度固定328
        boolean isFixed = true;// 尺寸固定
        boolean checkWidth=true;// 是否需要检验宽高
        MultipartFile image = null;
        UploadImg uploadImg = new UploadImg();
        fileDiv = "_bannerImgs";
        image = request.getFile("file_data");
        if (image == null) {
            result.put("code", "-1");
            result.put("msg", "图片上传失败");
            return result;
        }
        result = uploadImg.imageUpload(width, height, isFixed, checkWidth, true, 20, fileDiv, image);
        return result;
    }
    
    public Map<String, Object> getResult() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code", "1");
        result.put("msg", "操作成功");
        return result;
    }
    
    
    /**
     * 获取用户选项
     * @return
     */
    public List<UserInfo> getUserInfoList() {
        List<UserInfo> userInfoList = new ArrayList<UserInfo>();
        userInfoList.add(new UserInfo(1, "姓名", false));
        userInfoList.add(new UserInfo(2, "手机", false));
        userInfoList.add(new UserInfo(3, "年龄", false));
        userInfoList.add(new UserInfo(4, "星座", false));
        userInfoList.add(new UserInfo(5, "省份", false));
        userInfoList.add(new UserInfo(6, "城市", false));
        return userInfoList;
    }
    
    /**
     * 获取用户选项，并且根据数据库记录设置对应的checked属性为true;
     * @param signParam
     * @return
     */
    private List<UserInfo> getUserInfoList(String signParam) {
        List<UserInfo> userInfoList = getUserInfoList();
        String[] signParams = signParam.split(",");
        for (String s : signParams) {
            userInfoList.get(Integer.parseInt(s) - 1).setChecked(true);
        }
        return userInfoList;
    }
}

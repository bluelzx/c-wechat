package com.lvtu.wechat.common.model.activity.group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lvtu.wechat.common.base.BaseModel;

public class GroupActivity extends BaseModel{
    
    /**
     * 
     */
    private static final long serialVersionUID = -5586941454792505290L;

    /**
     * 活动名称
     */
    private String name;
    
    /**
     * 开始时间
     */
    private Date startDate;
    
    /**
     * 结束时间
     */
    private Date endDate;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 报名截止时间
     */
    private Date deadlineTime;
    
    /**
     * url
     */
    private String url;
    
    /**
     * 状态0-关闭，1-开启
     */
    private String state;
    
    /**
     * 分享模板ID
     */
    private String shareTemplateId;
    
    /**
     * 活动人数
     */
    private Integer teamNumber;
    
    /**
     * 报名参数（逗号','分隔）
     */
    private String signParam;
    
    /**
     * 首页重点提示
     */
    private String indexTips;
    
    /**
     * 发起新团按钮
     */
    private String showButton;
    
    /**
     * 活动文案
     */
    private String ruleCopy;
    
    /**
     * 填写页提示
     */
    private String writeTips;
    
    /**
     * 二维码文案
     */
    private String qrCodeCopy;
    
    /**
     * 推送图文消息URL
     */
    private String picUrl;
    
    /**
     * 获奖查询
     */
    private String rewardsUrl;
    
    /**
     * 二次转发量
     */
    private Integer secondaryForward;
    
    /**
     * 参与人数
     */
    private Integer participantsNum;
    
    /**
     * 总团数
     */
    private Integer groupNum;
    
    /**
     * 已成团数
     */
    private Integer completeGroupNum;
    
    /**
     * 二维码图片链接
     */
    private String qRCodeUrl;
    
    /**
     * banner图片集合
     */
    public List<GroupActivityBanner> bannerList = new ArrayList<GroupActivityBanner>();
    
    /**
     * 奖品集合
     */
    public List<GroupActivityPrize> prizeList = new ArrayList<GroupActivityPrize>();
    
    /**
     * banner图片链接
     */
    private String bannerUrls;
    
    /**
     * 奖品文案
     */
    private String prizes;
    /**
     * 公司Logo按钮
     */
    private String logoButton;
    
    /**
     * 扩展字段
     */
    private String back;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(Date deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShareTemplateId() {
        return shareTemplateId;
    }

    public void setShareTemplateId(String shareTemplateId) {
        this.shareTemplateId = shareTemplateId;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getSignParam() {
        return signParam;
    }

    public void setSignParam(String signParam) {
        this.signParam = signParam;
    }

    public String getIndexTips() {
        return indexTips;
    }

    public void setIndexTips(String indexTips) {
        this.indexTips = indexTips;
    }

    public String getShowButton() {
        return showButton;
    }

    public void setShowButton(String showButton) {
        this.showButton = showButton;
    }

    public String getRuleCopy() {
        return ruleCopy;
    }

    public void setRuleCopy(String ruleCopy) {
        this.ruleCopy = ruleCopy;
    }

    public String getWriteTips() {
        return writeTips;
    }

    public void setWriteTips(String writeTips) {
        this.writeTips = writeTips;
    }

    public String getQrCodeCopy() {
        return qrCodeCopy;
    }

    public void setQrCodeCopy(String qrCodeCopy) {
        this.qrCodeCopy = qrCodeCopy;
    }
    
    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getRewardsUrl() {
        return rewardsUrl;
    }

    public void setRewardsUrl(String rewardsUrl) {
        this.rewardsUrl = rewardsUrl;
    }
    
    public Integer getSecondaryForward() {
        return secondaryForward;
    }

    public void setSecondaryForward(Integer secondaryForward) {
        this.secondaryForward = secondaryForward;
    }

    public Integer getParticipantsNum() {
        return participantsNum;
    }

    public void setParticipantsNum(Integer participantsNum) {
        this.participantsNum = participantsNum;
    }

    public Integer getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(Integer groupNum) {
        this.groupNum = groupNum;
    }

    public Integer getCompleteGroupNum() {
        return completeGroupNum;
    }

    public void setCompleteGroupNum(Integer completeGroupNum) {
        this.completeGroupNum = completeGroupNum;
    }
    
    public String getqRCodeUrl() {
        return qRCodeUrl;
    }

    public void setqRCodeUrl(String qRCodeUrl) {
        this.qRCodeUrl = qRCodeUrl;
    }

    public String getBannerUrls() {
        return bannerUrls;
    }

    public void setBannerUrls(String bannerUrls) {
        this.bannerUrls = bannerUrls;
    }
    
    public String getPrizes() {
        return prizes;
    }

    public void setPrizes(String prizes) {
        this.prizes = prizes;
    }
    
    public String getLogoButton() {
		return logoButton;
	}

	public void setLogoButton(String logoButton) {
		this.logoButton = logoButton;
	}

	public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }
    
}

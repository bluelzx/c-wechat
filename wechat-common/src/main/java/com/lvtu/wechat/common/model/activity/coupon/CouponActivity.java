package com.lvtu.wechat.common.model.activity.coupon;

import java.util.Date;

import com.lvtu.wechat.common.base.BaseModel;

public class CouponActivity extends BaseModel {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3643922466091137305L;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 活动起始时间
     */
    private Date startDate;

    /**
     * 活动结束时间
     */
    private Date endDate;

    /**
     * 状态（1-开启，0-关闭）
     */
    private String state;
    
    /**
     * 分享模板ID
     */
    private String shareTemplateId;

    /**
     * 礼品方式
     */
    private String giftType;

    /**
     * 电子券名称
     */
    private String couponName;

    /**
     * URL
     */
    private String url;

    /**
     * 电子券总数
     */
    private Integer totalNum;

    /**
     * 可使用次数
     */
    private Integer usableNum;
    
    /**
     * 已使用次数
     */
    private Integer usedNum; 
    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 领取页副标题
     */
    private String subTitle;

    /**
     * 领取成功副标题
     */
    private String succSubTitle;

    /**
     * 领取页温馨提示
     */
    private String tips;
    
    /**
     * 领取成功使用说明
     */
    private String instructions;
    
    /**
     * 成功页图片URL
     */
    private String imgUrl;

    /**
     * 访问量
     */
    private Integer pageViews;
    
    
    /**
     * 新用户
     */
    private Integer newUser;
    
    /**
     * 老用户
     */
    private Integer oldUser;
    

    /**
     * 扩展字段1
     */
    private String back1;

    /**
     * 扩展字段2
     */
    private String back2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
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

    public String getGiftType() {
        return giftType;
    }

    public void setGiftType(String giftType) {
        this.giftType = giftType;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getUsableNum() {
        return usableNum;
    }

    public void setUsableNum(Integer usableNum) {
        this.usableNum = usableNum;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSuccSubTitle() {
        return succSubTitle;
    }

    public void setSuccSubTitle(String succSubTitle) {
        this.succSubTitle = succSubTitle;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public void setPageViews(Integer pageViews) {
        this.pageViews = pageViews;
    }

    public String getBack1() {
        return back1;
    }

    public void setBack1(String back1) {
        this.back1 = back1;
    }

    public String getBack2() {
        return back2;
    }

    public void setBack2(String back2) {
        this.back2 = back2;
    }

	public Integer getUsedNum() {
		return usedNum;
	}

	public void setUsedNum(Integer usedNum) {
		this.usedNum = usedNum;
	}

	public Integer getNewUser() {
		return newUser;
	}

	public void setNewUser(Integer newUser) {
		this.newUser = newUser;
	}

	public Integer getOldUser() {
		return oldUser;
	}

	public void setOldUser(Integer oldUser) {
		this.oldUser = oldUser;
	}

	
}
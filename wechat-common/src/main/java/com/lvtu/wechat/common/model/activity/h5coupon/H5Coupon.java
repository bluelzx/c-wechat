package com.lvtu.wechat.common.model.activity.h5coupon;

import java.util.Date;
import java.util.List;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 微信H5优惠券活动
 * @author qianqc
 *
 */
public class H5Coupon extends BaseModel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5704826851325788729L;

    /**
     * 活动名称
     */
    private String name;
    
    /**
     * 使用公众号平台类型(1-订阅号,2-服务号)
     */
    private String publicPlatformType;
    
    /**
     * 优惠券批次号
     */
    private String couponCodes;
    
    /**
     * 品牌商
     */
    private String brandBusiness;
    
    /**
     * 分享模版ID
     */
    private String shareTemplateId;
    
    /**
     * 开始时间
     */
    private Date startDate;
    
    /**
     * 结束时间
     */
    private Date endDate;
    
    /**
     * 状态(0-关闭，1开启)
     */
    private String state;
    
    /**
     * banner图片URL
     */
    private String bannerUrl;
    
    /**
     * 活动规则文案
     */
    private String ruleExplain;
    
    /**
     * 第一次领取文案
     */
    private String firstExplain;
    
    /**
     * 多次领取文案
     */
    private String multyExplain;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 扩展字段
     */
    private String back1;
    
    /**
     * 扩展字段2
     */
    private String back2;
    
    /**
     * 推荐产品集合
     */
    private List<H5CouponRcmdPrd> h5CouponRcmdPrdList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicPlatformType() {
        return publicPlatformType;
    }

    public void setPublicPlatformType(String publicPlatformType) {
        this.publicPlatformType = publicPlatformType;
    }

    public String getCouponCodes() {
        return couponCodes;
    }

    public void setCouponCodes(String couponCodes) {
        this.couponCodes = couponCodes;
    }
    
    public String getBrandBusiness() {
        return brandBusiness;
    }

    public void setBrandBusiness(String brandBusiness) {
        this.brandBusiness = brandBusiness;
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

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getRuleExplain() {
        return ruleExplain;
    }

    public void setRuleExplain(String ruleExplain) {
        this.ruleExplain = ruleExplain;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public List<H5CouponRcmdPrd> getH5CouponRcmdPrdList() {
        return h5CouponRcmdPrdList;
    }

    public void setH5CouponRcmdPrdList(List<H5CouponRcmdPrd> h5CouponRcmdPrdList) {
        this.h5CouponRcmdPrdList = h5CouponRcmdPrdList;
    }

    public String getShareTemplateId() {
        return shareTemplateId;
    }

    public void setShareTemplateId(String shareTemplateId) {
        this.shareTemplateId = shareTemplateId;
    }

    public String getFirstExplain() {
        return firstExplain;
    }

    public void setFirstExplain(String firstExplain) {
        this.firstExplain = firstExplain;
    }

    public String getMultyExplain() {
        return multyExplain;
    }

    public void setMultyExplain(String multyExplain) {
        this.multyExplain = multyExplain;
    }
    
}

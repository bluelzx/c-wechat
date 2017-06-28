package com.lvtu.wechat.common.model.advertising;

import com.lvtu.wechat.common.base.BaseModel;

public class Advertising extends BaseModel{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1744457107575591365L;
    
    /**
     * 一级分类
     */
    private String primaryClassification;

    /**
     * 二级分类
     */
    private String secondaryClassification;
    
    /**
     * 图片链接
     */
    private String imgURL;
    
    /**
     * 跳转链接
     */
    private String skipURL;

    /**
     * 活动ID
     */
    private String actId;
    
    /**
     * 排序
     */
    private Integer sort;

    public String getPrimaryClassification() {
        return primaryClassification;
    }

    public void setPrimaryClassification(String primaryClassification) {
        this.primaryClassification = primaryClassification;
    }

    public String getSecondaryClassification() {
        return secondaryClassification;
    }

    public void setSecondaryClassification(String secondaryClassification) {
        this.secondaryClassification = secondaryClassification;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
    
    public String getSkipURL() {
        return skipURL;
    }

    public void setSkipURL(String skipURL) {
        this.skipURL = skipURL;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    
}

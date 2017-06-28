package com.lvtu.wechat.common.vo.back;

import com.lvtu.wechat.common.base.BaseCondition;

/**
 * @author wxlizhi
 */
public class AdvertisingConditionVo extends BaseCondition {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7754890958427875735L;

    /**
     * 一级分类
     */
    private String primaryClassification;

    /**
     * 二级分类
     */
    private String secondaryClassification;

    /**
     * 活动ID
     */
    private String actId;

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

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }
    
}

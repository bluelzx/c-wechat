package com.lvtu.wechat.common.model.activity.group;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 组团活动奖品表
 * @author qianqc
 *
 */
public class GroupActivityPrize extends BaseModel {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4048427904641032180L;
    
    /**
     * 组团活动表id
     */
    private String groupActivityId;
    
    /**
     * 奖品等级
     */
    private String prizeLevel;
    
    /**
     * 奖品描述活动
     */
    private String prizeDetail;
    
    /**
     * 奖品图片URL
     */
    private String url;
    /**
     * 奖品次序
     */
    private String prizeOrder;
    /**
     * 图片跳转链接
     */
    private String linkUrl;
    /**
     * 奖项名额
     */
    private String peopleNum;
    
    /**
     * 备用字段1
     */
    private String back1;
    
    /**
     * 备用字段2
     */
    private String back2;

    public String getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(String groupActivityId) {
        this.groupActivityId = groupActivityId;
    }

    public String getPrizeLevel() {
        return prizeLevel;
    }

    public void setPrizeLevel(String prizeLevel) {
        this.prizeLevel = prizeLevel;
    }

    public String getPrizeDetail() {
        return prizeDetail;
    }

    public void setPrizeDetail(String prizeDetail) {
        this.prizeDetail = prizeDetail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    

    public String getPrizeOrder() {
		return prizeOrder;
	}

	public void setPrizeOrder(String prizeOrder) {
		this.prizeOrder = prizeOrder;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(String peopleNum) {
		this.peopleNum = peopleNum;
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
    
}

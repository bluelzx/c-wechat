package com.lvtu.wechat.common.model.activity.group;

import com.lvtu.wechat.common.base.BaseModel;

/**
 * 组团活动顶部滚动图片
 * @author qianqc
 *
 */
public class GroupActivityBanner extends BaseModel {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5558391922257387482L;

    /**
     * 组团活动表id
     */
    private String groupActivityId;
    
    /**
     * 图片的url链接
     */
    private String url;
    
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

package com.lvtu.wechat.common.enums;

public enum ClassificationType {
    
    ALL(0,0," "," ","全部"),
    SIGNFLOW(0,1,"primary","signFlow","签到送流量"),
    H5COUPON(0,2,"primary","h5coupon","H5送优惠券活动"),
    SIGN(1,11,"secondary","sign","签到"),
    RECEIVE(1,12,"secondary","receive","领取"),
    GIVING(2,21,"secondary","giving","赠送");

    /**
     * 父节点id
     */
    private Integer parentId;

    /**
     * 自身节点id
     */
    private Integer id;
    
    /**
     * 分类级别
     */
    private String type;

    /**
     * 分类
     */
    private String classification;

    /**
     * 展示内容
     */
    private String showName;

    

    private ClassificationType(Integer parentId, Integer id, String type, String classification, String showName) {
        this.parentId = parentId;
        this.id = id;
        this.type = type;
        this.classification = classification;
        this.showName = showName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

}

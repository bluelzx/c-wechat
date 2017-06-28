package com.lvtu.wechat.common.enums;

/**
 * 产品类型 enum
 * @author qianqc
 *
 */
public enum ProductType {
    ROUTE(1, "线路"),
    TICKET(2, "门票"),
    HOTEL(3, "酒店"),
    VISA(4, "签证"),
    CRUISE(5, "游轮");
    
    private ProductType(Integer value, String showName) {
        this.value = value;
        this.showName = showName;
    }
    
    public Integer getValue() {
        return value;
    }
    public String getShowName() {
        return showName;
    }

    private Integer value;
    
    private String showName;
    
    /**
     * 根据h5优惠券业务定制私有的获取enum数组
     * @return
     */
    public static ProductType[] h5CouponProductType() {
        ProductType[] productTypeArr = new ProductType[4];
        productTypeArr[0] = ProductType.ROUTE;
        productTypeArr[1] = ProductType.TICKET;
//        productTypeArr[2] = ProductType.HOTEL;
        productTypeArr[2] = ProductType.VISA;
        productTypeArr[3] = ProductType.CRUISE;
        return productTypeArr;
    }

}

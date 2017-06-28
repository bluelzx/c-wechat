package com.lvtu.wechat.common.enums;

/** 
* @ClassName: ProductOrderType 
* @Description: 甩尾产品类型
* @author zhengchongxiang
* @date 2016-9-5 下午5:01:18  
*/
public enum ProductOrderType {
    OUT_TOURISM(1 ,"出境游"),
    IN_TOURISM(2 ,"国内游"),
    SPECIAL_TICKET(3 ,"特价机票"),
    HOME_TOURISM(4,"亲子游"),
    HOT_TICKET(5,"特价门票");
    
    private Integer value;
    private String showName;

    private ProductOrderType(Integer value, String showName) {
        this.value = value;
        this.showName = showName;
    }
    public String getShowName() {
        return showName;
    }
	public Integer getValue() {
		return value;
	}
}

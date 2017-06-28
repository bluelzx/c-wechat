package com.lvtu.wechat.common.enums;

public enum PrizeLevel {
    ONE(1, "一等奖"),
    TWO(2, "二等奖"),
    THREE(3, "三等奖"),
    FOUR(4, "四等奖"),
    FIVE(5, "五等奖"),
    SIX(6, "六等奖"),
    SEVEN(7, "七等奖"),
    EIGHT(8, "八等奖"),
    NINE(9, "九等奖"),
    TEN(10, "十等奖");
    
    private Integer value;
    private String showName;

    private PrizeLevel(Integer value, String showName) {
        this.value = value;
        this.showName = showName;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public String getStringValue() {
        return value.toString();
    }
    
    public String getShowName(){
        return showName;
    }
    
    /**
     * 获取几位长度的奖品数组
     * @param num
     * @return
     */
    public static PrizeLevel[] getPrizeLevlArray(int num) {
        PrizeLevel[] prizeLevels = PrizeLevel.values();
        PrizeLevel[] destPrizeLevels = new PrizeLevel[num];
        System.arraycopy(prizeLevels, 0, destPrizeLevels, 0, num);
        return destPrizeLevels;
    }
    
    
}

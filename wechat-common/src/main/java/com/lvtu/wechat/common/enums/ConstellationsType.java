package com.lvtu.wechat.common.enums;

/**
 * 星座
 * @author qianqc
 *
 */
public enum ConstellationsType {
    Aries(0, "白羊座"),
    Taurus(1, "金牛座"),
    Gemini(2, "双子座"),
    Cancer(3, "巨蟹座"),
    Leo(4, "狮子座"),
    Virgo(5, "处女座"),
    Libra(6, "天秤座"),
    Scorpius(7, "天蝎座"),
    Sagittarius(8, "射手座"),
    Capricornus(9, "摩羯座"),
    Aquarius(10, "水瓶座"),
    Pisces(11, "双鱼座");
    
    private Integer value;
    
    private String showName;

    private ConstellationsType(Integer value, String showName) {
        this.value = value;
        this.showName = showName;
    }
    
    public String getShowName() {
        return showName;
    }
    
    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getValue() {
        return value.toString();
    }

    public static String getConstellationsShowName(String value){
        String showName="";
        if(value!=null){
            ConstellationsType[] constellationsTypes = ConstellationsType.values();
            for(ConstellationsType constellationsType : constellationsTypes){
                if(constellationsType.getValue().equals(value)){
                    showName=constellationsType.getShowName();
                    break;
                }
            } 
        }
        return showName;
    }
}

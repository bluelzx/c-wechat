package com.lvtu.wechat.common.enums;

/**
 * 用户资料通用enum类
 * @author qianqc
 *
 */
public enum UserInfoType {
    
    NAME(1, "姓名", false),
    TELE(2, "手机", false),
    AGE(3, "年龄", false),
    CONST(4, "星座", false),
    PROVINCE(5, "省份", false),
    CITY(6, "城市", false);
    
    
    private Integer value;
    private String showName;
    private boolean checked;
    
    private UserInfoType(Integer value, String showName, boolean checked) {
        this.value = value;
        this.showName = showName;
        this.checked = checked;
    }
    
    public String getShowName() {
        return showName;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public String getStringValue() {
        return value.toString();
    }
    
    public boolean getChecked() {
        return checked;
    }
    
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
    /**
     * 根据集齐好友抽奖活动定制userInfoType数组
     * @return
     */
    public static UserInfoType[] userInfoTypeArray() {
        UserInfoType[] userInfoTypeArr = new UserInfoType[6];
        userInfoTypeArr[0] = UserInfoType.NAME;
        userInfoTypeArr[1] = UserInfoType.TELE;
        userInfoTypeArr[2] = UserInfoType.AGE;
        userInfoTypeArr[3] = UserInfoType.CONST;
        userInfoTypeArr[4] = UserInfoType.PROVINCE;
        userInfoTypeArr[5] = UserInfoType.CITY;
        return userInfoTypeArr;
        
    }
    
    /**
     * 根据1,2,3,4 类型返回数组，对应的数组内容checked为true
     * @param userInfoTypes
     * @return
     */
    public static UserInfoType[] getUserInfoTypeArray(String userInfoTypes) {
        UserInfoType[] userInfoTypeArr = UserInfoType.userInfoTypeArray();
        String[] userInfoTypeValus = userInfoTypes.split(",");
        int _userInfoTypeValue = 0;
        for (String userInfoTypeValue : userInfoTypeValus) {
            _userInfoTypeValue = Integer.valueOf(userInfoTypeValue)-1;
            userInfoTypeArr[_userInfoTypeValue].setChecked(true);
        }
        return userInfoTypeArr;
    }
    
    public static UserInfoType[] getUserInfoTypeArrayBack(String userInfoTypes) {
        UserInfoType[] userInfoTypeArr = UserInfoType.userInfoTypeArray();
        String[] userInfoTypeValus = userInfoTypes.split(",");
        Integer _userInfoTypeValue;
        for(UserInfoType userInfo : userInfoTypeArr){
            for (String userInfoTypeValue : userInfoTypeValus) {
                _userInfoTypeValue = Integer.valueOf(userInfoTypeValue);
                if(_userInfoTypeValue.equals(userInfo.value)){
                    userInfo.setChecked(true);
                }
            }
        }
        return userInfoTypeArr;
    }
    
    public static UserInfoType[] getUserInfoTypeArrayNew() {
        UserInfoType[] userInfoTypeArr = UserInfoType.userInfoTypeArray();
        for(UserInfoType userInfo : userInfoTypeArr){
            userInfo.setChecked(false);
        }
        return userInfoTypeArr;
    }
    
    public static String getUserInfoTypeShowName(String userInfoTypes){
        UserInfoType[] userInfoTypeArr = UserInfoType.userInfoTypeArray();
        String[] userInfoTypeValus = userInfoTypes.split(",");
        Integer _userInfoTypeValue;
        String typeShowName="";
        for(UserInfoType userInfo : userInfoTypeArr){
            for (String userInfoTypeValue : userInfoTypeValus) {
                _userInfoTypeValue = Integer.valueOf(userInfoTypeValue);
                if(_userInfoTypeValue.equals(userInfo.value)){
                    typeShowName+=userInfo.showName+",";
                }
            }
        }
        typeShowName=typeShowName.substring(0, typeShowName.length()-1);
        return typeShowName;
    }
}

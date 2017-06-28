package com.lvtu.wechat.front.utils;

import org.apache.commons.lang3.StringUtils;

import com.lvtu.wechat.common.utils.Constants;

public class PhoneUtil {

    /**
     * 中国移动电话号码正则表达式
     */
    public static String cmccNumReg = "(134|135|136|137|138|139|150|151|152|157|158|159|182|183|184|187|188|147|178)[0-9]{8}";

    /**
     * 中国联通电话号码正则表达式
     */
    public static String cuccNumReg = "(130|131|132|155|156|185|186|145|175|176)[0-9]{8}";

    /**
     * 中国电信电话号码正则表达式
     */
    public static String ctccNumReg = "(133|153|180|181|189|177)[0-9]{8}";

    /**
     * 手机号正则表达
     */
    private static String phoneReg = "^1[3|4|5|7|8]\\d{9}$";

    /**
     * 读取配置,若有配置覆盖定义
     */
    static {
        String cmccReg = Constants.getConfig("cmcc_num_reg");
        String cuccReg = Constants.getConfig("cucc_num_reg");
        String ctccReg = Constants.getConfig("ctcc_num_reg");

        if (StringUtils.isNotBlank(cmccReg))
            cmccNumReg = cmccReg;
        if (StringUtils.isNotBlank(cmccReg))
            cuccNumReg = cuccReg;
        if (StringUtils.isNotBlank(cmccReg))
            ctccNumReg = ctccReg;
    }

    /**
     * 根据电话号码得到手机号运营商
     * 
     * @param phoneNum
     * @return
     */
    public static String getPhoneOperator(String phoneNum) {
        if (StringUtils.isBlank(phoneNum))
            return null;

        if (phoneNum.matches(cmccNumReg))
            return Constants.CMCC_CODE;
        if (phoneNum.matches(cuccNumReg))
            return Constants.CUCC_CODE;
        if (phoneNum.matches(ctccNumReg))
            return Constants.CTCC_CODE;

        return "";
    }

    /**
     * 验证号码是否符合三大运营商
     * 
     * @param phoneNum
     * @return
     */
    public static boolean isValide(String phoneNum) {
        if (StringUtils.isBlank(phoneNum))
            return false;

        if (phoneNum.matches(cmccNumReg) || phoneNum.matches(cuccNumReg) || phoneNum.matches(ctccNumReg))
            return true;

        return false;
    }

    /**
     * 验证号码是否为手机号
     * @param phoneNum
     * @return boolean
     */
    public static boolean isPhoneNum(String phoneNum) {
        if (StringUtils.isBlank(phoneNum)) {
            return false;
        }
        if (phoneNum.matches(phoneReg)) {
            return true;
        }
        return false;
    }
}

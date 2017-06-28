package com.lvtu.wechat.back.utils;

import com.lvtu.wechat.common.utils.PropertiesLoader;

public class Constants {
    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("wechat-back.properties");
    
    /**
     * 获取配置
     * 
     * @param key
     * @return
     */
    public static String getConfig(String key) {
        return loader.getProperty(key);
    }   
}
    
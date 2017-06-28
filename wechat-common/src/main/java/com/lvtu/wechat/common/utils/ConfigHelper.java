package com.lvtu.wechat.common.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {
	private static Properties p = new Properties();
//	public static int THREAD_NUM = 1;
//	public static int STEP_LEN = 1000;
	public static boolean DEBUG_MODE = false;


	public static String getString(String keyword) {
		return p.getProperty(keyword);
	}

	/**
	 * 根据路径返回配置文件
	 * 
	 * @param path
	 * @return
	 */
	public static final Properties getProperties(String path) {
		Properties properties = new Properties();
		InputStream inputStream = null;
		try
		{
			inputStream = ConfigHelper.class.getClassLoader().getResourceAsStream(path);
			properties.load(inputStream);			
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}finally
		{
			if(null !=inputStream ){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
}
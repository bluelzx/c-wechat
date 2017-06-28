package com.lvtu.wechat.front.utils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.lvtu.wechat.common.model.productorder.City;
import com.lvtu.wechat.common.service.productorder.IAreasService;
import com.lvtu.wechat.common.spring.SpringBeanProxy;
import com.lvtu.wechat.common.utils.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
* @ClassName: Areasutils 
* @Description: 甩尾产品订阅获取用户地理位置帮助类
* @author zhengchongxiang
* @date 2017-3-13 下午3:33:55  
*/
public class Areasutils {

	private static IAreasService areasService = SpringBeanProxy.getBean(
			IAreasService.class, "areasService");

	public static Map<String, Integer> getAdd(String log, String lat,
			String provinceName, String cityName) {
		// lat 小 log 大
		// 参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
		String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat
				+ "," + log + "&type=010";
		String res = "";
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			if(StringUtil.isEmptyString(lat) || StringUtil.isEmptyString(log)){								
				if (provinceName.equals("上海")
						|| provinceName.equals("北京")
						|| provinceName.equals("天津")
						|| provinceName.equals("重庆")
						|| provinceName.equals("香港")
						|| provinceName.equals("澳门")
						|| provinceName.equals("台湾")) {
					City city = areasService.selectByName(provinceName);
					map.put("provinceId", city.getProvinceId());
					map.put("cityId", city.getCode());
				} else {
					City city = areasService.selectByName(cityName);
					if (null != city) {
						map.put("provinceId", city.getProvinceId());
						map.put("cityId", city.getCode());
					} else {
						map.put("provinceId", 35);
						map.put("cityId", 92);
					}
				}				
				return map;				
			}
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			java.io.BufferedReader in = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line + "\n";
			}
			in.close();
			JSONObject jsonObject = JSONObject.fromObject(res);
			JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));
			JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));
			String allAdd = j_2.getString("admName");
			if (StringUtil.isNotEmptyString(allAdd)) {
				String arr[] = allAdd.split(",");
				provinceName = arr[0].substring(0, 2) + "%";
				cityName = arr[1].substring(0, 2) + "%";
				Integer provinceId = areasService.selectProvinceByName(provinceName);
				map.put("provinceId", provinceId);
				City city = areasService.selectByLikeName(cityName, provinceId);
				map.put("cityId", city.getCode());
			}else{//获取不到地理位置信息默认设置为海外
				map.put("provinceId", 35);
				map.put("cityId", 392);
			}

		} catch (Exception e) {
			map.put("provinceId", 35);
			map.put("cityId", 392);
		}
		return map;
	}
	
	public static void main(String[] args) {
		getAdd("112.20","112.20",null,null);
	}

}

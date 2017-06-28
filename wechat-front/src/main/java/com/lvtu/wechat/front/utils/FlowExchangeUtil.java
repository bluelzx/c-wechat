package com.lvtu.wechat.front.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.comm.utils.MemcachedUtil;
import com.lvtu.wechat.common.exception.CustomerException;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.utils.HttpsPayUtils;
import com.lvtu.wechat.common.utils.MD5;
import com.lvtu.wechat.common.utils.RSAUtil;

/**
 * @author zhengchongxiang
 * 
 */
public class FlowExchangeUtil {

	private static Logger logger = Logger.getLogger(FlowChargeUtils.class);

	/**
	 * 拼接请求url
	 * @param params
	 * @param uri
	 * @return
	 */
	protected static String generateUrl(Map<String, String> params, String uri) {
		List<NameValuePair> paramList = makeParams(params);
		StringBuffer geturl = new StringBuffer(uri + "?");
		for (int j = 0; j < paramList.size(); j++) {
			try {
				geturl.append(paramList.get(j).getName()+ "="
						+ URLEncoder.encode(paramList.get(j).getValue(),"utf-8") + "&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		String murl = geturl.toString();
		if (murl.endsWith("&")) {
			murl = murl.substring(0, murl.length() - 1);
		}
		return murl;
	}

	/**
	 * 参数组合
	 * @param params
	 * @return
	 */
	private static List<NameValuePair> makeParams(Map<String, String> params) {
		params.put("authTimespan",DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		String authAppKey = Constants.getConfig("auth_app_key");
		String authSecrect = Constants.getConfig("auth_secrect");
		String prestr = authAppKey;
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			prestr = prestr + key + "=" + value;
			paramList.add(new NameValuePair(key, value));
		}
		prestr += authSecrect;
		String mysign = MD5.toMd5(prestr);
		paramList.add(new NameValuePair("authAppkey", authAppKey));
		paramList.add(new NameValuePair("authSign", mysign));
		return paramList;
	}

	/**
	 * 流量订购
	 * @param mobile 手机号
	 * @param packCode 产品编号
	 * @param orderId 订单ID
	 * @return boolean true/false
	 * @throws Exception
	 */
	public static boolean buyFlow(String mobile, String productId,
			String orderNo) throws CustomerException {
		String uri = Constants.getConfig("flow_exchange_url");
		String notifyUrl = Constants.getConfig("notify_url");
		Map<String, String> params = new HashMap<String, String>();
		try {
			String puk = getPublicKey();// rsa加密公匙
			params.put("mobile", RSAUtil.encryptByPublicKey(mobile, puk));
			params.put("productId", productId);
			params.put("partnerOrderNo", orderNo);
			params.put("notifyUrl", notifyUrl);
			String getUrl = generateUrl(params, uri);
			logger.info("充值调用URL=" + getUrl);
			String result = HttpsPayUtils.requestGet(getUrl);
			logger.info("流量订购接口返回result=" + result);
			JSONObject obj = (JSONObject) JSONObject.parse(result);
			if (obj.getString("code").equals("0000")) {
				JSONObject dataObj = (JSONObject) obj.get("data");
				String returnOrderNo = (String) dataObj.get("orderNo");
				if (StringUtils.isNotBlank(returnOrderNo)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new CustomerException("流量充值失败");
		}
		return false;
	}

	/**
	 * 由订单号获取订单信息是否充值完成
	 * @param orderNo
	 * @return
	 */
	public static boolean isCommitedOrder(String orderNo) {
		try {
			String uri = Constants.getConfig("flow_order_query_url");
			Map<String, String> params = new HashMap<String, String>();
			params.put("partnerOrderNo", orderNo);
			String getUrl = generateUrl(params, uri);
			logger.info("请求URL=" + getUrl);
			String result = HttpsPayUtils.requestGet(getUrl);
			logger.info("由订单号查询订单信息result：" + result);
			if (result != null && JSONObject.parseObject(result).get("code").equals("0000")) {
				JSONObject orderJson = JSONObject.parseObject(result);
				JSONObject js = (JSONObject) orderJson.get("data");
				Integer status = (Integer) js.get("status");
				// status==1 充值完成
				if (status.equals(1)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
		return false;
	}

	/**
	 * 根据手机号获取可购买商品code
	 * @param mobile 手机号
	 * @param flowCode 流量包大小
	 * @return String 产品ID
	 */
	public static String getProductListByMobile(String mobile, String ExFlowSize)
			throws CustomerException {
		String uri = Constants.getConfig("flow_product_by_mobile_url");
		Map<String, String> params = new HashMap<String, String>();
		String productId = "";
		try {
			String puk = getPublicKey();// rsa加密公匙
			params.put("mobile", RSAUtil.encryptByPublicKey(mobile, puk));
			String getUrl = generateUrl(params, uri);
			logger.info("手机号获得可订购商品URL=" + getUrl);
			String result = HttpsPayUtils.requestGet(getUrl);
			logger.info("根据手机号获取可购买流量信息：result=" + result);
			JSONObject flowPacksJson = JSONObject.parseObject(result);
			String code = flowPacksJson.getString("code");
			if (StringUtils.isNotBlank(code) && code.equals("0000")) {
				JSONArray ptjsonData = (JSONArray) flowPacksJson.get("data");
				for (int i = 0; i < ptjsonData.size(); i++) {
					JSONObject jso = (JSONObject) ptjsonData.get(i);
					String flowSize = jso.getString("flowSize") + "";
					String cityName = jso.getString("cityName") + "";
					if (flowSize.equals(ExFlowSize) && !cityName.equals("全国")) {
						productId = jso.getString("id");
					}
				}
				if (productId.equals("")) {
					for (int i = 0; i < ptjsonData.size(); i++) {
						JSONObject jso = (JSONObject) ptjsonData.get(i);
						String flowSize = jso.getString("flowSize") + "";
						String cityName = jso.getString("cityName") + "";
						if (flowSize.equals(ExFlowSize) && cityName.equals("全国")) {
							productId = jso.getString("id");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			throw new CustomerException("领取流量包失败，请稍后再试！");
		}
		logger.info("选择订购产品编号="+productId);
		return productId;
	}

	/**
	 * 调用瑞冀流量接口获取公匙
	 * @return
	 */
	public static String getPublicKey() {
		MemcachedUtil mem = MemcachedUtil.getInstance();
		// 本地缓存中查找，若存在直接返回
		String pubkey = (String) mem.get(Constants.WX_RSA_PUBLIC_KEY);
		if (StringUtils.isNotBlank(pubkey)) {
			logger.info("-------------缓存中取出pubkey-----------");
			return pubkey;
		}
		String publicKeyURL = Constants.getConfig("get_pubkey_url");
		try {
			String result = HttpsPayUtils.requestGet(publicKeyURL);
			if (result != null) {
				JSONObject ptjsonObject = JSONObject.parseObject(result);
				JSONObject ptjsonData = (JSONObject) ptjsonObject.get("data");
				String keys = ptjsonData.getString("publicKey");
				if (StringUtils.isNotBlank(keys)) {
					// 加入缓存1天
					logger.info("获取公匙放入缓存pubkey=" + keys);
					mem.set(Constants.WX_RSA_PUBLIC_KEY, 60 * 60 * 24, keys);
					return keys;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		logger.info("RSA加密获取公匙失败");
		return null;
	}

	/**
	 * 调用瑞冀查询余额接口
	 * @return 余额是否充足
	 */
	public static boolean isOutOfBlance() {
		try {
			String uri = Constants.getConfig("flow_balance_query_url");
			Map<String, String> params = new HashMap<String, String>();
			String getUrl = generateUrl(params, uri);
			logger.info("请求URL=" + getUrl);
			String result = HttpsPayUtils.requestGet(getUrl);
			logger.info("查询余额信息result：" + result);
			if (result != null && JSONObject.parseObject(result).get("code").equals("0000")) {
				JSONObject orderJson = JSONObject.parseObject(result);
				JSONObject js = (JSONObject) orderJson.get("data");
				double balance = js.getDouble("balance");
				// 余额小于20元显示不足
				if (balance > 2000) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询余额接口调用失败");
		}
		return false;
	}
}

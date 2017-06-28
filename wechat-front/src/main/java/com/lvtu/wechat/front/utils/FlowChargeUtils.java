package com.lvtu.wechat.front.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import net.sf.json.xml.XMLSerializer;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.lvtu.wechat.common.utils.Constants;
import com.lvtu.wechat.common.utils.DateUtils;
import com.lvtu.wechat.common.utils.HttpsPayUtils;

/**
 * 流量充值工具类
 * 
 * @author xuyao
 * 
 */
public class FlowChargeUtils {

	private static Logger logger = Logger.getLogger(FlowChargeUtils.class);

	/**
	 * 生成流量兑换签名
	 * 
	 * @param echo
	 *            随机字符串 长度小于20
	 * @param timestamp
	 *            日期字符串YYYYMMDDHHMMSS
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String genReqChargeSign(String orderId, String echo,
			String timestamp) {
		String custInteId = Constants.getConfig("flow_charge_cust_id");
		String secretKey = Constants.getConfig("flow_charge_secret_key");
		String keyStr = custInteId + orderId + secretKey + echo + timestamp;
		byte[] btInput = keyStr.getBytes();
		// 获得MD5摘要算法的 MessageDigest 对象
		MessageDigest mdInst = null;
		try {
			mdInst = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 使用指定的字节更新摘要
		mdInst.update(btInput);
		// 获得密文
		byte[] md = mdInst.digest();

		return new String(Base64.encodeBase64(md));
	}

	/**
	 * 生成流量兑换回调签名
	 * 
	 * @param echo
	 * @param timestamp
	 * @return
	 */
	public static String genRespChargeSign(String echo, String timestamp) {
		String custInteId = Constants.getConfig("flow_charge_cust_id");
		String secretKey = Constants.getConfig("flow_charge_secret_key");
		String keyStr = custInteId + echo + secretKey + timestamp;
		byte[] btInput = keyStr.getBytes();
		// 获得MD5摘要算法的 MessageDigest 对象
		MessageDigest mdInst = null;
		try {
			mdInst = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 使用指定的字节更新摘要
		mdInst.update(btInput);
		// 获得密文
		byte[] md = mdInst.digest();

		return new String(Base64.encodeBase64(md));
	}

	/**
	 * 流量兑换
	 * 
	 * @param mobile
	 *            手机
	 * @param packCode
	 *            流量包ID
	 * @param orderId
	 *            订单ID
	 * @return
	 */
	public static boolean chargeFlow(String mobile, String packCode,
			String orderId) {
		try {
			String flowChargeURL = Constants.getConfig("flow_charge_url");

			String xmlData = createFlowReq(mobile, packCode, orderId);
			String result = HttpsPayUtils.requestPostXml(flowChargeURL, xmlData);
			if (StringUtils.isNotBlank(result)
					&& result.contains("<result>0000</result>")) {
				return true;
			} else {
				logger.error("---流量充值接口调用失败: " + result);
				return false;
			}
		} catch (Exception e) {
			logger.error("充值流量失败。 mobile:" + mobile + ",packCode:" + packCode
					+ ",orderId:" + orderId, e);
			return false;
		}
	}

	/**
	 * 创建流量充值请求报文
	 * 
	 * @param mobile
	 *            手机号
	 * @param packCode
	 *            流量代码
	 * @param orderId
	 *            订单ID
	 * @return
	 */
	private static String createFlowReq(String mobile, String packCode,
			String orderId) {
		StringBuffer xmlReqBuf = new StringBuffer();

		String custInteId = Constants.getConfig("flow_charge_cust_id");
		String timestamp = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
		String echo = UUID.randomUUID().toString().substring(0, 19);// 长度不能大于20
		String chargeSign = genReqChargeSign(orderId, echo, timestamp);

		xmlReqBuf.append("<request>");
		xmlReqBuf.append("<head>");
		xmlReqBuf.append("<custInteId>" + custInteId + "</custInteId>");
		xmlReqBuf.append("<echo>" + echo + "</echo>");
		xmlReqBuf.append("<orderId>" + orderId + "</orderId>");
		xmlReqBuf.append("<timestamp>" + timestamp + "</timestamp>");
		xmlReqBuf.append("<orderType>1</orderType>");// 1订购 2退订
		xmlReqBuf.append("<version>1</version>");
		xmlReqBuf.append("<chargeSign>" + chargeSign + "</chargeSign>");
		xmlReqBuf.append("</head>");
		xmlReqBuf.append("<body>");
		xmlReqBuf.append("<item>");
		xmlReqBuf.append("<packCode>" + packCode + "</packCode>");
		xmlReqBuf.append("<mobile>" + mobile + "</mobile>");
		xmlReqBuf.append("<effectType>1</effectType>");// 1立即生效
		xmlReqBuf.append("</item>");
		xmlReqBuf.append("</body>");
		xmlReqBuf.append("</request>");

		return xmlReqBuf.toString();
	}

	/**
	 * 校验流量充值回调请求是否合法
	 * 
	 * @param orderId
	 * @param echo
	 * @param timestamp
	 * @param reqKey
	 * @return
	 */
	public static boolean validateReq(String echo, String timestamp,
			String chargeSign) {
		if (StringUtils.isBlank(echo) || StringUtils.isBlank(timestamp)
				|| StringUtils.isBlank(chargeSign))
			return false;
		String genKey = genRespChargeSign(echo, timestamp);
		if (genKey.equals(chargeSign))
			return true;

		return false;
	}

	/**
	 * 流量余额查詢
	 * 
	 * @param echo
	 * @param timestamp
	 * @return
	 */
	public static String genBlanceQuerySign(String echo, String timestamp) {
		String custInteId = Constants.getConfig("flow_charge_cust_id");
		String secretKey = Constants.getConfig("flow_charge_secret_key");
		String keyStr = custInteId + secretKey + echo + timestamp;
		byte[] btInput = keyStr.getBytes();
		// 获得MD5摘要算法的 MessageDigest 对象
		MessageDigest mdInst = null;
		try {
			mdInst = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 使用指定的字节更新摘要
		mdInst.update(btInput);
		// 获得密文
		byte[] md = mdInst.digest();

		return new String(Base64.encodeBase64(md));
	}

	private static String queryBlanceReq() {
		StringBuffer xmlReqBuf = new StringBuffer();

		String custInteId = Constants.getConfig("flow_charge_cust_id");
		String timestamp = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
		String echo = UUID.randomUUID().toString().substring(0, 19);
		String chargeSign = genBlanceQuerySign(echo, timestamp);

		xmlReqBuf.append("<request>");
		xmlReqBuf.append("<head>");
		xmlReqBuf.append("<custInteId>" + custInteId + "</custInteId>");
		xmlReqBuf.append("<echo>" + echo + "</echo>");
		xmlReqBuf.append("<timestamp>" + timestamp + "</timestamp>");
		xmlReqBuf.append("<version>1</version>");
		xmlReqBuf.append("<chargeSign>" + chargeSign + "</chargeSign>");
		xmlReqBuf.append("</head>");
		xmlReqBuf.append("</request>");

		return xmlReqBuf.toString();
	}

	public static boolean isOutOfBlance() {
		try {
			String flowQueryURL = Constants.getConfig("flow_query_url");

			String xmlData = queryBlanceReq();
			String result = HttpsPayUtils.requestPostXml(flowQueryURL, xmlData);
			//String result = "<response><result>0000</result><balance>5</balance><desc></desc></response>";
			if (StringUtils.isNotBlank(result)
					&& result.contains("<result>0000</result>")) {
				String jsonResult = new XMLSerializer().read(result).toString();
				JSONObject resultJson = JSONObject.parseObject(jsonResult);
				double balance = resultJson.getDouble("balance");
				if (balance < 10)
					return false;
				return true;
			} else {
				logger.error("---流量余额接口调用失败: " + result);
				return false;
			}
		} catch (Exception e) {
			logger.error("流量余额调用失败。", e);
			return false;
		}
	}

//	public static void main(String[] args) {
//		System.out.println(isOutOfBlance());
//	}
}

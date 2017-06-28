package com.lvtu.wechat.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * 加解密工具类
 * 
 * @author xuyao
 *
 */
public class Crypto {

	/**
	 * Encrypt a String with the AES encryption standard. Private key must have
	 * a length of 16 bytes
	 * 
	 * @param value
	 *            The String to encrypt
	 * @param privateKey
	 *            The key used to encrypt
	 * @return An hexadecimal encrypted string
	 */
	public static String encryptAES(String value, String privateKey) {
		try {
			byte[] raw = privateKey.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			return String.valueOf(Hex.encodeHex(cipher.doFinal(value.getBytes())));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Decrypt a String with the AES encryption standard. Private key must have
	 * a length of 16 bytes
	 * 
	 * @param value
	 *            An hexadecimal encrypted string
	 * @param privateKey
	 *            The key used to encrypt
	 * @return The decrypted String
	 */
	public static String decryptAES(String value, String privateKey) {
		try {
			byte[] raw = privateKey.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			return new String(cipher.doFinal(Hex.decodeHex(value.toCharArray())));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
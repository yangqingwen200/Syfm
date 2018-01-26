package com.appserver.thirdpartpay.weixin.util;

import java.security.MessageDigest;


/**
 * MD5加密
 * @author wenwen
 * 
 * 2015年8月12日 下午11:38:48
 */
public class MD5Encoder {

	/**
	 * 得到MD5加密后的小写
	 * @param message
	 * @return
	 * 
	 *  2015年8月12日 下午11:38:54
	 */
	public static String getMD5(String message) {
		MessageDigest messageDigest = null;
		StringBuffer md5StrBuff = new StringBuffer();
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(message.getBytes("UTF-8"));

			byte[] byteArray = messageDigest.digest();
			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
				else
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		return md5StrBuff.toString();
	}
	public static void main(String[] args) {
		System.out.println(getMD5("123456"));
	}
}

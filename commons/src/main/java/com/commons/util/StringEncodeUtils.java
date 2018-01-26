package com.commons.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringEncodeUtils {

	/**
	 * utf-8转成iso-88591
	 * 一般是从java中传值到jsp中，需要转化下
	 * @return
	 */
	public static String utf8ToIso88591(String str){
		String ret = "";
		try {
			ret = new String(str.getBytes("UTF-8"),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * ISO-8859-1转化成utf-8
	 * 一般是从jsp中传值到java中，需要转化下
	 * @param str
	 * @return
	 */
	public static String iso88591ToUtf8(String str){
		String ret = "";
		try {
			ret = new String(str.getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * gbk转成iso-8859-1
	 * 一般用在文件下载时的文件名的编码
	 * @param str
	 * @return
	 */
	public static String gbkToIso88591(String str){
		String ret = "";
		try {
			ret = new String(str.getBytes("GBK"),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 转换字符串的编码
	 * @param str  字符串
	 * @param source 字符串本身编码
	 * @param replacement 转化后的编码
	 * @return
	 */
	public static String convertEncode(String str,String source,String replacement){
		String ret = "";
		try {
			ret = new String(str.getBytes(source),replacement);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 判断字符串中是否有中文
	 * 有中文返回true
	 * @auth: Yang
	 * @date 2016年7月7日 下午7:21:23
	 * @param str 需要判断的词组
	 * @return
	 */
	public static boolean isChineseChar(String str) {
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}

}

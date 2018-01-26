package com.appserver.thirdpartpay.weixin.util;


import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MaptoXML {

	/**
	 * map转成xml格式
	 * 
	 * @param map
	 * @return Yang 2016年2月9日 下午5:43:22
	 */
	public static Document map2Xml(Map<String, String> map) {
		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		Element university = doc.addElement("xml");
		Iterator<?> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			Element college = university.addElement(key);
			college.setText(map.get(key).toString());
		}
		return doc;
	}
	
	/**
	 * map转成xml格式
	 * 
	 * @param map
	 * @return Yang 2016年2月9日 下午5:43:22
	 * @throws UnsupportedEncodingException 
	 */
	public static String map2XmlString(Map<String, String> map) throws UnsupportedEncodingException {
		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		Element university = doc.addElement("xml");
		Iterator<?> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			Element college = university.addElement(key);
			college.setText(map.get(key).toString());
		}
		return new String(doc.asXML().toString().getBytes(), "ISO8859-1");
	}
	
    public static String map2XmlString1(Map<String, String> map) throws UnsupportedEncodingException {
        String xmlResult = "";

        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>");  
        sb.append("<xml>");
        for (String key : map.keySet()) {

            String value = "<![CDATA[" + map.get(key) + "]]>";
            sb.append("<" + key + ">" + value + "</" + key + ">");
        }
        sb.append("</xml>");
        xmlResult = sb.toString();

        return new String(xmlResult.toString().getBytes(), "ISO8859-1");  
        //return new String(xmlResult.getBytes("UTF-8"), "ISO-8859-1");
    }

	/**
	 * map拼装成key=value形式
	 * 
	 * @param map
	 * @return Yang 2016年2月9日 下午5:00:41
	 */
	public static String map2String(Map<String, String> map) {
		StringBuffer result = new StringBuffer();
		int size = map.size();
		int i = 1;
		Iterator<?> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			String value = map.get(key).toString();
			result.append(key);
			result.append("=");
			result.append(value);
			if (i < size) {
				result.append("&");
			}
			i++;
		}
		return result.toString();
	}

	/**
	 * 获得request输入流.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 *             Yang 2016年2月10日 上午9:26:40
	 */
	public static Map<String, String> parseRequestXml(HttpServletRequest request) throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new LinkedHashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		// 释放资源
		inputStream.close();
		inputStream = null;
		return map;
	}
	
	/**
	 * 解析xml,只能解析一级
	 * @param xmlString
	 * @return
	 * @throws DocumentException
	 * Yang
	 * 2016年2月10日 下午1:41:08
	 */
	public static Map<String, Object> xml2map2(String xmlString) throws DocumentException {
		Map<String, Object> map = new HashMap<String, Object>();
		Document doc = DocumentHelper.parseText(xmlString);
		Element rootElement = doc.getRootElement();
		List<Element> elements = rootElement.elements();
		for (Element element : elements) {
			map.put(element.getName(), element.getTextTrim());
		}
		return map;
	}
	
	/**
	 * 微信支付签名
	 * 
	 * @param str
	 * @return Yang 2016年2月9日 下午4:47:28
	 */
	public static String sign(String str, String key) {
		String result = "";
		
		String stResult = str + "&key=" + key;
		result = MD5Encoder.getMD5(stResult).toUpperCase();
		return result;
	}
	
}

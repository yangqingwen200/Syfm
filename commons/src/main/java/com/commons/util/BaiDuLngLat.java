package com.commons.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 调用百度查询经纬度方法
 * 
 * @author Yang
 * 
 *         2015年12月9日 上午11:16:20
 */
public class BaiDuLngLat {
	
	//调用百度接口的应用key值
	private final static String BAIDU_KEY = "vOwsIGlUwmufgbX9p3Czlnbd";

	/**
	 * 根据地理位置,调用百度API,获得经纬度
	 * 
	 * @param cityName
	 *            省份+城市: 如:黑龙江哈尔滨市(注意前后/中间不能含有空格)
	 * @param address
	 *            详细地址: 如: 南岗区闽江路103号(注意前后/中间不能含有空格)
	 * @return 含有经度和纬度的map
	 * @author Yang 2015年12月9日 上午11:13:34
	 */
	public static Map<String, Float> getBaiduLngLat(String cityName, String address) {
		Map<String, Float> map = new HashMap<String, Float>();
		Float startLng = 0.0f;
		Float startLat = 0.0f;

		StringBuffer sb = new StringBuffer();
		sb.append("output=json");
		sb.append("&ak=" + BaiDuLngLat.BAIDU_KEY);
		// 省份+城市
		sb.append("&cityName=" + cityName);
		// 省份+城市+详细地址
		sb.append("&keyWord=" + cityName + address);
		sb.append("&out_coord_type=bd09ll");

		// 得到百度返回的json串
		// 返回格式:{"status":"Success","results":{"precise":1,"location":{"lng":126.70395957002,"lat":45.746959975657}}}
		String result = JavaSendRequest.sendGet("http://api.map.baidu.com/telematics/v3/geocoding", sb.toString());

		// 得到key status的值,并判断是否等于Success
		String success = getJsonKey(result, "status");
		if ("Success".equals(success)) {

			// 下面的代码可以考虑用递归解析出来.暂未封装...
			String results = getJsonKey(result, "results");
			String location = getJsonKey(results, "location");
			String lng = getJsonKey(location, "lng"); // 得到经度
			String lat = getJsonKey(location, "lat"); // 得到纬度
			startLng = Float.parseFloat(lng);
			startLat = Float.parseFloat(lat);
		}
		map.put("lng", startLng);
		map.put("lat", startLat);
		return map;
	}

	/**
	 * 根据经纬度, 查询地理位置
	 * 
	 * @param location
	 *            经度和纬度组成, 中间用逗号隔开 http://api.map.baidu.com/telematics/v3/reverseGeocoding?output=json&ak=vOwsIGlUwmufgbX9p3Czlnbd&location=126.710495,45.753723
	 * @return Yang 2016年1月31日 下午12:03:38
	 */
	public static String getBaiduAddress(String location) {
		String address = "";

		StringBuffer sb = new StringBuffer();
		sb.append("output=json");
		sb.append("&ak=" + BaiDuLngLat.BAIDU_KEY);
		sb.append("&location=" + location);
		sb.append("&coord_type=bd09ll");

		// 得到百度返回的json串
		String result = JavaSendRequest.sendGet("http://api.map.baidu.com/telematics/v3/reverseGeocoding", sb.toString());
		// 得到key status的值,并判断是否等于Success
		String success = getJsonKey(result, "status");
		if ("Success".equals(success)) {
			address = getJsonKey(result, "description");
		}
		return address;
	}

	/**
	 * 调用百度API,获得两个点之间的距离
	 * 
	 * @param lng1Str
	 * @param lat1Str
	 * @param lng2Str
	 * @param lat2Str
	 * @return
	 * @author Yang 2015年12月9日 下午12:55:48
	 */
	public static Double distance(String lng1Str, String lat1Str, String lng2Str, String lat2Str) {
		StringBuffer sb = new StringBuffer();
		sb.append("output=json");
		sb.append("&ak=" + BaiDuLngLat.BAIDU_KEY);
		sb.append("&waypoints=" + lng1Str + "," + lng1Str + "," + lng2Str + "," + lat2Str);

		// 得到百度返回的json串
		String result = JavaSendRequest.sendGet("http://api.map.baidu.com/telematics/v3/distance", sb.toString());
		System.out.println(result);
		return null;
	}

	/**
	 *  BD-0911百度坐标转换成 GCJ-02国测局加密的坐标(或者高德坐标)
	 * @param bd_lat 纬度
	 * @param bd_lon 经度
	 * @return
	 *
	 * auth: Yang
	 * 2016年2月17日 上午10:50:53
	 */
	public static Map<String, Double> toGaoDeLngLat(double bd_lat, double bd_lon) {
		Map<String, Double> map = new HashMap<String, Double>();
		double gg_lat;
		double gg_lon;
	    double x = bd_lon - 0.0065, y = bd_lat - 0.006;
	    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
	    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
	    gg_lon = z * Math.cos(theta);
	    gg_lat = z * Math.sin(theta);
	    map.put("lat", gg_lat);
		map.put("lng", gg_lon);
		return map;
	}


	/**
	 * 得到json串里面里面的key值
	 * 
	 * @param json
	 * @param key
	 * @return
	 * @author Yang 2015年12月9日 上午9:36:31
	 */
	public static String getJsonKey(String json, String key) {
		String result = "";

		JSONObject jsonObj = JSONObject.fromObject(json);
		if (jsonObj.containsKey(key)) {
			result = jsonObj.get(key).toString();
		}
		return result;
	}
}

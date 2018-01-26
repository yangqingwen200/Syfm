package com.commons.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 调用高德查询经纬度方法
 * 
 * @author Yang
 * 
 *         2015年12月9日 上午11:16:20
 */
public class GaoDeLngLat {
	
	//调用高德接口的应用key值
	private final static String GAODE_KEY = "9ebc62c4e2004fd1f4bbd922a1f20b21";

	/**
	 * 根据地理位置,调用高德API,获得经纬度
	 * 
	 * @param address 详细地址
	 * @return 含有经度和纬度的map
	 * 
	 * @author Yang 2015年12月9日 上午11:13:34
	 */
	public static Map<String, Float> getGaoDeLngLat(String address) {
		Map<String, Float> map = new HashMap<String, Float>();
		Float startLng = 0.0f;
		Float startLat = 0.0f;

		StringBuffer sb = new StringBuffer();
		sb.append("output=json");
		sb.append("&key=" + GaoDeLngLat.GAODE_KEY);
		sb.append("&address=" + address); //地址里面含有中文的话, 必须经过utf-8 urlencode

		String result = JavaSendRequest.sendGet("http://restapi.amap.com/v3/geocode/geo", sb.toString());
		String success = getJsonKey(result, "status");
		if ("1".equals(success)) {
			String results = getJsonKey(result, "geocodes");
			String array = getJsonKeyArray(results);
			String location = getJsonKey(array, "location");
			String[] lo = location.split(",");
			startLng = Float.parseFloat(lo[0]);
			startLat = Float.parseFloat(lo[1]);
		}
		map.put("lng", startLng);
		map.put("lat", startLat);
		return map;
	}

	/**
	 * 根据经纬度, 查询地理位置
	 * 
	 * @param location 经度和纬度组成, 中间用逗号隔开
	 * 
	 * @return Yang 2016年1月31日 下午12:03:38
	 */
	public static String getGaoDeAddress(String location) {
		String address = "";

		StringBuffer sb = new StringBuffer();
		sb.append("output=json");
		sb.append("&location=" + location);
		sb.append("&key=" + GaoDeLngLat.GAODE_KEY);

		// 得到高德返回的json串
		String result = JavaSendRequest.sendGet("http://restapi.amap.com/v3/geocode/regeo", sb.toString());
		String success = getJsonKey(result, "status");
		if ("1".equals(success)) {
			address = getJsonKey(result, "regeocode");
			address = getJsonKey(address, "formatted_address");
		}
		return address;
	}

	/**
	 * 将 GCJ-02 国测局加密的坐标(或者高德坐标)转换成 BD-0911百度 坐标
	 * @param gg_lat 纬度
	 * @param gg_lon 经度
	 * @return
	 *
	 * auth: Yang
	 * 2016年2月17日 上午10:55:04
	 */
	public static Map<String, Double> toBaiDuLngLat(double gg_lat, double gg_lon)	{
		Map<String, Double> map = new HashMap<String, Double>();
		double bd_lat;
		double bd_lon;
		double x = gg_lon;
		double y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
		bd_lon = z * Math.cos(theta) + 0.0065;
		bd_lat = z * Math.sin(theta) + 0.006;
		
		map.put("lat", bd_lat);
		map.put("lng", bd_lon);
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
	
	/**
	 * 解析出来json数组, 得到第一个数值
	 * @param json
	 * @return
	 *
	 * auth: Yang
	 * 2016年2月19日 上午9:47:57
	 */
	public static String getJsonKeyArray(String json) {
		JSONArray fromObject = JSONArray.fromObject(json);
		Object object = fromObject.get(0);
		
		return String.valueOf(object);
	}
}

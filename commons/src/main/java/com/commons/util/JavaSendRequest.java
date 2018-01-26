package com.commons.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

/**
 * java模拟http请求
 * @author Yang
 *
 * 2015年12月9日 上午9:34:27
 */
public class JavaSendRequest {
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = "";
			if(param == null || "".equals(param)) {
				urlNameString = url;
			} else {
				urlNameString = url + "?" + param;
			}
			URL realUrl = new URL(urlNameString);
			URLConnection connection = realUrl.openConnection();
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection(); // 打开和URL之间的连接
			
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream()); // 获取URLConnection对象对应的输出流
			out.print(param); // 发送请求参数
			out.flush(); // flush输出流的缓冲

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 读取request的返回的数据
	 * @param request
	 * @return
	 *
	 * auth: Yang
	 * 2016年3月3日 下午4:20:25
	 */
	public static String readRequest(HttpServletRequest request) {
		String result = "";
		try {
			InputStream in = request.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = 0;
			while((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			out.close();
			in.close();
			result = new String(out.toByteArray(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}

}

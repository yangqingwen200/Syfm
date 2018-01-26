
package com.commons.util;

import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * Servlet工具类
 * 
 *
 */
public class Servlets {


	/**
	 * 取得ServletContext
	 */
	public static ServletContext getServletContext(){
		return ServletActionContext.getServletContext();
	}

	/**
	 * 取得HttpSession
	 */
	public static HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}

	/**
	 * 取得HttpRequest
	 */
	public static HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 取得HttpResponse
	 */
	public static HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	/**
	 * 取得servlet context tempdir
	 * 临时文件可放入该路径下
	 * @return 如：D:\apache-tomcat-6.0.33\work\Catalina\localhost\w
	 */
	public static String getContextAbsolutePath(){
		return ((File)getServletContext().getAttribute("javax.servlet.context.tempdir")).getAbsolutePath();
	}
	
	/**
	 * 
	 * 取得 Http 请求参数
	 * @param exclusives 按照指定的参数名称过滤 
	 * return Map，请求参数的名值对
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, ?> getParameters(String ... exclusives) {
		HttpServletRequest request = getRequest();
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if(exclusives != null && paramName!=null){
				for(String exclusive : exclusives){
					if(paramName.startsWith(exclusive)){
						paramName = null;
						break;
					}
				}
			}
			if(paramName!=null){
				String[] values = request.getParameterValues(paramName);
				if(values!=null && values.length>0){
					if(values.length==1){
						params.put(paramName, values[0]);
					}else{
						params.put(paramName, values);
					}
				}
			}
		}
		return params;
	}

	/**
	 * 生成http 参数字符串
	 * @param params
	 * @return 如:a=1&b=2&c=3
	 */
	public static String genParameterString(Map<String, ?> params) {
		if(params==null || params.size()==0){
			return "";
		}
		StringBuilder queryString = new StringBuilder("");
		if(params!=null && params.size()>0){
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				queryString.append(entry.getKey()).append('=').append(entry.getValue()).append("&");
			}
		}
		return queryString.toString();
	}
	
}

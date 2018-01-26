package com.commons.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by sunshangqiang on 15-7-14.
 */

public class HttpUtils {

	public static void main(String[] args) throws Exception {
		String imAccessToken = "";
		CloseableHttpClient httpClient = createSSLClientDefault();
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost("http://localhost:8080/SystemFramework/appserver/getAppUserInfo_appUser.do");
		httpPost.setHeader("Content-Type", "application/json");
		try {
			JSONObject paramsJson = new JSONObject();
			paramsJson.put("grant_type", "client_credentials");
			paramsJson.put("client_id", "YXA6sHfoMPeaEeS_dpWF7XIMTA");
			paramsJson.put("client_secret", "YXA6Dak22XlMCPc6EplD3NwJTaS7Uro");
			paramsJson.put("id", "1");
			httpPost.setEntity(new StringEntity(paramsJson.toJSONString()));
			response = httpClient.execute(httpPost);
			String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject resultJson = JSON.parseObject(jsonString);
			imAccessToken = resultJson.getString("access_token");
			System.out.println(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.isAborted();
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static String postConnect(String url, Map<String, Object> map) {
		String imAccessToken = "";
		CloseableHttpClient httpClient = createSSLClientDefault();
		CloseableHttpResponse response = null;
		HttpPost httpPost = new HttpPost(url);
		try {
			//建立一个NameValuePair数组，用于存储欲传送的参数
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			
			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> e = iterator.next();  
				params.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())));
			}
			
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpClient.execute(httpPost);
			String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
			JSONObject resultJson = JSON.parseObject(jsonString);
			imAccessToken = resultJson.getString("access_token");
			System.out.println(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.isAborted();
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imAccessToken;
	}

	public static CloseableHttpClient createClientDefault() {
		return HttpClients.createDefault();
	}

	public static CloseableHttpClient createSSLClientDefault(){
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {  
                // 信任所有  
                public boolean isTrusted(X509Certificate[] chain,  
                String authType) throws CertificateException {  
                    return true;  
                }  
            }).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			e.printStackTrace();
		}
		return  HttpClients.createDefault();
	}
}

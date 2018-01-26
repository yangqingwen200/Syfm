package com.appserver.thirdpartpay.alipay.util;

import java.io.Serializable;

/**
 * 用来传给支付宝的body内容bean,方便转成json格式.
 * 
 * @author Administrator
 * 
 *         2015年11月10日 下午12:23:47
 */
public class BodyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId; // 用户id
	/**
	 * 服务类型:服务类型: 1-一站式拿证；2-计时学车；3-包过；4-有证练车
	 */
	private String serviceType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
}

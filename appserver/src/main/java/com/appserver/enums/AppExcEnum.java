package com.appserver.enums;

import com.commons.enums.AbstractExcEnum;

/**
 * 异常信息的枚举类
 * @author Yang
 * @version v1.0
 * @date 2016年12月6日
 */
public enum AppExcEnum implements AbstractExcEnum {
	
	OK(1000, "OK"),

	ERROR(1001, "服务器开小差了!"),

	LESS_PARAMETER(1002, "请求缺少参数: {0}"),

	PARAMETER_VALUE_ISNULL(1003, "请求参数: {0} 值为空"),

	APPUSER_ISNOT_EXISTS(1004, "App用户id: {0} 不存在"),

	APPUSER_ACCOUNT_PASSWORD_ERR(1006, "用户: {0}不存在"),

	APPUSER_NOT_LOGIN(1007, "您还未登录"),

	LESS_PARAMETER_USERID_TOKEN(1008, "少传userId或token参数!"),

	APP_VERSION_LOW(1009, "App版本过低,请升级App!"),

	APPUSER_OTHERALIAS_LOGIN(1010, "你的账号在其他设备登录,请重新登录!"),

	BLACK_IP(1011, "你的ip已经被打入黑名单,明天再来吧"),

	;

	private final Integer msgCode;
	private final String msg;

	AppExcEnum(Integer msgCode, String msg) {
		this.msgCode = msgCode;
		this.msg = msg;
	}

	@Override
	public Integer getExcCode() {
		return msgCode;
	}

	@Override
	public String getExcMsg() {
		return msg;
	}
}

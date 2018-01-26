package com.commons.exception;

import com.commons.enums.AbstractExcEnum;

/**
 * 自定义Exception, 用来检查条件是否满足, 否则抛出此异常
 * @author Yang
 * @version v1.0
 * @date 2016年12月6日
 */
public class ValidateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private AbstractExcEnum excMsg;
	private Object[] paramValue;

	public ValidateException(AbstractExcEnum msginfo, Object... paramValue) {
		this.excMsg = msginfo;
		this.paramValue = paramValue;
	}

	public AbstractExcEnum getMsginfo() {
		return excMsg;
	}

	public Object[] getParamValue() {
		return paramValue;
	}

}

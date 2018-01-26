package com.commons.exception;

/**
 * 逻辑异常
 * @author Administrator
 * @date 2015-3-9
 */
public class LogicException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorMessage;
	private Object[] paramValue;
	
	public LogicException(String errorMessage, Object... paramValue) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.paramValue = paramValue;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Object[] getParamValue() {
		return paramValue;
	}
}

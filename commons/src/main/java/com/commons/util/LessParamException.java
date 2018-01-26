package com.commons.util;

/**
 * 缺少参数异常
 * @author Administrator
 * @Time 2015-3-9 下午01:32:20
 */
public class LessParamException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LessParamException() {
		super();
	}

	public LessParamException(String str) {
		super(str); 
	}


}

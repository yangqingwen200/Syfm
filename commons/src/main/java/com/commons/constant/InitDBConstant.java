package com.commons.constant;

/**
 * 容器启动时初始化的常量类, 值全部是从数据库表中配置</br>
 * <strong><p>必须{@link com InitializationListener}初始化加载, 需要在web.xml配置加载监听器</p></strong>
 * 作用: 加载数据库常量值配置(如code码等等), web容器一启动, 就从数据库加载出来, 无需每次使用都从数据库查询, 减少服务器和数据库的压力
 * @author Yang
 * @version v1.0
 * @date 2016年11月23日
 */
public class InitDBConstant {
	
	/**
	 * 用户每天登录赠送的积分
	 */
	public static String LOGIN_GET_SCORE; 
	
	/**
	 * 用户注册时发送短信验证码
	 */
	public static String REGISTER_SMS_VERIFY_CODE;
	
}

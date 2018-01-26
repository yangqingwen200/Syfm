package com.commons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface OpernationLog {

	/**
	 * 操作的描述信息
	 * @return
	 * @author Yang
	 * @version v1.0
	 * @date 2016年12月10日
	 */
	String value(); //

	/**
	 * 映射含有表名的实体bean
	 * @return
	 * @author Yang
	 * @version v1.0
	 * @date 2016年12月10日
	 */
	Class<?> cls();

	/*
		Class<?> cls() default void.class;
	*/

	// 没有写default就必须要写
	// String value(); //描述信息
}

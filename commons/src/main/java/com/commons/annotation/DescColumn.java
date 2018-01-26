package com.commons.annotation;

import java.lang.annotation.*;

/**
 * 用来做日志用到<br>
 * 在实体bean属性get方法上面, 标识此属性, 范例{@link com.commons.bean.system.SysMenu SysMenu}中的属性get方法
 * @author Yang
 * @version v1.0
 * @date 2016年12月12日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DescColumn {
	
	/**
	 * 列的描述信息
	 * @return
	 * @author Yang
	 * @version v1.0
	 * @date 2016年12月10日
	 */
	String value();
}

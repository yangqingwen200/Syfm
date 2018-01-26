package com.commons.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5InnoDBDialect;

import java.sql.Types;

/**
 * hibernate默认不支持text类型的，所以要修改方言源代码 
 * auth: Yang
 * 2016年8月20日 上午10:19:16
 */
public class MySQLDialect extends MySQL5InnoDBDialect {

	public MySQLDialect() {
		super();
		// 调用父类的registerHibernateType方法，注册Text类型
		registerHibernateType(Types.LONGVARCHAR, Hibernate.TEXT.getName());
	}
}

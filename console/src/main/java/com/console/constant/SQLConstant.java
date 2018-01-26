package com.console.constant;

public class SQLConstant {
	
	/**
	 * 公用的排序和分页<br>
	 * ?number 是将字符串转为数字类型, 可以进行加减操作
	 */
	public static final String COMMON_ORDERBY_LIMIT_BY_EASYUI = " order by"
			+ "<#if sort??> ${sort}</#if>"
			+ "<#if order??> ${order}</#if>"
			+ " limit"
			+ "<#if page??> ${(page?number - 1) * rows?number},</#if>"
			+ "<#if rows??> ${rows}</#if>";

	public static final String WEB_FIND_APP_USER = "select id, name, pw as pw, pw as pw1, telephone, "
			+ "sessionId, logourl, status from appuser where status = 0"
			+ "<#if name??> and name like '%${name}%'</#if>"
			+ "<#if telephone??> and telephone like '%${telephone}%'</#if>" + COMMON_ORDERBY_LIMIT_BY_EASYUI;

	public static final String WEB_FIND_PERMISSION = "select id, permission, code, remark, "
			+ "(SELECT mu.permission FROM sys_permission mu WHERE mu.id=m.parent) as parentName, parent, iselement from sys_permission m where 1=1"
			+ "<#if parent??> and (m.parent = ${parent} or id = ${parent})</#if>"
			+ "<#if iselement??> and m.iselement = ${iselement}</#if>"
			+ "<#if code??> and m.code like '%${code}%'</#if>"
			+ "<#if permission??> and m.permission like '%${permission}%'</#if>" + COMMON_ORDERBY_LIMIT_BY_EASYUI;
	
	public static final String WEB_FIND_WEB_USER = "select id, loginname, name, pw, sex, telephone, email, status from sys_user where 1=1 "
			+ "<#if admin??> and loginname != '${admin}'</#if>"
			+ "<#if sex??> and sex = ${sex}</#if>"
			+ "<#if status??> and status = ${status}</#if>"
			+ "<#if name??> and name like '%${name}%'</#if>"
			+ "<#if loginname??> and loginname like '%${loginname}%'</#if>" + COMMON_ORDERBY_LIMIT_BY_EASYUI;

	public static final String WEB_FIND_PERMISSION_ELEMENT = "select e.code, e.function_name, e.icon, e.buttom_name from sys_element e "
			+ "join sys_permission p on p.id=e.permission_id "
			+ "join sys_user_element sue on e.id=sue.element_id "
			+ "where p.id=? and e.parent!=0 and e.disabled=? AND sue.user_id=? order by e.create_time ";

	public static final String WEB_FIND_DRIVINGSCHOOL = "select id, name, link_man as linkMan, link_tel as linkTel, address, tel, DATE_FORMAT(create_date, '%Y-%m-%d') as createDate, introduction, status from ds_driving_school where disabled=1 "
			+ "<#if name??> and name like '%${name}%'</#if>"
			+ "<#if status??> and status = ${status}</#if>"
			+ "<#if link_tel??> and link_tel like '%${link_tel}%'</#if>" + COMMON_ORDERBY_LIMIT_BY_EASYUI;
}

package com.front.constant;

public class SQLConstant {
	
	/**
	 * 公用的排序和分页<br>
	 * ?number 是将字符串转为数字类型, 可以进行加减操作
	 */
	public static final String COMMON_ORDERBY_LIMIT_BY_BOOTSTRAPTABLE = " order by"
			+ "<#if sort??> ${sort}</#if>"
			+ "<#if order??> ${order}</#if>"
			+ " limit"
			+ "<#if offset??> ${offset},</#if>"
			+ "<#if limit??> ${limit}</#if>";

	public static final String PC_FIND_ONETOP_DRIVINGSCHOOL = "select id, name, link_tel, city_name, star_num, IF(is_examroom = 1, '有考场', '无考场') AS is_examroom, status, address from ds_driving_school where disabled=1 "
			+ "<#if name??> and name like '%${name}%'</#if>"
			+ "<#if linkTel??> and link_tel like '%${linkTel}%'</#if>"
			+ "<#if schoolId??> and id = ${schoolId}</#if>"
			+ "<#if status??> and status = ${status} </#if>";

	public static final String PC_FIND_KEER_DRIVINGSCHOOL = "select id, name, link_tel, city_name, star_num, IF(is_examroom = 1, '有考场', '无考场') AS is_examroom, status, address from ds_driving_school where disabled=1 "
			+ "<#if name??> and name like '%${name}%'</#if>"
			+ "<#if linkTel??> and link_tel like '%${linkTel}%'</#if>"
			+ "<#if schoolId??> and id = ${schoolId}</#if>"
			+ "<#if status??> and status = ${status}</#if>" + COMMON_ORDERBY_LIMIT_BY_BOOTSTRAPTABLE;

	public static final String PC_FIND_TIME_DRIVINGSCHOOL = "select id, name, link_tel, is_examroom, status, address from ds_driving_school where disabled=1 "
			+ "<#if name??> and name like '%${name}%'</#if>"
			+ "<#if linkTel??> and link_tel like '%${linkTel}%'</#if>"
			+ "<#if schoolId??> and id = ${schoolId}</#if>"
			+ "<#if status??> and status = ${status}</#if>" + COMMON_ORDERBY_LIMIT_BY_BOOTSTRAPTABLE;

	public static final String PC_FIND_KESAN_DRIVINGSCHOOL = "select id, name, link_tel, is_examroom, status, address, city_name, star_num, link_man, tel, DATE_FORMAT(create_date, '%Y-%m-%d %H:%i:%s') AS create_date, lng, lat from ds_driving_school where disabled=1 "
			+ "<#if name??> and name like '%${name}%'</#if>"
			+ "<#if address??> and address like '%${address}%'</#if>"
			+ "<#if linkMan??> and link_man like '%${linkMan}%'</#if>"
			+ "<#if linkTel??> and link_tel like '%${linkTel}%'</#if>"
			+ "<#if schoolId??> and id = ${schoolId}</#if>"
			+ "<#if isExamroom??> and is_examroom = ${isExamroom}</#if>"
			+ "<#if starNum??> and star_num >= ${starNum}</#if>"
			+ "<#if status??> and status = ${status}</#if>" + COMMON_ORDERBY_LIMIT_BY_BOOTSTRAPTABLE;
}

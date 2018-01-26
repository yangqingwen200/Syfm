package com.commons.constant;

/**
 * 容器启动时初始化的常量类, 值全部是从 classpath路径 config.properties文件中<br>
 * 作用: 加载初始化常量值, 如文件保存路径,百度key等等(当然也配置在数据库中), web容器一启动, 就初始化值.<br>
 * <strong>注意: 如果修改了properties的值, 必须重启web容器才会生效, 从properties配置文件中重新加载一次</>
 * @author Yang
 * @version v1.0
 * @date 2016年12月10日
 */
public class InitPpConstant {

	public static String BAIDU_KEY; // 百度Api key
	public static String LINUX_URL; // 正式服务器URL地址
	public static String LINUX_FOLDER; // Linux服务器文件夹保存路径
	public static String JPUSH_MASTER_SECRET; // 极光推送secret
	public static String JPUSH_APP_KEY; // 极光推送App key
	public static Boolean JPUSH_IOS_PRODUCT; // IOS是否是生产环境
	public static Integer PAGESIZE;
	public static Integer WIDTH;
	public static Integer HEIGHT;
	public static Boolean ISEPSCALING; // 是否启用压缩图片
	public static String LOCAL; // 本地地址
	public static Boolean IS_PRODUCT; // 是否是生产环境: true-是, false-不 是

	/*
	 * 加入set和get方法, 是为了spring注入需要
	 */
	public static String getBAIDU_KEY() {
		return BAIDU_KEY;
	}

	public static void setBAIDU_KEY(String bAIDU_KEY) {
		BAIDU_KEY = bAIDU_KEY;
	}

	public static String getLINUX_URL() {
		return LINUX_URL;
	}

	public static void setLINUX_URL(String lINUX_URL) {
		LINUX_URL = lINUX_URL;
	}

	public static String getLINUX_FOLDER() {
		return LINUX_FOLDER;
	}

	public static void setLINUX_FOLDER(String lINUX_FOLDER) {
		LINUX_FOLDER = lINUX_FOLDER;
	}

	public static String getJPUSH_MASTER_SECRET() {
		return JPUSH_MASTER_SECRET;
	}

	public static void setJPUSH_MASTER_SECRET(String jPUSH_MASTER_SECRET) {
		JPUSH_MASTER_SECRET = jPUSH_MASTER_SECRET;
	}

	public static String getJPUSH_APP_KEY() {
		return JPUSH_APP_KEY;
	}

	public static void setJPUSH_APP_KEY(String jPUSH_APP_KEY) {
		JPUSH_APP_KEY = jPUSH_APP_KEY;
	}

	public static Boolean getJPUSH_IOS_PRODUCT() {
		return JPUSH_IOS_PRODUCT;
	}

	public static void setJPUSH_IOS_PRODUCT(Boolean jPUSH_IOS_PRODUCT) {
		JPUSH_IOS_PRODUCT = jPUSH_IOS_PRODUCT;
	}

	public static Integer getPAGESIZE() {
		return PAGESIZE;
	}

	public static void setPAGESIZE(Integer pAGESIZE) {
		PAGESIZE = pAGESIZE;
	}

	public static Integer getWIDTH() {
		return WIDTH;
	}

	public static void setWIDTH(Integer wIDTH) {
		WIDTH = wIDTH;
	}

	public static Integer getHEIGHT() {
		return HEIGHT;
	}

	public static void setHEIGHT(Integer hEIGHT) {
		HEIGHT = hEIGHT;
	}

	public static Boolean getISEPSCALING() {
		return ISEPSCALING;
	}

	public static void setISEPSCALING(Boolean iSEPSCALING) {
		ISEPSCALING = iSEPSCALING;
	}

	public static String getLOCAL() {
		return LOCAL;
	}

	public static void setLOCAL(String lOCAL) {
		LOCAL = lOCAL;
	}

	public static Boolean getIS_PRODUCT() {
		return IS_PRODUCT;
	}

	public static void setIS_PRODUCT(Boolean iS_PRODUCT) {
		IS_PRODUCT = iS_PRODUCT;
	}
}

/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       MPI基本参数工具类
 * =============================================================================
 */
package com.appserver.thirdpartpay.unionpay.sdk;


import javax.servlet.ServletContext;

/**
 * 软件开发工具包 配制
 * 
 * @author xuyaowen
 * 
 */
public class SDKConfig {

	public static final String FILE_NAME = "acp_sdk.properties";

	/** 前台请求URL. */
	private String frontRequestUrl;
	/** 后台请求URL. */
	private String backRequestUrl;
	/** 单笔查询 */
	private String singleQueryUrl;
	/** 批量查询 */
	private String batchQueryUrl;
	/** 批量交易 */
	private String batchTransUrl;
	/** 文件传输 */
	private String fileTransUrl;
	/** 签名证书路径. */
	private String signCertPath;
	/** 签名证书密码. */
	private String signCertPwd;
	/** 签名证书类型. */
	private String signCertType;
	/** 加密公钥证书路径. */
	private String encryptCertPath;
	/** 验证签名公钥证书目录. */
	private String validateCertDir;
	/** 按照商户代码读取指定签名证书目录. */
	private String signCertDir;
	/** 磁道加密证书路径. */
	private String encryptTrackCertPath;
	/** 磁道加密公钥模数. */
	private String encryptTrackKeyModulus;
	/** 磁道加密公钥指数. */
	private String encryptTrackKeyExponent;
	/** 有卡交易. */
	private String cardRequestUrl;
	/** app交易 */
	private String appRequestUrl;
	/** 证书使用模式(单证书/多证书) */
	private String singleMode;

	/*缴费相关地址*/
	private String jfFrontRequestUrl;
	private String jfBackRequestUrl;
	private String jfSingleQueryUrl;
	private String jfCardRequestUrl;
	private String jfAppRequestUrl;

	/** 支付完成后前台通知地址 */
	private String frontNotifyUrl;
	/** 支付完成后后台通知地址 */
	private String backNotifyUrl;

	/** 商户号码 */
	private String merId;

	/** 版本号（全渠道固定值） */
	private String version = "5.0.0";
	/** 编码，默认UTF-8 */
	private String encoding_UTF8 = "UTF-8";

	/** 配置文件中的前台URL常量. */
	public static final String SDK_FRONT_URL = "acpsdk.frontTransUrl";
	/** 配置文件中的后台URL常量. */
	public static final String SDK_BACK_URL = "acpsdk.backTransUrl";
	/** 配置文件中的单笔交易查询URL常量. */
	public static final String SDK_SIGNQ_URL = "acpsdk.singleQueryUrl";
	/** 配置文件中的批量交易查询URL常量. */
	public static final String SDK_BATQ_URL = "acpsdk.batchQueryUrl";
	/** 配置文件中的批量交易URL常量. */
	public static final String SDK_BATTRANS_URL = "acpsdk.batchTransUrl";
	/** 配置文件中的文件类交易URL常量. */
	public static final String SDK_FILETRANS_URL = "acpsdk.fileTransUrl";
	/** 配置文件中的有卡交易URL常量. */
	public static final String SDK_CARD_URL = "acpsdk.cardTransUrl";
	/** 配置文件中的app交易URL常量. */
	public static final String SDK_APP_URL = "acpsdk.appTransUrl";

	
	/** 以下缴费产品使用，其余产品用不到，无视即可 */
	// 前台请求地址
	public static final String JF_SDK_FRONT_TRANS_URL= "acpsdk.jfFrontTransUrl";
	// 后台请求地址
	public static final String JF_SDK_BACK_TRANS_URL="acpsdk.jfBackTransUrl";
	// 单笔查询请求地址
	public static final String JF_SDK_SINGLE_QUERY_URL="acpsdk.jfSingleQueryUrl";
	// 有卡交易地址
	public static final String JF_SDK_CARD_TRANS_URL="acpsdk.jfCardTransUrl";
	// App交易地址
	public static final String JF_SDK_APP_TRANS_URL="acpsdk.jfAppTransUrl";
	
	
	/** 配置文件中签名证书路径常量. */
	public static final String SDK_SIGNCERT_PATH = "acpsdk.signCert.path";
	/** 配置文件中签名证书密码常量. */
	public static final String SDK_SIGNCERT_PWD = "acpsdk.signCert.pwd";
	/** 配置文件中签名证书类型常量. */
	public static final String SDK_SIGNCERT_TYPE = "acpsdk.signCert.type";
	/** 配置文件中密码加密证书路径常量. */
	public static final String SDK_ENCRYPTCERT_PATH = "acpsdk.encryptCert.path";
	/** 配置文件中磁道加密证书路径常量. */
	public static final String SDK_ENCRYPTTRACKCERT_PATH = "acpsdk.encryptTrackCert.path";
	/** 配置文件中磁道加密公钥模数常量. */
	public static final String SDK_ENCRYPTTRACKKEY_MODULUS = "acpsdk.encryptTrackKey.modulus";
	/** 配置文件中磁道加密公钥指数常量. */
	public static final String SDK_ENCRYPTTRACKKEY_EXPONENT = "acpsdk.encryptTrackKey.exponent";
	/** 配置文件中验证签名证书目录常量. */
	public static final String SDK_VALIDATECERT_DIR = "acpsdk.validateCert.dir";

	/** 配置文件中证书使用模式 */
	public static final String SDK_SINGLEMODE = "acpsdk.singleMode";

	/** 配置文件中前台通知地址常量 */
	public static final String SDK_FRONT_NOTIFY_URL = "acpsdk.frontNotifyUrl";
	/** 配置文件中后台通知地址常量 */
	public static final String SDK_BACK_NOTIFY_URL = "acpsdk.backNotifyUrl";

	/** 配置文件中商户号码常量 */
	public static final String SDK_MERCHANT_ID = "acpsdk.merId";

	/** 操作对象. */
	private static SDKConfig config;

	/**
	 * 获取config对象.
	 * 
	 * @return
	 */
	public static SDKConfig getConfig() {
		if (null == config) {
			config = new SDKConfig();
		}
		return config;
	}

	public void init(ServletContext context) {
		// 前台URL
		this.frontRequestUrl = context.getInitParameter(SDK_FRONT_URL);
		// 后台URL
		this.backRequestUrl = context.getInitParameter(SDK_BACK_URL);
		// 单笔交易查询URL
		this.singleQueryUrl = context.getInitParameter(SDK_SIGNQ_URL);
		// 批量交易查询URL
		this.batchQueryUrl = context.getInitParameter(SDK_BATTRANS_URL);
		// 文件类交易URL
		this.fileTransUrl = context.getInitParameter(SDK_FILETRANS_URL);
		// 有卡交易URL
		this.cardRequestUrl = context.getInitParameter(SDK_CARD_URL);
		// app交易URL常量
		this.appRequestUrl = context.getInitParameter(SDK_APP_URL);

		// 以下缴费产品使用，其余产品用不到，无视即可
		// 前台请求地址
		this.jfFrontRequestUrl = context.getInitParameter(JF_SDK_FRONT_TRANS_URL);
		// 后台请求地址
		this.jfBackRequestUrl = context.getInitParameter(JF_SDK_BACK_TRANS_URL);
		// 单笔查询请求地址
		this.jfSingleQueryUrl = context.getInitParameter(JF_SDK_SINGLE_QUERY_URL);
		// 有卡交易地址
		this.jfCardRequestUrl = context.getInitParameter(JF_SDK_CARD_TRANS_URL);
		// App交易地址
		this.jfAppRequestUrl = context.getInitParameter(JF_SDK_APP_TRANS_URL);

		// 签名证书路径
		this.signCertPath = context.getInitParameter(SDK_SIGNCERT_PATH);
		// 签名证书密码
		this.signCertPwd = context.getInitParameter(SDK_SIGNCERT_PWD);
		// 签名证书类型
		this.signCertType = context.getInitParameter(SDK_SIGNCERT_TYPE);
		// 密码加密证书路径
		this.encryptCertPath = context.getInitParameter(SDK_ENCRYPTCERT_PATH);
		// 磁道加密证书路径
		this.encryptTrackCertPath = context.getInitParameter(SDK_ENCRYPTTRACKCERT_PATH);
		// 磁道加密公钥模数
		this.encryptTrackKeyModulus = context.getInitParameter(SDK_ENCRYPTTRACKKEY_MODULUS);
		// 磁道加密公钥指数
		this.encryptTrackKeyExponent = context.getInitParameter(SDK_ENCRYPTTRACKKEY_EXPONENT);
		// 验证签名证书目录
		this.validateCertDir = context.getInitParameter(SDK_VALIDATECERT_DIR);

		// 证书使用模式
		this.singleMode = context.getInitParameter(SDK_SINGLEMODE);

		// 前台通知地址
		this.frontNotifyUrl = context.getInitParameter(SDK_FRONT_NOTIFY_URL);
		// 后台通知地址
		this.backNotifyUrl = context.getInitParameter(SDK_BACK_NOTIFY_URL);

		// 商户号码
		this.merId = context.getInitParameter(SDK_MERCHANT_ID);
	}

	public String getFrontRequestUrl() {
		return frontRequestUrl;
	}

	public void setFrontRequestUrl(String frontRequestUrl) {
		this.frontRequestUrl = frontRequestUrl;
	}

	public String getBackRequestUrl() {
		return backRequestUrl;
	}

	public void setBackRequestUrl(String backRequestUrl) {
		this.backRequestUrl = backRequestUrl;
	}

	public String getSignCertPath() {
		return signCertPath;
	}

	public void setSignCertPath(String signCertPath) {
		this.signCertPath = signCertPath;
	}

	public String getSignCertPwd() {
		return signCertPwd;
	}

	public void setSignCertPwd(String signCertPwd) {
		this.signCertPwd = signCertPwd;
	}

	public String getSignCertType() {
		return signCertType;
	}

	public void setSignCertType(String signCertType) {
		this.signCertType = signCertType;
	}

	public String getEncryptCertPath() {
		return encryptCertPath;
	}

	public void setEncryptCertPath(String encryptCertPath) {
		this.encryptCertPath = encryptCertPath;
	}
	
	public String getValidateCertDir() {
		return validateCertDir;
	}

	public void setValidateCertDir(String validateCertDir) {
		this.validateCertDir = validateCertDir;
	}

	public String getSingleQueryUrl() {
		return singleQueryUrl;
	}

	public void setSingleQueryUrl(String singleQueryUrl) {
		this.singleQueryUrl = singleQueryUrl;
	}

	public String getBatchQueryUrl() {
		return batchQueryUrl;
	}

	public void setBatchQueryUrl(String batchQueryUrl) {
		this.batchQueryUrl = batchQueryUrl;
	}

	public String getBatchTransUrl() {
		return batchTransUrl;
	}

	public void setBatchTransUrl(String batchTransUrl) {
		this.batchTransUrl = batchTransUrl;
	}

	public String getFileTransUrl() {
		return fileTransUrl;
	}

	public void setFileTransUrl(String fileTransUrl) {
		this.fileTransUrl = fileTransUrl;
	}

	public String getSignCertDir() {
		return signCertDir;
	}

	public void setSignCertDir(String signCertDir) {
		this.signCertDir = signCertDir;
	}

	public String getCardRequestUrl() {
		return cardRequestUrl;
	}

	public void setCardRequestUrl(String cardRequestUrl) {
		this.cardRequestUrl = cardRequestUrl;
	}

	public String getAppRequestUrl() {
		return appRequestUrl;
	}

	public void setAppRequestUrl(String appRequestUrl) {
		this.appRequestUrl = appRequestUrl;
	}
	
	public String getEncryptTrackCertPath() {
		return encryptTrackCertPath;
	}

	public void setEncryptTrackCertPath(String encryptTrackCertPath) {
		this.encryptTrackCertPath = encryptTrackCertPath;
	}
	
	public String getJfFrontRequestUrl() {
		return jfFrontRequestUrl;
	}

	public void setJfFrontRequestUrl(String jfFrontRequestUrl) {
		this.jfFrontRequestUrl = jfFrontRequestUrl;
	}

	public String getJfBackRequestUrl() {
		return jfBackRequestUrl;
	}

	public void setJfBackRequestUrl(String jfBackRequestUrl) {
		this.jfBackRequestUrl = jfBackRequestUrl;
	}

	public String getJfSingleQueryUrl() {
		return jfSingleQueryUrl;
	}

	public void setJfSingleQueryUrl(String jfSingleQueryUrl) {
		this.jfSingleQueryUrl = jfSingleQueryUrl;
	}

	public String getJfCardRequestUrl() {
		return jfCardRequestUrl;
	}

	public void setJfCardRequestUrl(String jfCardRequestUrl) {
		this.jfCardRequestUrl = jfCardRequestUrl;
	}

	public String getJfAppRequestUrl() {
		return jfAppRequestUrl;
	}

	public void setJfAppRequestUrl(String jfAppRequestUrl) {
		this.jfAppRequestUrl = jfAppRequestUrl;
	}

	public String getSingleMode() {
		return singleMode;
	}

	public void setSingleMode(String singleMode) {
		this.singleMode = singleMode;
	}

	public SDKConfig() {
		super();
	}

	public String getEncryptTrackKeyExponent() {
		return encryptTrackKeyExponent;
	}

	public void setEncryptTrackKeyExponent(String encryptTrackKeyExponent) {
		this.encryptTrackKeyExponent = encryptTrackKeyExponent;
	}

	public String getEncryptTrackKeyModulus() {
		return encryptTrackKeyModulus;
	}

	public void setEncryptTrackKeyModulus(String encryptTrackKeyModulus) {
		this.encryptTrackKeyModulus = encryptTrackKeyModulus;
	}

	public String getBackNotifyUrl() {
		return backNotifyUrl;
	}

	public void setBackNotifyUrl(String backNotifyUrl) {
		this.backNotifyUrl = backNotifyUrl;
	}

	public String getFrontNotifyUrl() {
		return frontNotifyUrl;
	}

	public void setFrontNotifyUrl(String frontNotifyUrl) {
		this.frontNotifyUrl = frontNotifyUrl;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getEncoding_UTF8() {
		return encoding_UTF8;
	}

	public void setEncoding_UTF8(String encoding_UTF8) {
		this.encoding_UTF8 = encoding_UTF8;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}

package com.appserver.thirdpartpay.weixin.demo;

public class WxPayDto {

	private String orderId;//订单号
	private String totalFee;//金额
	private String spbillCreateIp;//订单生成的机器 IP
	private String notifyUrl;//这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等
	private String body;// 商品描述根据情况修改
	private String openId;//微信用户对一个公众号唯一
	private String attach; //商家数据包，原样返回
	private String tradeType; //交易类型
	private String key; //商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	private String appid; //微信支付商户开通后 微信会提供appid和appsecret
	private String appsecret; 
	private String mchid; // 商户号
	private String getNonceStr;// 随机字符串
	
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the totalFee
	 */
	public String getTotalFee() {
		return totalFee;
	}
	/**
	 * @param totalFee the totalFee to set
	 */
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	/**
	 * @return the spbillCreateIp
	 */
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	/**
	 * @param spbillCreateIp the spbillCreateIp to set
	 */
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	/**
	 * @return the notifyUrl
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}
	/**
	 * @param notifyUrl the notifyUrl to set
	 */
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * @return the openId
	 */
	public String getOpenId() {
		return openId;
	}
	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAppsecret() {
		return appsecret;
	}
	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}
	public String getMchid() {
		return mchid;
	}
	public void setMchid(String mchid) {
		this.mchid = mchid;
	}
	public String getGetNonceStr() {
		return getNonceStr;
	}
	public void setGetNonceStr(String getNonceStr) {
		this.getNonceStr = getNonceStr;
	}
	
}

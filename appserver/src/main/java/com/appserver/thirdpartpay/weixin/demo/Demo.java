package com.appserver.thirdpartpay.weixin.demo;


import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author ex_yangxiaoyi
 * 
 */
public class Demo {
	

/*AppID(应用ID)
wx45004a1bf8af3683
AppSecret(应用密钥)
3f98864d4319582979519931dac4a027 隐藏 重置
*/
	
	
	//微信支付商户开通后 微信会提供appid和appsecret和商户号partner
	//private static String appid = "wx45004a1bf8af3683";
	//private static String appid = "wx426b3015555a46be";
	//private static String appsecret = "3f98864d4319582979519931dac4a027";
	//private static String appsecret = "01c6d59a3f9024db6336662ac95c8e74";
	//private static String partner = "1265558901";
	//private static String partner = "1225312702";
	//这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	//private static String partnerkey = "HNcar666666HNcar666666HNcar66666";
	//private static String partnerkey = "e10adc3949ba59abbe56e057f20f883e";
	//openId 是微信用户针对公众号的标识，授权的部分这里不解释
	private static String openId = "";
	//微信支付成功后通知地址 必须要求80端口并且地址不能带参数
	//private static String notifyurl = "http://dev.hncarlife.com:2007/DAP/weixinpay/weixinpay_saveWeixinCallback.do";																	 // Key

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		//微信支付jsApi
		WxPayDto tpWxPay = new WxPayDto();
		tpWxPay.setOpenId(openId);
		tpWxPay.setBody("商品信息");
		tpWxPay.setOrderId(getNonceStr());
		tpWxPay.setSpbillCreateIp("127.0.0.1");
		tpWxPay.setTotalFee("0.01");
	    getPackage(tpWxPay);*/
	    
	    //扫码支付
	    WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setAppid("wx426b3015555a46be");
	    tpWxPay1.setAppsecret("");
	    tpWxPay1.setKey("e10adc3949ba59abbe56e057f20f883e");
	    tpWxPay1.setMchid("1225312702");
	    tpWxPay1.setBody("商品信息");
	    tpWxPay1.setSpbillCreateIp("127.0.0.1");
	    tpWxPay1.setTotalFee("0.01");
	    tpWxPay1.setTradeType("NATIVE");
	    tpWxPay1.setOrderId("1235613156163");
	    tpWxPay1.setNotifyUrl("http://dev.hncarlife.com:2007/DAP/weixinpay/weixinpay_saveWeixinCallback.do");
	    tpWxPay1.setAttach("{\"userType:\"1}");
	    tpWxPay1.setGetNonceStr(getNonceStr());
		String codeurl = getCodeurl(tpWxPay1);
		System.out.println(codeurl);

	}
	
	/**
	 * 获取微信扫码支付二维码连接(PC端网页扫码支付, NATIVE)
	 */
	public static String getCodeurl(WxPayDto tpWxPayDto){
		
		// 1 参数
		
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = tpWxPayDto.getNotifyUrl();
		String trade_type = tpWxPayDto.getTradeType();
		// 商户号
		String mch_id = tpWxPayDto.getMchid();
		// 随机字符串
		String nonce_str = tpWxPayDto.getGetNonceStr();
		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();
		// 商户订单号
		String out_trade_no = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach = tpWxPayDto.getAttach();
		String appid = tpWxPayDto.getAppid();
		String partnerkey = tpWxPayDto.getKey();
		String appsecret = tpWxPayDto.getAppsecret(); //可以不要appsecret

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		code_url = GetWxOrderno.getCodeUrl(createOrderURL, xml);
		
		return code_url;
	}
	
	
	/**
	 * 获取请求 package (微信客户端H5页面JSAPI)
	 * @return
	 */
	public static String getPackage(WxPayDto tpWxPayDto) {
		
		String openId = tpWxPayDto.getOpenId();
		// 1 参数
		// 订单号
		// 附加数据 原样返回
		String attach = tpWxPayDto.getAttach();
		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = tpWxPayDto.getNotifyUrl();
		String trade_type = tpWxPayDto.getTradeType();

		// 商户号
		String mch_id = tpWxPayDto.getMchid();
		// 随机字符串
		String nonce_str = tpWxPayDto.getGetNonceStr();
		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();
		// 商户订单号
		String out_trade_no = tpWxPayDto.getOrderId();
		String appid = tpWxPayDto.getAppid();
		String appsecret = tpWxPayDto.getAppsecret();
		String partnerkey = tpWxPayDto.getKey();

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openId + "</openid>"
				+ "</xml>";
		String prepay_id = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		prepay_id = GetWxOrderno.getPayNo(createOrderURL, xml);

		//获取prepay_id后，拼接最后请求支付所需要的package
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id="+prepay_id;
		finalpackage.put("appId", appid);  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", nonce_str);  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		//要签名
		String finalsign = reqHandler.createSign(finalpackage);
		
		String finaPackage = "\"appId\":\"" + appid + "\",\"timeStamp\":\"" + timestamp
		+ "\",\"nonceStr\":\"" + nonce_str + "\",\"package\":\""
		+ packages + "\",\"signType\" : \"MD5" + "\",\"paySign\":\""
		+ finalsign + "\"";

		return finaPackage;
	}

	
	
	/**
	 * 获取请求预支付id报文(App专用  APP)
	 * auth: Yang
	 * 2016年7月22日 上午11:52:53
	 * @param tpWxPayDto
	 * @return
	 */
	public static String getPrepayId(WxPayDto tpWxPayDto) {
		// 1 参数

		// 总金额以分为单位，不带小数点
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = tpWxPayDto.getNotifyUrl();
		String trade_type = tpWxPayDto.getTradeType();
		// 商户号
		String mch_id = tpWxPayDto.getMchid();
		// 随机字符串
		String nonce_str = tpWxPayDto.getGetNonceStr();
		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();
		// 商户订单号
		String out_trade_no = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach = tpWxPayDto.getAttach();
		String appid = tpWxPayDto.getAppid();
		String partnerkey = tpWxPayDto.getKey();
		String appsecret = tpWxPayDto.getAppsecret(); //可以不要appsecret

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String prepay_id = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		prepay_id = GetWxOrderno.getPayNo(createOrderURL, xml);
		return prepay_id;
	}
	
	/**
	 * 获取随机字符串
	 * @return
	 */
	public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}

	/**
	 * 元转换成分
	 * @param money
	 * @return
	 */
	public static String getMoney(String amount) {
		if(amount==null){
			return "";
		}
		// 金额转化为分为单位
		String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额  
        int index = currency.indexOf(".");  
        int length = currency.length();  
        Long amLong = 0l;  
        if(index == -1){  
            amLong = Long.valueOf(currency+"00");  
        }else if(length - index >= 3){  
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
        }else if(length - index == 2){  
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
        }else{  
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
        }  
        return amLong.toString(); 
	}

}
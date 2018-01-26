package com.appserver.thirdpartpay.weixin.action;

import com.appserver.action.BaseAppAction;
import com.commons.util.DateUtil;
import com.commons.util.RandomNumber;
import com.commons.util.core.BL3Utils;
import com.commons.util.core.BaseDto;
import com.commons.util.core.Dto;
import com.commons.util.core.WebUtils;
import com.appserver.thirdpartpay.weixin.demo.Demo;
import com.appserver.thirdpartpay.weixin.demo.WxPayDto;
import com.appserver.thirdpartpay.weixin.util.MaptoXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 与微信相关的公用类 Yang
 * 
 * 2016年2月9日 下午2:35:39
 */
@Controller("com.weixin.action.WeixinAction")
@Scope("prototype")
public class WeixinAction extends BaseAppAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * app下单时调用微信的下单接口 
	 * auth: Yang
	 * 2016年7月19日 上午10:45:11
	 */
	public void appOrders() {
		try {
			this.checkRequestParam("serviceType", "user_id", "orderNo");
			Dto dto = WebUtils.getParamAsDto(request);
			BL3Utils.handleNullStr(dto);
			
			//Map<String, Object> map1=aliPayService.getOrderInfo(params);
			Map<String, Object> map1 = new HashMap<String, Object>();
			if(map1 == null || map1.isEmpty()) {
				throw new RuntimeException("微信支付的订单不存在");
			}
			String random =  RandomNumber.getStringRandom(10);
			
			WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setAppid("");
		    tpWxPay1.setKey("");
		    tpWxPay1.setMchid("");
		    tpWxPay1.setBody(String.valueOf(map1.get("goods")));
		    tpWxPay1.setSpbillCreateIp(this.getIpAddr(request));
		    tpWxPay1.setTotalFee(String.valueOf(map1.get("payment_amount")));
		    tpWxPay1.setTradeType("APP");
		    tpWxPay1.setOrderId(dto.getAsString("orderNo"));
		    tpWxPay1.setNotifyUrl("http://dev.hncarlife.com:2007/DAP/weixinpay/weixinpay_saveWeixinCallback.do");
		    tpWxPay1.setAttach(String.valueOf(map1.get("body")));
		    tpWxPay1.setGetNonceStr(random);
		    
		    String prepayId = Demo.getPrepayId(tpWxPay1); //获得prepayId
		    if(prepayId != null) {
				//返回给App相关信息,方便App调用微信接口
				Map<String, String> mapReturn = new LinkedHashMap<String, String>();
				mapReturn.put("appid", "");
				mapReturn.put("noncestr", random);
				mapReturn.put("package", "Sign=WXPay");
				mapReturn.put("partnerid", "");
				mapReturn.put("prepayid", prepayId);
				mapReturn.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
				
				String signReturn = MaptoXML.sign(MaptoXML.map2String(mapReturn), "");
				mapReturn.put("sign", signReturn); // 签名
				json.put("pay", mapReturn);
			} else {
				throw new RuntimeException("Call Weixin Interface Error!");
			}
		} catch (Exception e) {
			//this.judgementExceptionType(e);
		} finally {
			this.renderJson();
		}
	}
	
	/**
	 * pc端下单时调用微信的下单接口   (使用微信扫码模式二方式 NATIVE), 此方法返回二维码链接
	 * 
	 * 二维码变成图片, demo见  "微信链接变成二维码Demo_jsp.zip"
	 * 
	 * 不是使用微信客户端访问H5的方式, 那是使用JSAPI方式, 需要用到openId, appsecret, 详见Demo.java里面的方法
	 * auth: Yang
	 * 2016年7月19日 上午10:45:11
	 */
	public String pcOrders() {
		try {
			Dto dto = WebUtils.getParamAsDto(request);
			BL3Utils.handleNullStr(dto);
			
			//Map<String, Object> map1=aliPayService.getOrderInfo(params);
			Map<String, Object> map1 = new HashMap<String, Object>();
			if(map1 == null || map1.isEmpty()) {
				throw new RuntimeException("微信支付的订单不存在");
			}
			String random =  RandomNumber.getStringRandom(10);
			
			WxPayDto tpWxPay1 = new WxPayDto();
		    tpWxPay1.setAppid("wx426b3015555a46be"); // 微信分配的公众账号ID（企业号corpid即为此appId）:登录微信公共号平台--开发--基本配置--开发者ID
		    tpWxPay1.setKey("e10adc3949ba59abbe56e057f20f883e"); // 微信的商户号对应的秘钥key 32位长度
		    tpWxPay1.setMchid("1225312702"); // 微信的商户号: 登录微信公共号平台--微信支付--商户信息 中查看
		    tpWxPay1.setBody(String.valueOf(map1.get("goods")));
		    tpWxPay1.setSpbillCreateIp(this.getIpAddr(request));
		    tpWxPay1.setTotalFee(String.valueOf(map1.get("payment_amount")));
		    tpWxPay1.setTradeType("NATIVE");
		    tpWxPay1.setOrderId(dto.getAsString("orderNo"));
		    tpWxPay1.setNotifyUrl("http://dev.hncarlife.com:2007/DAP/weixinpay/weixinpay_savePcWeixinCallback.do");
		    tpWxPay1.setAttach(String.valueOf(map1.get("body")));
		    tpWxPay1.setGetNonceStr(random);
		    String codeurl = Demo.getCodeurl(tpWxPay1);
		    if(codeurl == null) {
		    	throw new RuntimeException("Call Weixin Interface Error!");
		    } 
			
		    request.setAttribute("codeurl", codeurl);
		    request.setAttribute("orderNo", dto.getAsString("orderNo"));
		    request.setAttribute("serviceType", dto.getAsInteger("serviceType"));
		    
		} catch (Exception e) {
			LOG.error("orders error, due to:", e);
		} 
		return "pay_detail";
	}

	/**
	 * PC端微信回调地址接口
	 * 
	 * @return Yang 2016年2月9日 下午5:22:57
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "unchecked"})
	public void savePcWeixinCallback() {
		StringBuffer sb = new StringBuffer("<xml><return_code>");
		String return_code = "";
		String return_msg = "";
		try {
			// xml请求解析微信返回的request数据
			Map<String, String> map = MaptoXML.parseRequestXml(request);
			
			if (map.containsKey("result_code") && !"SUCCESS".equals(map.get("result_code"))) {
				throw new RuntimeException("result_code fail");
			}
			
			String sign = map.get("sign").toString();
			map.remove("sign");
			String preSign = MaptoXML.map2String(map);//拼接字符串
			String signReturn = MaptoXML.sign(preSign, "e10adc3949ba59abbe56e057f20f883e");//签名
			if(!sign.equals(signReturn)) {
				throw new RuntimeException("sign fail");
			}

			String out_trade_no = map.get("out_trade_no"); //商户订单号
			String trade_no = map.get("transaction_id");  //交易的流水
			String account = map.get("total_fee"); // 支付金额, 返回单位是分, 记得除以100
			String buyer_id = map.get("accNo"); //买家支付卡号
			String buyer_email = map.get("accNo"); //买家支付卡号
			String seller_id = map.get("merId"); // 卖家商户号
			String seller_email = map.get("merId"); // 卖家商户号
			
			String gmt_create = DateUtil.getStringDate(map.get("time_end")); // 订单发送时间
			String gmt_payment =  DateUtil.getStringDate(map.get("time_end")); //交易付款时间，银联只有一个时间
			
			String body = URLDecoder.decode(map.get("attach"), "UTF-8");
			String trade_status = map.get("respCode"); // 交易状态
			Dto dto=new BaseDto();
			dto.put("out_trade_no",out_trade_no);
			dto.put("trade_no",trade_no);
			dto.put("account",String.valueOf((Double.parseDouble(account))/100));
			dto.put("buyer_id",buyer_id);
			dto.put("buyer_email",buyer_email);
			dto.put("seller_id",seller_id);
			dto.put("seller_email",seller_email);
			dto.put("trade_status",trade_status);
			dto.put("gmt_create",gmt_create);
			dto.put("gmt_payment",gmt_payment);
			dto.put("body",body);
			dto.put("payType","2"); //1-支付宝;2-微信支付;3-银联
			//this.aliPayService.bizHeadler(dto);
			
			return_code = "<![CDATA[SUCCESS]]>";
			return_msg = "<![CDATA[OK]]>";
		} catch (Exception e) {
			return_code = "<![CDATA[FAIL]]>";
			return_msg = "<![CDATA[" + e.getMessage() + "]]>";
			LOG.error("saveWeixinCallback error, due to:", e);
		} finally {
			sb.append(return_code + "</return_code><return_msg>" + return_msg + "</return_msg></xml>");
			this.out.print(sb.toString());
		}

	}

	// 得到访问的ip地址
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

}


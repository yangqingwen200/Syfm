package com.appserver.thirdpartpay.alipay.action;

import com.commons.util.DateUtil;
import com.commons.util.core.BaseDto;
import com.commons.util.core.Dto;
import com.appserver.action.BaseAppAction;
import com.appserver.thirdpartpay.alipay.config.AlipayConfig;
import com.appserver.thirdpartpay.alipay.util.AlipayNotify;
import com.appserver.thirdpartpay.alipay.util.AlipaySubmit;
import com.appserver.thirdpartpay.alipay.util.BodyBean;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝回调
 */
@Controller("com.sdk.alipay.action.AliPayAction")
@Scope("prototype")
public class AliPayAction extends BaseAppAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	
	public void aliPay(){
		try {
			////////////////////////////////////请求参数//////////////////////////////////////
			if(dto.get("orderNo")!=null){
//				Map<String, Object> map=aliPayService.getOrderInfo(params);
				Map<String, Object> map= null;

				//////////////////////////////////////////////////////////////////////////////////
				//把请求参数打包成数组
				Map<String, String> sParaTemp = new HashMap<String, String>();
				sParaTemp.put("_input_charset", AlipayConfig.input_charset);
				sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
				sParaTemp.put("body",map.get("body")!=null?URLEncoder.encode(map.get("body").toString(),"UTF-8"):null);
				sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
				sParaTemp.put("notify_url",AlipayConfig.notify_url);
				sParaTemp.put("out_trade_no", map.get("order_no")!=null?map.get("order_no").toString():null);
				sParaTemp.put("partner", AlipayConfig.partner);
				sParaTemp.put("payment_type", AlipayConfig.payment_type);
				sParaTemp.put("return_url",AlipayConfig.return_url);
				sParaTemp.put("seller_id", AlipayConfig.seller_id);
				sParaTemp.put("service", AlipayConfig.service);
				sParaTemp.put("sign_type", AlipayConfig.sign_type);
				sParaTemp.put("subject",map.get("goods")!=null?map.get("goods").toString():null);
				sParaTemp.put("total_fee", map.get("payment_amount")!=null?map.get("payment_amount").toString():"0");
				//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
				//如sParaTemp.put("参数名","参数值");

				//建立请求
				String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
				out.println(sHtmlText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	/**
	 * 支付宝即时到账交易接口
	 * 2015年11月11日 下午3:07:32
	 */
	public void notifyUrl() {
		try {
			Map<String,String> map = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				map.put(name, valueStr);
			}

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			if(AlipayNotify.verify(map)){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码
				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

				String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8"); //交易状态
				if(trade_status.equals("TRADE_FINISHED")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					//注意：
					//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				} else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					String out_trade_no = request.getParameter("out_trade_no") != null ? new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8") : ""; //商户订单号
					String trade_no = request.getParameter("trade_no") != null ?  new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8") : ""; //支付宝交易号
					String account = request.getParameter("total_fee") != null ? new String(request.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8") : "0.0"; //支付金额
					String buyer_id = request.getParameter("buyer_id") != null ?  new String(request.getParameter("buyer_id").getBytes("ISO-8859-1"),"UTF-8") : ""; //买家支付宝用户号
					String buyer_email = request.getParameter("buyer_email") != null ? new String(request.getParameter("buyer_email").getBytes("ISO-8859-1"),"UTF-8") : ""; //买家支付宝账号
					String seller_id = request.getParameter("seller_id") != null ?  new String(request.getParameter("seller_id").getBytes("ISO-8859-1"),"UTF-8") : ""; //买家支付宝用户号
					String seller_email = request.getParameter("seller_email") != null ? new String(request.getParameter("seller_email").getBytes("ISO-8859-1"),"UTF-8") : ""; //买家支付宝账号
					String gmt_create = request.getParameter("gmt_create") != null ? new String(request.getParameter("gmt_create").getBytes("ISO-8859-1"),"UTF-8") : DateUtil.formatDateTime(new Date());//交易创建时间
					String gmt_payment = request.getParameter("gmt_payment") != null ? new String(request.getParameter("gmt_payment").getBytes("ISO-8859-1"),"UTF-8") : DateUtil.formatDateTime(new Date()); //交易付款时间
					String body = request.getParameter("body") != null ? URLDecoder.decode(new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8"),"UTF-8") : "{}";
					Dto dto=new BaseDto();
					dto.put("out_trade_no",out_trade_no);
					dto.put("trade_no",trade_no);
					dto.put("account",account);
					dto.put("buyer_id",buyer_id);
					dto.put("buyer_email",buyer_email);
					dto.put("seller_id",seller_id);
					dto.put("seller_email",seller_email);
					dto.put("trade_status",trade_status);
					dto.put("gmt_create",gmt_create);
					dto.put("gmt_payment",gmt_payment);
					dto.put("body",body);
					dto.put("payType","1"); //1-支付宝;2-微信支付;3-银联
				//	this.aliPayService.bizHeadler(dto);

					//注意：
					//付款完成后，支付宝系统发送该交易状态通知
					//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				}

				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

				out.println("success");	//请不要修改或删除

				//////////////////////////////////////////////////////////////////////////////////////////
			}else{//验证失败
				out.println("fail");
			}
		} catch (Exception e) {
			LOG.error("checkAli exception", e);
		}
	}
	public String returnUrl(){
		try {
			//获取支付宝GET过来反馈信息
			Map<String,String> map = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				map.put(name, valueStr);
			}

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//支付宝交易号

			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			String body = request.getParameter("body") != null ? URLDecoder.decode(new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8"),"UTF-8") : "{}";
			System.out.println(body);
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

			//计算得出通知验证结果
			boolean verify_result = AlipayNotify.verify(map);

			if(verify_result){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码

				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
				if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					BodyBean bodyBean = (BodyBean) JSONObject.toBean(JSONObject.fromObject(body),BodyBean.class); //解析body内容,转换成bean形式
					if(bodyBean!=null){
						Dto pDto=new BaseDto();
						pDto.put("orderNo",out_trade_no);
						pDto.put("type",bodyBean.getServiceType());
					//	params.put("type",bodyBean.getServiceType());
						//map=pCommonService.toPaySuccess(dto);
						//sendMsgService.paySuccess(dto,2);
						return "pay_success";
					}
				}

				//该页面可做页面美工编辑
				out.println("验证成功<br />");
				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

				//////////////////////////////////////////////////////////////////////////////////////////
			}else{
				//该页面可做页面美工编辑
				out.println("验证失败");
			}
		} catch (Exception e) {
			LOG.error("checkAli exception", e);
		} finally {
		}
		return "";
	}
	/**
	 * 保存支付宝回调信息
	 *
	 * @author Yang
	 * 2015年11月11日 下午3:07:32
	 */
	public void saveZhifubaoCallback() {
		try {
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
			}

			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			if(AlipayNotify.verify(params)){//验证成功
				//////////////////////////////////////////////////////////////////////////////////////////
				//请在这里加上商户的业务逻辑程序代码
				//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

				String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8"); //交易状态
				if(trade_status.equals("TRADE_FINISHED")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					//注意：
					//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				} else if (trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					String out_trade_no = request.getParameter("out_trade_no") != null ? new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8") : ""; //商户订单号
					String trade_no = request.getParameter("trade_no") != null ?  new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8") : ""; //支付宝交易号
					String account = request.getParameter("total_fee") != null ? new String(request.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8") : "0.0"; //支付金额
					String buyer_id = request.getParameter("buyer_id") != null ?  new String(request.getParameter("buyer_id").getBytes("ISO-8859-1"),"UTF-8") : ""; //买家支付宝用户号
					String buyer_email = request.getParameter("buyer_email") != null ? new String(request.getParameter("buyer_email").getBytes("ISO-8859-1"),"UTF-8") : ""; //买家支付宝账号
					String seller_id = request.getParameter("seller_id") != null ?  new String(request.getParameter("seller_id").getBytes("ISO-8859-1"),"UTF-8") : ""; //买家支付宝用户号
					String seller_email = request.getParameter("seller_email") != null ? new String(request.getParameter("seller_email").getBytes("ISO-8859-1"),"UTF-8") : ""; //买家支付宝账号
					String gmt_create = request.getParameter("gmt_create") != null ? new String(request.getParameter("gmt_create").getBytes("ISO-8859-1"),"UTF-8") : DateUtil.formatDateTime(new Date());//交易创建时间
					String gmt_payment = request.getParameter("gmt_payment") != null ? new String(request.getParameter("gmt_payment").getBytes("ISO-8859-1"),"UTF-8") : DateUtil.formatDateTime(new Date()); //交易付款时间
					String body = request.getParameter("body") != null ? URLDecoder.decode(new String(request.getParameter("body").getBytes("ISO-8859-1"),"UTF-8"),"UTF-8") : "{}";
					Dto dto=new BaseDto();
					dto.put("out_trade_no",out_trade_no);
					dto.put("trade_no",trade_no);
					dto.put("account",account);
					dto.put("buyer_id",buyer_id);
					dto.put("buyer_email",buyer_email);
					dto.put("seller_id",seller_id);
					dto.put("seller_email",seller_email);
					dto.put("trade_status",trade_status);
					dto.put("gmt_create",gmt_create);
					dto.put("gmt_payment",gmt_payment);
					dto.put("body",body);
					dto.put("payType","1"); //1-支付宝;2-微信支付;3-银联
				//	this.aliPayService.bizHeadler(dto);
					//注意：
					//付款完成后，支付宝系统发送该交易状态通知
					//请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的

					//发送消息和推送消息
					BodyBean bodyBean = (BodyBean) JSONObject.toBean(JSONObject.fromObject(body),BodyBean.class); //解析body内容,转换成bean形式
					Dto pDto=new BaseDto();
					pDto.put("orderNo",out_trade_no);
					pDto.put("type",bodyBean.getServiceType());
				//	sendMsgService.paySuccess(dto,2);
				}
				//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

				out.println("success");	//请不要修改或删除
				//////////////////////////////////////////////////////////////////////////////////////////
			}else{//验证失败
				out.println("fail");
			}
		} catch (Exception e) {
			LOG.error("saveZhifubaoCallback exception", e);
		}
	}
}

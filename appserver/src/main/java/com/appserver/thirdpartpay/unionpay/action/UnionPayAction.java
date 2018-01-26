package com.appserver.thirdpartpay.unionpay.action;

import com.commons.util.DateUtil;
import com.commons.util.core.BaseDto;
import com.commons.util.core.Dto;
import com.appserver.action.BaseAppAction;
import com.appserver.thirdpartpay.alipay.util.BodyBean;
import com.appserver.thirdpartpay.unionpay.sdk.AcpService;
import com.appserver.thirdpartpay.unionpay.sdk.LogUtil;
import com.appserver.thirdpartpay.unionpay.sdk.SDKConfig;
import com.appserver.thirdpartpay.unionpay.sdk.SDKConstants;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 银联相关
 */
@Controller("com.unionpay.acp.action.UnionPayAction")
@Scope("prototype")
public class UnionPayAction extends BaseAppAction {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/** 金额为分的格式 */
	public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

	/**
	 * App发起支付调用银联接口, 获得tn参数 auth: Yang 2016年7月22日 下午8:27:47
	 */
	public void payApp() {
		try {
			// //////////////////////////////////请求参数//////////////////////////////////////
			this.checkRequestParam("orderNo", "serviceType", "userId");

			// Map<String, Object> map=aliPayService.getOrderInfo(dto);
			Map<String, Object> map = new HashMap<String, Object>();
			if (map == null || map.isEmpty()) {
				throw new RuntimeException("订单不存在");
			}

			SDKConfig config = SDKConfig.getConfig();
			// 组装银联支付请求参数
			Map<String, String> requestData = new HashMap<String, String>();
			requestData.put("version", config.getVersion()); // 版本号，全渠道默认值
			requestData.put("encoding", config.getEncoding_UTF8()); // 字符集编码，可以使用UTF-8,GBK两种方式
			requestData.put("signMethod", "01"); // 签名方法，只支持 01：RSA方式证书加密
			requestData.put("txnType", "01"); // 交易类型 ，01：消费
			requestData.put("txnSubType", "01"); // 交易子类型， 01：自助消费
			requestData.put("bizType", "000201"); // 业务类型，B2C网关支付，手机wap支付
			requestData.put("channelType", "08"); // 渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板
													// 08：手机

			/*** 商户接入参数 ***/
			requestData.put("merId", config.getMerId()); // 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
			requestData.put("accessType", "0"); // 接入类型，0：直连商户
			requestData.put("orderId", dto.get("orderNo").toString()); // 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
			requestData.put("txnTime",
					new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
			requestData.put("currencyCode", "156"); // 交易币种（境内商户一般是156 人民币）
			// 交易金额
			String amount = map.get("payment_amount") != null ? map.get(
					"payment_amount").toString() : "0";
			amount = changeY2F(amount);
			requestData.put("txnAmt", String.valueOf(amount)); // 交易金额，单位分，不要带小数点
			requestData.put(
					"reqReserved",
					map.get("body") != null ? URLEncoder.encode(map.get("body")
							.toString(), "UTF-8") : null); // 请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节

			// 后台通知地址（需设置为【外网】能访问 http
			// https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
			// 后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
			// 注意:1.需设置为外网能访问，否则收不到通知 2.http https均可
			// 3.收单后台通知后需要10秒内返回http200或302状态码
			// 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
			// 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d
			// 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
			requestData.put("backUrl", config.getBackNotifyUrl());

			/** 请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面 **/
			Map<String, String> submitFromData = AcpService.sign(requestData,
					config.getEncoding_UTF8()); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
			String requestFrontUrl = SDKConfig.getConfig().getAppRequestUrl(); // 获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
			Map<String, String> rspData = AcpService.post(submitFromData,
					requestFrontUrl, config.getEncoding_UTF8());

			/** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/
			// 应答码规范参考open.unionpay.com帮助中心 下载 产品接口规范 《平台接入接口规范-第5部分-附录》
			if (!rspData.isEmpty()) {
				if (AcpService.validate(rspData, config.getEncoding_UTF8())) {
					String respCode = rspData.get("respCode");
					if (("00").equals(respCode)) {
						// 成功,获取tn号
						String tn = rspData.get("tn");
						json.put("msg", tn);
						// TODO
					} else {
						// 其他应答码为失败请排查原因或做失败处理
						// TODO
						throw new RuntimeException("respCode返回不为00, respCode: "
								+ respCode);
					}
				} else {
					LogUtil.writeErrorLog("验证签名失败");
					// TODO 检查验证签名失败的原因
					throw new RuntimeException("验证签名失败");
				}
			} else {
				// 未返回正确的http状态
				LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
				throw new RuntimeException("未获取到返回报文或返回http状态码非200");
			}
		} catch (Exception e) {
			//this.judgementExceptionType(e);
		} finally {
			this.renderJson();
		}
	}

	/**
	 * PC端网页支付发起支付接口 auth: Yang 2016年7月22日 下午8:12:53
	 */
	public void pay() {
		try {
			// //////////////////////////////////请求参数//////////////////////////////////////
			if (dto.get("orderNo") != null) {
				//Map<String, Object> map = aliPayService.getOrderInfo(dto);
				Map<String, Object> map = new HashMap<String, Object>();
				SDKConfig config = SDKConfig.getConfig();

				// 组装银联支付请求参数
				Map<String, String> requestData = new HashMap<String, String>();
				requestData.put("version", config.getVersion()); // 版本号，全渠道默认值
				requestData.put("encoding", config.getEncoding_UTF8()); // 字符集编码，可以使用UTF-8,GBK两种方式
				requestData.put("signMethod", "01"); // 签名方法，只支持 01：RSA方式证书加密
				requestData.put("txnType", "01"); // 交易类型 ，01：消费
				requestData.put("txnSubType", "01"); // 交易子类型， 01：自助消费
				requestData.put("bizType", "000201"); // 业务类型，B2C网关支付，手机wap支付
				requestData.put("channelType", "07"); // 渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板
														// 08：手机

				/*** 商户接入参数 ***/
				requestData.put("merId", config.getMerId()); // 商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
				requestData.put("accessType", "0"); // 接入类型，0：直连商户
				requestData.put("orderId", dto.get("orderNo").toString()); // 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
				requestData.put("txnTime", new SimpleDateFormat(
						"yyyyMMddHHmmss").format(new Date())); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
				requestData.put("currencyCode", "156"); // 交易币种（境内商户一般是156 人民币）
				// 交易金额
				String amount = map.get("payment_amount") != null ? map.get(
						"payment_amount").toString() : "0";
				amount = changeY2F(amount);
				requestData.put("txnAmt", String.valueOf(amount)); // 交易金额，单位分，不要带小数点
				requestData.put(
						"reqReserved",
						map.get("body") != null ? URLEncoder.encode(
								map.get("body").toString(), "UTF-8") : null); // 请求方保留域，如需使用请启用即可；透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节

				// 前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面
				// 点击“返回商户”按钮的时候将异步通知报文post到该地址
				// 如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
				// 异步通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
				requestData.put("frontUrl", config.getFrontNotifyUrl());

				// 后台通知地址（需设置为【外网】能访问 http
				// https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
				// 后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
				// 注意:1.需设置为外网能访问，否则收不到通知 2.http https均可
				// 3.收单后台通知后需要10秒内返回http200或302状态码
				// 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
				// 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d
				// 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
				requestData.put("backUrl", config.getBackNotifyUrl());

				// ////////////////////////////////////////////////
				//
				// 报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
				//
				// ////////////////////////////////////////////////

				/** 请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面 **/
				Map<String, String> submitFromData = AcpService.sign(
						requestData, config.getEncoding_UTF8()); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

				String requestFrontUrl = SDKConfig.getConfig()
						.getFrontRequestUrl(); // 获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
				String html = AcpService.createAutoFormHtml(requestFrontUrl,
						submitFromData, config.getEncoding_UTF8()); // 生成自动跳转的Html表单

				LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);
				// 将生成的html写到浏览器中完成自动跳转打开银联支付页面；这里调用signData之后，将html写到浏览器跳转到银联页面之前均不能对html中的表单项的名称和值进行修改，如果修改会导致验签不通过
				out.write(html);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
	 * 
	 * @param amount
	 * @return
	 */
	public static String changeY2F(String amount) {
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
																// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(
					".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(
					".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(
					".", "") + "00");
		}
		return amLong.toString();
	}

	/**
	 * 将分为单位的转换为元 （除100）
	 * 
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String changeF2Y(String amount) throws Exception {
		if (!amount.matches(CURRENCY_FEN_REGEX)) {
			throw new Exception("金额格式有误");
		}
		return BigDecimal.valueOf(Long.valueOf(amount))
				.divide(new BigDecimal(100)).toString();
	}

	public void notifyUrl() {
		try {
			LogUtil.writeLog("BackRcvResponse接收后台通知开始");

			String encoding = (String) dto.get(SDKConstants.param_encoding);
			// 获取银联通知服务器发送的后台通知参数
			Map<String, String> reqParam = getAllRequestParam(request);

			LogUtil.printRequestLog(reqParam);

			Map<String, String> valideData = null;
			if (null != reqParam && !reqParam.isEmpty()) {
				Iterator<Map.Entry<String, String>> it = reqParam.entrySet()
						.iterator();
				valideData = new HashMap<String, String>(reqParam.size());
				while (it.hasNext()) {
					Map.Entry<String, String> e = it.next();
					String key = e.getKey();
					String value = e.getValue();
					value = new String(value.getBytes(encoding), encoding);
					valideData.put(key, value);
				}
			}

			// 重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
			if (!AcpService.validate(valideData, encoding)) {
				LogUtil.writeLog("验证签名结果[失败].");
				// 验签失败，需解决验签问题
				out.println("fail");
			} else {
				LogUtil.writeLog("验证签名结果[成功].");
				// 【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态

				String orderId = valideData.get("orderId"); // 获取后台通知的数据，其他字段也可用类似方式获取
				String respCode = valideData.get("respCode"); // 获取应答码，收到后台通知了respCode的值一般是00，可以不需要根据这个应答码判断。

				String out_trade_no = valideData.get("orderId"); // 订单号
				String trade_no = valideData.get("queryId"); // 交易的流水
				String account = changeF2Y(valideData.get("txnAmt")); // 支付金额(元)
				String buyer_id = valideData.get("accNo"); // 买家支付卡号
				String buyer_email = valideData.get("accNo"); // 买家支付卡号
				String seller_id = valideData.get("merId"); // 卖家商户号
				String seller_email = valideData.get("merId"); // 卖家商户号

				String gmt_create = DateUtil.getStringDate(valideData
						.get("txnTime")); // 订单发送时间
				String gmt_payment = DateUtil.getStringDate(valideData
						.get("txnTime")); // 交易付款时间，银联只有一个时间

				String body = URLDecoder.decode(valideData.get("reqReserved"),
						"UTF-8");
				String trade_status = valideData.get("respCode"); // 交易状态
				Dto dto = new BaseDto();
				dto.put("out_trade_no", out_trade_no);
				dto.put("trade_no", trade_no);
				dto.put("account", account);
				dto.put("buyer_id", buyer_id);
				dto.put("buyer_email", buyer_email);
				dto.put("seller_id", seller_id);
				dto.put("seller_email", seller_email);
				dto.put("trade_status", trade_status);
				dto.put("gmt_create", gmt_create);
				dto.put("gmt_payment", gmt_payment);
				dto.put("body", body);
				dto.put("payType", "3"); // 1-支付宝;2-微信支付;3-银联
				// this.aliPayService.bizHeadler(dto);

			}
			LogUtil.writeLog("BackRcvResponse接收后台通知结束");
			// 返回给银联服务器http 200 状态码
			out.println("success");

		} catch (Exception ex) {
			// log.error(ex);
			out.println("fail");
		}
	}

	/**
	 * 支付成功点击“返回商户”按钮的时候出现的处理页面
	 */
	public String returnUrl() throws Exception {
		LogUtil.writeLog("FrontRcvResponse前台接收报文返回开始");

		String encoding = (String) dto.get(SDKConstants.param_encoding);
		LogUtil.writeLog("返回报文中encoding=[" + encoding + "]");
		Map<String, String> respParam = getAllRequestParam(request);

		// 打印请求报文
		LogUtil.printRequestLog(respParam);

		Map<String, String> valideData = null;
		if (null != respParam && !respParam.isEmpty()) {
			Iterator<Map.Entry<String, String>> it = respParam.entrySet()
					.iterator();
			valideData = new HashMap<String, String>(respParam.size());
			while (it.hasNext()) {
				Map.Entry<String, String> e = it.next();
				String key = e.getKey();
				String value = e.getValue();
				value = new String(value.getBytes(encoding), encoding);
				valideData.put(key, value);
			}
		}
		if (!AcpService.validate(valideData, encoding)) {
			LogUtil.writeLog("验证签名结果[失败].");
			out.println("验证失败");
		} else {
			LogUtil.writeLog("验证签名结果[成功].");
			String body = URLDecoder.decode(valideData.get("reqReserved"),
					"UTF-8");
			BodyBean bodyBean = (BodyBean) JSONObject.toBean(
					JSONObject.fromObject(body), BodyBean.class); // 解析body内容,转换成bean形式
			if (bodyBean != null) {
				Dto pDto = new BaseDto();
				pDto.put("orderNo", valideData.get("orderId"));
				pDto.put("type", bodyBean.getServiceType());
				// dto.put("type",bodyBean.getServiceType());
				// map=pCommonService.toPaySuccess(dto);
				// sendMsgService.paySuccess(dto,2);
				LogUtil.writeLog("FrontRcvResponse前台接收报文返回结束");
				return "pay_success";
			}
		}

		return "";
	}

	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(
			final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (res.get(en) == null || "".equals(res.get(en))) {
					// System.out.println("======为空的字段名===="+en);
					res.remove(en);
				}
			}
			return res;
		}
		return res;
	}
}

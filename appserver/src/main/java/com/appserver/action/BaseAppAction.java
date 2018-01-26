package com.appserver.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.appserver.enums.AppExcEnum;
import com.commons.action.BaseAction;
import com.commons.exception.ValidateException;
import com.commons.util.core.BL3Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * App Action公用的基础类, 子类从此类继承
 * @author Yang
 * @version v1.0
 * @date 2016年12月6日
 */
public abstract class BaseAppAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	protected static final Logger LOG = LoggerFactory.getLogger(BaseAppAction.class);
	private AppExcEnum okOrFail = AppExcEnum.OK;
	protected JSONObject json = new JSONObject();
	private String message = null;

	/**
	 * FastJson有自动剔除null值的key<br>
	 * 如果要保留: JSON.toJSONString(json, SERIA_FEATURES)<br>
	 * <a href="http://blog.csdn.net/a258831020/article/details/47333807">点击链接查看</a>
	 */
	protected final static SerializerFeature[] SERIA_FEATURES = {
			SerializerFeature.WriteMapNullValue, //是否输出值为null的字段,默认为false 
			SerializerFeature.WriteNullListAsEmpty, //数值字段如果为null,输出为0,而非null
			SerializerFeature.WriteNullBooleanAsFalse, //List字段如果为null,输出为[],而非null
			SerializerFeature.WriteNullNumberAsZero, //字符类型字段如果为null,输出为”“,而非null
			SerializerFeature.WriteNullStringAsEmpty //Boolean字段如果为null,输出为false,而非null
	};

	/**
	 * 检查客户端请求是否少传参数, 开发过程中用到, 系统稳定之后, 可以去掉此检查<br>
	 * 
	 * 可以在配文件中增加标识{@link com.commons.constant.InitPpConstant InitPpConstant}判断当前环境开发环境还是生产环境
	 * @param parameterValues 需要检查的必传的参数
	 * @author Yang
	 * @version v1.0
	 * @date 2016年11月23日
	 */
	protected void checkRequestParam(String... parameterValues) {
		for (String o : parameterValues) {
			if (!dto.containsKey(o)) {
				throw new ValidateException(AppExcEnum.LESS_PARAMETER, o);
			}
			if (dto.get(o) == null || "".equals(dto.get(o).toString().trim())) {
				throw new ValidateException(AppExcEnum.PARAMETER_VALUE_ISNULL, o);
			}
		}
	}

	/**
	 * 将JSON对象转换成字符串, 输出到客户端.<br/><br/>
	 *
	 * Action中每个方法最后都要调用此方法, 可以改造为在拦截器中输出到App中.<br/>
	 * 思路: 在拦截器中从ValueStack中取出json对象的值(使用valueStack.findValue("json")), 然后print到App中.<br>
	 * 		<strong>前提是json必须要有set和get方法</strong><br/>
	 * 		AccessInterceptor_nouse 具体查看AccessInterceptor_nouse类
	 *
	 * @author Yang
	 * @version v1.0
	 * @date 2016年11月23日
	 */
	protected void renderJson() {
		json.put("code", okOrFail.getExcCode());
		json.put("msg", message != null ? message : okOrFail.getExcMsg());

		String result = JSON.toJSONString(json);
		LOG.info("Response msg: {}", result);
		out.print(result);
		out.flush();
		out.close();
	}

	/**
	 * 检测异常类型
	 *
	 * @param e
	 */
	protected void checkException(Exception e) {
		json.clear();
		if (e instanceof ValidateException) {
			ValidateException ve  = (ValidateException) e;
			okOrFail = (AppExcEnum)ve.getMsginfo();
			Object[] paramValue = ve.getParamValue();
			if(null != paramValue && paramValue.length > 0) {
				message = MessageFormat.format(okOrFail.getExcMsg(), paramValue);
			}
		} else {
			okOrFail = AppExcEnum.ERROR;
			LOG.error(BL3Utils.getErrorMessage(3), e);
		}
	}

}

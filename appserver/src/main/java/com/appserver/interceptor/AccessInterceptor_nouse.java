package com.appserver.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.appserver.enums.AppExcEnum;
import com.commons.exception.ValidateException;
import com.commons.util.core.Dto;
import com.commons.util.core.WebUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.MessageFormat;

/**
 * 用拦截器获得aciton中属性, 并print到App中, 项目暂未用到
 * @author Yang
 * @version v1.0
 * @date 2016年9月17日
 */
public class AccessInterceptor_nouse extends MethodFilterInterceptor {

	private static final long serialVersionUID = 4185130583194819455L;
	private final static Logger LOG = LoggerFactory.getLogger(AccessInterceptor_nouse.class);

	public String doIntercept(ActionInvocation actionInvocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		ActionContext context = actionInvocation.getInvocationContext();
		ValueStack valueStack = context.getValueStack();
		JSONObject json = new JSONObject();
		AppExcEnum okOrFail = AppExcEnum.OK;
		boolean isError = false;
		Object[] paramValue = null;

		long start = System.currentTimeMillis();
		Object action = actionInvocation.getAction();
		String methodName = actionInvocation.getProxy().getMethod(); // 得到访问的方法名称
		Class<?> actionClass = action.getClass();

		try {
			Dto pDto = WebUtils.getParamAsDto(request);
			String ip = WebUtils.getClientIp(request); //得到操作者的IP地址
			Object[] arr = new Object[]{ip, actionClass.getName(), methodName, pDto.toString()};
			LOG.info("Request msg: {}, {}.{}(), {}", arr);
			String invoke = actionInvocation.invoke();

			/**
			 * Action中所有属性(必须要有get和set方法)都会保存在ValueStack中,
			 * 所以在ValueStack修改(经过Action之前)或者得到Action中属性(经过Action之后)
			 */

			/**
			 * JSONObject json为Action中成员变量(必须要有set和get方法), 经过Action中处理之后, 把要返回前台的数据全部放在json中(put进去)
			 *
			 * 在拦截器中 从ValueStack中取出json对象值print给App. 避免Action中每个方法最后都要写print json给App.
			 */
			Object actionJson = valueStack.findValue("json");
			if(actionJson instanceof JSONObject) {
				json = (JSONObject) actionJson;
			}
			return invoke;  //如果访问Action方法为void, invoke的值null, 否则为方法返回什么, invoke值就是什么.

		} catch (Exception e) {
			/**
			 * 所有的异常信息都在这里处理, 包括service/action/interceptor中的异常
			 */
			if(e instanceof ValidateException) {
				ValidateException ve  = (ValidateException) e;
				okOrFail = (AppExcEnum)ve.getMsginfo();
				if(null != ve.getParamValue() && ve.getParamValue().length > 0) {
					isError = true;
					paramValue = ve.getParamValue();
				}
			} else {
				okOrFail = AppExcEnum.ERROR;
				LOG.error(actionClass.getName() + ", " + methodName + "(), due to:", e);
			}
			return null;
		} finally {
			json.put("code", okOrFail.getExcCode());
			json.put("msg", isError ? MessageFormat.format(okOrFail.getExcMsg(), paramValue) : okOrFail.getExcMsg());
			LOG.info("Response msg: {}", json.toJSONString());
			long end = System.currentTimeMillis();
			LOG.info("Consuming time: {}ms\n", (end - start));
			PrintWriter out = response.getWriter();
			out.println(json.toString());
			out.flush();
			out.close();
		}
	}

}
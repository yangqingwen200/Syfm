package com.appserver.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.appserver.enums.AppExcEnum;
import com.commons.exception.ValidateException;
import com.commons.util.core.Dto;
import com.commons.util.core.WebUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.MessageFormat;

/**
 * 得到App请求的ip地址, 访问类 方法 参数, 以及异常的捕获
 *
 * @author Yang
 * @version v1.0
 * @date 2016年9月17日
 */
public class AccessInterceptor extends MethodFilterInterceptor {
	
	private static final long serialVersionUID = 4185130583194819455L;
	private final static Logger LOG = LoggerFactory.getLogger(AccessInterceptor.class);

	public String doIntercept(ActionInvocation actionInvocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
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
			return invoke;

		} catch (Exception e) {
			/**
			 * interceptor中的异常在这里处理, 如果action的方法中没有使用try的话, 此处也会捕获到异常.
			 */
			AppExcEnum okOrFail = AppExcEnum.ERROR;
			String message = okOrFail.getExcMsg();

			if(e instanceof ValidateException) {
				ValidateException ve  = (ValidateException) e;
				okOrFail = (AppExcEnum)ve.getMsginfo();
				message = okOrFail.getExcMsg();
				if(null != ve.getParamValue() && ve.getParamValue().length > 0) {
					message = MessageFormat.format(message, ve.getParamValue());
				}
			} else {
				LOG.error(actionClass.getName() + ", " + methodName + "(), due to:", e);
			}
			PrintWriter out = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("code", okOrFail.getExcCode());
			json.put("msg", message);

			LOG.info("Response msg: {}", json.toJSONString());
			out.println(json.toString());
			out.flush();
			out.close();
			return null;
		} finally {
			long end = System.currentTimeMillis();
			LOG.info("Consuming time: {}ms\n", (end - start));
		}
	}

}

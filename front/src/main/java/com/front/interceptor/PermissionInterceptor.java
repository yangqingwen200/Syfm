package com.front.interceptor;

import com.front.constant.FrontConstant;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class PermissionInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 4185130583194819455L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String doIntercept(ActionInvocation actionInvocation) throws Exception {
		ActionContext invocationContext = actionInvocation.getInvocationContext();
		String actionPath = invocationContext.getName();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String _random = request.getParameter("_random"); //提交数据的请求 add or edit
		String random = request.getParameter("random"); //获得数据请求 get
		Map session = invocationContext.getSession();

		List<String> permission = (List<String>) session.get(FrontConstant.PERMISSION_KEY);
		if(null == permission) {
			String requestURI = "?_forward=" + request.getRequestURI();
			String forwardUrl = request.getContextPath() + requestURI;
			if(null != random) {
				response.getWriter().println(FrontConstant.HTML_FORWARD.replace("forwardUrl", forwardUrl));
			} else if(null != _random) {
				JSONObject json = new JSONObject();
				json.accumulate("message", false);
				json.accumulate("errorMsg", FrontConstant.HTML_FORWARD.replace("forwardUrl", forwardUrl));
				response.getWriter().println(json.toString());
			} else {
				response.sendRedirect(forwardUrl);
			}
			return null;
		} else if(!permission.contains(actionPath)) {
			if(null != _random) { //有_random参数说明为ajax请求, 返回的为json串
				JSONObject json = new JSONObject();
				json.accumulate("message", false);
				json.accumulate("errorMsg", FrontConstant.OPERATION_NO_PERMISSION);
				response.getWriter().println(json.toString());

			} else { //直接打印字符串在页面即可
				response.getWriter().println(FrontConstant.OPERATION_NO_PERMISSION);
			}
			return null;
		} else {
			return actionInvocation.invoke();
		}
	}
}

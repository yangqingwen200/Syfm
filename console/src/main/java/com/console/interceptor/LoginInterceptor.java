package com.console.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.commons.bean.system.SysUser;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class LoginInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 4185130583194819455L;
	
	@SuppressWarnings("rawtypes")
	public String doIntercept(ActionInvocation actionInvocation) throws Exception {
		Map session = (Map) actionInvocation.getInvocationContext().getSession();
		SysUser user = (SysUser)session.get("user");
		if(user != null) {
			return actionInvocation.invoke();
		} else {
			HttpServletResponse response = ServletActionContext.getResponse();
			JSONObject json = new JSONObject();
			PrintWriter out = response.getWriter();
			json.accumulate("message", false);
			json.accumulate("errorMsg", "登录超时或在别处登录，请重新登录。");
			out.println(json.toString());
			out.close();
			return null;
		}
	}
}

package com.console.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class PermissionInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 4185130583194819455L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String doIntercept(ActionInvocation actionInvocation) throws Exception {
		String actionPath = actionInvocation.getInvocationContext().getName();
		String[] path = actionPath.split("_");
		Map session = actionInvocation.getInvocationContext().getSession();
		JSONObject json = new JSONObject();

		// 判断session失效
		if (session.get("user") == null) {
			json.accumulate("errorMsg", "登录超时或在别处登录，请重新登录。");
		} else {
			// 获取用户许可
			if (session.get("upp") != null) {
				Map<String, Object> permission_l = (Map<String, Object>) session.get("upp");
				/*
				 * struts.xml 
				 * 有部分路径为: *_*_* 通配符形式, 中间的*为权限标识符
				 * 有部分路径为: *_* 通配符形式, 第一个的*为权限标识符
				 */
				String endpath = path.length > 2 ? path[1] : path[0];
				if (permission_l.containsValue(endpath)) {
					return actionInvocation.invoke();
				} else {
					json.accumulate("errorMsg", "温馨提示：您的操作权限不足。");
				}
			} else {
				json.accumulate("errorMsg", "温馨提示：权限为空。");
			}
		}

		json.accumulate("total", 0);
		json.accumulate("rows", 0);
		HttpServletResponse response = ServletActionContext.getResponse();
		if(path.length > 2) {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setAttribute("result", json.toString());
			request.getRequestDispatcher("/admin/common/nopermission.jsp").forward(request, response);
		} else {
			PrintWriter out = response.getWriter();
			out.println(json.toString());
			out.close();
		}
		return null;
	}
}

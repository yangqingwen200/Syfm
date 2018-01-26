package com.front.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yang
 * @version v1.0
 * @date 2017/8/1
 */
public class FrontExcInterceptor implements Interceptor {

    protected static final Logger LOG = LoggerFactory.getLogger(FrontExcInterceptor.class);

    @Override
    public void destroy() {

    }

    @Override
    public void init() {

    }

    public String intercept(ActionInvocation actioninvocation) {
        try {
            String result = null; // Action的返回值
            // 运行被拦截的Action, 期间如果发生异常会被catch住
            result = actioninvocation.invoke();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            HttpServletRequest request = ServletActionContext.getRequest();
            request.setAttribute("errorMessage", this.getClass().getSimpleName() + " Waring: " + e.getMessage());
            return "error";
        }
    }

}


package com.appserver.interceptor;

import com.appserver.constant.AppConstant;
import com.appserver.enums.AppExcEnum;
import com.commons.annotation.Login;
import com.commons.annotation.Version;
import com.commons.context.AppContext;
import com.commons.exception.ValidateException;
import com.commons.redis.CacheRedisClient;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 检查用户调用接口是否登录, 以及App目前的版本.<br/><br/>
 * 一般验证是Token,具体方法和设计文档<a href="http://www.cnblogs.com/whcghost/p/5657594.html">查看链接</a>
 * @author Yang
 * @version v1.0
 * date 2017/3/21
 */
public class LoginVersionInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 4185130583194819455L;
	private static final CacheRedisClient cacheRedis = AppContext.getBean("cacheRedisClient", CacheRedisClient.class);

	/**
	 * 抛出的异常在AccessInterceptor拦截器中处理
	 * @param actionInvocation
	 * @return
	 * @throws Exception
	 */
	public String doIntercept(ActionInvocation actionInvocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String methodName = actionInvocation.getProxy().getMethod(); // 得到访问的方法名称
		Object action = actionInvocation.getAction();
		Class<?> actionClass = action.getClass();
		Method method = actionClass.getMethod(methodName);

		//最好是采用Token方式来验证, 此处采用简单session来做简单校验.
		//Token具体方法和设计文档: http://www.cnblogs.com/whcghost/p/5657594.html
		Login login = method.getAnnotation(Login.class); //得到是否要登录标识
		if(null != login) {
			String userId = request.getParameter("userId");
			String token = request.getParameter("token");
			if(null == token || null == userId) { //少传参数
				throw new ValidateException(AppExcEnum.LESS_PARAMETER_USERID_TOKEN);
			}
			String tokenRedis = cacheRedis.get(AppConstant.USER_TOKEN + userId); //从redis中取出token对应的userId
			if(null == tokenRedis){
				throw new ValidateException(AppExcEnum.APPUSER_NOT_LOGIN);
			}
			if(!tokenRedis.equals(token)) {
				throw new ValidateException(AppExcEnum.APPUSER_OTHERALIAS_LOGIN);
			}
		}

		Version v = method.getAnnotation(Version.class); //得到该接口目前版本号
		String version = request.getParameter("version"); //得到App传递到服务器版本号
		if(null != v) {
			String ve = v.version();
			if(!ve.equals(version)) {
				throw new ValidateException(AppExcEnum.APP_VERSION_LOW);
			}
		}
		return actionInvocation.invoke();
	}
}

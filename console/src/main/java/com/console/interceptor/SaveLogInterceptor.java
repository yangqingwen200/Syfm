package com.console.interceptor;

import com.commons.annotation.OpernationLog;
import com.commons.constant.CommonConstant;
import com.commons.service.GenericService;
import com.commons.util.Asyncs;
import com.commons.util.MaptoBean;
import com.commons.util.core.Dto;
import com.commons.util.core.WebUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.commons.bean.system.SysLogOperation;
import com.commons.bean.system.SysUser;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

public class SaveLogInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 4185130583194819455L;
	private GenericService publicService;

	@SuppressWarnings("rawtypes")
	public String doIntercept(ActionInvocation actionInvocation) throws Exception {
		String methodName = actionInvocation.getProxy().getMethod(); // 得到访问的方法名称
		// 只有新增, 编辑, 删除才做日志
		if (methodName.startsWith("edit") || methodName.startsWith("delete") || methodName.startsWith("add")) {
			Class<?> actionClass = actionInvocation.getAction().getClass(); // 得到访问的action类
			Method method = actionClass.getMethod(methodName);
			OpernationLog annotation = method.getAnnotation(OpernationLog.class);
			if (null != annotation) {

				Map session = actionInvocation.getInvocationContext().getSession();
				HttpServletRequest request = ServletActionContext.getRequest();
				String ip = WebUtils.getClientIp(request); // 得到操作者的IP地址
				Dto pDto = WebUtils.getParamAsDto(request);
				SysUser user = (SysUser) session.get("user"); // 从session中得到登录用户信息

				/*
				 * 异步线程保存操作日志 其中用部分反射, 慎用
				 */
				Asyncs.submit(() -> {
					try {
						String value = annotation.value(); // 方法的描述信息
						Class<?> cls = annotation.cls(); // 得到实体bean的class
						Table annotation2 = cls.getAnnotation(Table.class);
						String tableName = annotation2.name(); // 表名
						StringBuffer descprition = new StringBuffer("表名: " + tableName + CommonConstant.ESCAPE_KEYWORD);

						String object = pDto.getAsString("id");
						if (methodName.startsWith("edit")) { // 编辑操作
							Object objDB = null;
							Field declaredField = cls.getDeclaredField("id"); // 得到private id
							if (null != declaredField) {
								/**
								 * 新思路: 可以考虑把从数据库加载出来的对象转为map, 比对key对应的value值是否有变化, 这样避免用大量的反射.
								 * 
								 * 如果用map的话, 仍然用反射, 找到属性对应的数据库字段的名称, 除非日志里面直接保存属性名称.
								 */
								descprition.append("id: " + object + CommonConstant.ESCAPE_KEYWORD);
								String typeName = declaredField.getGenericType().getTypeName();
								if ("java.lang.Integer".equals(typeName)) {
									objDB = this.publicService.load(cls, Integer.parseInt(object)); // 根据主键id, 从数据库加载值, id为int类型(对应java中的Integer)
								} else if ("java.lang.Long".equals(typeName)) {
									objDB = this.publicService.load(cls, Long.parseLong(object)); // 根据id, 从数据库加载值, id位bigint类型(对应java中的Long)
								} else {
									objDB = this.publicService.load(cls, object); // 根据id, 从数据库加载值, id位varchar类型或者char类型(对应java中的String)
								}

								Object mapToBeanBasic = MaptoBean.mapToBeanBasic(cls, pDto); // 将页面传递过来的值, 封装成对象值
								String differntFieldValue = MaptoBean.differntFieldValue(objDB, mapToBeanBasic); // 得到两个对象不同属性值拼成的字符串
								descprition.append(differntFieldValue);

							}

						} else if (methodName.startsWith("delete")) { // 删除操作
							descprition.append("id: " + object + CommonConstant.ESCAPE_KEYWORD);

						} else if (methodName.startsWith("add")) { // 新增操作
							Object mapToBeanBasic = MaptoBean.mapToBeanBasic(cls, pDto); // 将页面传递过来的值, 封装成对象值
							String differntFieldValue = MaptoBean.differntFieldValue(mapToBeanBasic, cls.newInstance()); // 得到两个对象不同属性值拼成的字符串
							descprition.append(differntFieldValue);
						}

						SysLogOperation sl = new SysLogOperation(user.getId(), user.getLoginname(), new Date(), ip, actionClass.getName(), methodName, descprition.toString(), value);
						this.publicService.save(sl);

					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
		}

		return actionInvocation.invoke();
	}

	public GenericService getPublicService() {
		return publicService;
	}

	@Autowired
	@Qualifier("publicService")
	public void setPublicService(GenericService publicService) {
		this.publicService = publicService;
	}
}

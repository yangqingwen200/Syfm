package com.commons.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 获得Spring容器中的service
 * @author Yang
 * @version v1.0
 * @date 2016年11月17日
 */
public class AppContext implements ApplicationContextAware {

	private static ApplicationContext APP;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AppContext.APP = applicationContext;
	}

	/**
	 * 通过名字类型获取spring管理的Bean
	 * 用法:　GenericService gs = (GenericService) AppContext.getBean("publicService");
	 * @return bean or null.
	 */
	public static Object getBean(String id) {
		return APP.getBean(id);
	}

	/**
	 * 通过Class类型获取spring管理的Bean
	 * 用法:　GenericService gs = AppContext.getBean(GenericService.class);
	 * 注意: 接口GenericService 只能有一个实现类, 否则就会报错
	 * @param tClass
	 * @return
	 * @author Yang
	 * @version v1.0
	 * @date 2016年11月21日
	 */
	public static <T> T getBean(Class<T> tClass) {
		return APP.getBean(tClass);
	}
	

	/**
	 * 通过名字和Class类型获取spring管理的Bean
	 * 用法:　GenericService gs = AppContext.getBean("publicService", GenericService.class);
	 * @param beanName bean名称
	 * @param requiredType bean的Class类型
	 * @return bean or null.
	 */
	public static <T> T getBean(String beanName, Class<T> requiredType) {
		if (null == beanName) {
			return null;
		}
		return APP.getBean(beanName, requiredType);
	}
}

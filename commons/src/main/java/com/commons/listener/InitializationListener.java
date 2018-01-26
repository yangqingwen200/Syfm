package com.commons.listener;

import com.commons.constant.InitDBConstant;
import com.commons.context.AppContext;
import com.commons.dao.GenericDao;
import com.commons.util.Asyncs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Tomcat容器启动,初始化常量类{@link com.commons.constant.InitDBConstant InitDBConstant}属性 , 避免每次使用都从数据库中查询
 * @author Yang
 * @version v1.0
 * @date 2016年11月16日
 */
public class InitializationListener extends ContextLoaderListener {

	private static final Logger LOG = LoggerFactory.getLogger(InitializationListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String simpleName = this.getClass().getSimpleName();
		LOG.info(simpleName + ": Begin Initialize Constant...");

		GenericDao publicDao = AppContext.getBean("publicDao", GenericDao.class);
		List<Map<String, Object>> findBySqlMap = publicDao.findSqlListMap("select code, value from sys_code");

		//利用反射, 给InitDBConstant类中的所有属性赋值
		Class<?> cla = InitDBConstant.class;
		Field[] filed = cla.getFields();
		for (Field field : filed) {
			String fieldName = field.getName();
			for (Map<String, Object> map : findBySqlMap) {
				if (fieldName.equals(map.get("code"))) {
					try {
						field.set(cla, map.get("value"));
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		LOG.info(simpleName + ": Over Initialize Constant...");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		String simpleName = this.getClass().getSimpleName();
		LOG.info(simpleName + ": Begin Shutdown Asyncs...");
		Asyncs.shutdownNow();  //关闭线程池, 避免Tomcat Stop时无法关闭
		LOG.info(simpleName + ": Over Shutdown Asyncs...");
	}

}

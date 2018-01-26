package com.commons.listener;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 为了不让logback.xml直接放在src(classpath)目录下, 自定义监听器, 改变其路径<br>
 * 注意: 这里使用相对路径.<br>
 * 使用绝对路径, 请查看网页博客, <a href="http://blog.csdn.net/lijunwyf/article/details/46792537">点击链接查看</a>
 * @author Yang
 * @version v1.0
 * @date 2016年9月24日
 */
public class LogbackConfigListener implements ServletContextListener {
	private static final String CONFIG_LOCATION = "logbackConfigLocation";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// 从web.xml中加载指定文件名的日志配置文件
		String logbackConfigLocation = event.getServletContext().getInitParameter(CONFIG_LOCATION);
		//得到配置文件的路径
		String fn = event.getServletContext().getRealPath(logbackConfigLocation);
		
		try {
			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			loggerContext.reset();
			JoranConfigurator joranConfigurator = new JoranConfigurator();
			joranConfigurator.setContext(loggerContext);
			joranConfigurator.doConfigure(fn);
		} catch (JoranException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		};

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}

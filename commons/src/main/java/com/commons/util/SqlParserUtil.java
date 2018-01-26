package com.commons.util;

import com.commons.enums.AbstractSqlEnum;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class SqlParserUtil {

	public static String parser(String sql, Map<String, Object> map) {
		String queryName = "template";
		return parser(queryName, sql, map);
	}

	public static String parser(AbstractSqlEnum sqi, Map<String, Object> map) {
		String queryName = String.valueOf(sqi.getSqlCode());
		String sql = sqi.getSql();
		return parser(queryName, sql, map);
	}
	
	public static String parser(String queryName, String sql, Map<String, Object> map) {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);  
		cfg.setDefaultEncoding("UTF-8");  
		//字符串模板加载器
		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();  
		stringTemplateLoader.putTemplate(queryName, sql);  
		
		cfg.setTemplateLoader(stringTemplateLoader);  
		Template template;
		StringWriter writer;
		try {
			template = cfg.getTemplate(queryName,"UTF-8");
			template.setNumberFormat("#");
			
			writer = new StringWriter();   
			template.process(map, writer);
		} catch (IOException | TemplateException e) {
			throw new RuntimeException("sql解析异常");
		}  
		
		//获得查询字符串
		return writer.toString().trim();
	}
	
}

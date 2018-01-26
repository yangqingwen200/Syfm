package com.commons.util;

import com.commons.annotation.DescColumn;
import com.commons.constant.CommonConstant;

import javax.persistence.Column;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MaptoBean {

	/**
	 * 返回具有默认值的对象<strong>(对象属性为全部java.lang基本数据类型)</strong>.<br>
	 * 
	 * 用法: <br>
	 * 新增: object为新new的对象.<br>
	 * 更新: object为从数据库加载出来的对象, 
	 * 		把map里面key(也就是对象的属性名称)对应的值, 赋值到object里面--新值, 
	 * 		map没有key, 还是保留数据库原来的值.
	 * @param object 
	 * @param map 装有object类属性的key的map集合
	 * @return 
	 * @return 把map里面的值赋值给对象
	 * @throws Exception
	 * 
	 *  2015年11月10日 下午9:02:47
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T mapToBeanBasic(T object, Map map) throws Exception {
		Class<?> tClass = object.getClass();

		Field[] fields = tClass.getDeclaredFields(); // 获得该类的所有属性
		for (Field field : fields) {
			String f = field.getName();
			if(map.containsKey(f)) {
				String type = field.getType().getSimpleName();
				PropertyDescriptor pd = new PropertyDescriptor(f, tClass);
				Object getValue = map.get(f);
				if ("Integer".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Integer.parseInt(getValue.toString().trim());
					} else {
						//在数据库中,id一般都为主键自增id,保存的时候,主键自增id是不需要赋值的,置为null即可
						if(f.equals("id")) {
							getValue = null; //本身为空, 可以不需要再赋值为空.
						} else {
							getValue = new Integer(0);
						}
					}
				} else if ("String".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = getValue.toString().trim();
					} else {
						getValue = new String();
					}
				} else if ("Double".equals(type) && !"".equals(getValue.toString().trim())) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Double.parseDouble(getValue.toString().trim());
					} else {
						getValue = new Double(0.0);
					}
				} else if ("Float".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Float.parseFloat(getValue.toString().trim());
					} else {
						getValue = new Float(0.0);
					}
				} else if("Boolean".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Boolean.parseBoolean(getValue.toString().trim());
					} else {
						getValue = new Boolean(false);
					}
				} else if("Long".equals(type) ) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						
						getValue = Long.parseLong(getValue.toString().trim());
					} else {
						getValue = new Long(0);
					}
				} else if("Short".equals(type) ) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						
						getValue = Short.parseShort(getValue.toString().trim());
					} else {
						getValue = new Short("0");
					}
				} else if("Date".equals(type)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = df.parse(getValue.toString().trim());
					} else {
						getValue = new Date();
					}
				}
				//在此扩充其他的类型
				Method method = pd.getWriteMethod(); // 获得set方法
				method.invoke(object, new Object[] { getValue }); //调用set方法,给改属性赋值
			}
		}
		return object;
	}
	
	/**
	 * 返回具有默认值的对象(对象属性包含引用类型).
	 * @param object 
	 * @param map 装有object类属性的key的map集合
	 * @return 
	 * @return 把map里面的值赋值给对象
	 * @throws Exception
	 * 
	 *  2015年11月10日 下午9:02:47
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T mapToBeanPreference(T object, Map map) throws Exception {
		Class<?> tClass = object.getClass();
		
		Field[] fields = tClass.getDeclaredFields(); // 获得该类的所有属性
		for (Field field : fields) {
			String f = field.getName();
			if(map.containsKey(f)) {
				String type = field.getType().getSimpleName();
				PropertyDescriptor pd = new PropertyDescriptor(f, tClass);
				Object getValue = map.get(f);
				
				if ("Integer".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Integer.parseInt(getValue.toString().trim());
					} else {
						//在数据库中,id一般都为主键自增id,保存的时候,主键自增id是不需要赋值的,置为null即可
						if(f.equals("id")) {
							getValue = null; //本身为空, 可以不需要再赋值为空.
						} else {
							getValue = new Integer(0);
						}
					}
				} else if ("String".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = getValue.toString().trim();
					} else {
						getValue = new String();
					}
				} else if ("Double".equals(type) && !"".equals(getValue.toString().trim())) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Double.parseDouble(getValue.toString().trim());
					} else {
						getValue = new Double(0.0);
					}
				} else if ("Float".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Float.parseFloat(getValue.toString().trim());
					} else {
						getValue = new Float(0.0);
					}
				} else if("Boolean".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Boolean.parseBoolean(getValue.toString().trim());
					} else {
						getValue = new Boolean(false);
					}
				} else if("Long".equals(type) ) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						
						getValue = Long.parseLong(getValue.toString().trim());
					} else {
						getValue = new Long(0);
					}
				} else if("Short".equals(type) ) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						
						getValue = Short.parseShort(getValue.toString().trim());
					} else {
						getValue = new Short("0");
					}
				} else if("Date".equals(type)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = df.parse(getValue.toString().trim());
					} else {
						getValue = new Date();
					}
				} else {
					//其他情况为引用数据类型
					Object newInstance = field.getType().newInstance();
					
					Field[] fieldS = field.getType().getDeclaredFields();
					for (Field field2 : fieldS) {
						if("id".equals(field2.getName())) {
							String types= field2.getType().getSimpleName();
							
							PropertyDescriptor pd1 = new PropertyDescriptor(field2.getName(),  field.getType());
							if ("Integer".equals(types)) {
								if(getValue != null && !"".equals(getValue.toString().trim())) {
									getValue = Integer.parseInt(getValue.toString().trim());
								} else {
									getValue = new Integer(0);
								}
							} else if("Long".equals(types) ) {
								if(getValue != null && !"".equals(getValue.toString().trim())) {
									
									getValue = Long.parseLong(getValue.toString().trim());
								} else {
									getValue = new Long(0);
								}
							}
							Method method = pd1.getWriteMethod();
							method.invoke(newInstance, new Object[] { getValue });
							getValue = newInstance;
							break;
						}
					}
				}
				
				Method method = pd.getWriteMethod(); // 获得set方法
				method.invoke(object, new Object[] { getValue }); //调用set方法,给改属性赋值
			}
		}
		return object;
	}
	
	/**
	 * 两个对象属性相互赋值.
	 * 源对象属性值为空 或者 两个对象属性值相等 不会被复制
	 * 
	 * 作用: 数据库中有些字段是不变的. 如 数据产生的create_time字段, 不需要更新
	 * auth: Yang
	 * 2016年8月20日 下午2:57:24
	 * @param fromObject 复制对象
	 * @param toObject 新对象
	 * @return
	 * @throws Exception
	 */
	public static <T> T copyNotNullProtertiesValue(T fromObject, T toObject) throws Exception {
		Class<?> tClassFromObject = fromObject.getClass();
		Class<?> tClassToObject = toObject.getClass();
		if(!tClassToObject.getName().equals(tClassFromObject.getName())) {
			System.out.println("两个不同对象无法相互复制");
			return null;
		}

		Field[] fieldsFrom = tClassFromObject.getDeclaredFields(); // 获得该类的所有属性
		Field[] fieldsTo = tClassToObject.getDeclaredFields(); // 获得该类的所有属性
		for (Field field : fieldsFrom) {
			String fFrom = field.getName();
			if(!"serialVersionUID".equals(fFrom)) {
				PropertyDescriptor pdFrom = new PropertyDescriptor(fFrom, tClassFromObject);
				Method getMethodFrom = pdFrom.getReadMethod();//从属性描述器中获取 get 方法
				Object valueFrom = getMethodFrom.invoke(fromObject);//调用方法获取方法的返回值
				if(valueFrom != null) { //判断属性值是否为空
					for (Field field2 : fieldsTo) {
						String fTo = field2.getName();
						if(fFrom.equals(fTo)) { //判断两个属性名称是否相等
							PropertyDescriptor pdTo = new PropertyDescriptor(fTo, tClassToObject);
							Method getMethodTo = pdTo.getReadMethod();
							Object valueTo = getMethodTo.invoke(toObject);
							if(!String.valueOf(valueFrom).equals(String.valueOf(valueTo))) { //判断两个对象属性值是否相等, 不相等才复制
								Method method = pdTo.getWriteMethod(); // 获得set方法
								method.invoke(toObject, new Object[] { valueFrom }); //调用set方法,给改属性赋值
							}
							break;
						}
					}
				}
			}
		}
		return toObject;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Object mapToBeanBasic(Class cls, Map map) throws Exception {
		Object newInstance = Class.forName(cls.getName()).newInstance();
		Class<?> tClass = cls;

		Field[] fields = tClass.getDeclaredFields(); // 获得该类的所有属性
		for (Field field : fields) {
			String f = field.getName();
			if(map.containsKey(f)) {
				String type = field.getType().getSimpleName();
				PropertyDescriptor pd = new PropertyDescriptor(f, tClass);
				Object getValue = map.get(f);
				if ("Integer".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Integer.parseInt(getValue.toString().trim());
					} else {
						//在数据库中,id一般都为主键自增id,保存的时候,主键自增id是不需要赋值的,置为null即可
						if(f.equals("id")) {
							getValue = null; //本身为空, 可以不需要再赋值为空.
						} else {
							getValue = new Integer(0);
						}
					}
				} else if ("String".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = getValue.toString().trim();
					} else {
						getValue = new String();
					}
				} else if ("Double".equals(type) && !"".equals(getValue.toString().trim())) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Double.parseDouble(getValue.toString().trim());
					} else {
						getValue = new Double(0.0);
					}
				} else if ("Float".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Float.parseFloat(getValue.toString().trim());
					} else {
						getValue = new Float(0.0);
					}
				} else if("Boolean".equals(type)) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = Boolean.parseBoolean(getValue.toString().trim());
					} else {
						getValue = new Boolean(false);
					}
				} else if("Long".equals(type) ) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						
						getValue = Long.parseLong(getValue.toString().trim());
					} else {
						getValue = new Long(0);
					}
				} else if("Short".equals(type) ) {
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						
						getValue = Short.parseShort(getValue.toString().trim());
					} else {
						getValue = new Short("0");
					}
				} else if("Date".equals(type)) {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(getValue != null && !"".equals(getValue.toString().trim())) {
						getValue = df.parse(getValue.toString().trim());
					} else {
						getValue = new Date();
					}
				}
				//在此扩充其他的类型
				Method method = pd.getWriteMethod(); // 获得set方法
				method.invoke(newInstance, new Object[] { getValue }); //调用set方法,给改属性赋值
			}
		}
		return newInstance;
	}
	
	/**
	 * 比较两个对象属性不同值, 拼装成字符串
	 * @param fromObject 
	 * @param toObject
	 * @return
	 * @throws Exception
	 * @author Yang
	 * @version v1.0
	 * @date 2016年12月11日
	 */
	public static String differntFieldValue(Object fromObject, Object toObject) throws Exception {
		StringBuffer sb = new StringBuffer();
		if(null == fromObject || null == toObject) {
			return "";
		}
		Class<?> tClassFromObject = fromObject.getClass();
		Class<?> tClassToObject = toObject.getClass();
		if(!tClassToObject.getName().equals(tClassFromObject.getName())) {
			return "";
		}

		Field[] fieldsFrom = tClassFromObject.getDeclaredFields(); // 获得该类的所有属性
		Field[] fieldsTo = tClassToObject.getDeclaredFields(); // 获得该类的所有属性
		for (Field field : fieldsFrom) {
			String fFrom = field.getName();
			if(!"serialVersionUID".equals(fFrom)) {
				PropertyDescriptor pdFrom = new PropertyDescriptor(fFrom, tClassFromObject);
				Method getMethodFrom = pdFrom.getReadMethod();//从属性描述器中获取 get 方法
				Object valueFrom = getMethodFrom.invoke(fromObject);//调用方法获取方法的返回值
				for (Field field2 : fieldsTo) {
					String fTo = field2.getName();
					if(fFrom.equals(fTo)) { //判断两个属性名称是否相等
						PropertyDescriptor pdTo = new PropertyDescriptor(fTo, tClassToObject);
						Method getMethodTo = pdTo.getReadMethod();
						Object valueTo = getMethodTo.invoke(toObject);
						if(null != valueFrom && null != valueTo &&
								!String.valueOf(valueFrom).equals(String.valueOf(valueTo))) { //判断两个对象属性值是否相等, 不相等才复制
							String desc = "";
							DescColumn dc = getMethodFrom.getAnnotation(DescColumn.class);
							if(null != dc) {
								String value = dc.value();
								desc = value;
								
							} else {
								 //如果get方法上没有写日志描述, 直接取数据库对应的字段
								Column annotation = getMethodFrom.getAnnotation(Column.class);
								desc = annotation.name();
							}
							sb.append(desc + ": " + valueFrom + (valueTo == null ? "" : "-->" + valueTo) + CommonConstant.ESCAPE_KEYWORD);
						}
						break;
					}
				}
			}
		}
		if(sb.length() == 0) {
			sb.append("值未发生变化");
		}
		return sb.toString();
	}

}

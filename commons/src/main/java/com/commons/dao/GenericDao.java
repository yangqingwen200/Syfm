package com.commons.dao;

import com.commons.util.Page;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface GenericDao {

	/**
	 * 更新对象
	 */
	<T> void update(T entityObject);

	<T> void saveOrUpdate(T entityObject);

	/**
	 * 保存对象.
	 * 如果对象已在本session中持久化了,不做任何事。<br>
	 * 如果另一个seesion拥有相同的持久化标识,抛出异常。<br>
	 * 如果没有持久化标识属性,调用save()。<br>
	 * 如果持久化标识表明是一个新的实例化对象,调用save()。<br>
	 * 如果是附带版本信息的(version或timestamp)且版本属性表明为新的实例化对象就save()。<br>
	 * 否则调用update()重新关联托管对象
	 *
	 * @param entityObject
	 */
	<T> void save(T entityObject);

	/**
	 * 删除对象.
	 */
	<T> void delete(T persistentObject);

	/**
	 * 把数据加载到指定的非持久化实例上
	 *
	 * @param cls
	 * @param id
	 */
	<T, PK> T get(Class<T> cls, PK id);

	/**
	 * 获取实体类型的全部对象
	 */
	<T> List<T> findAll(T entity);

	<T> List<T> findExample(T exampleInstance);

	<T> List<T> findExample(T exampleInstance, int startNo, int pageSize);

	<T> List<T> findExample(T exampleInstance, final Object[] parameter, final Object[] parameterValues, int startNo, int pageSize);

	<T> List<T> findExample(T exampleInstance, String... excludeProperty);

	<T> List<T> findHqlList(final String hql);

	<T> List<T> findHqlList(final String hql, final Object[] parameterValues);

	<T> List<T> findCriteria(DetachedCriteria crit);

	<T> List<T> findCriteria(DetachedCriteria crit, int startNo, int pageSize);

	void flush();

	void clear();

	<T> Iterator<T> iterator(String hql, Object... values);

	int executeUpdateHql(String hql);

	int executeUpdateHql(final String hql, final Object[] parameterValues);

	int executeUpdateSql(String sql);

	int executeUpdateSql(String sql, Map<String, Object> parameterMap);

	int executeUpdateSql(final String sql, final Object[] parameterValues);

	<T> List<T> findSqlList(String sql);

	<T> List<T> findSqlList(String sql, int startNo, int pageSize);

	<T> List<T> findSqlList(String sql, Map<String, Object> parameterMap);

	<T> List<T> findSqlList(String sql, Map<String, Object> parameterMap, int startNo, int pageSize);

	<T> List<T> findSqlList(String sql, Object[] parameterValues);

	<T, P extends Serializable> T findEntity(T t, P pk);

	int findHqlCount(String hql, Object[] parameterValues);

	int findHqlCount(String hql);

	int findSqlCount(String sql, Object[] parameterValues);

	/**
	 * 根据原生的sql语句求和
	 * @param sql
	 * @return
	 *
	 * 2015年8月12日 上午9:28:15
	 */
	int findSqlCount(String sql);

	/**
	 * 根据object数组传递的参数查询结果
	 *
	 * @param <T>
	 * @param sql
	 * @param parameterValues
	 *            参数
	 * @param startNo
	 * @param pageSize
	 * @return
	 */
	<T> List<T> findSqlList(String sql, Object[] parameterValues, int startNo, int pageSize);

	<T> List<T> findHqlList(String hql, Object[] parameterValues, int startNo, int pageSize);

	<T> List<T> findSqlTem(String sql, int startNo, int pageSize);

	/**
	 * map的key为查询的列名(可以取别名),value值查询的值
	 * @param sql sql语句
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> Map<String, T> findSqlMap(final String sql);

	/**
	 * map的key为查询的列名(可以取别名),value值查询的值
	 * @param sql sql语句
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> List<T> findSqlListMap(final String sql);

	/**
	 * map的key为查询的列名(可以取别名),value值查询的值
	 * @param sql sql语句
	 * @param values Object数组
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> Map<String, T> findSqlMap(final String sql,final Object...values);

	/**
	 * map的key为查询的列名(可以取别名),value值查询的值
	 * @param sql sql语句
	 * @param values Object数组
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> List<T> findSqlListMap(final String sql,final Object...values);

	/**
	 * map的key为查询的列名(可以取别名), 没有任何参数
	 * @param sql
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @author Yang
	 * @version v1.0
	 * @date 2016年12月8日
	 */
	<T> List<T> findSqlListMap(String sql, int pageNo, int pageSize);

	/**
	 *  map的key为查询的列名(可以取别名),value值查询的值
	 * @param sql sql语句
	 * @param pageNo 当前页数
	 * @param pageSize 每页大小
	 * @param values Object数组
	 * @return
	 *
	 * 2015年10月20日 下午7:53:06
	 */
	<T> List<T> findSqlListMap(final String sql, final int pageNo, final int pageSize, final Object[] values);

	/**
	 * map的key为查询的列名(必须取别名),value值查询的值
	 * @param hql hql语句
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> Map<String, T> findHqlMap(final String hql);

	/**
	 * map的key为查询的列名(必须取别名),value值查询的值
	 * @param hql hql语句
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> List<T> findHqlListMap(final String hql);

	/**
	 * map的key为查询的列名(必须取别名),value值查询的值
	 * @param hql hql语句
	 * @param values Object数组
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> Map<String, T> findHqlMap(final String hql, final Object... values);

	/**
	 * map的key为查询的列名(必须取别名),value值查询的值
	 * @param hql hql语句
	 * @param values Object数组
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> List<T> findHqlListMap(final String hql, final Object... values);

	/**
	 *  map的key为查询的列名(必须取别名),value值查询的值
	 * @param hql sql语句
	 * @param pageNo 当前页数
	 * @param pageSize 每页大小
	 * @param values Object数组
	 * @return
	 *
	 * 2015年10月20日 下午7:53:06
	 */
	<T> List<T> findHqlListMap(final String hql, final int pageNo, final int pageSize, final Object[] values);

	/**
	 * 使用原生的sql(包括模糊查找, 排序, 范围查找都在sql中)
	 * @param pageNo
	 * @param pageSize
	 * @param sql
	 * @param parameterValues
	 * @return
	 * Yang
	 * 2016年2月3日 上午9:33:08
	 */
	@SuppressWarnings("rawtypes")
	Page pagedQueryParamSql(final int pageNo, final int pageSize, final String sql, final Object[] parameterValues);

	Page pagedQueryParamSql(final Page page, final String sql, final Object[] parameterValues);

	/**
	 * 使用原生的sql(包括模糊查找, 排序, 范围查找都在sql中)
	 * Yang
	 * 2016年2月3日 上午9:33:08
	 */
	@SuppressWarnings("rawtypes")
	Page pagedQueryParamSql(final String sql);

	/**
	 * 使用hql(包括模糊查找, 排序, 范围查找都在hql中)
	 * @param pageNo
	 * @param pageSize
	 * @param hql
	 * @param parameterValues
	 * @return
	 * Yang
	 * 2016年2月3日 上午9:33:08
	 */
	@SuppressWarnings("rawtypes")
	Page pagedQueryParamHql(final int pageNo, final int pageSize, final String hql, final Object[] parameterValues);


	/**
	 * 使用PreparedStatement批量更新sql语句, sql语句相同, 参数值不一样.
	 * sql采用问号占位符, 预编译方式
	 * @param sql
	 * @param parameterValues
	 * @return
	 *
	 *  2016年5月9日 下午9:46:30
	 */
	int executeBatchUpdateSql(String sql, List<Object[]> parameterValues);

	/**
	 * 使用Statement批量更新sql语句, sql语句不相同, 参数值不一样.
	 * sql采用问号占位符, 最后拼接sql语句方式
	 * @param sqlList
	 * @param parameterValues
	 * @return
	 *
	 *  2016年5月9日 下午9:46:30
	 */
	int executeBatchUpdateSql(List<String> sqlList, List<Object[]> parameterValues);

	/**
	 *  使用Statement批量更新sql语句
	 *  sql语句全部使用拼接的方式(sql语句中无问号)
	 * @param sqlList
	 * @return
	 *
	 *  2016年5月15日 下午5:19:35
	 */
	int executeBatchUpdateSql(List<String> sqlList);

}

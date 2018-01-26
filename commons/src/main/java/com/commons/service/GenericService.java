/**
 * 
 */
package com.commons.service;

import com.commons.util.Page;
import org.hibernate.criterion.DetachedCriteria;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 公共的service 包括增删查改功能再能，如有特殊方法要实现可继承该接口和实现类
 * 
 */
public interface GenericService {

	void flush();

	void clear();

	<T, PK> T load(Class<T> cls, PK id);

	<T> void update(T entityObject);

	<T> void save(T entityObject);
	
	<T> void saveOrUpdate(T entityObject);

	<T> void delete(T persistendObject);

	<T> List<T> findAll(T entity);

	<T> List<T> findExample(T exampleInstance);

	<T> List<T> findExample(T exampleInstance, int startNo, int pageSize);

	<T> List<T> findExample(T exampleInstance, String... excludeProperty);

	<T> List<T> findHqlList(final String hql);

	<T> List<T> findHqlList(final String hql, final Object[] parameterValues);

	<T> List<T> findHqlList(String hql, int startNo, int pageSize, Object[] parameterValues);

	<T> List<T> findCriteria(DetachedCriteria crit);

	<T> List<T> findCriteria(DetachedCriteria crit, int startNo, int pageSize);

	<T> Iterator<T> iterator(String hql, Object... values);

	<T> List<T> findSqlList(String sql);

	<T> List<T> findSqlList(String sql, int startNo, int pageSize);

	<T> List<T> findSqlList(String sql, Map<String, Object> parameterMap);

	<T> List<T> findSqlList(String sql, Object... parameterValues);

	<T> List<T> findSqlList(String sql, int startNo, int pageSize, Map<String, Object> parameterMap);

	<T> List<T> findSqlList(String sql, int startNo, int pageSize, Object[] parameterValues);

	/**
	 * map的key为查询的列名(可以取别名)<br>
	 * 注意: 请确保查询出来只有一条记录,多条记录也只会取第一条数据
	 * @param sql sql语句
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> Map<String, T> findSqlMap(final String sql);

	/**
	 * map的key为查询的列名<br>
	 * 注意: 请确保查询出来只有一条记录,多条记录也只会取第一条数据
	 * @param sql sql语句
	 * @param values Object数组
	 * @return
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> Map<String, T> findSqlMap(final String sql, final Object...values);

	/**
	 * map的key为查询的列, 查询结果以map形式放在List中返回
	 * @param sql sql语句
	 * @return 装有map的list集合
	 *
	 * 2015年10月14日 上午9:59:26
	 */
	<T> List<T> findSqlListMap(final String sql);
	
	/**
	 * map的key为查询的列, 查询结果以map形式放在List中返回
	 * @param sql sql语句
	 * @param values new Object[]{}值组成的数组
	 * @return
	 *
	 * 2015年10月14日 上午9:59:26
	 */
	<T> List<T> findSqlListMap(final String sql, final Object... values);
	
	/**
	 * map的key为查询的列名, 分页 sql不带问号占位
	 * @param sql sql
	 * @param pageNo 当前页
	 * @param pageSize 每页大小
	 * @return 装有map的list
	 */
	<T> List<T> findSqlListMapPaging(final String sql, final int pageNo, final int pageSize);
	
	/**
	 * map的key为查询的列名, 分页  sql带问号占位
	 * @param sql sql语句
	 * @param pageNo 当前页数 从1开始
	 * @param pageSize 每页数量
	 * @param values 参数值数组
	 * @return
	 *
	 * 2015年10月14日 上午10:00:17
	 */
	<T> List<T> findSqlListMapPaging(final String sql, final int pageNo, final int pageSize, final Object[] values);

	/**
	 * map的key为查询的列名(必须取别名),value值查询的值
	 * @param hql hql语句
	 * @return 查询到第一条数据
	 *
	 * 2015年10月20日 下午7:51:56
	 */
	<T> Map<String, T> findHqlMap(final String hql);

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
	<T> List<T> findHqlListMap(final String hql, final Object... values);
	
	/**
	 *  map的key为查询的列名(必须取别名),value值查询的值
	 * @param pageNo 当前页数
	 * @param pageSize 每页大小
	 * @param values Object数组
	 * @return 
	 *
	 * 2015年10月20日 下午7:53:06
	 */
	<T> List<T> findHqlListMap(final String hql, final int pageNo, final int pageSize, final Object[] values);

	/**
	 * sql包含查询字段, 分页, 排序, 正序or倒序
	 *
	 * @param pageNo 当前页数
	 * @param pageSize 每页大小
	 * @param sql sql语句
	 * @return page
	 * Yang
	 * 2016年2月3日 上午9:33:08
	 */
	Page<?> pagedQuerySql(final int pageNo, final int pageSize, final String sql, final Object[] parameterValues);

	/**
	 * sql包含查询字段, 分页, 排序, 正序or倒序, 当前页, 每页大小
	 *
	 * @param sql sql语句, SqlParserInfo or String
	 * @param map where条件中的参数map(一般为前台传过来组成dto形式)
	 * @return page
	 */
	Page<?> pagedQuerySqlFreemarker(Object sql, Map<String, Object> map);

	/**
	 *  sql只包含查询字段
	 *
	 * @param page 对象中包含当前页, 每页数量大小, 排序字段, 正序or倒序
	 * @param sql sql语句
	 * @param parameterValues 参数
	 * @return page
	 */
	Page<?> pagedQuerySql(Page page, String sql, Object[] parameterValues);

	/**
	 * sql只包含查询字段
	 *
	 * @param page 含有分页, 排序, 正序or倒序, 当前页, 每页大小的page
	 * @param sql sql语句, 语法采用Freemarker形式
	 * @param map where条件中的参数map(一般为前台传过来组成dto形式)
	 * @return page
	 */
	Page<?> pagedQuerySqlFreemarker(Page page, String sql, Map<String, Object> map);

	/**
	 * 使用hql(包括模糊查找, 排序, 范围查找都在hql中).<br/>
	 * 注意: 在写hql语句的时候, 必须给查找的列取别名 <br/>
	 * 如: select id as id, name as name, pw as pw from Appuser 
	 * @param pageNo 当前页
	 * @param pageSize 每页大小
	 * @return page
	 * Yang
	 * 2016年2月3日 上午9:33:08
	 */
	Page<?> pagedQueryHql(final int pageNo, final int pageSize, final String hql, final Object[] parameterValues);

	/**
	 * 执行hql语句进行数据更新
	 *
	 * @param hql hql
	 * @return 返回被更新的行数
	 */
	int executeUpdateHql(String hql);

	/**
	 * 执行hql语句进行数据更新
	 *
	 * @param hql hql
	 * @param parameterValues 参数值
	 * @return 返回被更新的行数
	 */
	int executeUpdateHql(final String hql, final Object... parameterValues);

	/**
	 * 执行原生SQL语句
	 *
	 * @param sql sql
	 * @return 返回被更新的行数
	 */
	int executeUpdateSql(String sql);

	/**
	 * 执行原生SQL
	 *
	 * @param sql sql, 参数采用 :param占位符形式
	 * @param parameterMap map的key对应sql :param的字符串
	 * @return 返回被更新的行数
	 */
	int executeUpdateSql(String sql, Map<String, Object> parameterMap);

	/**
	 * 执行原生SQL语句
	 * @param sql sql语句
	 * @param parameterValues 参数值
	 * @return 执行数量
	 *
	 * 2015年10月20日 下午8:33:47
	 */
	int executeUpdateSql(final String sql, final Object... parameterValues);

	/**
	 * 使用Statement批量更新sql语句.<br/>
	 * sql语句全部使用拼接的方式(sql语句中无问号)
	 * @param sqlList sql组成list
	 * @return 返回被更新的行数
	 *
	 *  2016年5月15日 下午5:28:36
	 */
	int executeBatchUpdateSql(List<String> sqlList);
	
	/**
	 * 使用PreparedStatement批量更新sql语句, sql语句相同, 参数值不一样.<br/>
	 * sql采用问号占位符, 预编译方式
	 * @param sql sql语句
	 * @param parameterValues 参数值
	 * @return 返回被更新的行数
	 * 
	 *  2016年5月9日 下午9:46:30
	 */
	int executeBatchUpdateSql(String sql, List<Object[]> parameterValues);
	
	/**
	 * 使用Statement批量更新sql语句, sql语句不相同, 参数值不一样.<br/>
	 * sql采用问号占位符, 最后拼接sql语句方式
	 * @param sqlList 不同sql组成list
	 * @param parameterValues 不同sql对应不同参数值, 位置要对应
	 * @return 返回被更新的行数
	 * 
	 *  2016年5月9日 下午9:46:30
	 */
	int executeBatchUpdateSql(List<String> sqlList, List<Object[]> parameterValues);

	/**
	 * 求和hql
	 * @param hql hql
	 * @return 总数量
	 */
	int findHqlCount(String hql);

	/**
	 * 求和hql
	 * @param hql hql
	 * @param parameterValues 参数值
	 * @return 总数量
	 */
	int findHqlCount(String hql, Object... parameterValues);

	/**
	 * 求和sql
	 * @param sql sql
	 * @return 总数量
	 */
	int findSqlCount(String sql);

	/**
	 * 求和sql
	 * @param sql sql
	 * @param parameterValues 参数值
	 * @return 总数量
	 */
	int findSqlCount(String sql, Object... parameterValues);
}

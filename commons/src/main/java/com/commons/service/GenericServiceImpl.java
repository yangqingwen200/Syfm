/**
 * 
 */
package com.commons.service;

import com.commons.dao.GenericDao;
import com.commons.enums.AbstractSqlEnum;
import com.commons.redis.CacheRedisClient;
import com.commons.util.Page;
import com.commons.util.SqlParserUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("publicService")
public class GenericServiceImpl implements GenericService {

	public GenericDao publicDao;
	
	public CacheRedisClient cacheRedisClient;

	public GenericDao getPublicDao() {
		return publicDao;
	}

	@Autowired
	public void setPublicDao(@Qualifier("publicDao")GenericDao publicDao) {
		this.publicDao = publicDao;
	}
	
	public CacheRedisClient getCacheRedisClient() {
		return cacheRedisClient;
	}

	@Autowired
	public void setCacheRedisClient(@Qualifier("cacheRedisClient")CacheRedisClient cacheRedisClient) {
		this.cacheRedisClient = cacheRedisClient;
	}

	@Override
	public <T> void delete(T persistendObject) {
		this.publicDao.delete(persistendObject);
	}
	
	@Override
	public int executeUpdateHql(String hql) {
		return this.publicDao.executeUpdateHql(hql);
	}

	@Override
	public int executeUpdateHql(String hql, Object[] parameterValues) {
		return this.publicDao.executeUpdateHql(hql, parameterValues);
	}

	@Override
	public int executeUpdateSql(String sql) {
		return this.publicDao.executeUpdateSql(sql);
	}

	@Override
	public int executeUpdateSql(String sql, Map<String, Object> parameterMap) {
		return this.publicDao.executeUpdateSql(sql, parameterMap);
	}
	
	@Override
	public int executeUpdateSql(String sql, Object... parameterValues) {
		return this.publicDao.executeUpdateSql(sql, parameterValues);
	}

	@Override
	public <T> List<T> findAll(T entity) {
		return this.publicDao.findAll(entity);
	}

	@Override
	public <T> List<T> findCriteria(DetachedCriteria crit) {
		return this.publicDao.findCriteria(crit);
	}

	@Override
	public <T> List<T> findCriteria(DetachedCriteria crit, int startNo, int pageSize) {
		return this.publicDao.findCriteria(crit, startNo, pageSize);
	}

	@Override
	public <T> List<T> findExample(T exampleInstance) {
		return this.publicDao.findExample(exampleInstance);
	}

	@Override
	public <T> List<T> findExample(T exampleInstance, int startNo, int pageSize) {
		return this.publicDao.findExample(exampleInstance, startNo, pageSize);
	}

	@Override
	public <T> List<T> findExample(T exampleInstance, String... excludeProperty) {
		return this.publicDao.findExample(exampleInstance, excludeProperty);
	}

	@Override
	public <T> List<T> findHqlList(String hql) {
		return this.publicDao.findHqlList(hql);
	}

	@Override
	public <T> List<T> findHqlList(String hql, Object[] parameterValues) {
		return this.publicDao.findHqlList(hql, parameterValues);
	}

	@Override
	public <T> List<T> findSqlList(String sql) {
		return publicDao.findSqlList(sql);
	}

	@Override
	public <T> List<T> findSqlList(String sql, int startNo, int pageSize) {
		return this.publicDao.findSqlList(sql, startNo, pageSize);
	}

	@Override
	public <T> List<T> findSqlList(String sql, Map<String, Object> parameterMap) {
		return this.publicDao.findSqlList(sql, parameterMap);
	}

	@Override
	public <T> List<T> findSqlList(String sql, int startNo, int pageSize, Map<String, Object> parameterMap) {
		return this.publicDao.findSqlList(sql, parameterMap, startNo, pageSize);
	}

	@Override
	public <T> List<T> findSqlList(String sql, Object... parameterValues) {
		return this.publicDao.findSqlList(sql, parameterValues);
	}

	@Override
	public <T> Iterator<T> iterator(String hql, Object... values) {
		return this.publicDao.iterator(hql, values);
	}

	@Override
	public <T> void save(T entityObject) {
		this.publicDao.save(entityObject);
	}
	
	@Override
	public <T> void saveOrUpdate(T entityObject) {
		this.publicDao.saveOrUpdate(entityObject);
	}

	@Override
	public <T> void update(T entityObject) {
		this.publicDao.update(entityObject);
	}

	@Override
	public <T, PK> T load(Class<T> cls, PK id) {
		return publicDao.get(cls, id);
	}

	@Override
	public int findHqlCount(String hql, Object... parameterValues) {
		return publicDao.findHqlCount(hql, parameterValues);
	}

	@Override
	public int findHqlCount(String hql) {
		return publicDao.findHqlCount(hql);
	}

	@Override
	public <T> List<T> findSqlList(String sql, int startNo, int pageSize, Object[] parameterValues) {
		return publicDao.findSqlList(sql, parameterValues, startNo, pageSize);
	}

	public <T> List<T> findHqlList(String hql, int startNo, int pageSize, Object[] parameterValues) {
		return publicDao.findHqlList(hql, parameterValues, startNo, pageSize);
	}

	@Override
	public int findSqlCount(String sql, Object... parameterValues) {
		return this.publicDao.findSqlCount(sql, parameterValues);
	}

	@Override
	public int findSqlCount(String sql) {
		return this.publicDao.findSqlCount(sql);
	}

	@Override
	public void flush() {
		this.publicDao.flush();
	}
	
	@Override
	public void clear() {
		this.publicDao.clear();
	}

	@Override
	public <T> List<T> findSqlListMap(String sql, Object... values) {
		return this.publicDao.findSqlListMap(sql, values);
	}
	
	@Override
	public <T> List<T> findSqlListMapPaging(String sql, final int pageNo, final int pageSize) {
		return this.publicDao.findSqlListMap(sql, pageNo, pageSize);
	}
	
	@Override
	public <T> List<T> findSqlListMapPaging(final String sql, final int pageNo, final int pageSize, final Object[] values) {
		return this.publicDao.findSqlListMap(sql, pageNo, pageSize, values);
	}

	@Override
	public <T> List<T> findHqlListMap(String hql, Object... values) {
		return this.publicDao.findHqlListMap(hql, values);
	}

	@Override
	public <T> List<T> findHqlListMap(String hql, int pageNo, int pageSize, Object[] values) {
		return this.publicDao.findHqlListMap(hql, pageNo, pageSize, values);
	}

	@Override
	public Page<?> pagedQuerySql(int pageNo, int pageSize, String sql, Object[] parameterValues) {
		return this.publicDao.pagedQueryParamSql(pageNo, pageSize, sql, parameterValues);
	}
	
	@Override
	public Page<?> pagedQuerySql(Page page, String sql, Object[] parameterValues) {
		return this.publicDao.pagedQueryParamSql(page, sql, parameterValues);
	}

	@Override
	public Page<?> pagedQuerySqlFreemarker(Object sql, Map<String, Object> map) {
		if(AbstractSqlEnum.class.isAssignableFrom(sql.getClass())) {
			return this.publicDao.pagedQueryParamSql(SqlParserUtil.parser((AbstractSqlEnum)sql, map));
		} else if(sql instanceof String) {
			return this.publicDao.pagedQueryParamSql(SqlParserUtil.parser(String.valueOf(sql), map));
		} else {
			return null;
		}
	}
	
	@Override
	public Page<?> pagedQuerySqlFreemarker(Page page, String sql, Map<String, Object> map) {
		sql = SqlParserUtil.parser(sql, map);
		return this.publicDao.pagedQueryParamSql(page, sql, new Object[]{});
	}

	@Override
	public Page<?> pagedQueryHql(int pageNo, int pageSize, String hql, Object[] parameterValues) {
		return this.publicDao.pagedQueryParamHql(pageNo, pageSize, hql, parameterValues);
	}

	@Override
	public <T> List<T> findSqlListMap(String sql) {
		return this.publicDao.findSqlListMap(sql);
	}

	@Override
	public <T> List<T> findHqlListMap(String hql) {
		return this.publicDao.findHqlListMap(hql);
	}

	@Override
	public int executeBatchUpdateSql(String sql, List<Object[]> parameterValues) {
		return this.publicDao.executeBatchUpdateSql(sql, parameterValues);
	}

	@Override
	public int executeBatchUpdateSql(List<String> sqlList, List<Object[]> parameterValues) {
		return  this.publicDao.executeBatchUpdateSql(sqlList, parameterValues);
	}
	
	@Override
	public int executeBatchUpdateSql(List<String> sqlList) {
		return  this.publicDao.executeBatchUpdateSql(sqlList);
	}

	@Override
	public <T> Map<String, T> findSqlMap(String sql) {
		return this.publicDao.findSqlMap(sql);
	}
	
	@Override
	public <T> Map<String, T> findSqlMap(String sql, Object... values) {
		return this.publicDao.findSqlMap(sql, values);
	}

	@Override
	public <T> Map<String, T> findHqlMap(String hql) {
		return this.publicDao.findHqlMap(hql);
	}

	@Override
	public <T> Map<String, T> findHqlMap(String hql, Object... values) {
		return this.publicDao.findHqlMap(hql, values);
	}

}

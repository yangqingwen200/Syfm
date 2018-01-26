package com.commons.dao;

import com.commons.util.Page;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Dao封装
 * 
 * @author ower
 * 
 */
@Repository("publicDao")
public class GenericHibernateDaoImpl extends BasicHibernateDaoSupport implements GenericDao {

	@Override
	public void clear() {
		getHibernateTemplate().clear();
	}

	@Override
	public <T> void delete(T persistentObject) {
		getHibernateTemplate().delete(persistentObject);
	}

	@Override
	public int executeUpdateHql(String hql) {
		Session session = this.getSession();
		Query sqlQuery = session.createQuery(hql);
		return sqlQuery.executeUpdate();
	}

	@Override
	public int executeUpdateHql(final String hql, final Object[] parameterValues) {
		Object result = getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if ((parameterValues != null) && (parameterValues.length > 0)) {
					for (int i = 0; i < parameterValues.length; i++) {
						query = query.setParameter(i, parameterValues[i]);
					}
				}
				return query.executeUpdate();
			}
		});
		return (Integer) result;
	}

	@Override
	public int executeUpdateSql(String sql) {
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		return sqlQuery.executeUpdate();
	}

	@Override
	public int executeUpdateSql(String sql, Map<String, Object> parameterMap) {
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setProperties(parameterMap);
		return sqlQuery.executeUpdate();
	}

	@Override
	public int executeUpdateSql(final String sql, final Object[] parameterValues) {
		Object result = getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				if ((parameterValues != null) && (parameterValues.length > 0)) {
					for (int i = 0; i < parameterValues.length; i++) {
						query = query.setParameter(i, parameterValues[i]);
					}
				}
				return query.executeUpdate();
			}
		});
		return (Integer) result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAll(T entity) {
		return (List<T>) this.getHibernateTemplate().loadAll(entity.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findCriteria(DetachedCriteria crit) {
		return (List<T>) getHibernateTemplate().findByCriteria(crit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findCriteria(DetachedCriteria crit, int startNo, int pageSize) {
		return (List<T>) getHibernateTemplate().findByCriteria(crit, (startNo - 1) * pageSize, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findExample(T exampleInstance) {
		return getHibernateTemplate().findByExample(exampleInstance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findExample(T exampleInstance, int startNo, int pageSize) {
		return getHibernateTemplate().findByExample(exampleInstance, startNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findExample(T exampleInstance, String... excludeProperty) {
		DetachedCriteria crit = DetachedCriteria.forClass(exampleInstance.getClass());
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		return (List<T>) getHibernateTemplate().findByCriteria(crit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findHqlList(String hql) {
		return (List<T>) getHibernateTemplate().find(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findHqlList(String hql, Object[] parameterValues) {
		return (List<T>) getHibernateTemplate().find(hql, parameterValues);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findSqlList(String sql) {
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findSqlList(String sql, int startNo, int pageSize) {
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setFirstResult((startNo - 1) * pageSize);
		sqlQuery.setMaxResults(pageSize);
		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findSqlList(String sql, Map<String, Object> parameterMap) {
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setProperties(parameterMap);
		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findSqlList(String sql, Map<String, Object> parameterMap, int startNo, int pageSize) {
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		sqlQuery.setProperties(parameterMap);
		sqlQuery.setFirstResult((startNo - 1) * pageSize);
		sqlQuery.setMaxResults(pageSize);
		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findSqlList(String sql, Object[] parameterValues) {
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		int index = 0;
		for (Object object : parameterValues) {
			sqlQuery.setParameter(index, object);
			index++;
		}

		return sqlQuery.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findSqlList(String sql, Object[] parameterValues, int startNo, int pageSize) {
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		int index = 0;
		for (Object object : parameterValues) {
			sqlQuery.setParameter(index, object);

			index++;
		}

		sqlQuery.setFirstResult((startNo - 1) * pageSize);
		sqlQuery.setMaxResults(pageSize);
		return sqlQuery.list();
	}

	@Override
	public void flush() {
		getHibernateTemplate().flush();

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Iterator<T> iterator(String hql, Object... values) {
		return (Iterator<T>) getHibernateTemplate().iterate(hql, values);
	}

	@Override
	public <T, PK> T get(Class<T> cls, PK id) {
		return (T) getHibernateTemplate().get(cls, (Serializable) id);

	}

	@Override
	public <T> void save(T entityObject) {
		this.getHibernateTemplate().saveOrUpdate(entityObject);
		this.getHibernateTemplate().flush();
	}

	@Override
	public <T> void update(T entityObject) {
		getHibernateTemplate().update(entityObject);
		this.getHibernateTemplate().flush();
	}
	
	@Override
	public <T> void saveOrUpdate(T entityObject) {
		this.getHibernateTemplate().saveOrUpdate(entityObject);

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, P extends Serializable> T findEntity(T t, P pk) {
		return (T) getHibernateTemplate().get(t.getClass(), pk);
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public int findHqlCount(String hql, Object[] parameterValues) {
		List countList = getHibernateTemplate().find(hql, parameterValues);
		return (countList != null && countList.size() > 0 && countList.get(0) != null) ? ((Long) countList.get(0)).intValue() : 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int findHqlCount(String hql) {
		List countList = getHibernateTemplate().find(hql);
		return (countList != null && countList.size() > 0 && countList.get(0) != null) ? ((Long) countList.get(0)).intValue() : 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findHqlList(String hql, Object[] parameterValues, int startNo, int pageSize) {
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		for (int i = 0; i < parameterValues.length; i++) {
			query.setParameter(i, parameterValues[i]);
		}
		query.setFirstResult((startNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findSqlTem(String sql, int startNo, int pageSize) {
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql);
		query.setFirstResult((startNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findExample(T exampleInstance, Object[] parameter, Object[] parameterValues, int startNo, int pageSize) {
		DetachedCriteria crit = DetachedCriteria.forClass(exampleInstance.getClass());
		for (int i = 0; i < parameter.length; i++) {
			crit.add(Restrictions.eq(parameter[i].toString(), parameterValues[i]));
		}
		int firstResult = (startNo - 1) * pageSize;
		return (List<T>) this.getHibernateTemplate().findByCriteria(crit, firstResult, pageSize);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int findSqlCount(String sql, Object[] parameterValues) {
		int result = 0;
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		int index = 0;
		for (Object object : parameterValues) {
			sqlQuery.setParameter(index, object);
			index++;
		}
		List countList = sqlQuery.list();
		if(countList.size() > 0) {
			String type = countList.get(0).getClass().getName();
			if("java.math.BigInteger".equals(type)) {
				result = new BigInteger(countList.get(0).toString()).intValue() ;
			} else {
				result = ((Long) countList.get(0)).intValue();
			}
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int findSqlCount(String sql) {
		int result = 0;
		Session session = this.getSession();
		SQLQuery sqlQuery = session.createSQLQuery(sql);
		List countList = sqlQuery.list();
		
		if(countList.size() > 0) {
			String type = countList.get(0).getClass().getName();
			if("java.math.BigInteger".equals(type)) {
				result = new BigInteger(countList.get(0).toString()).intValue() ;
			} else {
				result = ((Long) countList.get(0)).intValue();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findHqlListMap(final String hql) {
		return (List<T>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findSqlListMap(final String sql) {
		return (List<T>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findSqlListMap(final String sql, final Object... values) {
		return (List<T>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findSqlListMap(final String sql, final int pageNo, final int pageSize) {
		return (List<T>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				query.setFirstResult((pageNo - 1) * pageSize);
				query.setMaxResults(pageSize);
				return query.list();
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findSqlListMap(final String sql, final int pageNo, final int pageSize, final Object[] values) {
		return (List<T>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				query.setFirstResult((pageNo - 1) * pageSize);
				query.setMaxResults(pageSize);
				return query.list();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findHqlListMap(final String hql, final Object... values) {
		return (List<T>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				/**
				 * 设置参数
				 */
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				return query.list();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findHqlListMap(final String hql, final int pageNo, final int pageSize, final Object[] values) {
		return (List<T>) getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				/**
				 * 设置参数
				 */
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				query.setFirstResult((pageNo - 1) * pageSize);
				query.setMaxResults(pageSize);
				return query.list();
			}
		});
	}
	
	@Override
	public Page<?> pagedQueryParamSql(String sql) {
		String sqlwhere = this.disposeWhere(sql);
		
		//求和sql
		String fromSql = "";
		fromSql = "from" + StringUtils.substringAfter(sqlwhere, "from");
		fromSql = StringUtils.substringBefore(fromSql, "order by");
		String countSql = "select count(*) " + fromSql;
		
		SQLQuery sqlQuery = this.getSession().createSQLQuery(countSql);
		List countList = sqlQuery.list();
		long totalCount = 0;
		if(countList != null && countList.size() > 0 ) {
			if(countList.get(0) instanceof BigInteger) {
				totalCount = ((BigInteger)countList.get(0)).longValue();
			} else {
				totalCount = (Long) countList.get(0);
			}
		}
		
		List list = new ArrayList();
		if(totalCount > 0) {
			final String sqlwhere1 = sqlwhere;
			list = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sqlwhere1).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					return query.list();
				}
			});
		}
		
		Page page = new Page(totalCount);
		page.setContent(list);
		return page;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Page pagedQueryParamSql(int pageNo, final int pageSize, final String sql, final Object[] parameterValues) {
		final int startIndex = (pageNo - 1) * pageSize;
		String sqlwhere = this.disposeWhere(sql);
		
		//求和sql
		String fromSql = "";
		fromSql = "from" + StringUtils.substringAfter(sqlwhere, "from");
		fromSql = StringUtils.substringBefore(fromSql, "order by");
		String countSql = "select count(*) " + fromSql;
		
		SQLQuery sqlQuery = this.getSession().createSQLQuery(countSql);
		int index = 0;
		for (Object object : parameterValues) {
			sqlQuery.setParameter(index, object);
			index++;
		}
		List countList = sqlQuery.list();
		int totalCount = 0;
		if(countList != null && countList.size() > 0 ) {
			if(countList.get(0) instanceof BigInteger) {
				totalCount = ((BigInteger)countList.get(0)).intValue();
			} else {
				totalCount = (Integer) countList.get(0);
			}
		}
		
		List list = new ArrayList();
		if(totalCount > 0) {
			final String sqlwhere1 = sqlwhere;
			list = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sqlwhere1).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					if (parameterValues != null && parameterValues.length > 0) {
						for (int i = 0; i < parameterValues.length; i++) {
							query.setParameter(i, parameterValues[i]);
						}
					}
					query.setFirstResult(startIndex);
					query.setMaxResults(pageSize);
					return query.list();
				}
			});
		}
		
		Page page = new Page(totalCount, pageSize, pageNo);
		page.setContent(list);
		return page;
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Page pagedQueryParamSql(final Page page, final String sql, final Object[] parameterValues) {
		final int startIndex = (page.getPageNow() - 1) * page.getPageSize();
		String sqlwhere = this.disposeWhere(sql);

		//求和sql
		String fromSql = "";
		fromSql = "from" + StringUtils.substringAfter(sqlwhere, "from");
		String countSql = "select count(*) " + fromSql;

		SQLQuery sqlQuery = this.getSession().createSQLQuery(countSql);
		int index = 0;
		for (Object object : parameterValues) {
			sqlQuery.setParameter(index, object);
			index++;
		}
		List countList = sqlQuery.list();
		int totalCount = 0;
		if(countList != null && countList.size() > 0 ) {
			if(countList.get(0) instanceof BigInteger) {
				totalCount = ((BigInteger)countList.get(0)).intValue();
			} else {
				totalCount = (Integer) countList.get(0);
			}
		}

		List list = new ArrayList();
		if(totalCount > 0) {
			String orderAttr = page.getOrderAttr();
			String orderType = page.getOrderType();
			if(orderAttr != null && !"".equals(orderAttr) && orderType != null && !("".equals(orderType))) {
				sqlwhere += " order by " + orderAttr + " " + orderType;
			}
			final String sqlwhere1 = sqlwhere;
			list = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createSQLQuery(sqlwhere1).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					if (parameterValues != null && parameterValues.length > 0) {
						for (int i = 0; i < parameterValues.length; i++) {
							query.setParameter(i, parameterValues[i]);
						}
					}
					query.setFirstResult(startIndex);
					query.setMaxResults(page.getPageSize());
					return query.list();
				}
			});
		}

		Page pages = new Page(totalCount, page.getPageSize(), page.getPageNow(), page.getOrderAttr(), page.getOrderType());
		pages.setContent(list);
		return pages;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Page pagedQueryParamHql(int pageNo, final int pageSize, final String hql, final Object[] parameterValues) {
		final int startIndex = (pageNo - 1) * pageSize;
		String sqlwhere = this.disposeWhere(hql);
		
		//求和sql
		String fromSql = "";
		fromSql = "from " + StringUtils.substringAfter(sqlwhere, "from");
		fromSql = StringUtils.substringBefore(fromSql, "order by");
		String countSql = "select count(*) " + fromSql;
		
		Query sqlQuery = this.getSession().createQuery(countSql);
		int index = 0;
		for (Object object : parameterValues) {
			sqlQuery.setParameter(index, object);
			index++;
		}
		List countList = sqlQuery.list();
		int totalCount = (countList != null && countList.size() > 0 && countList.get(0) != null) ? ((Long) countList.get(0)).intValue() : 0;
		
		List list = new ArrayList();
		if(totalCount > 0) {
			final String sqlwhere1 = sqlwhere;
			list = getHibernateTemplate().executeFind(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery(sqlwhere1).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					if (parameterValues != null && parameterValues.length > 0) {
						for (int i = 0; i < parameterValues.length; i++) {
							query.setParameter(i, parameterValues[i]);
						}
					}
					query.setFirstResult(startIndex);
					query.setMaxResults(pageSize);
					return query.list();
				}
			});
		}
		
		Page page = new Page(totalCount, pageSize, pageNo);
		page.setContent(list);
		return page;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int executeBatchUpdateSql(String sql, List<Object[]> parameterValues) {
		Connection conn = this.getSession().connection(); 
		PreparedStatement stmt;
		int count = 0;
		try {
			stmt = conn.prepareStatement(sql);
			if(sql.trim().endsWith("?")) {
				sql = sql + " ";
			}
			String[] sqlArray = sql.split("\\?");
			for (Object[] objects : parameterValues) {
				if(sqlArray.length == 1 && objects.length > 0) {
					throw new SQLException(sql + "没有问号, " +  Arrays.toString(objects) + "数组中不需要传值!");
				}
				if(sqlArray.length - 1 != objects.length) {
					throw new SQLException(sql + "语句中问号和" + Arrays.toString(objects) + "数组值个数不相等!");
				}
				for (int i=0; i<objects.length; i++) {
					Object object = objects[i];
					stmt.setObject(i + 1, object);
				}
				stmt.addBatch();
			}
			count = stmt.executeBatch().length;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		return count; 
	}

	@SuppressWarnings("deprecation")
	@Override
	public int executeBatchUpdateSql(List<String> sqlList, List<Object[]> parameterValues) {
		Connection conn = this.getSession().connection(); 
		Statement stmt = null;
		int count = 0;
		try {
			if(sqlList.size() != parameterValues.size()) {
				throw new SQLException("sql语句条数list大小和值数组list大小不相等!");
			}
			
			stmt = conn.createStatement();
			for(int i=0; i<sqlList.size(); i++) {
				String sql = sqlList.get(i).trim();
				if(sql.endsWith("?")) {
					sql = sql + " ";
				}
				String[] sqlArray = sql.split("\\?");
				Object[] values = parameterValues.get(i);
				if(sqlArray.length == 1 && values.length > 0) {
					throw new SQLException(sql + "没有问号, " + Arrays.toString(values) + "数组中不需要传值!");
				}
				if(sqlArray.length -1 != values.length) {
					throw new SQLException(sql + "语句中问号和" + Arrays.toString(values) + "数组值个数不相等!");
				}
				StringBuffer sb = new StringBuffer();
				for (int j=0; j<values.length; j++) {
					sb.append(sqlArray[j]);
					if(values[j] instanceof String) {
						sb.append("'" + values[j] + "'");
					} else {
						sb.append(values[j]);
					}
				}
				
				sb.append(sqlArray[sqlArray.length-1]);
				stmt.addBatch(sb.toString());
			}
			int[] executeBatch = stmt.executeBatch();
			count = executeBatch.length;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		return count; 
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int executeBatchUpdateSql(final List<String> sqlList) {
		Object result = getHibernateTemplate().execute(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Connection conn = session.connection(); 
				Statement stmt = conn.createStatement();
				for (String string : sqlList) {
					stmt.addBatch(string);
				}
				return stmt.executeBatch().length;
			}
		});
		return (Integer) result;
	}
	
	/**
	 * 去掉sql语句中的垃圾条件where 1=1 and
	 * @param sqlwhere
	 * @return
	 * 
	 *  2016年6月19日 上午12:49:41
	 */
	public String disposeWhere(String sqlwhere) {
		sqlwhere = sqlwhere.replaceAll("  ", " "); //避免sql拼接出现两个空格的情况
		if(sqlwhere.contains("where 1=1 and")) {
			sqlwhere = sqlwhere.replace("where 1=1 and", "where");
			return sqlwhere;
		}
		if(sqlwhere.contains("where 1=1 AND")) {
			sqlwhere = sqlwhere.replace("where 1=1 AND", "where");
			return sqlwhere;
		}
		if(sqlwhere.contains("WHERE 1=1 and")) {
			sqlwhere = sqlwhere.replace("WHERE 1=1 and", "where");
			return sqlwhere;
		}
		if(sqlwhere.contains("WHERE 1=1 AND")) {
			sqlwhere = sqlwhere.replace("WHERE 1=1 AND", "where");
			return sqlwhere;
		}
		if(sqlwhere.contains("where 1=1")) {
			sqlwhere = sqlwhere.replace(" where 1=1", "");
			return sqlwhere;
		}
		if(sqlwhere.contains("WHERE 1=1")) {
			sqlwhere = sqlwhere.replace(" WHERE 1=1", "");
			return sqlwhere;
		}
		return sqlwhere; 
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Map<String, T> findSqlMap(final String sql) {
		return (Map<String, T>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List list = query.list();
				if(list.size() > 0) {
					return list.get(0);
				} else {
					return new HashMap();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Map<String, T> findSqlMap(final String sql, final Object... values) {
		return (Map<String, T>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				List list = query.list();
				if(list.size() > 0) {
					return list.get(0);
				} else {
					return new HashMap();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Map<String, T> findHqlMap(final String hql) {
		return (Map<String, T>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List list = query.list();
				if(list.size() > 0) {
					return list.get(0);
				} else {
					return new HashMap();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Map<String, T> findHqlMap(final String hql, final Object... values) {
		return (Map<String, T>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				/**
				 * 设置参数
				 */
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				List list = query.list();
				if(list.size() > 0) {
					return list.get(0);
				} else {
					return new HashMap();
				}
			}
		});
	}

}

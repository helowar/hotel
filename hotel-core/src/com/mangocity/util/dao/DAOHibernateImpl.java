package com.mangocity.util.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.mangocity.util.FakeDeletedEntity;
import com.mangocity.util.ListUtil;
import com.mangocity.util.StringConstants;
import com.mangocity.util.exception.DAOException;
import com.mangocity.util.log.MyLog;

/**
 */
public class DAOHibernateImpl extends HibernateDaoSupport implements DAO, Serializable {

	private static final MyLog log = MyLog.getLogger(DAOHibernateImpl.class);

    public Serializable save(Object obj) {
        HibernateTemplate template = getHibernateTemplate();
        return template.save(obj);
    }

    public void update(Object obj) {
        HibernateTemplate template = getHibernateTemplate();
        template.update(obj);
    }

    public void saveOrUpdate(Object obj) throws DAOException {
        HibernateTemplate template = getHibernateTemplate();
        template.saveOrUpdate(obj);
    }

    public void saveOrUpdateHtlCtct(Object obj) {
        HibernateTemplate template = getHibernateTemplate();
        template.saveOrUpdate(obj);
    }

    public void remove(Object obj) {
        HibernateTemplate template = getHibernateTemplate();
        template.delete(obj);

    }

    public void remove(final String hsql, final Object[] values) {
        getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hsql);
                for (int i = 0; null != values && i < values.length; i++) {
                    Object value = values[i];
                    setObjectFromType(value, query, i);
                }
                query.executeUpdate();
                return null;
            }
        });
    }

    public void merge(Object obj) {
        HibernateTemplate template = getHibernateTemplate();
        template.merge(obj);
    }

    /**
     * 根据类名和ID来删除对象
     */
    public void remove(Class clazz, Serializable id) {
        Object obj = load(clazz, id);

        remove(obj);
    }

    /**
     * 假删除对象
     */
    public void removeByFake(FakeDeletedEntity obj) {
        obj.setDeleted(true);

        save(obj);
    }

    public void removeByFake(Class clazz, Serializable id) {
        FakeDeletedEntity obj = (FakeDeletedEntity) load(clazz, id);

        obj.setDeleted(true);

        save(obj);
    }

    public void remove(String ids, String table) {
        HibernateTemplate template = getHibernateTemplate();
        String hql = "select c from " + table + " as c where c.id in (" + ids + ")";
        template.delete(hql);
    }

    public List listAll(Class clazz) {
        HibernateTemplate template = getHibernateTemplate();
        return template.loadAll(clazz);
    }

    /**
     * 批量保存
     * 
     * @param entities
     */
    public void saveOrUpdateAll(Collection entities) {
        super.getHibernateTemplate().saveOrUpdateAll(entities);
    }

    /**
     * 命名查询
     * 
     * @param queryID
     * @param params
     * @return
     */
    public List queryByNamedQuery(String queryID, Object[] params) {

        return super.getHibernateTemplate().findByNamedQuery(queryID, params);

    }

    /**
     * 加载对象时，同时加锁
     * 
     * @param aclass
     * @param id
     * @return
     * @throws Exception
     */
    public Object findAndLockById(Class aclass, String id) throws Exception {
        return super.getHibernateTemplate().get(aclass, id, LockMode.UPGRADE);
    }

    public Object load(Class clazz, Serializable sid) {
        HibernateTemplate template = getHibernateTemplate();
        return template.load(clazz, sid);
    }

    public long getSequenceNextVal(final String seqName) {
        HibernateCallback cb = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Object ret = null;
                Connection conn = session.connection();
                PreparedStatement sttm = conn.prepareStatement("select " + seqName
                    + ".nextval from dual");
                ResultSet rs = null;
                try {
                    rs = sttm.executeQuery();
                    if (rs.next()) {
                        ret = Long.valueOf(rs.getLong(1));
                    }
                } catch (Exception e) {
                    log.error(e);
                } finally {
                    rs.close();
                    sttm.close();
                }

                return ret;
            }
        };

        Long ret = (Long) getHibernateTemplate().execute(cb);
        if (null == ret) {
            return -1;
        } else {
            return ret.longValue();
        }
    }

    public Object find(Class clazz, Serializable sid) {
        String hql = "from " + clazz.getName() + " as entity where entity.id = ?";
        List list = doquery(hql, sid, false);
        if (0 < list.size()) {
            return list.get(0);
        }
        return null;
    }

    public int totalNum(Class clazz) {
        final String queryString = "select count(*) from " + clazz.getName();
        final HibernateTemplate template = getHibernateTemplate();
        Integer integer = (Integer) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Iterator it = template.iterate(queryString);
                // Iterator it = queryObject.iterate();
                if (it.hasNext()) {
                    return (Integer) it.next();
                }
                return Integer.valueOf(0);
            }

        });
        return integer.intValue();
    }

    public int totalNum(String hsql) {
        int count = 0;
        SQLQuery query = getCurrentSession().createSQLQuery(hsql);
        query.addScalar("countRow", Hibernate.INTEGER);
        List topList = query.list();
        if (null != topList.get(0))
            count = (Integer) topList.get(0);
        return count;

    }

    public List pageList(Class clazz, final int startIndex, final int maxSize) {
        final String queryString = "from " + clazz.getName();
        final HibernateTemplate template = getHibernateTemplate();
        return template.executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query queryObject = session.createQuery(queryString);
                queryObject.setFirstResult(startIndex);
                queryObject.setMaxResults(maxSize);
                Iterator it = queryObject.iterate();
                return ListUtil.itTo(it);
            }
        });
    }

    public Session getCurrentSession() {
        return getSession();
    }

    synchronized public int getNum(String tableName, String sysNo) {
        String hql = "select max(c." + sysNo + ") from " + tableName + " as c";
        List li = doquery(hql, false);
        for (Iterator iterator = li.iterator(); iterator.hasNext();) {
            Object o = iterator.next();
            if (null != o) {
                return ((Integer) o).intValue() + 1;
            }
        }
        return 1;
    }

    public Object find(String hsql) {
        List results = doquery(hsql, false);
        if (null != results && 0 < results.size()) {
            return results.get(0);
        }
        return null;
    }

    public Object find(String hsql, Object value) {
        List results = doquery(hsql, value, false);
        if (null != results && 0 < results.size()) {
            return results.get(0);
        }
        return null;
    }

    public Object find(String hsql, Object[] values) {
        List results = doquery(hsql, values, false);
        if (null != results && 0 < results.size()) {
            return results.get(0);
        }
        return null;
    }

    public List query(String hsql, Object value) {
        return doquery(hsql, value, false);
    }
    
    public List query(String hsql) {
        return doquery(hsql, false);
    }
    
    public List query(String hsql, Object[] value) {
        return doquery(hsql, value, false);
    }

    public List doquery(final String hql, final Object value, final boolean cache) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                setObjectFromType(value, query, 0);
                if (cache) {
                    return ListUtil.itTo(query.iterate());
                } else {
                    return query.list();
                }
            }
        });
    }

    public List doquery(final String hql, final boolean cache) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                if (cache) {
                    return ListUtil.itTo(query.iterate());
                } else {
                    return query.list();
                }
            }
        });
    }
    
    public List doquerySQL(final String sql, final boolean cache) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createSQLQuery(sql);
                if (cache) {
                    return ListUtil.itTo(query.iterate());
                } else {
                    return query.list();
                }
            }
        });
    }
	
	/**
	 * 支持sql语句更新,返回更新记录数
	 */
	public int doSqlUpdate(final String sql) {
        return (Integer)getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return session.createSQLQuery(sql).executeUpdate();
            }
        });
    }
    public List doquery(final String hql, final Object[] values, final boolean cache) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                List list = null;
                for (int i = 0; null != values && i < values.length; i++) {
                    Object value = values[i];
                    setObjectFromType(value, query, i);
                }
                if (cache) {
                    list = ListUtil.itTo(query.iterate());
                    session.close();
                    return list;
                } else {
                    list = query.list();
                    session.close();
                    return list;
                }
            }
        });
    }
    
    public List doquerySQL(final String sql, final Object[] values, final boolean cache) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createSQLQuery(sql);
                List list = null;
                for (int i = 0; null != values && i < values.length; i++) {
                    Object value = values[i];
                    setObjectFromType(value, query, i);
                }
                if (cache) {
                    list = ListUtil.itTo(query.iterate());
                    session.close();
                    return list;
                } else {
                    list = query.list();
                    session.close();
                    return list;
                }
            }
        });
    }
   

    public List doquery(final String hql, final Object[] values, final int startIndex,
        final int maxResults, final boolean cache) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
            	List list;
                Query query = session.createQuery(hql);
                for (int i = 0; null != values && i < values.length; i++) {
                    Object value = values[i];
                    setObjectFromType(value, query, i);

                }
                query.setFirstResult(startIndex);
                query.setMaxResults(maxResults);
                if (cache) {
                	list = ListUtil.itTo(query.iterate());
                	session.close();
                	return list;
                } else {
                	list = query.list();
                	session.close();
                    return list;
                }
            }
        });
    }
    
    public List doquerySQL(final String hql, final Object[] values, final int startIndex,
            final int maxResults, final boolean cache,final Class classes) {
            return getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                	List list;
                	
                    Query query =(( null!=classes)?session.createSQLQuery(hql).addEntity(classes):session.createSQLQuery(hql));
                    for (int i = 0; null != values && i < values.length; i++) {
                        Object value = values[i];
                        setObjectFromType(value, query, i);

                    }
                    query.setFirstResult(startIndex);
                    query.setMaxResults(maxResults);
                    if (cache) {
                    	list = ListUtil.itTo(query.iterate());
                    	session.close();
                    	return list;
                    } else {
                    	list = query.list();
                    	session.close();
                        return list;
                    }
                }
            });
        }

    private void setObjectFromType(Object value, Query query, int i) {
        if (value instanceof Date) {
            query.setDate(i, (Date) value);
        } else if (value instanceof BigDecimal) {
            query.setBigDecimal(i, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            query.setBigInteger(i, (BigInteger) value);
        } else {
            query.setParameter(i, value);
        }
    }

    public List doquery(final String hql, final int startIndex, final int maxResults,
        final boolean cache) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                query.setFirstResult(startIndex);
                query.setMaxResults(maxResults);
                if (cache) {
                    return ListUtil.itTo(query.iterate());
                } else {
                    return query.list();
                }
            }
        });
    }
    
    public List doquerySQL(final String hql, final int startIndex, final int maxResults,
            final boolean cache,final Class classes) {
            return getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Query query = session.createSQLQuery(hql).addEntity(classes);
                    query.setFirstResult(startIndex);
                    query.setMaxResults(maxResults);
                    if (cache) {
                        return ListUtil.itTo(query.iterate());
                    } else {
                        return query.list();
                    }
                }
            });
        }

    public List doquery(final String hql, final Object value, final int startIndex,
        final int maxResults, final boolean cache) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                setObjectFromType(value, query, 0);
                query.setFirstResult(startIndex);
                query.setMaxResults(maxResults);
                if (cache) {
                    return ListUtil.itTo(query.iterate());
                } else {
                    return query.list();
                }
            }
        });
    }

    public void compositeHSql(StringBuffer buffer, Object[] params, String field, List results) {

        if (null != params) {
            if (1 == params.length) {
                buffer.append(StringConstants.AND).append(field).append("  = ? ");
                results.add(params[0]);
            } else if (1 < params.length) {

                buffer.append(StringConstants.AND).append(field).append(StringConstants.SPACE)
                    .append(" in ( ? ");
                results.add(params[0]);

                for (int m = 1; m < params.length; m++) {
                    buffer.append(" , ? ");
                    results.add(params[m]);
                }

                buffer.append(" ) ");

            }

            buffer.append(StringConstants.SPACE);
        }
    }

    public int doUpdateBatch(final String hql, final Object[] values) {
        return getHibernateTemplate().bulkUpdate(hql, values);

    }

	/**
	 *sql语句查询分页 
	 *haibo.li
	 */
	public List pageSqlList(final String sql,final int startIndex, final int maxSize) {
		final HibernateTemplate template = getHibernateTemplate();
		return template.executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Iterator it =null;
				try{
					Query queryObject = session.createSQLQuery(sql);
					queryObject.setFirstResult(startIndex);
					queryObject.setMaxResults(maxSize);
					it = queryObject.list().iterator();
				}catch(Exception ex){
					log.error(ex);
				}finally{
					session.close();
				}
				if(it==null){
					return null;
				}else{
					return ListUtil.itTo(it);
				}
			}
		});
	}

	public List queryByNativeSql4Map(final String sql, final Object[] param, final boolean cache) {
		// TODO Auto-generated method stub
		final HibernateTemplate template = getHibernateTemplate();
		return (List) template.execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List list = null;
                for (int i = 0; null != param && i < param.length; i++) {
                    Object value = param[i];
                    setObjectFromType(value, query, i);
                }
                if (cache) {
                    list = ListUtil.itTo(query.iterate());
                    session.close();
                    return list;
                } else {
                    list = query.list();
                    session.close();
                    return list;
                }
			}

		});
	}
}
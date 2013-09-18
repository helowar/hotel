package com.mangocity.util.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.mangocity.util.FakeDeletedEntity;
import com.mangocity.util.ListUtil;

/**
 * 读取只读数据库的DAO类
 * @author chenjiajie
 *
 */
public class DAOHibernateImplReadOnly extends HibernateDaoSupport implements DAOHibernateReadOnly {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1543991284731527941L;

    public int doSqlUpdate(String sql) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int doUpdateBatch(String hql, Object[] values) {
        // TODO Auto-generated method stub
        return 0;
    }

    public List doquery(String hql, Object value, boolean cache) {
        // TODO Auto-generated method stub
        return null;
    }

    public List doquery(String hql, boolean cache) {
        // TODO Auto-generated method stub
        return null;
    }

    public List doquery(String hql, Object[] values, boolean cache) {
        // TODO Auto-generated method stub
        return null;
    }

    public List doquery(String hql, Object[] values, int startIndex,
            int maxResults, boolean cache) {
        // TODO Auto-generated method stub
        return null;
    }

    public List doquery(String hql, int startIndex, int maxResults,
            boolean cache) {
        // TODO Auto-generated method stub
        return null;
    }

    public List doquery(String hql, Object value, int startIndex,
            int maxResults, boolean cache) {
        // TODO Auto-generated method stub
        return null;
    }

    public List doquerySQL(String sql, boolean cache) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object find(Class clazz, Serializable sid) {
        // TODO Auto-generated method stub
        return null;
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

    public Session getCurrentSession() {
        return getSession();
    }

    public long getSequenceNextVal(String seqName) {
        // TODO Auto-generated method stub
        return 0;
    }

    public List listAll(Class clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object load(Class clazz, Serializable sid) {
        // TODO Auto-generated method stub
        return null;
    }

    public List pageList(Class clazz, int startIndex, int maxSize) {
        // TODO Auto-generated method stub
        return null;
    }

    public List query(String hsql, Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    public List query(String hsql, Object[] value) {
        // TODO Auto-generated method stub
        return null;
    }

    public List queryByNamedQuery(String queryID, Object[] params) {
        // TODO Auto-generated method stub
        return null;
    }

    public void remove(Object obj) {
        // TODO Auto-generated method stub

    }

    public void remove(String ids, String table) {
        // TODO Auto-generated method stub

    }

    public void remove(Class clazz, Serializable id) {
        // TODO Auto-generated method stub

    }

    public void removeByFake(FakeDeletedEntity obj) {
        // TODO Auto-generated method stub

    }

    public void removeByFake(Class clazz, Serializable id) {
        // TODO Auto-generated method stub

    }

    public Serializable save(Object obj) {
        // TODO Auto-generated method stub
        return null;
    }

    public void saveOrUpdate(Object obj) {
        // TODO Auto-generated method stub

    }

    public void saveOrUpdateAll(Collection entities) {
        // TODO Auto-generated method stub

    }

    public void update(Object obj) {
        // TODO Auto-generated method stub

    }
    
    public void releaseSessian(Session session){
        super.releaseSession(session);
    }

	public List queryByNativeSql4Map(String sql, Object[] param, boolean cache) {
		// TODO Auto-generated method stub
		return null;
	}
}

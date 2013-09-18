package com.mangocity.util.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

import com.mangocity.util.FakeDeletedEntity;

/**
 */
public interface DAO {
    public Serializable save(Object obj);

    public void update(Object obj);

    public void saveOrUpdate(Object obj);

    public void saveOrUpdateAll(Collection entities);

    public void remove(Object obj);

    public List listAll(Class clazz);

    public Object load(Class clazz, Serializable sid);

    public Object find(Class clazz, Serializable sid);

    public List pageList(Class clazz, int startIndex, int maxSize);

    public Session getCurrentSession();

    public Object find(String hsql);

    public Object find(String hsql, Object value);

    public Object find(String hsql, Object[] values);

    public List query(String hsql, Object value);

    public List query(String hsql, Object[] value);

    public List doquery(String hql, Object value, boolean cache);

    public List doquery(String hql, boolean cache);

    public List doquery(String hql, Object[] values, boolean cache);

    public List doquery(String hql, Object[] values, int startIndex, int maxResults, boolean cache);

    public List doquery(String hql, int startIndex, int maxResults, boolean cache);

    public List doquery(String hql, Object value, int startIndex, int maxResults, boolean cache);

    public long getSequenceNextVal(String seqName);
    
    public List doquerySQL(final String sql, final boolean cache);
    
    public int doUpdateBatch(final String hql, final Object[] values);
    
    public int doSqlUpdate(String sql);

    public void removeByFake(FakeDeletedEntity obj);

    public void removeByFake(Class clazz, Serializable id);

    public void remove(String ids, String table);

    public void remove(Class clazz, Serializable id);

    /**
     * 命名查询
     * 
     * @param queryID
     * @param params
     * @return
     */
    public List queryByNamedQuery(String queryID, Object[] params);
    
    /**
     * 根据原生态的sql查询数据库，并用Map封装行结果。
     * @param sql
     * @param param
     * @param cache
     * @return
     */
    public List queryByNativeSql4Map(String sql,Object[] param,boolean cache);
}

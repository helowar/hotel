package com.mangocity.hdl.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

public interface IDao {
    public Serializable save(Object obj);

    public void update(Object obj);

    public void saveOrUpdate(Object obj) ;
    
    public void saveOrUpdateAll(Collection entities);

    public void remove(Object obj) ;


    public List listAll(Class clazz);

    public Object load(Class clazz, Serializable sid);

    public Object find(Class clazz, Serializable sid);

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

	public void remove(String ids, String table);

	public void remove(Class clazz, Serializable id);

	/**
	 * 命名查询
	 * @param queryID
	 * @param params
	 * @return
	 */
	public List queryByNamedQuery(String queryID, Object[] params);
}

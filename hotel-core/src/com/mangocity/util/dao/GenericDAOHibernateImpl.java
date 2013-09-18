/*
 * Copyright 2005-2010 the original author or authors.
 *
 * you may not use this file on any business environment.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mangocity.util.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.mangocube.corenut.commons.exception.ErrorCode;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.mangocity.hotel.exception.DAException;
import com.mangocity.util.ListUtil;

public class GenericDAOHibernateImpl extends HibernateDaoSupport implements GenericDAO 
{
	public enum DataAccessError{
	      @ErrorCode(comment = "Exception occured when accessing DB.")
	      DATA_ACCESS_FAILED,
	      
	      @ErrorCode(comment = "Exception occured when getting records from result set.")
	      FETCH_RESULTSET_FAILED
	}
	
	@SuppressWarnings("unchecked")
    public <T> T save(T model) 
    {
		try
		{
			return (T)getHibernateTemplate().save(model);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }

    public <T> void update(T model) 
    {
    	try
		{
    		getHibernateTemplate().update(model);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }

    public <T> void saveOrUpdate(T model) 
    {
    	try
		{
    		getHibernateTemplate().saveOrUpdate(model);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }
    
    public <T> void remove(T model) 
    {
    	try
		{
    		getHibernateTemplate().delete(model);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }
    
    public <T> void merge(T model) 
    {
    	try
		{
    		getHibernateTemplate().merge(model);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }


    public <T> void remove(Class<T> clazz, Serializable id)
    {
    	try
		{
    		T entity = load(clazz, id);
            getHibernateTemplate().delete(entity);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }

    @SuppressWarnings("unchecked")
    public <T> void remove(String[] ids, String className) 
    {
    	StringBuilder hql = new StringBuilder();
		hql.append(" select entity from ");
		hql.append(className);
		hql.append(" as entity where entity.id in ( ");
		
		List entityIdList = new ArrayList(ids.length);
		for (String entityId : ids) {
			hql.append("?, ");
			entityIdList.add(Long.valueOf(entityId));
		}		
		hql.setCharAt(hql.length() - 2, ')');
        List<T> entityList = query(hql.toString(), entityIdList.toArray(), true);
        
        try
		{
        	getHibernateTemplate().deleteAll(entityList);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }

    public <T> void saveOrUpdateAll(Collection<T> entities) 
    {
    	try
		{
    		getHibernateTemplate().saveOrUpdateAll(entities);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz, Serializable sid)
    {
    	try
		{
    		return (T)getHibernateTemplate().get(clazz, sid);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }

    @SuppressWarnings("unchecked")
    public <T> T load(Class<T> clazz, Serializable sid) 
    {
    	try
		{
    		return (T)getHibernateTemplate().load(clazz, sid);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> loadAll(Class<T> clazz)
    {
    	List<T> entityList = null;
    	try
		{
    		entityList = getHibernateTemplate().loadAll(clazz);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
		
    	if(entityList == null)
    	{
    		entityList = new ArrayList<T>(0);
    	}
    	
        return entityList;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryByNativeSQL(final String nativeSql, final int startIndex, final int maxResults, 
    		final Object[] paramValues, final Class<T> clazz)
    {
    	try
		{
    		return getHibernateTemplate().executeFind(new HibernateCallback() {
    			public Object doInHibernate(Session session) throws HibernateException {
    				Query query = (( null != clazz)?session.createSQLQuery(nativeSql).addEntity(clazz) : session.createSQLQuery(nativeSql));
					query.setFirstResult(startIndex);
					if(maxResults > 0)
					{
						query.setMaxResults(maxResults);
					}
					if(paramValues != null && paramValues.length > 0)
					{
						for (int i = 0; i < paramValues.length; i++) 
						{
		                    setObjectFromType(paramValues[i], query, i);
		                }
					}
					
					List resultList = query.list();
					if(resultList == null)
					{
						resultList = Collections.EMPTY_LIST;
					}
					
					return resultList;
    			}
    		});
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}    	
    }
    
    public <T> List<T> queryByNativeSQL(String nativeSql, Object[] paramValues, Class<T> clazz)
    {
    	return queryByNativeSQL(nativeSql, 0, 0, paramValues, clazz);
    }
    
    public <T> List<T> queryByNativeSQL(String nativeSql, Object[] paramValues) {
    	return queryByNativeSQL(nativeSql, 0, 0, paramValues, null);
    }

    @SuppressWarnings("unchecked")
	public <T> List<T> find(final String hsql, final Object[] paramValues) {
    	List<T> entityList = null;
    	try
		{
    		entityList = doquery(hsql, paramValues, false);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
		
    	if(entityList == null)
    	{
    		entityList = new ArrayList<T>(0);
    	}
    	
        return entityList;
    }
    
    @SuppressWarnings("unchecked")
    public <T> List<T> query(final String hql, final Object[] paramValues, final int startIndex, 
    		final int maxResults, final boolean cacheable)
    {
    	try
		{
    		return getHibernateTemplate().executeFind(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    Query query = session.createQuery(hql);
                    if(paramValues != null && paramValues.length > 0)
                    {
    	                for (int i = 0; i < paramValues.length; i++) 
    	                {
    	                    setObjectFromType(paramValues[i], query, i);
    	                }
                    }
                    query.setFirstResult(startIndex);
                    if(maxResults > 0)
    				{
    					query.setMaxResults(maxResults);
    				}
                    
                    List resultList = null;
                    if (cacheable) {
                    	resultList = ListUtil.itTo(query.iterate());                	
                    } else {
                    	resultList = query.list(); 
                    	if(resultList == null)
                    	{
                    		resultList = Collections.EMPTY_LIST;
                    	}
                    }
                    
                    return resultList;
                }
            });
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }
    
    public <T> List<T> query(String hql, Object[] paramValues, boolean cacheable)
    {
    	return query(hql, paramValues, 0, 0, cacheable);
    }
    
    public <T> List<T> query(String hql, Object[] paramValues)
    {
    	return query(hql, paramValues, 0, 0, false);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryByNamedQuery(String queryID, Object[] paramValues) 
    {
    	List<T> entityList = null;
    	try
		{
    		entityList = getHibernateTemplate().findByNamedQuery(queryID, paramValues);
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
		
    	if(entityList == null)
    	{
    		entityList = Collections.EMPTY_LIST;
    	}
    	
        return entityList;
    }
    
    private void setObjectFromType(Object value, Query query, int i) 
    {
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

	public int updateByQL(final String hql, final Object[] paramValues) 
	{
		try
		{
			return (Integer)getHibernateTemplate().execute(new HibernateCallback() {
	            public Object doInHibernate(Session session) throws HibernateException {
	                Query query = session.createQuery(hql);
	                if(paramValues != null && paramValues.length > 0)
	                {
		                for (int i = 0; i < paramValues.length; i++) 
		                {
		                    setObjectFromType(paramValues[i], query, i);
		                }
	                }
	                
	                return Integer.valueOf(query.executeUpdate());
	            }
	        });
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
	}

    @Deprecated
    public void execProcedure(final String sql, final Object[] paramValues) 
    {
    	try
		{
    		getHibernateTemplate().execute(new HibernateCallback() { 
                public Object doInHibernate(Session session) throws HibernateException { 
                    try 
                    { 
                        Connection conn = session.connection();
                        CallableStatement stmt = conn.prepareCall(sql); 
                        if(paramValues != null && paramValues.length > 0)
                        {
        	                for (int idx = 1; idx <= paramValues.length; idx++) 
        	                {
        	                	stmt.setObject(idx, paramValues[idx - 1]); //TODO
        	                }
                        }                    
                        stmt.execute(); 
                    } 
                    catch (SQLException sqlException)
                    {
                    	throw new HibernateException(sqlException.getMessage(), sqlException);
                    } 

                    return null; 
                } 
            });
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public Map<Integer, ?> execProcedure(final String sql, final Map<Integer, ?> inParamsIdxAndValue, 
    		final Map<Integer, Integer> outParamsIdxAndType) 
    {
    	Map<Integer, ?> result = null;
    	try
		{
    		result = (Map<Integer, ?>)getHibernateTemplate().execute(new HibernateCallback() { 
                public Object doInHibernate(Session session) throws HibernateException { 
                	CallableStatement cstmt = null;
                	Map resultMap = new HashMap(outParamsIdxAndType.size());
                    try 
                    {
                        Connection conn = session.connection();
                        cstmt = conn.prepareCall(sql); 
                        if(inParamsIdxAndValue != null && !inParamsIdxAndValue.isEmpty())
                        {
        	                for (Map.Entry<Integer, ?> inParamsEntry: inParamsIdxAndValue.entrySet()) 
        	                {
        	                	cstmt.setObject(inParamsEntry.getKey().intValue(), inParamsEntry.getValue());//TODO
        	                }
                        }
                        
                        for (Map.Entry<Integer, Integer> outParamsEntry: outParamsIdxAndType.entrySet()) 
    	                {                    	
                        	cstmt.registerOutParameter(outParamsEntry.getKey().intValue(), 
                        			outParamsEntry.getValue().intValue());
    	                }
                        
                        cstmt.execute();
                        
                        //获得存储过程的所有返回值并将它们的索引和对应的返回值添加到Map集合中
                        for (Map.Entry<Integer, Integer> outParamsEntry: outParamsIdxAndType.entrySet()) 
    	                {
                        	resultMap.put(outParamsEntry.getKey(),cstmt.getObject(outParamsEntry.getKey().intValue()));
    	                }
                    } 
                    catch (SQLException sqlException)
                    {
                    	throw new HibernateException(sqlException.getMessage(), sqlException);
                    }
                    finally
                    {
                    	try
                    	{
                    		cstmt.close();
                    	}
                    	catch(SQLException sqlException)
                    	{
                    		throw new HibernateException(sqlException.getMessage(), sqlException);
                    	}
                    }

                    return resultMap; 
                } 
            });
		}
		catch(DataAccessException dae)
		{
			throw new DAException(DataAccessError.DATA_ACCESS_FAILED, dae);
		}
    	return result;
    }
    
    public Session getCurrentSession() {
        return getSession();
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
    
    public int doUpdateBatch(final String hql, final Object[] values) {
        return getHibernateTemplate().bulkUpdate(hql, values);
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
}
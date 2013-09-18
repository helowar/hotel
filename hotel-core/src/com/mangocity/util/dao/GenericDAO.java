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
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GenericDAO {
	
	/**
	 * 持久化实体对象
	 * @param model	实体对象
	 * @return	实体对象
	 */
    public <T> T save(T model);
    
    /**
     * 更新实体对象
     * @param entity	实体对象
     * @return
     */
    public <T> void update(T model);
    
    /**
     * 保存或更新实体对象
     * @param entity	实体对象
     * @return
     */
    public <T> void saveOrUpdate(T model);
    
    /**
     * 保存或更新多个实体对象
     * @param modelList	实体对象集合
     * @return
     */
    public <T> void saveOrUpdateAll(Collection<T> modelList);

    /**
     * 根据ID加载指定的实体
     * @param clazz		实体类类对象
     * @param sid		实体对象ID
     * @return	实体对象
     */
    public <T> T load(Class<T> clazz, Serializable sid);
    
    /**
     * 根据ID查询指定的实体对象
     * @param clazz		实体类类对象
     * @param sid		实体对象ID
     * @return	实体对象
     */
    public <T> T get(Class<T> clazz, Serializable sid);
    
    /**
     * 加载指定实体类的所有对象
     * @param clazz		实体类类对象
     * @return	实体对象集合
     */
    public <T> List<T> loadAll(Class<T> clazz);

    /**
     * 根据指定条件查询指定数量的实体对象
     * @param ql			查询语句
     * @param paramValues	查询条件
     * @param startIndex	起始索引位置
     * @param maxResults	要查询的实体数量
     * @param cacheable		是否从缓存中查询
     * @return	实体对象集合
     */
    public <T> List<T> query(String ql, Object[] paramValues, int startIndex, int maxResults, boolean cacheable);
    
    /**
     * 根据指定条件查询实体对象
     * @param ql			查询语句
     * @param paramValues	查询条件
     * @param cacheable		是否从缓存中查询
     * @return	实体对象集合
     */
    public <T> List<T> query(String ql, Object[] paramValues, boolean cacheable);
    
    /**
     * 根据指定条件查询实体对象
     * @param ql			查询语句
     * @param paramValues	查询条件
     * @return	实体对象集合
     */
    public <T> List<T> query(String ql, Object[] paramValues);
    
    /**
     * 使用本地SQL查询，如果指定了clazz对象，则结果集中的每个元素都是clazz的实例，否则，
     * 如果结果集中的每行只有一个字段，则返回的List中每个元素对应这个字段的值；
     * 如果结果集中的每行有多个字段，则结果集中的每个实例为Object[]类型
     * 
     * @param nativeSql		本地SQL语句
     * @param startIndex	起始索引位置
     * @param maxResults	要查询的实体数量
     * @param paramValues	参数值数组
     * @param Class			Class对象
     * @return	结果集
     */
	public <T> List<T> queryByNativeSQL(String nativeSql, int startIndex, int maxResults, Object[] paramValues,
			Class<T> clazz);
	
	/**
     * 使用本地SQL查询，返回clazz的实例集合
     * 
     * @param nativeSql		本地SQL语句
     * @param paramValues	参数值数组
     * @param Class			Class对象
     * @return	结果集
     */
	public <T> List<T> queryByNativeSQL(String nativeSql, Object[] paramValues,	Class<T> clazz);
	
	/**
     * 使用本地SQL查询，如果结果集中的每行只有一个字段，则返回的List中每个元素对应这个字段的值；
     * 如果结果集中的每行有多个字段，则结果集中的每个实例为Object[]类型
     * 
     * @param nativeSql		本地SQL语句
     * @param paramValues	参数值数组
     * @return	结果集
     */
	public <T> List<T> queryByNativeSQL(String nativeSql, Object[] paramValues);
    
    /**
     * 删除实体对象
     * @param clazz	实体类类对象
     * @param id	实体ID
     * @return
     */
    public <T> void remove(Class<T> clazz, Serializable id);
    
    /**
     * 批量删除实体对象
     * @param ids		实体ID数组
     * @param className	实体类类名
     * @return
     */
    public <T> void remove(String[] ids, String className);  
    
    /**
     * 根据ql语句进行更新。QL语句可以是Hibernate中的HQL语句、JDBC中的本地SQL语句、JPA中的JPA QL语句等，这依赖于具体的实现
     * 
     * @param ql  			ql语句 						
     * @param paramValues	参数值数组
     * @return	更新的记录数
     */
    public int updateByQL(String ql,Object[] paramValues);
    
    /**
     * 执行没有返回值的存储过程
     * 
     * @param sql			SQL语句
     * @param paramValues	参数值数组
     * @return
     */
    @Deprecated
    public void execProcedure(String sql, Object[] paramValues);
    
    /**
     * 执行有返回值的存储过程
     * 
     * @param sql					SQL语句
     * @param inParamsIdxAndValue	输入参数的索引和值的键值对
     * @param outParamsIdxAndType	输出参数的索引和数据类型的键值对
     * @return	输出参数的索引和值的键值对
     */
    @Deprecated
    public Map<Integer, ?> execProcedure(final String sql, final Map<Integer, ?> inParamsIdxAndValue, 
    		final Map<Integer, Integer> outParamsIdxAndType);
}

package com.mangocity.util.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.common.util.PaginatedList;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.mangocity.util.dao.paging.ReflectUtil;

/**
 */
public class DAOIbatisImpl extends SqlMapClientDaoSupport implements IbatisDao, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8745923796633131831L;
    private SqlExecutor sqlExecutor;

    public DAOIbatisImpl() {
        super();
    }

    /**
     * 初始运行
     */
    public void initialize() {
        if (null != sqlExecutor) {
            SqlMapClient sqlMapClient = getSqlMapClientTemplate().getSqlMapClient();
            if (sqlMapClient instanceof ExtendedSqlMapClient) {
                ReflectUtil.setFieldValue(((ExtendedSqlMapClient) sqlMapClient).getDelegate(),
                    "sqlExecutor", SqlExecutor.class, sqlExecutor);
            }
        }
    }

    /**
     * 分页查询
     * 
     * @param id
     * @param params
     *            查询参数，里面必须要有一个pageNo的页号参数
     * @param pageSize
     * @return
     */
    public PaginatedList queryByPagination(String id, Map params, int pageSize) {

        return getSqlMapClientTemplate().queryForPaginatedList(id, params, pageSize);
    }

    /**
     * 不分页查询
     * 
     * @param id
     *            查询ID
     * @param params
     *            查询参数
     * @return
     */
    public List queryForList(String id, Map params) {
        return super.getSqlMapClientTemplate().queryForList(id, params);
    }

    /**
     * 调用存储过程 add by kun.chen 2007-9-14 ◎param id--存储过程ID ◎param params --存储过程参数列表 ◎return
     */
    public Object queryForObject(String id, Map params) {
        return super.getSqlMapClientTemplate().queryForObject(id, params);
    }

    public List queryForList(String queryID, Object obj) {
        return super.getSqlMapClientTemplate().queryForList(queryID, obj);
    }

    public Object queryForObject(String queryID, Object obj) {
        return super.getSqlMapClientTemplate().queryForObject(queryID, obj);
    }

    public Object save(String queryID, Object obj) {
        return super.getSqlMapClientTemplate().insert(queryID, obj);
    }

    public int update(String queryID, Object obj) {
        return super.getSqlMapClientTemplate().update(queryID, obj);
    }

    public int delete(String queryID, Object obj) {
        return super.getSqlMapClientTemplate().delete(queryID, obj);
    }

    public SqlExecutor getSqlExecutor() {
        return sqlExecutor;
    }

    public void setSqlExecutor(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }
}

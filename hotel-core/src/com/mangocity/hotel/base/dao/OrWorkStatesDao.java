package com.mangocity.hotel.base.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mangocity.hotel.base.persistence.OrWorkStates;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * OrWorkStates Dao类
 * 
 * @author chenkeming
 * 
 */
public class OrWorkStatesDao extends DAOHibernateImpl implements Serializable{

    public void insertObject(Object obj) {
        super.save(obj);
    }

    public Object loadObject(Serializable objID) {
        return (OrWorkStates) super.load(OrWorkStates.class, objID);
    }

    public void updateObject(Object obj) {
        super.saveOrUpdate(obj);
    }

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdate(OrWorkStates obj) {// parasoft-suppress PB.IMC "与父类同名,暂不修改"
        if (null == obj.getID()) {
            insertObject(obj);
        } else
            updateObject(obj);
    }

    /**
     * 命名查询
     */
    public List queryByNamedQuery(String queryID, Object[] params) {
        return super.getHibernateTemplate().findByNamedQuery(queryID, params);
    }
    /**
     * 标记房控人员的相关权限
     * 返回 0：一般房控人员 1：房控主管 -1：非房控人员
     * addby juesu.chen 2009-12-24
     */
    public int markRSCRights(String loginId){
        String sqlStr = "select pr.PERMISSION from HTL_POPEDOM_CONTROL pr where pr.LOG_NAME=? and pr.CONTROL_TYPE='2'";
        List result = super.doquerySQL(sqlStr, new Object[]{loginId}, false);
        if(result.size() > 0){
        	return ((BigDecimal)result.get(0)).intValue();
        }
        return -1;
    }
}

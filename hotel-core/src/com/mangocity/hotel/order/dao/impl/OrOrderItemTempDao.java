package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrOrderItemTemp;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * OrOrderItemTemp Dao类
 * 
 * @author chenkeming
 * 
 */
public class OrOrderItemTempDao extends DAOHibernateImpl implements Serializable {

    public void insertObject(Object obj) {
        super.save(obj);
    }

    public Object loadObject(Serializable objID) {
        return (OrOrderItemTemp) super.load(OrOrderItemTemp.class, objID);
    }

    public void updateObject(Object obj) {
        super.saveOrUpdate(obj);
    }

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdate(OrOrderItemTemp obj) {// parasoft-suppress PB.IMC "与父类同名,暂不修改"
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

    public List queryAuditItemByAuditIds(final Long[] auditIds) {
        List params = new ArrayList();
        StringBuffer hql = new StringBuffer().append("from AuditItem a where 1=1 ");
        super.compositeHSql(hql, auditIds, "a.audit.ID", params);
        hql.append(" order by a.roomIndex, a.fellowDate");

        String hSql = hql.toString();
        return super.getHibernateTemplate().find(hSql, params.toArray());
    }

}

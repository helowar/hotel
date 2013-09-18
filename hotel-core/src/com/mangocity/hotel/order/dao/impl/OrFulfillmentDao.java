package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * OrFulfillment Dao类
 * 
 * @author chenkeming
 * 
 */
public class OrFulfillmentDao extends DAOHibernateImpl {

    public void insertObject(Object obj) {
        super.save(obj);
    }

    public Object loadObject(Serializable objID) {
        return (OrFulfillment) super.load(OrFulfillment.class, objID);
    }

    public void updateObject(Object obj) {
        super.saveOrUpdate(obj);
    }

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdate(OrFulfillment obj) {// parasoft-suppress PB.IMC "与父类同名,暂不修改"
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

}

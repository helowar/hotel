package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrFellowInfo;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * OrFellowInfo Dao类
 * 
 * @author chenkeming
 * 
 */
public class OrFellowInfoDao extends DAOHibernateImpl {

    public void insertObject(Object obj) {
        super.save(obj);
    }

    public Object loadObject(Serializable objID) {
        return (OrFellowInfo) super.load(OrFellowInfo.class, objID);
    }

    public void updateObject(Object obj) {
        super.saveOrUpdate(obj);
    }

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdate(OrFellowInfo obj) {// parasoft-suppress PB.IMC "与父类同名,暂不修改"
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

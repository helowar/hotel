package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrMemberConfirm;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * OrMemberConfirm Dao类
 * 
 * @author chenkeming
 * 
 */
public class OrMemberConfirmDao extends DAOHibernateImpl {

    public void insertObject(Object obj) {
        super.save(obj);
    }

    public Object loadObject(Serializable objID) {
        return (OrMemberConfirm) super.load(OrMemberConfirm.class, objID);
    }

    public void updateObject(Object obj) {
        super.saveOrUpdate(obj);
    }

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdate(OrMemberConfirm obj) {// parasoft-suppress PB.IMC "与父类同名,暂不修改"
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
     * 查询记录数
     * 
     * @param sql
     * @return
     */
    public int getNum(String sql) {
        return super.totalNum(sql);
    }
}

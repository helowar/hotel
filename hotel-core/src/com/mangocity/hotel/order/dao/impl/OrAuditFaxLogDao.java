package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrAuditFaxLog;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * OrAuditFaxLog Dao类
 * 
 * @author chenkeming
 * 
 */
public class OrAuditFaxLogDao extends DAOHibernateImpl implements Serializable {

    public void insertObject(Object obj) {
        super.save(obj);
    }

    public Object loadObject(Serializable objID) {
        return (OrAuditFaxLog) super.find(OrAuditFaxLog.class, objID);
    }

    public void updateObject(Object obj) {
        super.saveOrUpdate(obj);
    }

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdate(OrAuditFaxLog obj) {// parasoft-suppress PB.IMC "与父类同名,暂不修改"
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
     * 根据参数获取OrAuditFaxLog
     * 
     * @param data
     *            *
     * @return
     */
    public List getAuditLogList(Date data) {
     return queryByNamedQuery("hQueryAudit_Log", new Object[] { data });
    }

}

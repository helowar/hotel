package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrPaperContact;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * OrPaperContact Dao类
 * 
 * @author chenkeming
 * 
 */
public class OrPaperContactDao extends DAOHibernateImpl implements Serializable{

    public void insertObject(Object obj) {
        super.save(obj);
    }

    public Object loadObject(Serializable objID) {
        return (OrPaperContact) super.load(OrPaperContact.class, objID);
    }

    public void updateObject(Object obj) {
        super.saveOrUpdate(obj);
    }

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdate(OrPaperContact obj) {// parasoft-suppress PB.IMC "与父类同名,暂不修改"
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
     * 批量删除
     * @param hotelId
     */
    public void batchRemove(Long[] hotelId){
    	super.remove("delete OrPaperContact c where c.ID in(?)", hotelId);
    }

}

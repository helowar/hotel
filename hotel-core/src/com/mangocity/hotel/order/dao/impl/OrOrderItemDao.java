package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrOrderItem;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 * OrOrderItem Dao类
 * 
 * @author chenkeming
 * 
 */
public class OrOrderItemDao extends DAOHibernateImpl implements Serializable {

    public void insertObject(Object obj) {
        super.save(obj);
    }

    public Object loadObject(Serializable objID) {
        return (OrOrderItem) super.load(OrOrderItem.class, objID);
    }

    public void updateObject(Object obj) {
        super.saveOrUpdate(obj);
    }

    /**
     * 插入或更新obj
     * 
     * @param obj
     */
    public void saveOrUpdate(OrOrderItem obj) {// parasoft-suppress PB.IMC "父类重名，暂不修改。"
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
    
    
    public List findSimilarOrOrderShow(Long orderId, Date date) {

    	return queryByNamedQuery("hQueryOrder_ItemShow1", new Object[] {orderId, date });
    }
    
    
    /**
     * 根据参数获得order对象
     * 
     * @param hotelId
     * @param date
     * @return
     */

    public List findSimilarOrderNumbers(Object hotelId, Date date) {
        String hsql = "SELECT DISTINCT(a.orderID) from VOrOrderItem a " +
                "where	a.hotelId = ? and a.night = ? " +
                "and a.orderID in (select o.ID from VOrOrder o " +
                "where o.payMethod = 'pay' and o.orderState != 14 " +
                " and o.orderState != 13 and o.orderState != 5)";
        return query(hsql, new Object[] { hotelId, date });
    }


}

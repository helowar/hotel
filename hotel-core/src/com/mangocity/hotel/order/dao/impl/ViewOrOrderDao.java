package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.order.dao.IViewOrOrderDao;
import com.mangocity.hotel.order.persistence.VOrOrder;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * OrOrder Dao类
 * 
 * @author yong.zeng
 * 
 */
public class ViewOrOrderDao extends GenericDAOHibernateImpl implements IViewOrOrderDao{

    public void insertObject(VOrOrder vorder) {
    	save(vorder);;
    }

    public VOrOrder loadOrder(Serializable orderID) {
        //return (VOrOrder) super.load(VOrOrder.class, orderID);
        return load(VOrOrder.class, orderID);
    }

    public void updateOrder(VOrOrder order) {
        super.saveOrUpdate(order);
    }
    

    /**
     * 根据ID获取VOrOrder(直接操作数据库)
     * @param orderID
     * @return
     */
    public VOrOrder findVOrOrder(Serializable orderID) {
        return super.get(VOrOrder.class, orderID);
    }
  

    /**
     * 命名查询
     */
    public List queryByNamedQuery(String queryID, Object[] params) {
        return queryByNamedQuery(queryID, params);
    }
    /**
     * 根据参数获取orderItemList
     * 
     * @param id
     * @param orderType
     * 
     * 
     */

    public List<VOrOrder> getViewOrders(Long orderID, int orderType) {
        return queryByNamedQuery("hQueryOrder_audit", new Object[] { orderID,
            orderType });
    }
    
    /**
     * 获取VOrOrder
     * @param orderID
     * @return
     */
    public VOrOrder getVOrder(Long orderID) {
        return(VOrOrder) queryByNamedQuery("from VOrOrder where ID=? and orderType=1", new Object[]{orderID});

    }


}

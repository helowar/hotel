package com.mangocity.hotel.order.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.dao.IViewOrOrderItemDao;
import com.mangocity.hotel.order.persistence.VOrOrderItem;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
 * OrOrderItem Dao类
 * 
 * 
 */
public class ViewOrOrderItemDao  extends GenericDAOHibernateImpl implements IViewOrOrderItemDao{

    public void insertObject(VOrOrderItem obj) {
        super.save(obj);
    }

    public VOrOrderItem loadObject(Serializable objID) {
        return (VOrOrderItem) super.load(VOrOrderItem.class, objID);
    }

    public void updateObject(VOrOrderItem obj) {
        super.saveOrUpdate(obj);
    }


    /**
     * 命名查询
     */
    public List queryByNamedQuery(String queryID, Object[] params) {
        return super.queryByNamedQuery(queryID, params);
    }

    /**
     * 根据参数获取orderItemList
     * 
     * @param hotelId
     * @param data
     * @return
     */
    public List<VOrOrderItem> getViewOrderItem(Long hotelId, Date data) {
        return queryByNamedQuery("hQueryOrder_item", new Object[] { hotelId,
            data });
    }

    /**
     * 根据参数获取orderItemList
     * 
     * @param id
     * @param auditType
     */
    public List<VOrOrderItem> getViewOrderItems(Long selID, int typeID) {
        return queryByNamedQuery("hQueryOrder_items", new Object[] { selID,
            typeID });
    }

    /**
     * 根据参数获取orderItemList
     * 
     * @param orderid
     * @param auditType
     * 
     */
    public List<VOrOrderItem> getViewOrderItemTypes(Long selID, int typeID) {
        return queryByNamedQuery("hQueryOrder_itemType", new Object[] {
            selID, typeID });
    }
    
    
    /**
     * 根据参数获取orderItemList
     * 
     * @param hotelId
     * @param data
     *            *
     * @return
     */
    public List<VOrOrderItem> getOrderItemNum(Long hotelId, Date data, int roomIndex) {
        return queryByNamedQuery("hQueryOrder_ItemNum", new Object[] {
            hotelId, data, roomIndex });

    }

    
    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @return
     */
    public List<VOrOrderItem> getVOrderItem(Long orderId, Integer auditType2) {
        return queryByNamedQuery("hQueryOrder_itemAudit", new Object[] {
            orderId, auditType2 });

    }

    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @param date
     * @return
     */
    public List<VOrOrderItem> getVOrderItem1(Long orderId, Date data) {
       return queryByNamedQuery("hQueryOrder_itemDate", new Object[] {
            orderId, data });

    }

    
    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param auditId
     * @return
     */
    public List<VOrOrderItem> getViewOrderItem(Long hotelId, Date data_1, Date date) {
        return queryByNamedQuery("hQueryOrder_itemRoom", new Object[] {
            hotelId, data_1, date });

    }
    
    /**
     * 根据参数获得orOrderItrm对象
     * 
     * @param hotelId
     * @param date
     * @return
     */
    public List<VOrOrderItem> findReturnDailyAudit(Object hotelId, Date date) {
    	return queryByNamedQuery("hreturnODaily", new Object[] { hotelId,date });
    }
    
    
    /**
     * 根据参数获得orOrderItem对象
     * 
     * @param orderId
     * @param date
     * @return
     */

    public List findSimilarOrOrderItemTemp(Long orderId, Date date) {
        return queryByNamedQuery("hQueryOrder_ItemTemp", new Object[] {orderId, date });
    }

  

    public List findSimilarViewOrOrderShow(Long orderId, Date date) {

        return queryByNamedQuery("hQueryOrder_ItemShow", new Object[] {orderId, date });
    }
    
    
    /**
     * 修改未入住情况下的订单明细显示状态
     * 
     * @param orderId
     * @param date
     * @param roomIndex
     * @return
     */
    public List<VOrOrderItem> findSimilarViewOrOrderShow(Long orderId, Date date, int roomIndex) {

    	return queryByNamedQuery("hQueryOrder_ItemNotShow", new Object[] {orderId, date, roomIndex });
  }
    
    /**
     * 根据参数获得orOrderItem对象
     * 
     * @param orderId
     * @param date
     * @return
     */

    public List findSimilarOrOrderItem(Long orderId, Date date) {

       return queryByNamedQuery("hQueryOrder_ItemState", new Object[] {
            orderId, date });
  
    }
}

package com.mangocity.hotel.order.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.VOrOrderItem;

/**
 */
public interface IViewOrOrderItemDao {

	public void insertObject(VOrOrderItem obj);

    public VOrOrderItem loadObject(Serializable objID);

    public void updateObject(VOrOrderItem obj);

    /**
     * 命名查询
     */
    public List queryByNamedQuery(String queryID, Object[] params);

    /**
     * 根据参数获取orderItemList
     * 
     * @param hotelId
     * @param data
     * @return
     */
    public List<VOrOrderItem> getViewOrderItem(Long hotelId, Date data);

    /**
     * 根据参数获取orderItemList
     * 
     * @param id
     * @param auditType
     */
    public List<VOrOrderItem> getViewOrderItems(Long selID, int typeID);

    /**
     * 根据参数获取orderItemList
     * 
     * @param orderid
     * @param auditType
     * 
     */
    public List<VOrOrderItem> getViewOrderItemTypes(Long selID, int typeID);
    
    
    /**
     * 根据参数获取orderItemList
     * 
     * @param hotelId
     * @param data
     *            *
     * @return
     */
    public List<VOrOrderItem> getOrderItemNum(Long hotelId, Date data, int roomIndex);
    
    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @return
     */
    public List<VOrOrderItem> getVOrderItem(Long orderId, Integer auditType2);
    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param orderid
     * @param date
     * @return
     */
    public List<VOrOrderItem> getVOrderItem1(Long orderId, Date data);
    
    /**
     * 根据orderid主键获取orderItemList
     * 
     * @param auditId
     * @return
     */
    public List<VOrOrderItem> getViewOrderItem(Long hotelId, Date data_1, Date date);
    }

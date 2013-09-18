package com.mangocity.hotel.order.dao;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.order.persistence.VOrOrder;

/**
 */
public interface IViewOrOrderDao {

	public void insertObject(VOrOrder vorder);

    public VOrOrder loadOrder(Serializable orderID);

    public void updateOrder(VOrOrder order);

    /**
     * 根据ID获取VOrOrder(直接操作数据库)
     * @param orderID
     * @return
     */
    public VOrOrder findVOrOrder(Serializable orderID);
  

    /**
     * 命名查询
     */
    public List queryByNamedQuery(String queryID, Object[] params);
    /**
     * 根据参数获取orderItemList
     * 
     * @param id
     * @param orderType
     * 
     * 
     */

    public List<VOrOrder> getViewOrders(Long orderID, int orderType);
    /**
     * 获取VOrOrder
     * @param orderID
     * @return
     */
    public VOrOrder getVOrder(Long orderID);
}

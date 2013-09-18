package com.mangocity.hotel.order.service;

import com.mangocity.hotel.order.persistence.OrLockedOrders;

/**
 * 订单加解锁操作接口
 * 
 * @author neil
 * 
 */
public interface ILockedOrderService {

    public boolean insertLockedOrder(OrLockedOrders lockedOrder);

    public OrLockedOrders loadLockedOrder(OrLockedOrders lockedOrders);

    public int updateLockedOrder(OrLockedOrders lockedOrder);

    public int deleteLockedOrder(OrLockedOrders lockedOrder);

    public int deleteLockedOrders(String orders);

    public void replaceLockedOrders(OrLockedOrders oldOrder, OrLockedOrders newOrder);
}

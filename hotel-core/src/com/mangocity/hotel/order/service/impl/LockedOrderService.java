package com.mangocity.hotel.order.service.impl;

import java.io.Serializable;

import com.mangocity.hotel.order.dao.impl.OrLockedOrderDao;
import com.mangocity.hotel.order.persistence.OrLockedOrders;
import com.mangocity.hotel.order.service.ILockedOrderService;

/**
 * 订单加解锁操作接口实现
 * 
 * @author neil
 */
public class LockedOrderService implements ILockedOrderService, Serializable {

    private OrLockedOrderDao orLockedOrderDao;// parasoft-suppress SERIAL.NSFSC "暂不修改" 

    public boolean insertLockedOrder(OrLockedOrders lockedOrder) {
        return orLockedOrderDao.insertLockedOrder(lockedOrder);
    }

    public OrLockedOrders loadLockedOrder(OrLockedOrders lockedOrders) {
        return orLockedOrderDao.loadLockedOrder(lockedOrders);
    }

    public int deleteLockedOrder(OrLockedOrders lockedOrder) {
        return orLockedOrderDao.deleteLockedOrder(lockedOrder);
    }

    public int deleteLockedOrders(String orders) {
        return orLockedOrderDao.deleteLockedOrders(orders);
    }

    public int updateLockedOrder(OrLockedOrders lockedOrder) {
        return orLockedOrderDao.updateLockedOrder(lockedOrder);
    }

    public void setOrLockedOrderDao(OrLockedOrderDao orLockedOrderDao) {
        this.orLockedOrderDao = orLockedOrderDao;
    }

    public void replaceLockedOrders(OrLockedOrders oldOrder, OrLockedOrders newOrder) {
        orLockedOrderDao.deleteLockedOrder(oldOrder);
        orLockedOrderDao.insertLockedOrder(newOrder);
    }

}

package com.mangocity.hotel.order.dao.impl;

import com.mangocity.hotel.order.persistence.OrLockedOrders;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 * 订单加解锁操作
 * 
 * @author neil
 */
public class OrLockedOrderDao extends DAOIbatisImpl {
	private static final MyLog log = MyLog.getLogger(OrLockedOrderDao.class);
    public boolean insertLockedOrder(OrLockedOrders lockedOrder) {
        boolean isExistsOrder = false;
        try {
            super.save("insertLockedOrders", lockedOrder);
            isExistsOrder = true;
        } catch (Exception ex) {
            log.error(ex.getMessage(),ex);
        }
        return isExistsOrder;
    }

    public OrLockedOrders loadLockedOrder(OrLockedOrders lockedOrders) {
        return (OrLockedOrders) super.queryForObject("getLockedOrders", lockedOrders);
    }

    public int updateLockedOrder(OrLockedOrders lockedOrder) {
        return super.update("updateLockedOrders", lockedOrder);
    }

    public int deleteLockedOrder(OrLockedOrders lockedOrder) {
        return super.delete("deleteLockedOrder", lockedOrder);
    }

    public int deleteLockedOrders(String orders) {
        return super.delete("deleteLockedOrders", orders);
    }
}

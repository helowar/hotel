package com.mangocity.hotel.order.service.impl;

import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.HtlRoomstateCc;
import com.mangocity.hotel.order.service.IHotelRoomStateCC;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
 */
public class HotelRoomStateCCService extends DAOHibernateImpl implements IHotelRoomStateCC {
	
	private static final MyLog log = MyLog.getLogger(HotelRoomStateCCService.class);
	
    private OrOrderDao orOrderDao;

	public HtlRoomstateCc findRoomFulLog(Long RoomStateCCID) {
        return orOrderDao.get(HtlRoomstateCc.class, RoomStateCCID);
    }

    public void updateRoomFulLog(HtlRoomstateCc htlRoomstateCc) {
        orOrderDao.update(htlRoomstateCc);

    }

    public void setOrOrderDao(OrOrderDao orOrderDao) {
        this.orOrderDao = orOrderDao;
    }
}

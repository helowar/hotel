package com.mangocity.hotel.base.dao.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HtlRoomDao;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.util.dao.DAOHibernateImpl;
import com.mangocity.util.log.MyLog;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 
*@since 
*/
@SuppressWarnings("serial")
public class HtlRoomDaoImpl extends DAOHibernateImpl implements HtlRoomDao {

    @SuppressWarnings("unused")
    private static final MyLog log = MyLog.getLogger(OrOrderDao.class);
    
    
    @SuppressWarnings("unchecked")
    public List<HtlRoom>  getHtlRoomByRoomTypeId(Long roomTypeId, Date checkinDate, Date checkoutDate) {
        
        return this.queryByNamedQuery("findHtlRoomByRoomTypeId", new Object[] { checkinDate, checkoutDate, roomTypeId });
    }
    
    /**
     * by ting.li
     */
    @SuppressWarnings("unchecked")
    public List<HtlRoom>  getHtlRooms(Long hotelId,Long roomTypeId, Date checkinDate, Date checkoutDate) {
        
        return this.queryByNamedQuery("lstRoomsByRoomTypeId", new Object[] {hotelId,roomTypeId, checkinDate, checkoutDate});
    }
    
   
}

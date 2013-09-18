package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HotelRoomDAO;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 1.0
*@since 1.0
*/
public class HotelRoomDAOImpl extends GenericDAOHibernateImpl implements HotelRoomDAO
{
	public HtlRoom qryHtlRoomByRoomId(long roomId) {
		return this.get(HtlRoom.class, Long.valueOf(roomId));
	}

    public HtlRoom createRoomIfNotExist(Long roomTypeId, Long hotelId,
            Date ableSaleDate, Long contractId) {
        List<HtlRoom> lstRoom = this.queryByNamedQuery("findRoomByBizKey",
                new Object[] { roomTypeId, hotelId, ableSaleDate });
        if (lstRoom.isEmpty()) {

            return lstRoom.get(0);
        }
        
        HtlRoom room = new HtlRoom();
        room.setRoomTypeId(roomTypeId);
        room.setHotelId(hotelId);
        room.setAbleSaleDate(ableSaleDate);
        room.setLastAssureTime(BizRuleCheck.getDefaultLastAssureTime());
        room.setContractId(contractId.longValue());
        room.setHasFree(BizRuleCheck.getFalseString());
        this.save(room);

        return room;
    }
    
    public HtlRoom qryRoomByRoomTypeHotelIdSaleDate(long roomTypeId, long hotelId, Date ableSaleDate) {
    	String hql = "from HtlRoom room where room.roomTypeId = ? and room.hotelId = ? and room.ableSaleDate = ?";
        List<HtlRoom> lstRoom = super.query(hql, new Object[] { Long.valueOf(roomTypeId), Long.valueOf(hotelId), 
        		ableSaleDate });

        return lstRoom.isEmpty() ? null : lstRoom.get(0);
    }
    
    public List<HtlRoom> lstRooms(Long hotelId, Long roomTypeId, String beginDate,
            String endDate) {

        return this.queryByNamedQuery("lstRooms", new Object[] { hotelId,roomTypeId, 
                java.sql.Date.valueOf(beginDate), java.sql.Date.valueOf(endDate) });
    }
    
    public List<HtlRoom> qryRoomState(long hotelId, Date beginDate, Date endDate,
            String[] weeks, String[] roomTypes) {
        
        StringBuilder sb = new StringBuilder(100);
        String sWeek = BizRuleCheck.ArrayToString(weeks);
        String sRoomType = BizRuleCheck.ArrayToString(roomTypes);
        List<Object> paramList = new ArrayList<Object>(4);
        sb.append(" from HtlRoom room where room.hotelId = ?");
        paramList.add(hotelId);
        if (null != sWeek && !"".equals(sWeek)) {
            sb.append(" and room.week exist ( ? ) ");
            paramList.add(sWeek);
        }
        if (null != sRoomType && !"".equals(sRoomType)) {
            sb.append(" and room.roomTypeId exist ( ? ) ");
            paramList.add(sRoomType);
        }
        sb.append(" and room.ableSaleDate between ? and ? order  by room.ableSaleDate asc ");
        paramList.add(beginDate);
        paramList.add(endDate);
        
        return this.query(sb.toString(), paramList.toArray());
    }
    
    public List<HtlRoom> qryHtlRoomByRoomTypeSaleDateRange(long roomTypeID, Date beginDate, Date endDate) {        
    	String hql = "from HtlRoom where roomTypeId = ? and ableSaleDate >= ? and ableSaleDate < ? " +
    			"order by ableSaleDate ";
        
        return super.query(hql, new Object[]{Long.valueOf(roomTypeID), beginDate, endDate});
    }
    
    public List<HtlRoom> qryHtlRoomByHotelIdRoomType(long roomType, long hotelId, Date beginDate, Date endDate) {        
    	String hql = "from HtlRoom htlRoom where htlRoom.roomTypeId = ? and htlRoom.hotelId = ? " +
 					" and htlRoom.ableSaleDate between ? and ? order by htlRoom.ableSaleDate ";
        
        return super.query(hql, new Object[] {Long.valueOf(roomType), Long.valueOf(hotelId), beginDate, endDate });
    }
    
    public void updateHtlRoom(HtlRoom htlRoom) {
    	super.update(htlRoom);
    }

	public List<HtlRoom> getHtlRooms(Long hotelId, Long roomTypeId, Date checkinDate, Date checkoutDate) {
		String sql="select room.* from hwtemp_htl_room room where room.hotel_id=? and  room.room_type_id = ? and " +
				"room.able_sale_date >= ? and room.able_sale_date < ? ";
		Object[] params=new Object[]{hotelId,roomTypeId,checkinDate,checkoutDate};
		return super.queryByNativeSQL(sql, params, HtlRoom.class);
	}
}

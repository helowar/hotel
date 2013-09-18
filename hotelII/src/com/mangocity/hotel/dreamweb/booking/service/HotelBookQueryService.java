package com.mangocity.hotel.dreamweb.booking.service;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.hotel.order.persistence.OrOrder;

public class HotelBookQueryService implements IHotelBookQueryService {

	private OrOrderDao orOrderDao;

	/**
	 * @return the orOrderDao
	 */
	public OrOrderDao getOrOrderDao() {
		return orOrderDao;
	}

	/**
	 * @param orOrderDao
	 *            the orOrderDao to set
	 */
	public void setOrOrderDao(OrOrderDao orOrderDao) {
		this.orOrderDao = orOrderDao;
	}


	public String checkOrderDuplication(Long hotelId, Long roomTypeId,
			String linkMan, String linkPhone, Date checkInDate,
			Date checkOutDate) {
		String result = null;
		// TODO Auto-generated method stub
/*		String hql = " from OrOrder o"
				+ "where o.orderState not in (14, 16)"
				+ "and sysdate - o.createDate <= 1 / 2"
				+ "and o.hotelName = 'IT测试专用请勿下单在店'" 
				+"and o.roomTypeId='123'"
				+ "and o.linkMan = '侯滇滇'"
				+ "and o.mobile = '15013718134'"
				+ "where o.checkinDate = to_date('2012/03/15','yy/MM/dd')"
				+ "and o.checkoutDate = to_date('2012/03/16','yy/MM/dd')";*/
		String hql = "  from OrOrder o where o.orderState not in (1,14)  and sysdate - o.createDate <= 1 / 2 \n"
				+ " and o.hotelId = ? \n" 
				+ " and o.roomTypeId=? \n"
				+ " and o.linkMan =? \n"
				+ " and o.mobile = ? \n"
				+ " and o.checkinDate = ? \n"
				+ " and o.checkoutDate = ? \n";
		Object[] paramValues = new Object[] { hotelId, roomTypeId, linkMan,linkPhone, checkInDate, checkOutDate };
		List<OrOrder> orderList = orOrderDao.query(hql, paramValues);
		// result = hotelBookQueryDAO.checkOrderDuplication(hotelName,
		// roomTypeId, linkMan, linkPhone, checkInDate, checkOutDate);
		//List list = orderRecordDao.getAllOrderRecord();
		int roomQuantity = 0;
		int roomId = 0;
		if(orderList.size() >= 1){
			OrOrder order = orderList.get(0);
			roomQuantity = order.getRoomQuantity();
			roomId = Integer.parseInt(order.getRoomTypeId().toString());
		}		
		result = String.valueOf(roomId)+"|"+String.valueOf(roomQuantity);
		result = String.valueOf(roomQuantity);
		//System.out.println(result);
		return result;
	}

}

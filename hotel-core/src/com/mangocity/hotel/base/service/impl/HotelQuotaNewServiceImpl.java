package com.mangocity.hotel.base.service.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HotelQuotaNewDao;
import com.mangocity.hotel.base.persistence.HtlQuotaNew;
import com.mangocity.hotel.base.service.HotelQuotaNewService;

public class HotelQuotaNewServiceImpl implements HotelQuotaNewService {

	private HotelQuotaNewDao hotelQuotaNewDao;
	public List<HtlQuotaNew> queryQuotaByRoomTypeId(Long hotelId, Long roomTypeId, Date checkinDate, Date checkoutDate) {
		// TODO Auto-generated method stub
		return hotelQuotaNewDao.queryQuotaByRoomTypeId(hotelId, roomTypeId, checkinDate, checkoutDate);
	}
	public void setHotelQuotaNewDao(HotelQuotaNewDao hotelQuotaNewDao) {
		this.hotelQuotaNewDao = hotelQuotaNewDao;
	}


	
}

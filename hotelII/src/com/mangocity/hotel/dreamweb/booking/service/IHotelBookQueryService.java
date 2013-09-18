package com.mangocity.hotel.dreamweb.booking.service;

import java.util.Date;

public interface IHotelBookQueryService {
	
	/**
	 * QC3891	检查用户是否重复提交订单
	 * @param hotelName
	 * @param roomTypeId
	 * @param linkMan
	 * @param linkPhone
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	public String checkOrderDuplication(Long hotelId,Long roomTypeId,String linkMan,String linkPhone,Date checkInDate,Date checkOutDate);

}

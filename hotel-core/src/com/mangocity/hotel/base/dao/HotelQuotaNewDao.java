package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlQuotaNew;

public interface HotelQuotaNewDao {
	/**
	 * 根据酒店Id和床型Id查出所有的配额.add by ting.li
	 * @param hotelId
	 * @param roomTypeId
	 * @param checkIdDate
	 * @param checkoutDate
	 * @return
	 */
	public List<HtlQuotaNew> queryQuotaByRoomTypeId(Long hotelId,Long roomTypeId,Date checkinDate,Date checkoutDate);
}

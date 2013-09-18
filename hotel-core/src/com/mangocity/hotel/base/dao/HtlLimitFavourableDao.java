package com.mangocity.hotel.base.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlLimitFavourable;
import com.mangocity.hotel.base.persistence.HtlLimitFavourableHotel;

/**
 * 限量返现活动
 * @author xuyiwen
 *
 */
public interface HtlLimitFavourableDao {
	void saveHtlLimitFavourable(HtlLimitFavourable htlLimitFavourable);
	
	/**
	 * 查询酒店在一段时间内的返限活动
	 */
	public List<HtlLimitFavourableHotel> queryLimitFavourableHotel(String hotelIdList,Date beginDate,Date endDate);
	
	Boolean judgeLimitFav(Long hotelId,Date checkIn,Date checkOut);
	
	BigDecimal queryLimitFavRate(Long hotelId, Date date);

	void deleteFav(Long favId);

	HtlLimitFavourable queryLimitFav(Long favId);
	
	void updateFavActiviyToBeginOrEnd(Long favId,String status);
	
	String sumRoomNightAndReturnCash(Long favId);
	
	HtlLimitFavourable queryByFavId(Long favId);
	
	Boolean isHaveAnotherActivityBegin(Long favId);
}

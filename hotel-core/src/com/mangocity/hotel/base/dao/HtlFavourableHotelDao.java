package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlFavourableHotel;

public interface HtlFavourableHotelDao {
	void saveOrUpdateAll(List<HtlFavourableHotel> htlFavourableHotels);

	void delete(Long htlID, Long parID);

	void batchDelete(List<Long> hotelIdList, Long parID);
}

package com.mangocity.hotel.base.manage;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlFavourableHotel;

public interface HtlFavourableHotelManage {
	void saveOrUpdateAll(List<HtlFavourableHotel> htlFavourableHotels);

	void delete(Long htlID, Long parID);

	void batchDelete(List<Long> hotelIdList, Long parID); 
}

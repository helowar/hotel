package com.mangocity.hotel.dreamweb.search.service;

import java.util.List;

import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;

public interface HotelInfoService {
	public HotelBasicInfo queryHotelInfoByHotelId(String hotelId);

	public HotelBasicInfo queryHotelInfo(HotelBasicInfoSearchParam hotelBasicInfoSearchParam,
			String ipAddress);

	public List queryHotelInfoByMgis(String cityCode, String distanceName);
}

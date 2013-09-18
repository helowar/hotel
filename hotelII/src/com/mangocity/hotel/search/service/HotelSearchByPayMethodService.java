package com.mangocity.hotel.search.service;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.util.hotel.constant.PayMethod;

public interface HotelSearchByPayMethodService {
	public List<Long> queryHotelIdByMethod(Map<String,HotelBasicInfo> hotelBasicInfos,QueryHotelCondition queryHotelCondition,String payMethod);
		
	public Map<String,HotelBasicInfo> showHotelByPayMethod(Map<String,HotelBasicInfo> hotelBasicInfos,List<Long> prepayHotelIdList);
	
	public void setPrepayHotel(Map<String,HotelBasicInfo> hotelBasicInfos,String hotelIdsStr, QueryHotelCondition queryHotelCondition,String payMethod);

}

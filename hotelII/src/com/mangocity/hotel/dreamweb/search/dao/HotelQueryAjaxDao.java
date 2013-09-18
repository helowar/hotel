package com.mangocity.hotel.dreamweb.search.dao;

import java.util.List;

public interface HotelQueryAjaxDao {

	/**
	 * 模糊查询酒店名称
	 * @param hotelName
	 * @param cityCode
	 * @return
	 */
	public List autoHotelNameQuery(String hotelName, String cityCode);
	
	public List findMemberHotelCheckOut(String memberId);
}

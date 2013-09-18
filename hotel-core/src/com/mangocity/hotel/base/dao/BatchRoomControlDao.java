package com.mangocity.hotel.base.dao;

import java.util.List;
import java.util.Map;


public interface BatchRoomControlDao {
	List<Map> queryHotelRoomType(Long id);
}

package com.mangocity.hotel.base.dao;

import java.sql.SQLException;
import java.util.List;

import com.mangocity.hotel.base.service.assistant.HotelEmapResultInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;

public interface IHotelEmapDao {
	public List<HotelEmapResultInfo> queryHotelsByCondition(HotelQueryCondition condition) throws SQLException;
}

package com.mangocity.hotel.search.index;


import java.util.Date;

import com.mangocity.hotel.search.model.HotelBasicInfo;

public interface HotelInfoIndexer {
	
	/**
	 * 将所有酒店的基本信息保存到索引中
	 */
	public void createHotelInfoIndex();
	
	/**
	 * 根据酒店id相应更新lucene索引文件
	 * 
	 * @param hotelId
	 */
	public void updateHotelBasicInfoDoc(Long hotelId);
	
	/**
	 * 根据地于是位置的ID更新酒店的地理位置距离LUCENE索引文件
	 * @param valueOf
	 */
	public void updateMgisInfoDoc(Long geoId);
}

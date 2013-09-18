package com.mangocity.hotel.base.dao;

import java.util.List;

public interface EpOrderDAO {
   
	/**
	 * @ 查找Ep 酒店
	 * @return List<String>
	 * 
	 */
	
	public List<String> queryEpHotelId();
	
	
	/**
	 * @ 保存Ep 酒店订单信息
	 * @param List<hotelId>
	 */
	
	public void saveEpOrderData(List<String> hotelList);
}

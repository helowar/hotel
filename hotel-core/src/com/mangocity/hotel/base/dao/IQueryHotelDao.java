package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;

/**
 * 用ibatis来查询酒店相关信息
 * @author zuoshengwei
 *
 */
public interface IQueryHotelDao {
	
	 /**
     * 查询会员的历史入住酒店 
     * @param queryID
     * @param memeCd
     * @return
     */
     public List<Object[]> queryMyHotel(String queryID, String memeCd); 
     
     /**
 	 * 根据输入的查询条件，查询酒店的房型，价格信息
 	 * @param hotelQueryCondition
 	 * @return
 	 */
     public List<Object[]> queryHotelByCCWithPrice(HotelQueryCondition hotelQueryCondition);
     
     /***
      * 根据输入的查询条件，查询酒店的基本信息
      * @param hotelQueryCondition
      * @return
      */
     public List<Object[]> queryHotelByCCWithoutPrice(HotelQueryCondition hotelQueryCondition);
     
}

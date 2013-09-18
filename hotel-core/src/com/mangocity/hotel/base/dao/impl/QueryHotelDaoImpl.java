package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IQueryHotelDao;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.util.dao.DAOIbatisImpl;


/**
 * 
 * @author zuoshengwei
 *
 */

public class QueryHotelDaoImpl extends DAOIbatisImpl implements IQueryHotelDao {
	

	public List<Object[]> queryHotelByCCWithPrice(HotelQueryCondition hotelQueryCondition) {
		
			return super.getSqlMapClientTemplate().queryForList("queryHotelByCCWithPrice",hotelQueryCondition);
	        
	 }


	public List<Object[]> queryHotelByCCWithoutPrice(HotelQueryCondition hotelQueryCondition) {
		
	        Map<String,Object> params = new HashMap<String,Object>();
	        
	        params.put("cityId", hotelQueryCondition.getCityId());
	        
	        params.put("bizZone", hotelQueryCondition.getBizZone());
	        
	        params.put("chnAddress", hotelQueryCondition.getChnAddress());
	        
	        return super.queryByPagination("queryHotelByCCWithoutPrice", params, hotelQueryCondition.getPageSize());
	        
	 }
   
    public List<Object[]> queryMyHotel(String queryID, String membercd) {
    	
    	return super.getSqlMapClientTemplate().queryForList(queryID, membercd);
         
        
    }
    
}

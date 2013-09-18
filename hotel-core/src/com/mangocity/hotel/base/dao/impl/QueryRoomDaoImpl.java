package com.mangocity.hotel.base.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.mangocity.hotel.base.dao.IQueryRoomDao;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.util.dao.DAOIbatisImpl;

/**
 * 
 * @author zuoshengwei
 *
 */
public class QueryRoomDaoImpl extends DAOIbatisImpl implements IQueryRoomDao {
	
  
    public HtlRoom findRoomByBizKey(Long roomTypeId, Long hotelId, String ableSaleDate) {
    	
        Map<String,Object> params = new HashMap<String,Object>();
        
        params.put("ableSaleDate", ableSaleDate);
        
        params.put("roomTypeId", roomTypeId);
        
        params.put("hotelId", hotelId);
        
        return (HtlRoom) super.getSqlMapClientTemplate().queryForObject("findRoomByBizKey", params);
    
    }
}

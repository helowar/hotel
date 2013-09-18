package com.mangocity.hotel.base.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IQueryPriceDao;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.util.dao.DAOIbatisImpl;

/**
 * @author zuoshengwei
 *
 */

public class QueryPriceDaoImpl extends DAOIbatisImpl implements IQueryPriceDao {

    public HtlPrice findPriceByBizKey(Long roomId, int memberTypeId) {
    	
        Map<String,Object> params = new HashMap<String,Object>();
        
        params.put("roomId", roomId);
        
        params.put("memberTypeId", memberTypeId);
        
        return (HtlPrice) super.getSqlMapClientTemplate().queryForObject("findPriceByBizKey",params);
 
    }
   
    public List<Object[]> queryPriceHistory(String queryID, Map<String,Object> map) {
    	
    	return super.queryForList(queryID, map);
    
    }
    
}

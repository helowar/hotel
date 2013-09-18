package com.mangocity.hotel.base.dao;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlPrice;


/**
 * 用Ibatis来查询价格相关信息
 * @author zuoshengwei
 *
 */
public interface IQueryPriceDao {
 
	/**
	 * 根据会员类型查询指定房间的价格信息
	 * @param roomId
	 * @param memberTypeId
	 * @return
	 */
    public HtlPrice findPriceByBizKey(Long roomId, int memberTypeId);
    
    /**
     * 查询价格的历史记录
     * @param queryID
     * @param map
     * @return
     */
    public List<Object[]> queryPriceHistory(String queryID, Map<String,Object> map);
  
}

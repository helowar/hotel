package com.mangocity.hotel.order.manager;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.order.dao.impl.OrOrderDao;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

/**
 * 中台Manager
 * 
 * @author chenkeming
 */
public class HraManager {
	
	private static final MyLog log = MyLog.getLogger(HraManager.class);
	
    /**
     * 订单dao
     */
    private OrOrderDao orOrderDao;    
    
    /**
     * ibatis查询dao
     */
    private DAOIbatisImpl queryDao;  
    
    /**
     * 获取工作效率及订单实时监控数据
     * chenjuesu editAt 2010-3-24 下午04:11:32
     * @param params 
     * @return
     */
    public List getWorkingRateAndOrderStations(Map params){
    	//更新今天的数据
    	Object optType = params.get("optType");
    	List result = null;
    	if ("1".equals(optType)) {
			orOrderDao.getWorkingRateAndOrderStations();
			// 取出查询的记录
			result = queryDao.queryForList("queryWorkingRates", params);
		} else {
			result = queryDao.queryForList("queryMyWorkingRates", params);
		}
    	
    	return result;
    }
    
    /**
     * 异步获取工作效率数据
     * chenjuesu editAt 2010-4-9 上午09:49:08
     * @param optType　获取类型　１：组别　２：人员
     * @param params　　查询参数
     * @return
     */
    public List getWorkingRateAndOrderStationsByAjax(int optType,
			Map<String, String> params) {
		List result = null;
		if (1 == optType)
			result = orOrderDao.getWorkingRateAndOrderStationsByAjax(params);
		else if (2 == optType) {
			result = queryDao.queryForList("queryMyWorkingRatesByAjax", params);
		} else {
			result = queryDao.queryForList("queryMyWorkingRatesTopByAjax",
					params);
		}
		return result;
	}
    
    /**
     * 
     * 获取某个工号的工作效率数据
     * 
     * @param loginName
     * @return
     */
    public List getMyWorkingRateByLoginName(String loginName){
    	return orOrderDao.getMyWorkingRateByLoginName(loginName);
    }    
    
    /** getter and setter begin */

    public void setOrOrderDao(OrOrderDao orOrderDao) {
        this.orOrderDao = orOrderDao;
    }
	
	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

	/** getter and setter end */
}

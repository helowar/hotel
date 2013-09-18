package com.mangocity.hotel.base.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HotelBrandinfo;
import com.mangocity.hotel.base.persistence.QueryCashBackControl;
import com.mangocity.hotel.base.service.HotelBrandQueryService;
import com.mangocity.util.dao.DAOIbatisImpl;
import com.mangocity.util.log.MyLog;

public class HotelBrandQueryServiceImpl implements HotelBrandQueryService {
	private static final MyLog log = MyLog.getLogger(HotelBrandQueryServiceImpl.class);
    /**
     * ibatis查询dao
     */
	private DAOIbatisImpl queryDao;
	
	public List<HotelBrandinfo> queryHotelBrands() {

		List list = null;
    	Map<String, Object> paramMap = new HashMap<String, Object>();

		//1,开始查询
		try{
			list= queryDao.queryForList("selectHotelBrandfoAll", paramMap);
			if(list == null || list.size()==0 )
			{
				log.info("没有查找到相关品牌信息");
				return null;
			}
			log.info("品牌查询数量："+list.size());
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public List<HotelBrandinfo> queryHotelBybrands(String citycode, String brand) {
		// TODO Auto-generated method stub
		List list = null;
    	Map<String, Object> paramMap = new HashMap<String, Object>();
    	paramMap.put("brandname", brand);
    	paramMap.put("citycode", citycode);
		//1,开始查询
		try{
			list= queryDao.queryForList("selectHotelBrandByName", paramMap);
			if(list == null || list.size()==0 )
			{
				log.info("没有查找到相关品牌信息");
				return null;
			}
			log.info("品牌查询数量："+list.size());
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public DAOIbatisImpl getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(DAOIbatisImpl queryDao) {
		this.queryDao = queryDao;
	}

}

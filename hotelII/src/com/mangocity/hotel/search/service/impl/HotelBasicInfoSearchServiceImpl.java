package com.mangocity.hotel.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.search.dao.HotelQueryDao;
import com.mangocity.hotel.search.model.HotelCurrentlyBookstate;
import com.mangocity.hotel.search.model.HotelCurrentlySatisfyQuery;
import com.mangocity.hotel.search.model.QueryCommodityInfo;
import com.mangocity.hotel.search.model.QueryDynamicCondition;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.HotelBasicInfoSearchService;
import com.mangocity.hotel.search.service.assistant.HotelInfo;
import com.mangocity.util.StringUtil;


public class HotelBasicInfoSearchServiceImpl implements HotelBasicInfoSearchService {
	private HotelQueryDao hotelQueryDao;
	
	/**
	 * 根据查询条件得到酒店静态信息,实际上是LuceneHotelSearchService.searchHotelBasicInfo
	 * @return
	 */
	public List<HotelInfo> searchstaticInfo(QueryHotelCondition queryHotelCondition){
		return null;
	}
	
	/**
	 * 查询动态信息,早餐或价格过滤
	 * @param queryHotelCondition
	 * @return
	 */
	public List<HotelInfo> searchDynamicInfo(QueryHotelCondition queryHotelCondition){
		return null;
	}
	
	
	/**
	 * 包括静动态信息查询
	 * @param queryHotelCondition
	 * @return
	 */
	public List<HotelInfo> searchHotelBasicInfo(QueryHotelCondition queryHotelCondition){
		return null;
	}
	
	
	/**
	 * 过滤掉不满足早餐和价格的商品信息
	 * @return
	 */
	public String filterDynamicinfoGetIdLst(QueryDynamicCondition  qcc){
		String hotelIds=qcc.getHotelIdLst();
		if(StringUtil.isValidStr(hotelIds)){
			return hotelQueryDao.queryHotelDynamicinfoGetStr(qcc);
		}
		else{
			return "";
		}
		
	}
	/**
	 * 过滤掉不满足早餐和价格的商品信息(用于扩展)
	 * @return
	 */
	public List<HotelCurrentlySatisfyQuery> filterDynamicinfoGetBean(QueryDynamicCondition  qcc){
		return dynamicinfoLogic(hotelQueryDao.queryHotelDynamicinfo(qcc));
	}
	
	/**
	 * 是否满足动态信息查询条件(考虑扩展)
	 * @param commMap
	 * @return
	 */
	public List<HotelCurrentlySatisfyQuery>  dynamicinfoLogic(Map<Long,List<QueryCommodityInfo>> commMap){
 
    	boolean curhasBreakfast = false;
    	boolean cursatisfyPrice = false;
    	List<QueryCommodityInfo> qcLst = null;
    	List<HotelCurrentlySatisfyQuery> hcbLst = new ArrayList<HotelCurrentlySatisfyQuery>();

    	for(Map.Entry<Long,List<QueryCommodityInfo>> mapEntry:commMap.entrySet()){
    		qcLst = mapEntry.getValue();
    		curhasBreakfast = isHasBreakfast(qcLst);
    		cursatisfyPrice = satisfyPrice(qcLst);
    		
    		HotelCurrentlySatisfyQuery hc = new HotelCurrentlySatisfyQuery();
    		hc.setHotelId(mapEntry.getKey().longValue());
    		hc.setCanSatisfyBreakfast(curhasBreakfast);
    		hc.setCanSatisfyPrice(cursatisfyPrice);
    		hcbLst.add(hc);
    	}
    		return hcbLst;
    	}
    	
    	/**
    	 * 判断是否满足早餐条件(考虑扩展)
    	 * @param qcLst
    	 * @return
    	 */
    	public boolean isHasBreakfast(List<QueryCommodityInfo> qcLst){
    		return true; 
    	}
    	/**
    	 * 判断是否满足价格查询条件逻辑(考虑扩展)
    	 * @param qcLst
    	 * @param type
    	 * @return
    	 */
    	public boolean satisfyPrice(List<QueryCommodityInfo> qcLst){
    		return true;
    	}
	public HotelQueryDao getHotelQueryDao() {
		return hotelQueryDao;
	}
	public void setHotelQueryDao(HotelQueryDao hotelQueryDao) {
		this.hotelQueryDao = hotelQueryDao;
	}
	
	
}

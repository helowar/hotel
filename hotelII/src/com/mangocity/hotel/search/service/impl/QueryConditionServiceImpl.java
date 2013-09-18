package com.mangocity.hotel.search.service.impl;

import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlGeoDistance;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.QueryDynamicCondition;
import com.mangocity.hotel.search.model.QueryHotelCondition;
import com.mangocity.hotel.search.service.QueryConditionService;
import com.mangocity.hotel.search.service.assistant.SpecialRequest;
import com.mangocity.hotel.search.service.assistant.SpecialRequestDividation;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;


public class QueryConditionServiceImpl implements QueryConditionService {

	
	/**
	 * 组装静态查询条件(用于过滤静态信息)
	 * @param queryHotelCondition
	 * @return
	 */
	public HotelBasicInfoSearchParam fitQueryStaticCondition(QueryHotelCondition queryHotelCondition){
		HotelBasicInfoSearchParam hotelBasicInfoSearchParam = new HotelBasicInfoSearchParam();
		MyBeanUtil.copyProperties(hotelBasicInfoSearchParam,queryHotelCondition);
		SpecialRequest sr = SpecialRequestDividation.divideSpecialRequest(queryHotelCondition.getSpecialRequestString());
		hotelBasicInfoSearchParam.setSpecialRequest(sr);
		
		// 转化地理位置查询条件
		HtlGeographicalposition htlGeoPos = null;
		String geoId = queryHotelCondition.getGeoId();		
		if(StringUtil.isValidStr(geoId)) {
			htlGeoPos = new HtlGeographicalposition();
			htlGeoPos.setID(Long.valueOf(geoId));
		} else {
			String geoName  = queryHotelCondition.getGeoName();
			if(StringUtil.isValidStr(geoName)) {
				htlGeoPos = new HtlGeographicalposition();
				htlGeoPos.setName(geoName);	
			}
		}
		if(null != htlGeoPos) {
			htlGeoPos.setCityCode(queryHotelCondition.getCityCode());
			hotelBasicInfoSearchParam.setHtlGeographicalposition(htlGeoPos);
		}
		
		return hotelBasicInfoSearchParam;
	}
	
	/**
	 * 组装动态查询条件(用于过滤动态信息) 
	 * @param hotelBasicInfoSearchParam
	 * @param hotelBasicInfos
	 * @return
	 * @author 
	 */
	public QueryDynamicCondition fitQueryDynamicCondition(HotelBasicInfoSearchParam hotelBasicInfoSearchParam,Map<String, HotelBasicInfo> hotelBasicInfos){
		if(hotelBasicInfoSearchParam == null ){return null;}
		QueryDynamicCondition queryCon = new QueryDynamicCondition();
		MyBeanUtil.copyProperties(queryCon, hotelBasicInfoSearchParam);
		
		boolean dynamicCondition = false;
		
		SpecialRequest specialRequest = hotelBasicInfoSearchParam.getSpecialRequest();
		
		// 设置是否含早
		if (specialRequest != null && specialRequest.isContainBreakfast()) {
			queryCon.setContainBreakfast(true);
			dynamicCondition = true;
		}
		
		// 是否含免费宽带
		if (specialRequest != null && specialRequest.isContainBroadBand()) {
			queryCon.setHasFreeNet(true);
			dynamicCondition = true;
		}
		
		//设置价格区间
		double minPrice = StringUtil.getStrTodouble(hotelBasicInfoSearchParam.getMinPrice());
		double maxPrice = StringUtil.getStrTodouble(hotelBasicInfoSearchParam.getMaxPrice());
		if(minPrice>0){
			dynamicCondition = true;
			queryCon.setMinPrice(minPrice);
		}
		if(maxPrice>0&& maxPrice>=minPrice){
			dynamicCondition = true;
			queryCon.setMaxPrice(maxPrice);
		}
		
		if(dynamicCondition==false)return null;//不需要查询动态信息
		//组装从lucene来的hotelId
		String hotelIdLst = fitHotelLstFromHotelInfos(hotelBasicInfos);
		queryCon.setHotelIdLst(hotelIdLst);
		return queryCon;
	}
	
	
	
	
	/**
	 * 组装商品查询条件(用于查询商品信息) 
	 * @param hotelBasicInfoSearchParam
	 * @param hotelBasicInfos
	 * @return
	 * @author 
	 */
	public QueryCommodityCondition fitQueryCommodityCondition(HotelBasicInfoSearchParam hotelBasicInfoSearchParam,Map<String, HotelBasicInfo> hotelBasicInfos){
		if(hotelBasicInfoSearchParam == null ){return null;}
		QueryCommodityCondition queryCon = new QueryCommodityCondition();
		MyBeanUtil.copyProperties(queryCon, hotelBasicInfoSearchParam);
		
		//设置是否含早
		if(hotelBasicInfoSearchParam.getSpecialRequest()!=null&&hotelBasicInfoSearchParam.getSpecialRequest().isContainBreakfast()){
		      queryCon.setContainBreakfast(true);
		}
		
		//设置价格区间
		double minPrice = StringUtil.getStrTodouble(hotelBasicInfoSearchParam.getMinPrice());
		double maxPrice = StringUtil.getStrTodouble(hotelBasicInfoSearchParam.getMaxPrice());
		if(minPrice>0){
			queryCon.setMinPrice(minPrice);
		}
		if(maxPrice>0&& maxPrice>=minPrice){
			queryCon.setMaxPrice(maxPrice);
		}
		
		//组装从lucene来的hotelId
		String hotelIdLst = fitHotelLstFromHotelInfos(hotelBasicInfos);
		queryCon.setHotelIdLst(hotelIdLst);
		return queryCon;
	}
	/**
	 * 组装hotelIdLst,将查询过滤结果转化成hotelIdLst
	 * @param hotelBasicInfos
	 * @return
	 * @author 
	 */
	public String fitHotelLstFromHotelInfos(Map<String, HotelBasicInfo> hotelBasicInfos){
		if(null == hotelBasicInfos  || hotelBasicInfos.isEmpty()){
	    	return new String();
	    }
		StringBuffer hotelIdLstBf = new StringBuffer();
		for (Map.Entry<String, HotelBasicInfo> strEntry: hotelBasicInfos.entrySet()) { 	
		       String hotelId = strEntry.getKey(); 
		       hotelIdLstBf.append(hotelId+",");
		}
	      String hotelIdLst = hotelIdLstBf.toString();
	      // 去掉最后一个逗号
	      hotelIdLst = StringUtil.deleteLastChar(hotelIdLst,',');
	     return hotelIdLst;
	}
}

package com.mangocity.hotel.search.searchengine.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

import com.mangocity.hotel.search.index.HotelInfoIndexConstants;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.searchengine.service.BooleanQueryBuilder;
import com.mangocity.hotel.search.service.assistant.SpecialRequest;
import com.mangocity.util.DateUtil;

/**
 * 
 * 基本信息查询的Query builder
 * 
 * @author chenkeming
 *
 */
public class BasicInfoQueryBuilder implements BooleanQueryBuilder {
	
	public BooleanQuery buildQuery(HotelBasicInfoSearchParam hotelBasicInfoSearchParam) {

		BooleanQuery bq = new BooleanQuery();
		
		// 如果有hotelId条件
		if (null != hotelBasicInfoSearchParam.getHotelId()
				&& 0 < hotelBasicInfoSearchParam.getHotelId().trim().length()) {
			Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ID,
					hotelBasicInfoSearchParam.getHotelId());
			TermQuery hotelIdQuery = new TermQuery(term);
			bq.add(hotelIdQuery, BooleanClause.Occur.MUST);
			return bq;
		}
		
		// 增加酒店所在城市（代码）的查询条件
		if (hotelBasicInfoSearchParam.getCityCode() != null
				&& !hotelBasicInfoSearchParam.getCityCode().trim().equals("")) {
			Term term = new Term(
					HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CITY_ID,
					hotelBasicInfoSearchParam.getCityCode());
			TermQuery cityCodeQuery = new TermQuery(term);
			bq.add(cityCodeQuery, BooleanClause.Occur.MUST);
		}
		// 如果有hotelIds条件 add by diandian.hou
		if(null!=hotelBasicInfoSearchParam.getHotelIdsStr()
				&& 0 < hotelBasicInfoSearchParam.getHotelIdsStr().trim().length()){
			String[] hotelIds = hotelBasicInfoSearchParam.getHotelIdsStr().split(",");
			BooleanQuery hotelIdsQuery = new BooleanQuery();
			for(int i = 0; i<hotelIds.length;i++){
				Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ID,hotelIds[i]);
				WildcardQuery hotelQuery = new WildcardQuery(term);
				hotelIdsQuery.add(hotelQuery,BooleanClause.Occur.SHOULD);
			}
			bq.add(hotelIdsQuery, BooleanClause.Occur.MUST);
			return bq;
		}
		
		//添加酒店品牌查询 add by diandian.hou 2011-10-13
		if(null != hotelBasicInfoSearchParam.getHotelBrand()){
			Term term = new Term(
					HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BRAND,
					hotelBasicInfoSearchParam.getHotelBrand());
			TermQuery hotelBrandQuery = new TermQuery(term);
			bq.add(hotelBrandQuery, BooleanClause.Occur.MUST);
			return bq;
		}
		
		
	 	
		// 增加酒店所在城市行政区（代码）的查询条件
		if (hotelBasicInfoSearchParam.getDistrict() != null
				&& !hotelBasicInfoSearchParam.getDistrict().trim().equals("")) {
			Term term = new Term(
					HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ZONE, "*"
							+ hotelBasicInfoSearchParam.getDistrict()
							+ "*");
//			TermQuery districtCodeQuery = new TermQuery(term);
			WildcardQuery districtCodeQuery = new WildcardQuery(term);
			bq.add(districtCodeQuery, BooleanClause.Occur.MUST);
		}
		// 增加酒店所属商业区（代码）的查询条件（如果有多个商业区，则按“或”查询）
		if (hotelBasicInfoSearchParam.getBizZone() != null
				&& !hotelBasicInfoSearchParam.getBizZone().trim().equals("")) {
			String[] bizZones = hotelBasicInfoSearchParam.getBizZone().split(
					",");
			if (bizZones.length == 1) {
				Term term = new Term(
						HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BIZ_ZONE,
						bizZones[0]);
//				TermQuery bizZoneQuery = new TermQuery(term);
				WildcardQuery bizZoneQuery = new WildcardQuery(term);
				bq.add(bizZoneQuery, BooleanClause.Occur.MUST);
			} else {
				BooleanQuery bizZonesQuery = new BooleanQuery();
				for (String bizZone : bizZones) {
					Term term = new Term(
							HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BIZ_ZONE,
							bizZone);
//					TermQuery bizZoneQuery = new TermQuery(term);
					WildcardQuery bizZoneQuery = new WildcardQuery(term);
					bizZonesQuery.add(bizZoneQuery, BooleanClause.Occur.SHOULD);
				}
				bq.add(bizZonesQuery, BooleanClause.Occur.MUST);
			}
		}
		// 增加酒店名称的查询条件（模糊查询）
		if (hotelBasicInfoSearchParam.getHotelName() != null
				&& !hotelBasicInfoSearchParam.getHotelName().trim().equals("")) {
			Term term = new Term(
					HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CHINESE_NAME, "*"
							+ hotelBasicInfoSearchParam.getHotelName() + "*");
			WildcardQuery hotelNameQuery = new WildcardQuery(term);
			bq.add(hotelNameQuery, BooleanClause.Occur.MUST);
		}
		// 增加酒店星级（代码）的查询条件（如果有多个星级， 则按“或”查询）
		if (hotelBasicInfoSearchParam.getStarLeval() != null
				&& !hotelBasicInfoSearchParam.getStarLeval().trim().equals("")) {
			String[] starLevels = hotelBasicInfoSearchParam.getStarLeval()
					.split(",");
			if (starLevels.length == 1) {				
				Term term = new Term(
						HotelInfoIndexConstants.INDEX_FIELD_HOTEL_STAR_LEVEL,
						"*" + starLevels[0] + "*");
//				TermQuery starLevelQuery = new TermQuery(term);
				WildcardQuery starLevelQuery = new WildcardQuery(term);
				bq.add(starLevelQuery, BooleanClause.Occur.MUST);
			} else {
				BooleanQuery starLevelsQuery = new BooleanQuery();
				for (String starLevel : starLevels) {
					Term term = new Term(
							HotelInfoIndexConstants.INDEX_FIELD_HOTEL_STAR_LEVEL,
							"*" + starLevel + "*");
//					TermQuery starLevelQuery = new TermQuery(term);
					WildcardQuery starLevelQuery = new WildcardQuery(term);
					starLevelsQuery.add(starLevelQuery,
							BooleanClause.Occur.SHOULD);
				}
				bq.add(starLevelsQuery, BooleanClause.Occur.MUST);
			}
		}
		// 增加酒店类型的查询条件（如果有多个酒店类型，则按“或”查询）
		if (hotelBasicInfoSearchParam.getHotelType() != null
				&& !hotelBasicInfoSearchParam.getHotelType().trim().equals("")) {
			String[] hotelTypes = hotelBasicInfoSearchParam.getHotelType()
					.split(",");
			if (hotelTypes.length == 1) {
				Term term = new Term(
						HotelInfoIndexConstants.INDEX_FIELD_HOTEL_TYPE,
						"*" + hotelTypes[0] + "*");
//				TermQuery hotelTypeQuery = new TermQuery(term);
				WildcardQuery hotelTypeQuery = new WildcardQuery(term);
				bq.add(hotelTypeQuery, BooleanClause.Occur.MUST);
			} else {
				BooleanQuery hotelTypesQuery = new BooleanQuery();
				for (String hotelType : hotelTypes) {
					Term term = new Term(
							HotelInfoIndexConstants.INDEX_FIELD_HOTEL_TYPE,
							"*" + hotelType + "*");
//					TermQuery hotelTypeQuery = new TermQuery(term);
					WildcardQuery hotelTypeQuery = new WildcardQuery(term);
					hotelTypesQuery.add(hotelTypeQuery,
							BooleanClause.Occur.SHOULD);
				}
				bq.add(hotelTypesQuery, BooleanClause.Occur.MUST);
			}
		}
		
		//酒店品牌
		// 增加酒店所在城市（代码）的查询条件
		if (hotelBasicInfoSearchParam.getHotelGroup() != null
				&& !hotelBasicInfoSearchParam.getHotelGroup().trim().equals("")) {
			Term term = new Term(
					HotelInfoIndexConstants.INDEX_FIELD_HOTEL_GROUP,
					hotelBasicInfoSearchParam.getHotelGroup());
			TermQuery hotelGroupQuery = new TermQuery(term);
			bq.add(hotelGroupQuery, BooleanClause.Occur.MUST);
		}
		
		//特殊要求
		SpecialRequest specialRequest=hotelBasicInfoSearchParam.getSpecialRequest();
		if(specialRequest!=null){
		List<String> freeSerivceList = specialRequest.getFreeService();
		// 增加酒店所提供的免费服务的查询条件（如果有多项免费服务，则按“和”查询）
		 if (freeSerivceList != null && !freeSerivceList.isEmpty()) {
			BooleanQuery freeServicesQuery = new BooleanQuery();
			for (String freeService : freeSerivceList) {
				StringBuilder termStr = new StringBuilder(freeService);
				Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FREE_SERVICE,termStr.toString());
				WildcardQuery freeServiceQuery = new WildcardQuery(term);
				freeServicesQuery.add(freeServiceQuery,BooleanClause.Occur.MUST);
			}
			bq.add(freeServicesQuery, BooleanClause.Occur.MUST);
		}
	
		 
		//添加餐饮休闲设施（按“和”查询）
		List<String>  mealFixtrueList = specialRequest.getListMealFixtrue();
		 if (mealFixtrueList != null && !mealFixtrueList.isEmpty()) {
				BooleanQuery mealFixtruesQuery = new BooleanQuery();
				for (String mealFixtrue : mealFixtrueList) {
					StringBuilder termStr = new StringBuilder(mealFixtrue);
					Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_MEAL_FIXTRUE,termStr.toString());
					WildcardQuery mealFixtrueQuery = new WildcardQuery(term);					
					mealFixtruesQuery.add(mealFixtrueQuery,BooleanClause.Occur.MUST);
				}
				bq.add(mealFixtruesQuery, BooleanClause.Occur.MUST);
			}
		 //添加酒店客房设施（按"和"查询）
		List<String> roomFixtureList = specialRequest.getListRoomEquipment();
		if (roomFixtureList != null && !roomFixtureList.isEmpty()) {
			BooleanQuery roomFixturesQuery = new BooleanQuery();
			for (String roomFixture : roomFixtureList) {
				StringBuilder termStr = new StringBuilder(roomFixture);
				Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ROOM_FIXTRUE,termStr.toString());
				WildcardQuery roomFixtureQuery = new WildcardQuery(term);
				roomFixturesQuery.add(roomFixtureQuery,BooleanClause.Occur.MUST);
			}
			bq.add(roomFixturesQuery, BooleanClause.Occur.MUST);
		}
		 
		 //添加是否最近开业装修
		 boolean recentlyOpenedAndFit = specialRequest.isRecentlyOpenedAndFit();
		if (recentlyOpenedAndFit) {
			Date currencyDate = new Date();
			// format like '20110401
			BooleanQuery recentlyOpenedAndFitQuery = new BooleanQuery();
			Integer dateNum = Integer.valueOf(DateUtil.dateToStringNew(currencyDate));
			NumericRangeQuery<Integer> praciceQuery = NumericRangeQuery.newIntRange(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_PRACICE_DATE,
					dateNum-10000,dateNum,true,true);
			NumericRangeQuery<Integer> fitQuery = NumericRangeQuery.newIntRange(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FITMENT_DATE,
					dateNum-10000,dateNum,true,true);
			recentlyOpenedAndFitQuery.add(praciceQuery, BooleanClause.Occur.SHOULD);
			recentlyOpenedAndFitQuery.add(fitQuery,BooleanClause.Occur.SHOULD);
			bq.add(recentlyOpenedAndFitQuery,BooleanClause.Occur.MUST);
		}
		} 
		return bq;
	}
	
}

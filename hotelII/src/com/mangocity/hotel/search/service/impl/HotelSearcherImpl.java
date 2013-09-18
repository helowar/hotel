package com.mangocity.hotel.search.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.search.index.HotelInfoIndexConstants;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.HotelBasicInfoSearchParam;
import com.mangocity.hotel.search.model.RoomType;
import com.mangocity.hotel.search.searchengine.service.BooleanQueryBuilder;
import com.mangocity.hotel.search.searchengine.service.IndexSearcherService;
import com.mangocity.hotel.search.searchengine.service.ResultProcessor;
import com.mangocity.hotel.search.searchengine.service.impl.BasicInfoQueryBuilder;
import com.mangocity.hotel.search.service.HotelSearcher;
import com.mangocity.util.DateUtil;
import com.mangocity.util.log.MyLog;

public class HotelSearcherImpl implements HotelSearcher {
	
	private static final MyLog log = MyLog.getLogger(HotelSearcherImpl.class);
	
	private IndexSearcherService indexSearcherService;
	
	// query 构造器
	private static final BooleanQueryBuilder basicQueryBuilder = new BasicInfoQueryBuilder();
	
	private static final ResultProcessor<HotelBasicInfo> basicInfoResultProcessor = new ResultProcessor<HotelBasicInfo>(){		
		public HotelBasicInfo processResult(Document doc){
			HotelBasicInfo hotelBasicInfo = new HotelBasicInfo();
			hotelBasicInfo.setHotelId(Integer.parseInt(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ID)));
			hotelBasicInfo.setChnName(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CHINESE_NAME));
			hotelBasicInfo.setEngName(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ENGLISH_NAME));
			hotelBasicInfo.setCityId(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CITY_ID));
			hotelBasicInfo.setCityName(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CITY_NAME));
			hotelBasicInfo.setLayerHigh(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_LAYER_HIGH));
			hotelBasicInfo.setLayerCount(Short.parseShort(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_LAYER_COUNT)));			
			hotelBasicInfo.setWebSiteURL(doc.get(HotelInfoIndexConstants.INDEX_FIELD_WEB_SITE));
			hotelBasicInfo.setCommendType(doc.get(HotelInfoIndexConstants.INDEX_FIELD_COMMEND_TYPE));
			hotelBasicInfo.setAutoIntroduce(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_INTRODUCE));
			hotelBasicInfo.setParentHotelGroup(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_GROUP));
			hotelBasicInfo.setChnAddress(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CHINESE_ADDRESS));
			hotelBasicInfo.setEngAddress(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ENGLISH_ADDRESS));
			hotelBasicInfo.setPraciceDate(formatDate(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_PRACICE_DATE)));
			hotelBasicInfo.setFitmentDate(formatDate(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FITMENT_DATE)));
			hotelBasicInfo.setNoSmokingFloor(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_NO_SMOKING_FLOOR));
			hotelBasicInfo.setAllNoSmoking("1".equals(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ISALLNOSMOKING)) ? true
							: false);
			hotelBasicInfo.setHandicappedFixtrue(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_HANDICAPPED_FIXTRUE));
			hotelBasicInfo.setHotelIntroduce(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CHINESE_INTRODUCE));
			hotelBasicInfo.setTelephone(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_TELEPHONE));
			hotelBasicInfo.setLatitude(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_LATITUDE));
			hotelBasicInfo.setLongitude(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_LONGITUDE));
			hotelBasicInfo.setFirstPageRecommend(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FIRSTPAGE_RECOMMEND));
			hotelBasicInfo.setWebShowBaseInfo(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_WEBSHOWBASEINFO));
			hotelBasicInfo.setWebPrepayShow(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_IS_WEB_PREPAY));
			hotelBasicInfo.setAlertMessage(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ALERT_MESSAGE));
			hotelBasicInfo.setAroundView(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_AROUND_VIEW));			
			if("1".equals(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ISALLNOSMOKING))){
				hotelBasicInfo.setAllNoSmoking(true);
			}			
			hotelBasicInfo.setOutPictureName(doc
					.get(HotelInfoIndexConstants.INDEX_FIELD_OUT_PICTURE_NAME));
			hotelBasicInfo.setCheckInTime(doc
					.get(HotelInfoIndexConstants.INDEX_FIELD_CHECKIN_TIME));
			hotelBasicInfo.setCheckOutTime(doc
					.get(HotelInfoIndexConstants.INDEX_FIELD_CHECKOUT_TIME));
			
			// 处理交通信息
			hotelBasicInfo.handleTrafficInfoStr(doc
					.get(HotelInfoIndexConstants.INDEX_FIELD_TRAFFIC_INFO));
			
			//以下各字段的值在在索引中是以以下形式存储的：
			//code1!fullname1#code2!fullname2#......
			//所以从格式化的字段值中提取相应的code
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_STAR_LEVEL).equals("")){
				/*String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_STAR_LEVEL));
				hotelBasicInfo.setHotelStar(strCodes);*/
				hotelBasicInfo.setHotelStar(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_STAR_LEVEL));	
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ZONE).equals("")){
				/*String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ZONE));				
				hotelBasicInfo.setZone(strCodes);*/				
				hotelBasicInfo.setZone(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ZONE));
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FITMENT_DEGREE).equals("")){
				/*String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FITMENT_DEGREE));
				hotelBasicInfo.setFitmentDegree(formatDate(strCodes));*/
				hotelBasicInfo.setFitmentDegree(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FITMENT_DEGREE));
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BRAND).equals("")){
				String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BRAND));	
				hotelBasicInfo.setPlateName(strCodes);
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BIZ_ZONE).equals("")){
//				String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BIZ_ZONE));
//				hotelBasicInfo.setBizZone(strCodes);
				//String[] codes = extractCodeAndNameFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BIZ_ZONE));
				hotelBasicInfo.setBizZone(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BIZ_ZONE));
				hotelBasicInfo.setBizChnName(InitServlet.businessSozeObj.get(hotelBasicInfo.getBizZone()));
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CREDIT_CARD).equals("")){
//				String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CREDIT_CARD));
//				hotelBasicInfo.setCreditCard(strCodes);
				hotelBasicInfo.setCreditCard(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CREDIT_CARD));
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_TYPE).equals("")){
				hotelBasicInfo.setHotelType(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_TYPE));
				/*String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_TYPE));
				hotelBasicInfo.setHotelType(strCodes);*/
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ROOM_FIXTRUE).equals("")){
				String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ROOM_FIXTRUE));
				hotelBasicInfo.setRoomFixtrue(strCodes);
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_MEAL_FIXTRUE).equals("")){
				String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_MEAL_FIXTRUE));
				hotelBasicInfo.setMealAndFixture(strCodes);
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FREE_SERVICE).equals("")){
				String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FREE_SERVICE));
				hotelBasicInfo.setFreeService(strCodes);
			}
			if(!doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_THEME).equals("")){
				String strCodes = extractCodeArrayFromFormattedField(doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_THEME));
				hotelBasicInfo.setTheme(strCodes);
			}
			
			String roomTypes = doc.get(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ROOM_TYPE);
			if (roomTypes != null && !roomTypes.trim().equals("")){
				String[] roomTypeArray = roomTypes.split(HotelInfoIndexConstants.SEPERATOR_BETWEEN_RECORDS);
				List<RoomType> roomTypeList = new ArrayList<RoomType>(roomTypeArray.length);
				for(String strRoomType : roomTypeArray){ 
					String[] roomTypeProperties = strRoomType.split(HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
					RoomType roomType = new RoomType();
					roomType.setRoomTypeId(Long.valueOf(roomTypeProperties[0]));
					roomType.setRoomTypeName(roomTypeProperties[1]);
					roomType.setRoomAcreage(roomTypeProperties[2]);
					roomType.setRoomFloor(roomTypeProperties[3]);
					roomType.setMaxNumOfPersons(Integer.valueOf(roomTypeProperties[4]));
				    if (roomTypeProperties[5].equals("1")){
				    	roomType.setFlagAddBed(true);
				    }else{
				    	roomType.setFlagAddBed(false);
				    }
					roomType.setRoomEquipment(roomTypeProperties[6]);
					roomType.setAddBedNum(Integer.valueOf(roomTypeProperties[7]));
					
					roomTypeList.add(roomType);
				}
				
				hotelBasicInfo.setListRoomType(roomTypeList);
			}
			
			return hotelBasicInfo;
		}

		private String extractCodeArrayFromFormattedField(String fieldValue) {
			String[] recordArray =fieldValue.split(HotelInfoIndexConstants.SEPERATOR_BETWEEN_RECORDS);
			StringBuilder sb = new StringBuilder();
			for(String strRecord : recordArray){ 
				String[] codeAndNameArray = strRecord.split(HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
				sb.append(codeAndNameArray[0]).append(HotelInfoIndexConstants.SEPERATOR_BETWEEN_CMD_CODE);					
			}
			sb.setLength(sb.length() - 1);
			return sb.toString();
		}	
		
		/**
		 * 
		 * 根据lucene的field来获取其对值(如商业区code和商业区中文名称)
		 * 
		 * @param fieldValue
		 * @return
		 */
		private String[] extractCodeAndNameFromFormattedField(String fieldValue) {
			String[] recordArray = fieldValue
					.split(HotelInfoIndexConstants.SEPERATOR_BETWEEN_RECORDS);
			String strRecord = recordArray[0];
			String[] codeAndNameArray = strRecord
					.split(HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
			return (new String[] { codeAndNameArray[0], codeAndNameArray[1] });
		}
	};
	
	@SuppressWarnings("unchecked")
	public Map<String, HotelBasicInfo> searchHotelBasicInfo(HotelBasicInfoSearchParam hotelBasicInfoSearchParam) {
		BooleanQuery bq = basicQueryBuilder.buildQuery(hotelBasicInfoSearchParam);
		List<HotelBasicInfo> hotelBasicInfoList = indexSearcherService.search(bq, basicInfoResultProcessor);		
		if(hotelBasicInfoList.isEmpty()){
			return Collections.EMPTY_MAP;
		}	
		
		Map<String, HotelBasicInfo> hotelBasicInfoMap = new HashMap<String, HotelBasicInfo>(hotelBasicInfoList.size());
		for(HotelBasicInfo hotelBasicInfo : hotelBasicInfoList){
			hotelBasicInfoMap.put(String.valueOf(hotelBasicInfo.getHotelId()), hotelBasicInfo);
		}
		
		return hotelBasicInfoMap;
	}
	
	public List<HtlGeographicalposition> searchHotelGeoInfo(HtlGeographicalposition geo) {
		BooleanQuery bq = new BooleanQuery();
		//按ID查询经纬度信息
		if(null!=geo.getID()&& 0 < geo.getID().longValue()){
			Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_GOE_ID, geo.getID().toString());
			TermQuery nameQuery = new TermQuery(term);
			bq.add(nameQuery, BooleanClause.Occur.MUST);
		}else{
			if(null!=geo.getGisId() && geo.getGisId() != 0){
				Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_GOE_GISID, geo.getGisId().toString());
				TermQuery gisIdQuery = new TermQuery(term);
				bq.add(gisIdQuery, BooleanClause.Occur.MUST);
			}
			
			if(null!=geo.getAddress()&&!"".equals(geo.getAddress().trim())){
				Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_GOE_ADDRESS, "*" + geo.getAddress() + "*");
				WildcardQuery addressQuery = new WildcardQuery(term);
				bq.add(addressQuery, BooleanClause.Occur.MUST);
			}
			if(null!=geo.getCityName()&&!"".equals(geo.getCityName().trim())){
				Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_GOE_CITYNAME, "*" + geo.getCityName() + "*");
				WildcardQuery cityNameQuery = new WildcardQuery(term);
				bq.add(cityNameQuery, BooleanClause.Occur.MUST);
			}
			if(null!=geo.getName()&&!"".equals(geo.getName().trim())){
				Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_GOE_NAME, "*" + geo.getName() + "*");
				WildcardQuery nameQuery = new WildcardQuery(term);
				bq.add(nameQuery, BooleanClause.Occur.MUST);
			}
			//增加城市（代码）的查询条件
			if (geo.getCityCode() != null && !geo.getCityCode().trim().equals("")) {
				Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_GOE_CITYCODE, geo.getCityCode());
				TermQuery cityCodeQuery = new TermQuery(term);
				bq.add(cityCodeQuery, BooleanClause.Occur.MUST);
			}
			//增加地理信息类型（代码）的查询条件
			if (geo.getGptypeId() != null && geo.getGptypeId().longValue() != 0L) {
				Term term = new Term(HotelInfoIndexConstants.INDEX_FIELD_GOE_GPTYPEID, String.valueOf(geo.getGptypeId()));
				TermQuery geoTypeQuery = new TermQuery(term);
				bq.add(geoTypeQuery, BooleanClause.Occur.MUST);
			}	
		}
		return  indexSearcherService.search(bq, geoInfoResultProcessor);
	}
	private static final ResultProcessor<HtlGeographicalposition> geoInfoResultProcessor = new ResultProcessor<HtlGeographicalposition>(){		
		public HtlGeographicalposition processResult(Document doc){
			HtlGeographicalposition geo=new HtlGeographicalposition();
			geo.setID(Long.parseLong(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_ID)));
			geo.setAddress(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_ADDRESS));
			geo.setCityCode(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_CITYCODE).equals("")?null: 
				doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_CITYCODE));
			geo.setCityName(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_CITYNAME));
			String gisId=doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_GISID); 
			if(null!=gisId&&!"".equals(gisId.trim())&&!"null".equals(gisId)){
			    geo.setGisId(Long.parseLong(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_GISID)));
			}else{
				geo.setGisId(0L);
			}
			String gptypeId=doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_GPTYPEID); 
			if(null!=gptypeId&&!"".equals(gptypeId.trim())&&!"null".equals(gptypeId)){
			    geo.setGptypeId(Long.parseLong(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_GPTYPEID)));
			}else{
				geo.setGptypeId(0L);
			}
			String lat=doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_LATITUDE);
			if(null!=lat&&!"".equals(lat.trim())&&!"null".equals(lat)){
				geo.setLatitude(Double.parseDouble(lat));
			}else{
				geo.setLatitude(0.0);
			}
			String lng=doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_LONGITUDE);
			if(null!=lng&&!"".equals(lng.trim())&&!"null".equals(lng)){
				geo.setLongitude(Double.parseDouble(lng));
			}else{
				geo.setLongitude(0.0);
			}
			geo.setName(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_NAME));
			geo.setOperationDate(DateUtil.toDateByFormat(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_OPERATIONDATE), "yyyy-MM-dd  HH:mm:ss"));
			geo.setOperationer(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_OPERATIONER));
			geo.setOperationerId(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_OPERATIONERID));
			geo.setProvinceName(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_PROVINCENAME).equals("")?null: 
				doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_PROVINCENAME));
			String SeqNo=doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_SEQNO);
			if(null!=SeqNo&&!"".equals(SeqNo.trim())&&!"null".equals(SeqNo)){
			   geo.setSeqNo(Long.valueOf(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_SEQNO)));
			}else{
			   geo.setSeqNo(0L);
			}
			geo.setSortNum("".equals(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_SORTNUM)) ? 
					HtlGeographicalposition.MAX_SORT_NUM : Integer.valueOf(doc.get(HotelInfoIndexConstants.INDEX_FIELD_GOE_SORTNUM)));
			

			return geo;
		}		
	};
	//add by diandian.hou
	/**
	 * 20110405变为2011-04-05，0变为null
	 */
	private static String formatDate(String dateStr){
		if (dateStr == null || "".equals(dateStr) ){
			return null;
		}
		if(dateStr.length() != 8){
			return null;
		}
		String yearStr = dateStr.substring(0, 4);
		String monthStr = dateStr.substring(4, 6);
		String dayStr = dateStr.substring(6, 8);
		return yearStr.concat("-")+monthStr.concat("-")+dayStr;
	}

	public IndexSearcherService getIndexSearcherService() {
		return indexSearcherService;
	}

	public void setIndexSearcherService(IndexSearcherService indexSearcherService) {
		this.indexSearcherService = indexSearcherService;
	}

	

}

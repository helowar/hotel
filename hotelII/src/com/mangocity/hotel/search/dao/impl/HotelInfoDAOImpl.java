package com.mangocity.hotel.search.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mangocity.hotel.base.persistence.HtlAlerttypeInfo;
import com.mangocity.hotel.base.persistence.HtlFavourableDecrease;
import com.mangocity.hotel.base.persistence.HtlFavourableReturn;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.persistence.HtlPriceType;
import com.mangocity.hotel.base.persistence.HtlSalesPromo;
import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.search.dao.HotelInfoDAO;
import com.mangocity.hotel.search.index.HotelInfoIndexConstants;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.QueryCommodityCondition;
import com.mangocity.hotel.search.model.RoomType;
import com.mangocity.util.DateUtil;
import com.mangocity.util.dao.GenericDAOHibernateImpl;

public class HotelInfoDAOImpl extends GenericDAOHibernateImpl implements HotelInfoDAO {
	
	public int queryHotelCount() {
		String sql = "select count(*) from htl_hotel hotel where hotel.ACTIVE = '1' ";
		List<?> resultList = super.queryByNativeSQL(sql, null);
		
		return Integer.parseInt(resultList.get(0).toString());
	}

	/*@SuppressWarnings("unchecked")
	public List<CommonDataModel> queryBizZoneByHotelId(long hotelId) {
		StringBuilder sb = new StringBuilder();
    	sb.append(" select hotel.COUNTRY, hotel.STATE, hotel.CITY, hotel.BIZ_ZONE ");
    	sb.append(" from htl_hotel hotel where hotel.HOTEL_ID = ? ");
    	List<Object[]> resultList = super.queryByNativeSQL(sb.toString(), new Object[]{Long.valueOf(hotelId)});
    	if(resultList.isEmpty() || resultList.get(0)[0] == null || resultList.get(0)[1] == null 
    			|| resultList.get(0)[2] == null || resultList.get(0)[3] == null){
    		return Collections.EMPTY_LIST;
    	}
    	
    	Object[] objArray = resultList.get(0);
    	String treePath = "/root/comment/area/" + objArray[0].toString() + "/" + objArray[1].toString() 
    		+ "/" + objArray[2].toString() +"/business";
    			
    	return this.queryFullNameByCodeFromCDM(7, objArray[3].toString().split(","), treePath);
	}*/

	/*@SuppressWarnings("unchecked")
	public List<CommonDataModel> queryFreeServicesByHotelId(long hotelId) {
		StringBuilder sb = new StringBuilder();
    	sb.append(" select hotel.FREE_SERVICE ");
    	sb.append(" from htl_hotel hotel where hotel.HOTEL_ID = ? ");
    	List<?> resultList = super.queryByNativeSQL(sb.toString(), new Object[]{Long.valueOf(hotelId)});
    	if(resultList.isEmpty() || resultList.get(0) == null){
    		return Collections.EMPTY_LIST;
    	}
    	
    	String treePath = "/root/hotel/res_freeService";
    	return this.queryFullNameByCodeFromCDM(3, resultList.get(0).toString().split(","), treePath);
	}*/
	
	public Collection<HotelBasicInfo> queryHotelBasicInfo() {
		return doQueryHotelBasicInfo(null);
	}
	
	/**
	 * 
	 * ��ݾƵ�id��þƵ��lucene����Ϣ
	 * 
	 * @param hotelId
	 * @return
	 */
	public Collection<HotelBasicInfo> queryHotelBasicInfo(Long hotelId) {
		return doQueryHotelBasicInfo(hotelId);
	}

	@SuppressWarnings("unchecked")
	public Collection<HotelBasicInfo> doQueryHotelBasicInfo(Long searchHotelId) {
		StringBuilder sb = new StringBuilder();
    	sb.append(" select hotel.HOTEL_ID, hotel.CHN_NAME, hotel.ENG_NAME, hotel.COUNTRY, hotel.STATE, hotel.CITY, ");
    	sb.append(" hotel.ZONE, hotel.LAYER_HIGH, hotel.LAYER_COUNT, hotel.HOTEL_STAR, hotel.WEBSITE, hotel.CHN_ADDRESS, ");
    	sb.append(" hotel.ENG_ADDRESS, hotel.PRACICE_DATE, hotel.FITMENT_DATE, hotel.FITMENT_DEGREE, hotel.TELEPHONE, ");
    	sb.append(" hotel.OTHER_CREDIT, hotel.AUTO_INTRODUCE, hotel.CHN_HOTEL_INTRODUCE, hotel.PARENT_HOTEL_GROUP, ");
    	sb.append(" hotel.HANDICAPPED_FIXTRUE, hotel.AROUND_VIEW, hotel.ALERT_MESSAGE, hotel.NOSMOKINGFLOOR, ");
    	sb.append(" hotel.WEBPREPAYSHOW, hotel.WEBSHOWBASEINFO, hotel.NAMEPLATE_NAME, hotel.FIRSTPAGE_RECOMMEND, ");
    	sb.append(" hotel.GISID, hotel.LONGITUDE, hotel.LATITUDE, hotel.ISALLNOSMOKING, ");
    	
    	sb.append(" hotel.BIZ_ZONE, hotel.HOTEL_TYPE, hotel.FREE_SERVICE, hotel.MEAL_FIXTRUE, hotel.ROOM_FIXTRUE, ");
    	sb.append(" hotel.CREDIT_CARD_INFO, hotel.THEME, roomType.ROOM_TYPE_ID, roomType.ROOM_NAME, roomType.ACREAGE, ");
    	sb.append(" roomType.ROOM_FLOOR, roomType.ROOM_MAXPERSONS, roomType.IS_ADD_BED, roomType.ROOM_EQUIPMENT, ");
    	sb.append(" roomType.ADD_BED_QTY, ");    	
    	sb.append(" (SELECT p.PICTURE_NAME FROM HTL_PICTURE p WHERE p.HOTEL_ID = hotel.HOTEL_ID and p.PICTURE_TYPE = '0' and rownum = 1) as OUT_PICTURE_NAME, ");
    	
    	sb.append(" hotel.CHECKIN_TIME, hotel.checkout_time, ");
    	sb.append(" PKG_HTL_SEARCH.getHotelTrafficInfo(hotel.HOTEL_ID) as trafficInfo, ");
    	sb.append(" (select c.commendType from htl_comm_list c WHERE c.HOTEL_ID = hotel.HOTEL_ID and sysdate between c.begin_date and c.end_date and rownum = 1) as commendType ");
    	
    	sb.append(" from HTL_HOTEL hotel, HTL_ROOMTYPE roomType ");
    	sb.append(" where hotel.ACTIVE = '1' and hotel.HOTEL_ID = roomType.HOTEL_ID ");
    	if(null == searchHotelId) {
    		sb.append(" order by hotel.HOTEL_ID ");
    	} else {
    		sb.append(" and hotel.HOTEL_ID = ? ");
    	}   	
    	
    	List<Object[]> resultList = super
				.queryByNativeSQL(sb.toString(), (null == searchHotelId ? null
						: new Object[] { searchHotelId }));
		if (resultList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
    	
		Map<String, HotelBasicInfo> hotelInfoMap = new HashMap<String, HotelBasicInfo>(resultList.size());		
		for(Object[] objArray : resultList){
			HotelBasicInfo hotelInfo = null;
			String hotelId = objArray[0].toString();
			if(!hotelInfoMap.containsKey(hotelId)){
    			hotelInfo = getHotelBasicInfoFromQueryResult(objArray);
    			
				List<RoomType> roomTypeList = new ArrayList<RoomType>();
				RoomType roomType = getRoomTypeFromQueryResult(objArray);
				roomTypeList.add(roomType);
				hotelInfo.setListRoomType(roomTypeList);
				
				hotelInfoMap.put(hotelId, hotelInfo);
			}else{
				hotelInfo = hotelInfoMap.get(hotelId);    				
				RoomType roomType = getRoomTypeFromQueryResult(objArray);					
				hotelInfo.getListRoomType().add(roomType);    				
			}
		}
    	
    	return hotelInfoMap.values();
    }

	public List queryHotelByCityCode(String cityCode) {
		String sql="select hotel.HOTEL_ID, hotel.LONGITUDE, hotel.LATITUDE from HTL_HOTEL hotel where hotel.ACTIVE = '1' and CITY=? ";
		return super.queryByNativeSQL(sql, new Object[]{cityCode});
	}
	
	private HotelBasicInfo getHotelBasicInfoFromQueryResult(Object[] objArray) {
		HotelBasicInfo hotelInfo = new HotelBasicInfo();    			
		long hotelId = Long.parseLong(objArray[0].toString());
		hotelInfo.setHotelId(hotelId);//HOTEL_ID
		hotelInfo.setChnName(objArray[1] == null?null : objArray[1].toString());//CHN_NAME
		hotelInfo.setEngName(objArray[2] == null?null : objArray[2].toString());//ENG_NAME
		hotelInfo.setCityId(objArray[5] == null?null : objArray[5].toString());//CITY
		/*if(objArray[3] != null && objArray[4] != null && objArray[5] != null){
			String cityName = this.queryFullNameByCodeFromCDM(5, objArray[5].toString(), 
					"/root/comment/area/" + objArray[3].toString() + "/" + objArray[4].toString());
			hotelInfo.setCityName(cityName);			
		}*/
		if(objArray[5] != null){
			hotelInfo.setCityName(InitServlet.cityObj.get(objArray[5]));	
		}		
		/*if(objArray[3] != null && objArray[4] != null && objArray[5] != null && objArray[6] != null){
			String zoneName = this.queryFullNameByCodeFromCDM(7, objArray[6].toString(), 
					"/root/comment/area/" + objArray[3].toString() + "/" + objArray[4].toString()
					 + "/" + objArray[5].toString() + "/district");
			hotelInfo.setZone(objArray[6].toString() + HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS + zoneName);
		}*/
		if(objArray[6] != null){
			hotelInfo.setZone(objArray[6].toString());
		}		
		hotelInfo.setLayerHigh(objArray[7] == null?null : objArray[7].toString());//LAYER_HIGH
		if (objArray[8] != null){
			hotelInfo.setLayerCount(Short.parseShort(objArray[8].toString()));//LAYER_COUNT
		}
		if (objArray[9] != null){
			/*String starLevelName = this.queryFullNameByCodeFromCDM(3, objArray[9].toString(), 
					"/root/hotel/select_hotelStarLevel");
			hotelInfo.setHotelStar(objArray[9].toString() + HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS + starLevelName);//HOTEL_STAR
*/			hotelInfo.setHotelStar(objArray[9].toString());//HOTEL_STAR
		}		
		hotelInfo.setWebSiteURL(objArray[10] == null?null : objArray[10].toString());//WEBSITE
		hotelInfo.setChnAddress(objArray[11] == null?null : objArray[11].toString());//CHN_ADDRESS
		hotelInfo.setEngAddress(objArray[12] == null?null : objArray[12].toString());//ENG_ADDRESS
		hotelInfo.setPraciceDate(objArray[13] == null?null : objArray[13].toString());//PRACICE_DATE
		hotelInfo.setFitmentDate(objArray[14] == null?null : objArray[14].toString());//FITMENT_DATE
		if (objArray[15] != null) {
			/*String fitmentDegreeName = this.queryFullNameByCodeFromCDM(3,
					objArray[15].toString(), "/root/hotel/sel_fitmentDegree");
			hotelInfo.setFitmentDegree(objArray[15].toString()
					+ HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS
					+ fitmentDegreeName);// FITMENT_DEGREE
*/			hotelInfo.setFitmentDegree(objArray[15].toString());// FITMENT_DEGREE
		}		
		hotelInfo.setTelephone(objArray[16] == null?null : objArray[16].toString());//TELEPHONE		
		hotelInfo.setAutoIntroduce(objArray[18] == null?null : objArray[18].toString());//AUTO_INTRODUCE
		hotelInfo.setHotelIntroduce(objArray[19] == null?null : objArray[19].toString());//CHN_HOTEL_INTRODUCE
		//????
		hotelInfo.setParentHotelGroup(objArray[20] == null?null : objArray[20].toString());//PARENT_HOTEL_GROUP
		
		hotelInfo.setHandicappedFixtrue(objArray[21] == null?null : objArray[21].toString());//HANDICAPPED_FIXTRUE
		hotelInfo.setAroundView(objArray[22] == null?null : objArray[22].toString());//AROUND_VIEW	
		hotelInfo.setAlertMessage(objArray[23] == null?null : objArray[23].toString());//ALERT_MESSAGE
		hotelInfo.setNoSmokingFloor(objArray[24] == null?null : objArray[24].toString());//NOSMOKINGFLOOR
		hotelInfo.setWebPrepayShow(objArray[25] == null?null : objArray[25].toString());//WEBPREPAYSHOW
		hotelInfo.setWebShowBaseInfo(objArray[26] == null?null : objArray[26].toString());//WEBSHOWBASEINFO	
		//????
		hotelInfo.setPlateName(objArray[27] == null?null : objArray[27].toString());//NAMEPLATE_NAME
		
		hotelInfo.setFirstPageRecommend(objArray[28] == null?null : objArray[28].toString());//FIRSTPAGE_RECOMMEND
		if (objArray[29] != null){
			hotelInfo.setGisId(Integer.parseInt(objArray[29].toString()));//GISID
		}		
		hotelInfo.setLongitude(objArray[30] == null?null : objArray[30].toString());//LONGITUDE
		hotelInfo.setLatitude(objArray[31] == null?null : objArray[31].toString());//LATITUDE
		if(objArray[32] == null || Integer.parseInt(objArray[32].toString()) == 0){//ISALLNOSMOKING
			hotelInfo.setAllNoSmoking(false);
		}else{
			hotelInfo.setAllNoSmoking(true);
		}	
		
		if (objArray[33] != null){ //BIZ_ZONE
			hotelInfo.setBizZone(objArray[33].toString());
			//hotelInfo.setBizChnName(InitServlet.businessSozeObj.get(objArray[33]));
//			List<CommonDataModel> commonDataList = this.queryBizZoneByHotelId(hotelId);
//			hotelInfo.setBizZone(convertCommonData2FormattedStr(commonDataList));
		}
		if (objArray[34] != null){ // HOTEL_TYPE
			hotelInfo.setHotelType(objArray[34].toString());
			/*List<CommonDataModel> commonDataList = this.queryHotelTypesByHotelId(hotelId);
			hotelInfo.setHotelType(convertCommonData2FormattedStr(commonDataList));*/
		}
		if (objArray[35] != null){
//			List<CommonDataModel> commonDataList = this.queryFreeServicesByHotelId(hotelId);
//			hotelInfo.setFreeService(convertCommonData2FormattedStr(commonDataList));//FREE_SERVICE
			hotelInfo.setFreeService(objArray[35].toString());
		}
		if (objArray[36] != null){
//			List<CommonDataModel> commonDataList = this.queryMealFixturesByHotelId(hotelId);
//			hotelInfo.setMealAndFixture(convertCommonData2FormattedStr(commonDataList));//MEAL_FIXTRUE
			hotelInfo.setMealAndFixture(objArray[36].toString());
		}
		if (objArray[37] != null){
//			List<CommonDataModel> commonDataList = this.queryRoomFixturesByHotelId(hotelId);
//			hotelInfo.setRoomFixtrue(convertCommonData2FormattedStr(commonDataList));//ROOM_FIXTRUE
			hotelInfo.setRoomFixtrue(objArray[37].toString());
		}
		if (objArray[38] != null){
			hotelInfo.setCreditCard(objArray[38].toString());
//			List<CommonDataModel> commonDataList = this.querySupportedCreditCardsByHotelId(hotelId);
//			hotelInfo.setCreditCard(convertCommonData2FormattedStr(commonDataList));//CREDIT_CARD_INFO
		}
		if (objArray[39] != null){
//			List<CommonDataModel> commonDataList = this.queryThemesByHotelId(hotelId);
//			hotelInfo.setTheme(convertCommonData2FormattedStr(commonDataList));//THEME
			hotelInfo.setTheme(objArray[39].toString()); //THEME
		}
		
		// OUT_PICTURE_NAME
		if (objArray[48] != null) {
			hotelInfo.setOutPictureName(objArray[48].toString());
		}
		
		// 酒店规定入住和退房时间
		String tmpStr = null;
		int tmpLen = 0;
		if(null != objArray[49]) {
			tmpStr = objArray[49].toString();
			tmpLen = tmpStr.length();
			hotelInfo.setCheckInTime(tmpStr.substring(0, tmpLen - 2) + ":"
					+ tmpStr.substring(tmpLen - 2));			
		} else {
			hotelInfo.setCheckInTime("");
		}
		if(null != objArray[50]) {
			tmpStr = objArray[50].toString();
			tmpLen = tmpStr.length();
			hotelInfo.setCheckOutTime(tmpStr.substring(0, tmpLen - 2) + ":"
					+ tmpStr.substring(tmpLen - 2));			
		} else {
			hotelInfo.setCheckOutTime("");
		}
		
		// 交通信息		
		if(null != objArray[51]) {
			hotelInfo.setTrafficInfo(objArray[51].toString());
			/*tmpStr = objArray[50].toString();
			String[] trafficDetails = tmpStr.split("!");
			List<TrafficInfo> listTraffic = new ArrayList<TrafficInfo>(2);
			hotelInfo.setListTraffic(listTraffic);
			for(String trafficDetail : trafficDetails) {
				String[] fields = trafficDetail.split("#");
				TrafficInfo info = new TrafficInfo();
				info.setAddress(fields[0]);
				info.setDistance(fields[1]);
				listTraffic.add(info);
			}*/
		}
		
		// 主推		
		if(null != objArray[52]) {
			hotelInfo.setCommendType(objArray[52].toString());
		}
		
		return hotelInfo;
	}
	
	/*private String handleServiceString(String services) {
		String[] serviceArray = services.split(",");
		StringBuilder sb = new StringBuilder("");
		for(String service : serviceArray) {
			if(1 == service.length()) {
				sb.append("0");	
			}			
			sb.append(service).append(",");
		}
		return sb.toString();
	}*/

	/*private String convertCommonData2FormattedStr(List<CommonDataModel> commonDataList) {
		StringBuilder sb = new StringBuilder();
		for (CommonDataModel commonDataModel : commonDataList) {
			sb.append(commonDataModel.getCode()).append(HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS).append(
					commonDataModel.getFullName()).append(HotelInfoIndexConstants.SEPERATOR_BETWEEN_RECORDS);
		}
		return sb.toString();
	}*/
	
	private RoomType getRoomTypeFromQueryResult(Object[] objArray) {
		RoomType roomType = new RoomType();
		roomType.setRoomTypeId(Long.valueOf(objArray[40].toString()));//ROOM_TYPE_ID
		roomType.setRoomTypeName(objArray[41] == null?null : objArray[41].toString());//ROOM_NAME
		roomType.setRoomAcreage(objArray[42] == null?null : objArray[42].toString());//ACREAGE
		roomType.setRoomFloor(objArray[43] == null?null : objArray[43].toString());//ROOM_FLOOR
		if (objArray[44] != null){
			roomType.setMaxNumOfPersons(Integer.parseInt(objArray[44].toString()));//ROOM_MAXPERSONS
		}
		if (objArray[45] != null && !"0".equals(objArray[45].toString())){
			roomType.setFlagAddBed(true);//ALLOW_ADD_BED
		}else{
			roomType.setFlagAddBed(false);
		}
		roomType.setRoomEquipment(objArray[46] == null?null : objArray[46].toString());//ROOM_EQUIPMENT
		if (objArray[47] != null){
			roomType.setAddBedNum(Integer.parseInt(objArray[47].toString()));//ADD_BED_QTY
		}
		
		return roomType;
	}
	
	/*private String queryFullNameByCodeFromCDM(int level, String code, String treePath) {
		String sql = "select title from cmd.t_cdm_basedata where levels = ? and treepath = ? and name = ? ";    	
    	List<?> resultList = super.queryByNativeSQL(sql, new Object[]{Integer.valueOf(level), treePath, code});
    	
    	return resultList.isEmpty()?null : (String)resultList.get(0);
    }*/
	
	/*@SuppressWarnings("unchecked")
	private List<CommonDataModel> queryFullNameByCodeFromCDM(int level, String[] codeArray, String treePath) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select name, title from cmd.t_cdm_basedata where levels = ? and treepath = ? ");
		sb.append(" and name in ( ");
		
		List paramList = new ArrayList(codeArray.length + 1);
		paramList.add(Integer.valueOf(level));
		paramList.add(treePath);
		for(String code : codeArray) {
			sb.append("?, ");
			paramList.add(code);
		}
		sb.setLength(sb.length() - 2);
		sb.append(")");
		
		List<Object[]> codeList = super.queryByNativeSQL(sb.toString(), paramList.toArray());
		List<CommonDataModel> commonDataModelList = new ArrayList<CommonDataModel>(codeList.size());
		for(Object[] objArray : codeList){
			CommonDataModel dataModel = new CommonDataModel();
			dataModel.setCode(objArray[0].toString());
			dataModel.setFullName(objArray[1].toString());
			commonDataModelList.add(dataModel);
		}
		
    	return commonDataModelList;
    }*/

	/*@SuppressWarnings("unchecked")
	public List<CommonDataModel> queryHotelTypesByHotelId(long hotelId) {
		StringBuilder sb = new StringBuilder();
    	sb.append(" select hotel.HOTEL_TYPE ");
    	sb.append(" from htl_hotel hotel where hotel.HOTEL_ID = ? ");
    	List<?> resultList = super.queryByNativeSQL(sb.toString(), new Object[]{Long.valueOf(hotelId)});
    	if(resultList.isEmpty() || resultList.get(0) == null){
    		return Collections.EMPTY_LIST;
    	}
    	
    	String treePath = "/root/hotel/checkbox_hotelType";
    	return this.queryFullNameByCodeFromCDM(3, resultList.get(0).toString().split(","), treePath);
	}*/

	/*@SuppressWarnings("unchecked")
	public List<CommonDataModel> queryMealFixturesByHotelId(long hotelId) {
		StringBuilder sb = new StringBuilder();
    	sb.append(" select hotel.MEAL_FIXTRUE ");
    	sb.append(" from htl_hotel hotel where hotel.HOTEL_ID = ? ");
    	List<?> resultList = super.queryByNativeSQL(sb.toString(), new Object[]{Long.valueOf(hotelId)});
    	if(resultList.isEmpty() || resultList.get(0) == null){
    		return Collections.EMPTY_LIST;
    	}
    	
    	String treePath = "/root/hotel/hotel_liefallow";
    	return this.queryFullNameByCodeFromCDM(3, resultList.get(0).toString().split(","), treePath);
	}*/

	/*@SuppressWarnings("unchecked")
	public List<CommonDataModel> queryRoomFixturesByHotelId(long hotelId) {
		StringBuilder sb = new StringBuilder();
    	sb.append(" select hotel.ROOM_FIXTRUE ");
    	sb.append(" from htl_hotel hotel where hotel.HOTEL_ID = ? ");
    	List<?> resultList = super.queryByNativeSQL(sb.toString(), new Object[]{Long.valueOf(hotelId)});
    	if(resultList.isEmpty() || resultList.get(0) == null){
    		return Collections.EMPTY_LIST;
    	}
    	
    	String treePath = "/root/hotel/room_equipment";
    	return this.queryFullNameByCodeFromCDM(3, resultList.get(0).toString().split(","), treePath);
	}*/

	/*@SuppressWarnings("unchecked")
	public List<CommonDataModel> querySupportedCreditCardsByHotelId(long hotelId) {
		StringBuilder sb = new StringBuilder();
    	sb.append(" select hotel.CREDIT_CARD_INFO ");
    	sb.append(" from htl_hotel hotel where hotel.HOTEL_ID = ? ");
    	List<?> resultList = super.queryByNativeSQL(sb.toString(), new Object[]{Long.valueOf(hotelId)});
    	if(resultList.isEmpty() || resultList.get(0) == null){
    		return Collections.EMPTY_LIST;
    	}
    	
    	String treePath = "/root/comment/creditCard";
    	return this.queryFullNameByCodeFromCDM(3, resultList.get(0).toString().split(","), treePath);
	}*/

	/*@SuppressWarnings("unchecked")
	public List<CommonDataModel> queryThemesByHotelId(long hotelId) {//???
		StringBuilder sb = new StringBuilder();
    	sb.append(" select hotel.THEME ");
    	sb.append(" from htl_hotel hotel where hotel.HOTEL_ID = ? ");
    	List<?> resultList = super.queryByNativeSQL(sb.toString(), new Object[]{Long.valueOf(hotelId)});
    	if(resultList.isEmpty() || resultList.get(0) == null){
    		return Collections.EMPTY_LIST;
    	}
    	
    	String treePath = "/root/hotel/checkbox_hotelType";
    	return this.queryFullNameByCodeFromCDM(3, resultList.get(0).toString().split(","), treePath);
	}*/
	
	/*@SuppressWarnings("unchecked")
	public List<SalePromotionMango> querySalesPremotion() {
		String sql = " select HISID, CONTENT, BEGINEND, NAMESTR, URL from H_PRESALE ";
		List<Object[]> resultList = super.queryByNativeSQL(sql, null);
		if(resultList.isEmpty()){
			return Collections.EMPTY_LIST;
		}
		
		List<SalePromotionMango> salePromotionList = new ArrayList<SalePromotionMango>(resultList.size());
		for(Object[] objArray : resultList){
			SalePromotionMango salePromotion = new SalePromotionMango();
			salePromotion.setPromotionId(Long.parseLong(objArray[0].toString()));//HISID
			salePromotion.setPromotionContent(objArray[1] == null?null : objArray[1].toString());//CONTENT
//			salePromotion.setBeginDate(objArray[2]);//BEGINEND
//			salePromotion.setEndDate(objArray[2]);
			salePromotion.setPromotionName(objArray[3] == null?null : objArray[3].toString());//NAMESTR
			salePromotion.setPromotionUrl(objArray[4] == null?null : objArray[4].toString());//URL
			salePromotionList.add(salePromotion);
		}
		
		return salePromotionList;
	}*/

    /**
     * 根据条件(指定酒店，指定渠道，指定日期)查询酒店促销信息
     * @param queryBean
     * @return
     */
    public List<HtlSalesPromo> querySalepromos(QueryCommodityCondition queryBean){
    	String hqlstr = "select promo from HtlSalesPromo promo,HtlContract contract,HtlHotel hotel "+ 
						        " where promo.contractID = contract.ID and hotel.ID = contract.hotelId "+
						        " and  hotel.ID in("+queryBean.getHotelIdLst()+" ) and promo.endDate>=? and promo.beginDate<?";    	
    	return super.query(hqlstr, new Object[]{queryBean.getInDate(), queryBean.getOutDate()}, 0, 0, false);
    	
    }

    /**
     * 
     * 根据条件查询芒果促销信息
     * 
     * @param queryBean
     * @return
     */
    public List<Object[]> queryPreSales(QueryCommodityCondition queryBean){
    	String hqlstr = "select ph.hotel_id,p.presale_name,p.presale_content,p.url, to_char(p.begin_Date, 'yyyy-mm-dd') || '开始,' ||to_char(p.end_Date, 'yyyy-mm-dd') || '结束' as beginEnd from Htl_Presale p,Htl_Presale_Hotel ph "
				+ "where p.Presale_ID = ph.Presale_ID "
				+ "and ph.hotel_Id in ("
				+ queryBean.getHotelIdLst()
				+ ") "
				+ "and p.begin_Date <= ? "
				+ "and p.end_Date > ? "
				+ "and p.webShow = '1' ";
		return super.queryByNativeSQL(hqlstr, new Object[] {
				queryBean.getOutDate(), queryBean.getInDate() });
    }
    
    /**
	 * 根据条件(指定酒店，指定渠道，指定日期)查询优惠(住M送N,打折,连住包价)信息 查询条件中的入住离店日期的判断如下 startdate<=checkout
	 * and enddate>=checkin 客人离店之前优惠必须开始了，同时客人入住之后优惠还未结束的才取出来
	 * ,或者是!(startdate>checkout or enddate< checkin)
	 * 
	 * @param queryBean
	 *            hotelId,outDate,inDate.
	 * @return
	 */
    public List<HtlFavourableclause> queryFavourableClauses(QueryCommodityCondition queryBean){
    	List<HtlFavourableclause> favourableclauseLst = new ArrayList<HtlFavourableclause>();
    	String hqlstr = "  from HtlFavourableclause hf where hf.hotelId in ("+queryBean.getHotelIdLst()+")   and hf.beginDate < ?   and hf.endDate>=?";
    	favourableclauseLst = super.query(hqlstr, new Object[]{queryBean.getOutDate(),queryBean.getInDate()}, 0, 0, false);
    	//System.out.println("fffff:"+favourableclauseLst.get(0).getClass());
    	for(HtlFavourableclause favourable:favourableclauseLst){
    	Hibernate.initialize(favourable.getLstPackagerate());
    	}
    	return favourableclauseLst;
    }
    
    /**
     * 根据条件(指定酒店，指定渠道，指定日期)查询酒店返现信息
     * @param queryBean
     * @return
     */
    public List<HtlFavourableReturn> queryFavourableReturns(QueryCommodityCondition queryBean){
    	
		String hqlstr = " select  a  from HtlFavourableReturn a   where "
				+ " a.hotelId in (" + queryBean.getHotelIdLst() + ") "
				+ " and endDate >= ? " + " and beginDate <= ? "
				+ " and fun_date_week_judge(?,?,week) > 0 order by modifyTime desc";

		return super.query(hqlstr, new Object[] { queryBean.getInDate(),
				queryBean.getOutDate(), queryBean.getInDate(),
				queryBean.getOutDate() }, 0, 0, false);
    }
    
    /**
     *根据条件(指定酒店，指定渠道，指定日期)查询酒店立减信息
     * @param queryBean
     * @return
     */
    public List<HtlFavourableDecrease> queryFavourableDecrease(QueryCommodityCondition queryBean){   	
    	String hqlstr = "from HtlFavourableDecrease fd where fd.hotelId in("+queryBean.getHotelIdLst()+")  and fd.beginDate<= ?   and  fd.endDate>=?";
    	return super.query(hqlstr, new Object[]{queryBean.getInDate(),queryBean.getOutDate()}, 0, 0, false);	
    }
    
    /**
     * 根据查询条件取出酒店的提示信息
     * @param qcc
     * @return
     */
    public List<HtlAlerttypeInfo> queryArlerttypeInfoList(QueryCommodityCondition qcc){
    	String hqlstr ="from HtlAlerttypeInfo ha where ha.hotelId in ("+qcc.getHotelIdLst()+") and ha.endDate>=? and ha.beginDate<=?";
    	return super.query(hqlstr, new Object[]{qcc.getOutDate(),qcc.getInDate()});
    }
    
    /**
     * 查询酒店的供应商信息
     * @param hotelIdLst
     * @return
     */
    public Map<String,String> querySupplierInfo(String hotelIdLst) {
    	String hqlstr = "select hp.price_type_id,hp.supplier_id from htl_roomtype hr,"
    		+" htl_price_type hp where hr.room_type_id = hp.room_type_id"
    		+" and hr.hotel_id in ("+hotelIdLst+")";
    	List<Object[]> supplierLst = super.queryByNativeSQL(hqlstr, null);
    	if(null==supplierLst||supplierLst.isEmpty()) {
    		return null;
    	}
    	Map<String,String> supplierMap = new HashMap<String,String>();
    	for(Object[] supplierArr:supplierLst) {
    		supplierMap.put(supplierArr[0].toString(), null==supplierArr[1]?"":supplierArr[1].toString());
    	}
    	return supplierMap;
    }
    
    /**
     * 查询酒店的价格类型信息 add by ting.li
     * @param hotelIdLst
     * @return
     */
    public Map<String,HtlPriceType> queryPriceTypeInfo(String hotelIdLst) {
    	String hqlstr = "select hp.price_type_id,hp.supplier_id,hp.show_member_price from htl_roomtype hr,"
    		+" htl_price_type hp where hr.room_type_id = hp.room_type_id"
    		+" and hr.hotel_id in ("+hotelIdLst+")";
    	List<Object[]> priceTypeList = super.queryByNativeSQL(hqlstr, null);
    	if(null==priceTypeList||priceTypeList.isEmpty()) {
    		return null;
    	}
    	Map<String,HtlPriceType> priceTypeMap = new HashMap<String,HtlPriceType>();
    	for(Object[] supplierArr:priceTypeList) {
    		HtlPriceType priceType=new HtlPriceType();
    		priceType.setID(Long.valueOf(supplierArr[0].toString()));
    		priceType.setSupplierID(null==supplierArr[1]?null:Long.valueOf(supplierArr[1].toString()));
    		
    		int showMemberPrice=(null==supplierArr[2]? 0:Integer.valueOf(supplierArr[2].toString()));
    		priceType.setShowMemberPrice(showMemberPrice == 0 ? false:true);
    		priceTypeMap.put(supplierArr[0].toString(), priceType);
    	}
    	return priceTypeMap;
    }
    
	public HotelBasicInfo queryHotelSimpleInfo(Long hotelId,Date checkInDate,Date checkOutDate) {

	    checkOutDate=DateUtil.getDate(checkOutDate, -1);
	    
		StringBuilder sql = new StringBuilder();
		sql.append(" select h.chn_name,h.eng_name,h.city from htl_hotel h where exists ");

		sql.append(" (");
		sql.append(" select 'Z' from htl_contract hc ");
		sql.append(" where hc.hotel_id = ? ");
		sql.append(" and hc.begin_date <= ? ");
		sql.append(" and hc.end_date >= ?  ");
		sql.append(" )");

		sql.append("  and not exists ");
		sql.append(" (");

		sql.append(" select 'X' from htl_hotel_ext hhe ");
		sql.append(" where  ((? between hhe.stopsell_begin_date and hhe.stopsell_end_date) or ");
		sql.append("  ( ? between hhe.stopsell_begin_date and hhe.stopsell_end_date)) ");
		sql.append(" and hhe.hotel_id = ? ");
		sql.append(" and hhe.ISSTOPSELL = '1' ");

		sql.append(" ) ");

		sql.append(" and h.hotel_id =? ");
		sql.append(" and h.active = 1 ");
		
		Object[] params=new Object[]{hotelId,checkInDate,checkOutDate,checkInDate,checkOutDate,hotelId,hotelId};
				
		List<Object[]> results=super.queryByNativeSQL(sql.toString(), params);
		
		HotelBasicInfo hotelBasicInfo =null;
		if(results !=null && results.size() >0){
			hotelBasicInfo = new HotelBasicInfo();
			Object[] result=results.get(0);
			hotelBasicInfo.setChnName(String.valueOf(result[0]));
			hotelBasicInfo.setEngName(String.valueOf(result[1]));
			hotelBasicInfo.setCityId(String.valueOf(result[2]));
		}
		
		return hotelBasicInfo;
	}
    
    /**
     * 
     * 根据酒店id获取所在城市code
     * 
     * @param hotelId
     * @return
     */
    public String getCityCodeByHotelId(Long hotelId) {
    	List<String> lstResult = super.queryByNativeSQL(
				"select h.city from htl_hotel h where h.hotel_Id = ? ",
				new Object[] { hotelId });
    	if(!lstResult.isEmpty()) {
    		return lstResult.get(0);	
    	} else {
    		return null;
    	}
    }

	public Collection<HotelBasicInfo> queryHotelBasicInfoByPage(
			int currentPage, int pagesize) {
		StringBuilder sb = new StringBuilder();
    	sb.append(" select hotel.HOTEL_ID, hotel.CHN_NAME, hotel.ENG_NAME, hotel.COUNTRY, hotel.STATE, hotel.CITY, ");
    	sb.append(" hotel.ZONE, hotel.LAYER_HIGH, hotel.LAYER_COUNT, hotel.HOTEL_STAR, hotel.WEBSITE, hotel.CHN_ADDRESS, ");
    	sb.append(" hotel.ENG_ADDRESS, hotel.PRACICE_DATE, hotel.FITMENT_DATE, hotel.FITMENT_DEGREE, hotel.TELEPHONE, ");
    	sb.append(" hotel.OTHER_CREDIT, hotel.AUTO_INTRODUCE, hotel.CHN_HOTEL_INTRODUCE, hotel.PARENT_HOTEL_GROUP, ");
    	sb.append(" hotel.HANDICAPPED_FIXTRUE, hotel.AROUND_VIEW, hotel.ALERT_MESSAGE, hotel.NOSMOKINGFLOOR, ");
    	sb.append(" hotel.WEBPREPAYSHOW, hotel.WEBSHOWBASEINFO, hotel.NAMEPLATE_NAME, hotel.FIRSTPAGE_RECOMMEND, ");
    	sb.append(" hotel.GISID, hotel.LONGITUDE, hotel.LATITUDE, hotel.ISALLNOSMOKING, ");
    	
    	sb.append(" hotel.BIZ_ZONE, hotel.HOTEL_TYPE, hotel.FREE_SERVICE, hotel.MEAL_FIXTRUE, hotel.ROOM_FIXTRUE, ");
    	sb.append(" hotel.CREDIT_CARD_INFO, hotel.THEME, roomType.ROOM_TYPE_ID, roomType.ROOM_NAME, roomType.ACREAGE, ");
    	sb.append(" roomType.ROOM_FLOOR, roomType.ROOM_MAXPERSONS, roomType.IS_ADD_BED, roomType.ROOM_EQUIPMENT, ");
    	sb.append(" roomType.ADD_BED_QTY, ");    	
    	sb.append(" (SELECT p.PICTURE_NAME FROM HTL_PICTURE p WHERE p.HOTEL_ID = hotel.HOTEL_ID and p.PICTURE_TYPE = '0' and rownum = 1) as OUT_PICTURE_NAME, ");
    	
    	sb.append(" hotel.CHECKIN_TIME, hotel.checkout_time, ");
    	sb.append(" PKG_HTL_SEARCH.getHotelTrafficInfo(hotel.HOTEL_ID) as trafficInfo, ");
    	sb.append(" (select c.commendType from htl_comm_list c WHERE c.HOTEL_ID = hotel.HOTEL_ID and sysdate between c.begin_date and c.end_date and rownum = 1) as commendType ");
    	
    	sb.append(" from HTL_HOTEL hotel, HTL_ROOMTYPE roomType ");
    	sb.append(" where hotel.ACTIVE = '1' and hotel.HOTEL_ID = roomType.HOTEL_ID ");
    	sb.append(" and hotel.HOTEL_ID in (select hotel_id from (select t.hotel_id ,rownum num from htl_hotel t where t.ACTIVE = '1' and rownum<=? order by t.hotel_id) where num>?)");
    	
    	sb.append(" order by hotel.HOTEL_ID ");
     	
    	
    	List<Object[]> resultList = super.queryByNativeSQL(sb.toString(), new Object[] { pagesize*currentPage, pagesize*(currentPage-1)});
		if (resultList.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		
		Map<String, HotelBasicInfo> hotelInfoMap = new HashMap<String, HotelBasicInfo>(resultList.size());		
		for(Object[] objArray : resultList){
			HotelBasicInfo hotelInfo = null;
			String hotelId = objArray[0].toString();
			if(!hotelInfoMap.containsKey(hotelId)){
    			hotelInfo = getHotelBasicInfoFromQueryResult(objArray);
    			
				List<RoomType> roomTypeList = new ArrayList<RoomType>();
				RoomType roomType = getRoomTypeFromQueryResult(objArray);
				roomTypeList.add(roomType);
				hotelInfo.setListRoomType(roomTypeList);
				
				hotelInfoMap.put(hotelId, hotelInfo);
			}else{
				hotelInfo = hotelInfoMap.get(hotelId);    				
				RoomType roomType = getRoomTypeFromQueryResult(objArray);					
				hotelInfo.getListRoomType().add(roomType);    				
			}
		}
    	
    	return hotelInfoMap.values();
	}


}

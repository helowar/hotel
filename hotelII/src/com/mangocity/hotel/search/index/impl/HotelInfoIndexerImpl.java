package com.mangocity.hotel.search.index.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import com.mangocity.hotel.base.dao.IGeograpPositionDAO;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.search.dao.HotelInfoDAO;
import com.mangocity.hotel.search.index.HotelInfoIndexConstants;
import com.mangocity.hotel.search.index.HotelInfoIndexer;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.mangocity.hotel.search.model.RoomType;
import com.mangocity.hotel.search.searchengine.service.IndexWriterService;
import com.mangocity.hotel.search.util.DateUtil;
import com.mangocity.util.log.MyLog;




public class HotelInfoIndexerImpl implements HotelInfoIndexer  {
	
	private static final MyLog log = MyLog.getLogger(HotelInfoIndexerImpl.class);
	
	private HotelInfoDAO hotelInfoDAO;
	
	private IGeograpPositionDAO geograpPositionDAO;
	
	private IndexWriterService indexWriterService;
	
	private int pagesize = 10000;

	/**
	 * 将所有酒店的信息保存到索引中
	 */
	public synchronized void createHotelInfoIndex() {
	        long begin = System.currentTimeMillis();
	        log.info("开始建立索引库---------------------");
		    
		    boolean isExist = indexWriterService.checkIndexFileExist();
		    if(!isExist){
		    	
		    	int  hotelcount = hotelInfoDAO.queryHotelCount();
				
				int totalpage =0;
				if(hotelcount % pagesize == 0) {
					totalpage = hotelcount/pagesize;
				}else{
					totalpage = hotelcount/pagesize + 1;
				}
				
				log.info(" totalpage is +"+totalpage);
		    	
				for(int currentPage = 1; currentPage<=totalpage ; currentPage++){
					log.info("current Page is :"+ currentPage);
					Collection<HotelBasicInfo> hotelInfoList = hotelInfoDAO.queryHotelBasicInfoByPage(currentPage, pagesize);
					List<Document> docList = new ArrayList<Document>(hotelInfoList.size());
					for(HotelBasicInfo hotelInfo: hotelInfoList) {
						docList.add(populateBasicInfo2Doc(hotelInfo));
					}
					indexWriterService.addDocuments(docList);
					hotelInfoList=null;
					docList =null;
				}
		    	
				indexWriterService.addDocuments(getHotelGeoInfoDocsByGeoId());
				
				indexWriterService.commit();
				
		    }
		 long end = System.currentTimeMillis();
		 log.info("结束建立索引库，总共用时："+(end-begin)+"ms");

	}
	
	/**
	 * 
	 * 根据酒店id相应更新lucene索引文件
	 * 
	 * @param hotelId
	 */
	public synchronized void updateHotelBasicInfoDoc(Long hotelId) {		
		Collection<HotelBasicInfo> hotelInfoList = hotelInfoDAO.queryHotelBasicInfo(hotelId);
		if(hotelInfoList.isEmpty()) {
			indexWriterService.delDocuments(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ID, String.valueOf(hotelId));	
		} else {	
			for(HotelBasicInfo hotelInfo: hotelInfoList) {
				indexWriterService.updDocument(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ID, hotelInfo.getHotelId()+"", populateBasicInfo2Doc(hotelInfo));
			}		
		}
	}

	public synchronized void updateMgisInfoDoc(Long geoId) {
		//11.2日更新，王建补上
			HtlGeographicalposition  geo = geograpPositionDAO.queryGeopositionById(geoId);	
			if(geo!=null) {
				indexWriterService.updDocument(HotelInfoIndexConstants.INDEX_FIELD_GOE_ID, String.valueOf(geoId), populateHotelGeoInfo2Doc(geo));
			} else {			
				indexWriterService.delDocuments(HotelInfoIndexConstants.INDEX_FIELD_GOE_ID, String.valueOf(geoId));
			}			
	}	

	/**
	 * 将封装在HotelBasicInfo对象中的信息拷贝到Document中
	 * 
	 * @param hotelBasicInfo
	 * @return
	 */
	private Document populateBasicInfo2Doc(HotelBasicInfo hotelBasicInfo) {
		Document doc = new Document();
		
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ID,
			String.valueOf(hotelBasicInfo.getHotelId()), 
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CHINESE_NAME,
			hotelBasicInfo.getChnName() == null ? "" : hotelBasicInfo.getChnName(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ENGLISH_NAME,
			hotelBasicInfo.getEngName() == null ? "" : hotelBasicInfo.getEngName(),
			Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CITY_ID,
			hotelBasicInfo.getCityId() == null ? "" : hotelBasicInfo.getCityId(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CITY_NAME,
			hotelBasicInfo.getCityName() == null ? "" : hotelBasicInfo.getCityName(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));				
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_LAYER_HIGH,
			hotelBasicInfo.getLayerHigh() == null ? "" : hotelBasicInfo.getLayerHigh(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_LAYER_COUNT,
			String.valueOf(hotelBasicInfo.getLayerCount()),
			Field.Store.YES, Field.Index.NO));				
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_WEB_SITE,
			hotelBasicInfo.getWebSiteURL() == null ? "" : hotelBasicInfo.getWebSiteURL(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CHINESE_ADDRESS,
			hotelBasicInfo.getChnAddress() == null ? "" : hotelBasicInfo.getChnAddress(),
			Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ENGLISH_ADDRESS,
			hotelBasicInfo.getEngAddress() == null ? "" : hotelBasicInfo.getEngAddress(),
			Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new NumericField(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_PRACICE_DATE,Field.Store.YES, true).
				setIntValue(hotelBasicInfo.getPraciceDate() == null ? 0 : Integer.valueOf(hotelBasicInfo.getPraciceDate().replaceAll("-", ""))));
		doc.add(new NumericField(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FITMENT_DATE,Field.Store.YES,true).
				setIntValue(hotelBasicInfo.getFitmentDate() == null ? 0 : Integer.valueOf(hotelBasicInfo.getFitmentDate().replaceAll("-", ""))));				
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_TELEPHONE,
			hotelBasicInfo.getTelephone() == null ? "" : hotelBasicInfo.getTelephone(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_INTRODUCE,
			hotelBasicInfo.getAutoIntroduce() == null ? "" : hotelBasicInfo.getAutoIntroduce(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CHINESE_INTRODUCE,
			hotelBasicInfo.getHotelIntroduce()== null ? "" : hotelBasicInfo.getHotelIntroduce(),
			Field.Store.YES, Field.Index.ANALYZED));		
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_HANDICAPPED_FIXTRUE,
			hotelBasicInfo.getHandicappedFixtrue() == null ? "" : hotelBasicInfo.getHandicappedFixtrue(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_AROUND_VIEW,
			hotelBasicInfo.getAroundView() == null ? "" : hotelBasicInfo.getAroundView(),
			Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ALERT_MESSAGE,
			hotelBasicInfo.getAlertMessage() == null ? "" : hotelBasicInfo.getAlertMessage(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_NO_SMOKING_FLOOR,
			hotelBasicInfo.getNoSmokingFloor() == null ? "" : hotelBasicInfo.getNoSmokingFloor(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_IS_WEB_PREPAY,
			hotelBasicInfo.getWebPrepayShow() == null ? "" : hotelBasicInfo.getWebPrepayShow(),
			Field.Store.YES, Field.Index.NO));		
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_WEBSHOWBASEINFO,
			hotelBasicInfo.getWebShowBaseInfo() == null ? "" : hotelBasicInfo.getWebShowBaseInfo(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FIRSTPAGE_RECOMMEND,
			hotelBasicInfo.getFirstPageRecommend() == null ? "" : hotelBasicInfo.getFirstPageRecommend(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_GISID,
			String.valueOf(hotelBasicInfo.getGisId()),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_LONGITUDE,
			hotelBasicInfo.getLongitude() == null ? "" : hotelBasicInfo.getLongitude(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_LATITUDE,
			hotelBasicInfo.getLatitude() == null ? "" : hotelBasicInfo.getLatitude(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ISALLNOSMOKING,
			hotelBasicInfo.isAllNoSmoking()?"1" : "0",
			Field.Store.YES, Field.Index.NO));
		
		//以下各字段的值在数据库中存储的是代码，建索引时将把代码和对应名称以以下格式进行存储：
		//code1!fullname1#code2!fullname2#......
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ZONE,
			hotelBasicInfo.getZone() == null ? "" : hotelBasicInfo.getZone(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_STAR_LEVEL,
			hotelBasicInfo.getHotelStar() == null ? "" : hotelBasicInfo.getHotelStar(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FITMENT_DEGREE,
			hotelBasicInfo.getFitmentDegree() == null ? "" : hotelBasicInfo.getFitmentDegree(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_GROUP,
			hotelBasicInfo.getParentHotelGroup() == null ? "" : hotelBasicInfo.getParentHotelGroup(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BRAND,
			hotelBasicInfo.getPlateName() == null ? "" : hotelBasicInfo.getPlateName(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_BIZ_ZONE,
			hotelBasicInfo.getBizZone() == null?"" : hotelBasicInfo.getBizZone(),
			Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_CREDIT_CARD,
			hotelBasicInfo.getCreditCard() == null?"" : hotelBasicInfo.getCreditCard(),
			Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_TYPE,
			hotelBasicInfo.getHotelType() == null?"" : hotelBasicInfo.getHotelType(),
			Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ROOM_FIXTRUE,
			hotelBasicInfo.getRoomFixtrue() == null?"" : hotelBasicInfo.getRoomFixtrue(),
			Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_MEAL_FIXTRUE,
			hotelBasicInfo.getMealAndFixture() == null?"" : hotelBasicInfo.getMealAndFixture(),
			Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_FREE_SERVICE,
			hotelBasicInfo.getFreeService() == null?"" : hotelBasicInfo.getFreeService(),
			Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_THEME,
			hotelBasicInfo.getTheme() == null?"" : hotelBasicInfo.getTheme(),
			Field.Store.YES, Field.Index.ANALYZED));
		
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_OUT_PICTURE_NAME,
				hotelBasicInfo.getOutPictureName() == null ? ""
						: hotelBasicInfo.getOutPictureName(), Field.Store.YES,
				Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_CHECKIN_TIME,
				null == hotelBasicInfo.getCheckInTime() ? "" : hotelBasicInfo
						.getCheckInTime(), Field.Store.YES, Field.Index.NO));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_CHECKOUT_TIME,
				null == hotelBasicInfo.getCheckOutTime() ? "" : hotelBasicInfo
						.getCheckOutTime(), Field.Store.YES, Field.Index.NO));
		
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_TRAFFIC_INFO,
				hotelBasicInfo.getTrafficInfo() == null ? "" : hotelBasicInfo
						.getTrafficInfo(), Field.Store.YES, Field.Index.NO));	
		
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_COMMEND_TYPE,
				hotelBasicInfo.getCommendType() == null ? "" : hotelBasicInfo
						.getCommendType(), Field.Store.YES, Field.Index.NO));		
		
		if(null != hotelBasicInfo.getListRoomType()) {
			StringBuilder roomTypeSB = new StringBuilder();
			for(RoomType roomType : hotelBasicInfo.getListRoomType()){
				roomTypeSB.append(String.valueOf(roomType.getRoomTypeId())).append(
						HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
				roomTypeSB.append(roomType.getRoomTypeName() == null ? "" : roomType.getRoomTypeName()).append(
						HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
				roomTypeSB.append(roomType.getRoomAcreage() == null ? "" : roomType.getRoomAcreage()).append(
						HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
				roomTypeSB.append(roomType.getRoomFloor() == null ? "" : roomType.getRoomFloor()).append(
						HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
				roomTypeSB.append(String.valueOf(roomType.getMaxNumOfPersons())).append(
						HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
				roomTypeSB.append(roomType.isFlagAddBed() ? "1" : "0").append(
						HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
				roomTypeSB.append(roomType.getRoomEquipment() == null ? "" : roomType.getRoomEquipment()).append(
						HotelInfoIndexConstants.SEPERATOR_BETWEEN_FIELDS);
				roomTypeSB.append(String.valueOf(roomType.getAddBedNum())).append(
						HotelInfoIndexConstants.SEPERATOR_BETWEEN_RECORDS);					
			}
			doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_HOTEL_ROOM_TYPE, roomTypeSB.toString(), 
				Field.Store.YES, Field.Index.ANALYZED));	
		}
		
		return doc;
	}
	
	/**
	 * 替换字符串中的#和！
	 * @param str
	 * @return
	 */
	private String replaceSpecialSymbol(String str) {
		if(null==str||"".equals(str)) {
			return "";
		}else {
			str = str.replaceAll("#", "");
			str = str.replaceAll("!", "");
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	private List<Document> getHotelGeoInfoDocsByGeoId() {
		List list = geograpPositionDAO.queryAllPosition();
		if(list.isEmpty()){
			return Collections.EMPTY_LIST;
		}
		log.info("total geograpPosition count: " + list.size());
		List<Document> docList = new ArrayList<Document>(list.size());
		for(Iterator i = list.iterator();i.hasNext();){
			HtlGeographicalposition geo =(HtlGeographicalposition) i.next();
			//log.info(geo.getName());
			docList.add(populateHotelGeoInfo2Doc(geo));
		}
		return docList;
	}

	private Document populateHotelGeoInfo2Doc(HtlGeographicalposition geo) {
		Document doc = new Document();
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_ID,String.valueOf(geo.getID()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_ADDRESS,String.valueOf(geo.getAddress()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_CITYCODE, geo.getCityCode() == null?"": String.valueOf(geo.getCityCode()), 
				Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_CITYNAME,String.valueOf(geo.getCityName()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_GISID, geo.getGisId() == null?"" : String.valueOf(geo.getGisId()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_GPTYPEID,String.valueOf(geo.getGptypeId()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_LATITUDE, geo.getLatitude() == null?"" : String.valueOf(geo.getLatitude()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_LONGITUDE, geo.getLongitude() == null?"" : String.valueOf(geo.getLongitude()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_NAME,String.valueOf(geo.getName()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_OPERATIONDATE, geo.getOperationDate() == null?"" : DateUtil.formatDateToYMDHMS(geo.getOperationDate()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_OPERATIONERID, geo.getOperationerId() == null?"" : String.valueOf(geo.getOperationerId()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_PROVINCENAME,geo.getProvinceName() == null? "" : String.valueOf(geo.getProvinceName()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_SEQNO, geo.getSeqNo() == null?"" : String.valueOf(geo.getSeqNo()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_OPERATIONER, geo.getOperationer() == null?"" : String.valueOf(geo.getOperationer()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		// add by diandian.hou 添加地理信息排序
		doc.add(new Field(HotelInfoIndexConstants.INDEX_FIELD_GOE_SORTNUM, geo.getSortNum() == null?"" : String.valueOf(geo.getSortNum()), Field.Store.YES,
				Field.Index.ANALYZED));
		return doc;
	}	
	
	
	public void setHotelInfoDAO(HotelInfoDAO hotelInfoDAO) {
		this.hotelInfoDAO = hotelInfoDAO;
	}

	public void setGeograpPositionDAO(IGeograpPositionDAO geograpPositionDAO) {
		this.geograpPositionDAO = geograpPositionDAO;
	}

	public void setIndexWriterService(IndexWriterService indexWriterService) {
		this.indexWriterService = indexWriterService;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	
	
}
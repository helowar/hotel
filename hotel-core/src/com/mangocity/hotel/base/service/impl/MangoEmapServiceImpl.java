package com.mangocity.hotel.base.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlArea;
import com.mangocity.hotel.base.persistence.HtlGeographicalposition;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.MangoEmapVo;
import com.mangocity.hotel.base.service.GeoDistanceService;
import com.mangocity.hotel.base.service.IGeograpPositionService;
import com.mangocity.hotel.base.service.MangoEmapService;
import com.mangocity.hotel.base.service.SystemDataService;
import com.mangocity.hotel.base.util.SendLuceneMQ;

public class MangoEmapServiceImpl implements MangoEmapService{
	private static Log log = LogFactory.getLog(MangoEmapServiceImpl.class);
	private IGeograpPositionService geograpPositionService;
	private GeoDistanceService geoDistanceService;
	private HotelManage hotelManage;
	private SystemDataService systemDataService;
	
	// 发送MQ功能
    private SendLuceneMQ sendLuceneMQ;
	public MangoEmapVo queryMangoEmapById(MangoEmapVo mapVo) {
		MangoEmapVo vo=new MangoEmapVo();
		//当类型为0时表示是酒店，其它均为地理信息
		if(null!=mapVo.getType()&& 0==mapVo.getType()){
			 HtlHotel hotel = hotelManage.findHotel(mapVo.getId());
			 vo.setId(hotel.getID());
			 vo.setAddress(hotel.getChnAddress());
			 vo.setLatitude(hotel.getLatitude());
			 vo.setLongitude(hotel.getLongitude());
			 vo.setName(hotel.getChnName());
			 vo.setType(0L);
			 vo.setBaiduLatitude(hotel.getBaiduLatitude());
			 vo.setBaiduLongitude(hotel.getBaiduLongitude());
			 HtlArea area=systemDataService.queryAreaCode(hotel.getCity());
			 if(area!=null){
				 vo.setCityName(area.getCityName());
			 }
		}else{
			HtlGeographicalposition geo = geograpPositionService.queryGeopositionById(mapVo.getId());
			vo.setId(geo.getID());
			vo.setAddress(geo.getAddress());
			vo.setLatitude(geo.getLatitude());
			vo.setLongitude(geo.getLongitude());
			vo.setName(geo.getName());
			vo.setType(geo.getGptypeId());
			vo.setBaiduLatitude(geo.getBaiduLatitude());
			vo.setBaiduLongitude(geo.getBaiduLongitude());
		    vo.setCityName(geo.getCityName());
		}
		return vo;
	}
	
	public void saveMangoEmap(MangoEmapVo mangoEmapVo) {
		if(null!=mangoEmapVo&&mangoEmapVo.getType()==0){
			 HtlHotel hotel = hotelManage.findHotel(mangoEmapVo.getId());
			 hotel.setLatitude(mangoEmapVo.getLatitude());
			 hotel.setLongitude(mangoEmapVo.getLongitude());
			 hotel.setGisid(mangoEmapVo.getId());
			 log.debug("MangoEmapServiceImpl.saveMangoEmap hotleId:"+mangoEmapVo.getId()+" Latitude:"+mangoEmapVo.getLatitude()+" Longitude:"+mangoEmapVo.getLongitude());
			 hotelManage.modifyHotel(hotel);
			 geoDistanceService.generateGoeInfoByHotel(hotel.getID());
			 sendTopic("hotelmap:hotel#"+hotel.getID());
			 
		}else{
			HtlGeographicalposition geo = geograpPositionService.queryGeopositionById(mangoEmapVo.getId());
			geo.setLatitude(mangoEmapVo.getLatitude());
			geo.setLongitude(mangoEmapVo.getLongitude());
			geo.setGisId(mangoEmapVo.getId());
			geograpPositionService.updateGeoposition(geo);
			log.debug("MangoEmapServiceImpl.saveMangoEmap geo:"+mangoEmapVo.getId()+" Latitude:"+mangoEmapVo.getLatitude()+" Longitude:"+mangoEmapVo.getLongitude());
			geoDistanceService.generateGoeInfoByGeoId(geo.getID());
			sendTopic( "hotelmap:mgis#" + geo.getID());
		}
	}
	
	/**
	 * 保存百度地标
	 */
	public void saveMangoEmapBaidu(MangoEmapVo mangoEmapVo) {
		if(null!=mangoEmapVo&&mangoEmapVo.getType()==0){
			 HtlHotel hotel = hotelManage.findHotel(mangoEmapVo.getId());
			 hotel.setBaiduLatitude(mangoEmapVo.getBaiduLatitude());
			 hotel.setBaiduLongitude(mangoEmapVo.getBaiduLongitude());
			 hotel.setGisid(mangoEmapVo.getId());
			 hotelManage.modifyHotel(hotel);
			 log.debug("MangoEmapServiceImpl.saveMangoEmapBaidu hotleId:"+mangoEmapVo.getId()+" Latitude:"+mangoEmapVo.getLatitude()+" Longitude:"+mangoEmapVo.getLongitude());
		}else{
			HtlGeographicalposition geo = geograpPositionService.queryGeopositionById(mangoEmapVo.getId());
			geo.setBaiduLatitude(mangoEmapVo.getBaiduLatitude());
			geo.setBaiduLongitude(mangoEmapVo.getBaiduLongitude());
			geo.setGisId(mangoEmapVo.getId());
			geograpPositionService.updateGeoposition(geo);
			log.debug("MangoEmapServiceImpl.saveMangoEmapBaidu geo:"+mangoEmapVo.getId()+" Latitude:"+mangoEmapVo.getLatitude()+" Longitude:"+mangoEmapVo.getLongitude());
		}
	}
	
	private void sendTopic(String topic){
		sendLuceneMQ.send(topic);
	}
	public IGeograpPositionService getGeograpPositionService() {
		return geograpPositionService;
	}
	public void setGeograpPositionService(
			IGeograpPositionService geograpPositionService) {
		this.geograpPositionService = geograpPositionService;
	}
	public HotelManage getHotelManage() {
		return hotelManage;
	}
	public void setHotelManage(HotelManage hotelManage) {
		this.hotelManage = hotelManage;
	}

	public GeoDistanceService getGeoDistanceService() {
		return geoDistanceService;
	}

	public void setGeoDistanceService(GeoDistanceService geoDistanceService) {
		this.geoDistanceService = geoDistanceService;
	}

	public void setSendLuceneMQ(SendLuceneMQ sendLuceneMQ) {
		this.sendLuceneMQ = sendLuceneMQ;
	}

	public void setSystemDataService(SystemDataService systemDataService) {
		this.systemDataService = systemDataService;
	}

}

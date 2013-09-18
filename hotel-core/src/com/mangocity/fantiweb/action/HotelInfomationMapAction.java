package com.mangocity.fantiweb.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.framework.exception.ServiceException;
import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.manage.HotelManageWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.mgis.app.service.baseinfo.GisService;
import com.mangocity.mgis.domain.valueobject.GisInfo;
import com.mangocity.mgis.domain.valueobject.LatLng;
import com.mangocity.mgis.domain.valueobject.MapsInfo;

public class HotelInfomationMapAction extends GenericCCAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5342240126632636246L;
	private Double lat;
	private Double lng;
	private Long hotelId; 
	private GisService gisService;
	private String city;
	private List<GisInfo> gisList;
	private HotelManageWeb hotelManageWeb;
	private HotelPageForWebBean hotelPageForWebBean;
	private Map gisMaps = new HashMap();
	
	public String execute(){
        try {
        	LatLng latLng=new LatLng(lat,lng);
    		gisList=hotelManageWeb.calculateMapNearHotel(latLng,hotelId,city,5,null);
    		List mapsInfoList = new ArrayList();
    		  String path = request.getRealPath("/");
    		for (int i = 0; i < gisList.size(); i++) {
    			GisInfo gisInfo = gisList.get(i);
    			if(null!=gisInfo && gisInfo.getGisId()>0){
	    			 MapsInfo mapsInfo = new MapsInfo();
	                 mapsInfo.setGisId(gisInfo.getGisId());
	                 mapsInfo.setName(gisInfo.getName());
	                 mapsInfo.setImageUrl(path + "/images/emap/mango1.gif");
	                 mapsInfo.setImageInfo(" "+String.valueOf(i + 1));
	                 mapsInfo.setLatLng(gisInfo.getLatLng());
	                 mapsInfo.setDescription(String.valueOf(gisInfo.getDataType())+"_"+mapsInfo.getName());
	                 mapsInfoList.add(mapsInfo);
    			}
    		}
			gisMaps = gisService.kmlGenerator(mapsInfoList, 0, 0, path);
		} catch (ServiceException e) {
			log.error("HotelInfomationMapAction ServiceException error:",e);
		} catch (IOException e) {
			log.error("HotelInfomationMapAction IOException error:",e);
		}catch (Exception e){
			log.error("HotelInfomationMapAction Exception error:",e);
		}
		finally{
		return SUCCESS;
		}
	}


	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public List<GisInfo> getGisList() {
		return gisList;
	}
	public void setGisList(List<GisInfo> gisList) {
		this.gisList = gisList;
	}
	public GisService getGisService() {
		return gisService;
	}
	public void setGisService(GisService gisService) {
		this.gisService = gisService;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}


	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}


	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}


	public HotelPageForWebBean getHotelPageForWebBean() {
		return hotelPageForWebBean;
	}


	public void setHotelPageForWebBean(HotelPageForWebBean hotelPageForWebBean) {
		this.hotelPageForWebBean = hotelPageForWebBean;
	}


	public Map getGisMaps() {
		return gisMaps;
	}


	public void setGisMaps(Map gisMaps) {
		this.gisMaps = gisMaps;
	}


	public Long getHotelId() {
		return hotelId;
	}


	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

}

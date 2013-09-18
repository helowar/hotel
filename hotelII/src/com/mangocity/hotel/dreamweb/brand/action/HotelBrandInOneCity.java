package com.mangocity.hotel.dreamweb.brand.action;

import java.util.List;

import com.mangocity.hotel.base.web.InitServlet;
import com.mangocity.hotel.dreamweb.search.service.HotelBrandSearchService;
import com.mangocity.hotel.search.model.HotelBrandBean;
import com.mangocity.hotel.search.model.HotelForCityBean;
import com.mangocity.util.StringUtil;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionSupport;
 
public class HotelBrandInOneCity extends ActionSupport{
	
	private String cityCode;
	private String cityName;
	private String brandCode;
	private String brandName;
	private String brandIntroduce;
	private String brandPicture;
	
	private HotelForCityBean hotelForOneCity;
	private List<HotelForCityBean> brandHotelList;
	private List<HotelBrandBean> otherBrandsList; 
	private static final MyLog log = MyLog.getLogger(HotelBrandQueryAction.class);
	
	//注入的service
	private HotelBrandSearchService hotelBrandSearchService;
	
	public String execute(){
		 try{
			 if(StringUtil.isValidStr(cityCode)){
		        	cityCode = cityCode.toUpperCase();
		        }
			 cityName = InitServlet.cityObj.get(cityCode);
		     brandName = hotelBrandSearchService.queryHotelBrandName(brandCode);
		     brandPicture = brandCode;
		     brandIntroduce = hotelBrandSearchService.queryBrandIntroduce(brandCode);
		     brandHotelList = hotelBrandSearchService.queryHotelsByBrand(brandCode);//得到所有城市的品牌酒店
		     List<HotelForCityBean> hotelListForOneCity = hotelBrandSearchService.queryHotelsByBrand(brandCode, cityCode);//得到该城市的品牌酒店,只有一条记录
		     if(hotelListForOneCity.size()>0){
		         hotelForOneCity =  hotelListForOneCity.get(0);
		     }
		     otherBrandsList = hotelBrandSearchService.queryOtherHotelBrands(brandCode);//其它品牌酒店
		     
		 }catch(Exception e){
			 log.error("hotelbrand query error:",e);
			 e.printStackTrace();
		 }
		 return SUCCESS;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public void setHotelBrandSearchService(
			HotelBrandSearchService hotelBrandSearchService) {
		this.hotelBrandSearchService = hotelBrandSearchService;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public HotelForCityBean getHotelForOneCity() {
		return hotelForOneCity;
	}

	public void setHotelForOneCity(HotelForCityBean hotelForOneCity) {
		this.hotelForOneCity = hotelForOneCity;
	}

	public List<HotelForCityBean> getBrandHotelList() {
		return brandHotelList;
	}

	public String getBrandIntroduce() {
		return brandIntroduce;
	}

	public void setBrandIntroduce(String brandIntroduce) {
		this.brandIntroduce = brandIntroduce;
	}

	public String getBrandPicture() {
		return brandPicture;
	}

	public void setBrandPicture(String brandPicture) {
		this.brandPicture = brandPicture;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<HotelBrandBean> getOtherBrandsList() {
		return otherBrandsList;
	}

	public void setOtherBrandsList(List<HotelBrandBean> otherBrandsList) {
		this.otherBrandsList = otherBrandsList;
	}

	

}

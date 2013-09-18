package com.mangocity.hotel.dreamweb.brand.action;

import java.util.List;
import com.mangocity.hotel.dreamweb.search.service.HotelBrandSearchService;
import com.mangocity.hotel.search.model.BrandCitiesByLetter;
import com.mangocity.hotel.search.model.HotelBrandBean;
import com.mangocity.hotel.search.model.HotelForCityBean;
import com.mangocity.util.log.MyLog;
import com.opensymphony.xwork2.ActionSupport;
 
public class HotelBrandQueryAction extends ActionSupport {

	
	private String brandCode;
	private String brandName;
	private String brandPicture;
	private String brandIntroduce;
	private List<HotelForCityBean> brandHotelList;
	private List<BrandCitiesByLetter> brandCitiesList;
	private List<HotelBrandBean> otherBrandsList;
	private static final MyLog log = MyLog.getLogger(HotelBrandQueryAction.class);
	
	//注入的service
	private HotelBrandSearchService hotelBrandSearchService;
	public String execute(){
		 try{
		     brandName = hotelBrandSearchService.queryHotelBrandName(brandCode);
		     brandPicture = brandCode; //http://himg.mangocity.com/img/h/2011/1041.jpg picture指的是1041
		     brandIntroduce = hotelBrandSearchService.queryBrandIntroduce(brandCode);
		     brandHotelList = hotelBrandSearchService.queryHotelsByBrand(brandCode);
		     brandCitiesList = hotelBrandSearchService.sortBrandCitiesByLetter(brandHotelList);//城市按字母排序
		     otherBrandsList = hotelBrandSearchService.queryOtherHotelBrands(brandCode);//其它品牌酒店
		     
		 }catch(Exception e){
			 log.error("hotelbrand query error:",e);
		 }
		 return SUCCESS;
	}
	public void setHotelBrandSearchService(HotelBrandSearchService hotelBrandSearchService) {
		this.hotelBrandSearchService = hotelBrandSearchService;
	}
	
	public String getBrandName() {
		return brandName;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public List<HotelForCityBean> getBrandHotelList() {
		return brandHotelList;
	}
	public List<BrandCitiesByLetter> getBrandCitiesList() {
		return brandCitiesList;
	}
	public void setBrandCitiesList(List<BrandCitiesByLetter> brandCitiesList) {
		this.brandCitiesList = brandCitiesList;
	}
	public String getBrandIntroduce() {
		return brandIntroduce;
	}
	public List<HotelBrandBean> getOtherBrandsList() {
		return otherBrandsList;
	}
	public String getBrandPicture() {
		return brandPicture;
	}
	public void setBrandPicture(String brandPicture) {
		this.brandPicture = brandPicture;
	}
	
}

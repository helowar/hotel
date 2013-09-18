package com.mangocity.hotel.search.model;

import java.util.List;
import java.util.ArrayList;
public class BrandCitiesByLetter implements Comparable<BrandCitiesByLetter>{
 
	public String cityFirstLetter;
	public List<HotelForCityBean> brandCitiesList;
	public String getCityFirstLetter() {
		return cityFirstLetter;
	}
	
	public int compareTo(BrandCitiesByLetter brandCitiesByLetter) {
		if ( brandCitiesByLetter==null || brandCitiesByLetter.getCityFirstLetter()==null) {
			return -1;
		}
		if(this.cityFirstLetter==null){
			return 1;
		}
		return this.cityFirstLetter.compareTo(brandCitiesByLetter.getCityFirstLetter());
	}
		
	public void setCityFirstLetter(String cityFirstLetter) {
		this.cityFirstLetter = cityFirstLetter;
	}
	public List<HotelForCityBean> getBrandCitiesList() {
		if(brandCitiesList==null){
			brandCitiesList = new ArrayList<HotelForCityBean>();
		}
		return brandCitiesList;
	}
	public void setBrandCitiesList(List<HotelForCityBean> brandCitiesList) {
		this.brandCitiesList = brandCitiesList;
	}
	
}

package com.mangocity.hotel.dreamweb.search.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.mangocity.hotel.dreamweb.search.service.HotelInfoService;
import com.mangocity.hotel.search.model.HotelBasicInfo;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class HotelAccessAction extends ActionSupport{
	private static final long serialVersionUID = 2552966122317224937L;
	private HotelInfoService hotelInfoService;
	private List hotelNameAndIdLis = new ArrayList();	
	public String execute(){
		List hotelIdCookies = new ArrayList();
		Cookie[] cookies = getRequest().getCookies();
		if(null != cookies && cookies.length>0){
			for (int j = 0; j < cookies.length; j++) {
				if (cookies[j].getName().indexOf("hotelId") > -1&&-1==cookies[j].getName().indexOf("hotelIdArray")) {
					hotelIdCookies.add(cookies[j]);
	
				}
			}
		}
		for (int kk = hotelIdCookies.size() - 1; kk >= 0; kk--) {
			String[] forHotel = new String[2];
			Cookie cookieIte = (Cookie) hotelIdCookies.get(kk);
			if (cookieIte.getMaxAge() != 0) {
				String hotelIdd = cookieIte.getValue();
				HotelBasicInfo hotel = hotelInfoService.queryHotelInfoByHotelId(hotelIdd);
				if(null!=hotel){
					String hotelName = hotel.getChnName();
					forHotel[0] = hotelIdd;
					forHotel[1] = hotelName;
					hotelNameAndIdLis.add(forHotel);
				}
			} else {
				hotelIdCookies.remove(kk);
			}

		}
		return SUCCESS;
	}
	private HttpServletRequest getRequest(){
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		return request;
	}
	public HotelInfoService getHotelInfoService() {
		return hotelInfoService;
	}
	public void setHotelInfoService(HotelInfoService hotelInfoService) {
		this.hotelInfoService = hotelInfoService;
	}
	public List getHotelNameAndIdLis() {
		return hotelNameAndIdLis;
	}
	public void setHotelNameAndIdLis(List hotelNameAndIdLis) {
		this.hotelNameAndIdLis = hotelNameAndIdLis;
	}
	
}

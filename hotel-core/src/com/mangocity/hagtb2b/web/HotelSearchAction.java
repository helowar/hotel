package com.mangocity.hagtb2b.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.manage.HotelManageWeb;


public class HotelSearchAction extends GenericCCAction {

	private static final long serialVersionUID = -6371488760878200329L;
	
	//城市ID
	private String cityIdCookie;
	
	//城市名称
	private String cityNameCookie;
	
	//存放我浏览过的酒店的hotelId、hotelName
	private List hotelNameAndIdStr = new ArrayList();
	
	private HotelManageWeb hotelManageWeb;
	
	private String  flag_userNoLogin;
	
	private String  agent_imgUrl;

	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

	public String execute(){
		
		Cookie[] cookies = super.request.getCookies();
		if(null!=super.request.getParameter("flag_userNoLogin")
				&& null!=super.request.getParameter("imgUrl")){
			flag_userNoLogin =super.request.getParameter("flag_userNoLogin").toString();
			agent_imgUrl  = super.request.getParameter("imgUrl").toString();
		}
		if(null!=cookies){
		for (int j = 0; j < cookies.length; j++) {
			if(cookies[j].getName().equalsIgnoreCase("cityIdCookie")){
				cityIdCookie = cookies[j].getValue();
		    }else if(cookies[j].getName().equalsIgnoreCase("cityNameCookie")){
		        cityNameCookie = cookies[j].getValue();
		    }
		}
		}
		hotelNameAndIdStr = super.findCookies();
		
		return SUCCESS ;
		
	}

	public String getCityIdCookie() {
		return cityIdCookie;
	}

	public void setCityIdCookie(String cityIdCookie) {
		this.cityIdCookie = cityIdCookie;
	}

	public String getCityNameCookie() {
		return cityNameCookie;
	}

	public void setCityNameCookie(String cityNameCookie) {
		this.cityNameCookie = cityNameCookie;
	}

	public List getHotelNameAndIdStr() {
		return hotelNameAndIdStr;
	}

	public void setHotelNameAndIdStr(List hotelNameAndIdStr) {
		this.hotelNameAndIdStr = hotelNameAndIdStr;
	}

	public String getFlag_userNoLogin() {
		return flag_userNoLogin;
	}

	public void setFlag_userNoLogin(String flag_userNoLogin) {
		this.flag_userNoLogin = flag_userNoLogin;
	}

	public String getAgent_imgUrl() {
		return agent_imgUrl;
	}

	public void setAgent_imgUrl(String agent_imgUrl) {
		this.agent_imgUrl = agent_imgUrl;
	}



}


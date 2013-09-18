package com.mangocity.webnew.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import com.mangocity.hotel.order.web.GenericCCAction;
import com.mangocity.hweb.manage.HotelManageWeb;

public class HotelSearchHKAction extends GenericCCAction{

	private static final long serialVersionUID = 8627753319420170890L;
	
	//存放我浏览过的酒店的hotelId、hotelName
	private List hotelNameAndIdStr = new ArrayList();
	
	private HotelManageWeb hotelManageWeb;
	
	public HotelManageWeb getHotelManageWeb() {
		return hotelManageWeb;
	}

	public void setHotelManageWeb(HotelManageWeb hotelManageWeb) {
		this.hotelManageWeb = hotelManageWeb;
	}

	public List getHotelNameAndIdStr() {
		return hotelNameAndIdStr;
	}

	public void setHotelNameAndIdStr(List hotelNameAndIdStr) {
		this.hotelNameAndIdStr = hotelNameAndIdStr;
	}

	public String execute(){
		hotelNameAndIdStr = super.findCookies();
		return SUCCESS;
	}
	
	

}

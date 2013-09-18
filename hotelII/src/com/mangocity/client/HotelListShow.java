package com.mangocity.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.mangocity.client.hotel.search.serviceImpl.HotelQueryAndShowImpl;

public class HotelListShow implements EntryPoint {

	public HotelQueryAndShowImpl hotelQueryAndShowImpl = HotelQueryAndShowImpl.getInstance();

	public void onModuleLoad() {
		//Window.alert("aa");
		hotelQueryAndShowImpl.init();
	}
}

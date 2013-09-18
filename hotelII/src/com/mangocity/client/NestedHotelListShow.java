package com.mangocity.client;
import com.google.gwt.core.client.EntryPoint;
import com.mangocity.client.hotel.search.serviceImpl.NestHotelQueryAndShowImpl;

public class NestedHotelListShow implements EntryPoint {
	
	public NestHotelQueryAndShowImpl nestHotelQueryAndShowImpl = NestHotelQueryAndShowImpl.getInstance();
	public void onModuleLoad() {
		nestHotelQueryAndShowImpl.init();
	}
	
}

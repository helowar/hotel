package com.mangocity.client.hotel.panel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.mangocity.client.hotel.search.serviceImpl.NestHotelQueryAndShowImpl;


public class NestAnchor extends Anchor {

	private String cityCode;
	private String bizCode;
	private NestAnchor[] nestAnchors;
	final NestHotelQueryAndShowImpl nestHotelQueryAndShowImpl = NestHotelQueryAndShowImpl.getInstance(); 
	public NestAnchor(String anchorText,String cityCode,String bizCode,NestAnchor[] nestAnchors){
		super(anchorText);
		this.cityCode = cityCode;
		this.bizCode = bizCode;
		this.nestAnchors = nestAnchors;
	}
		
	public void setClickHandler(){
		this.addClickHandler(
				new ClickHandler(){
					public void onClick(ClickEvent event) {
						jsSetClass();
						nestHotelQueryAndShowImpl.queryHotelList(cityCode, bizCode,1);
					}
				});
	}
	
	public void jsSetClass(){
		this.setStyleName("area_bf");
		for(int i = 0; i<nestAnchors.length;i++){
			if(bizCode.equals(nestAnchors[i].getBizCode())){
				continue;
			}
			nestAnchors[i].setStyleName("");
		}
	}
		
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getBizCode() {
		return bizCode;
	}
	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public NestAnchor[] getNestAnchors() {
		return nestAnchors;
	}

	public void setNestAnchors(NestAnchor[] nestAnchors) {
		this.nestAnchors = nestAnchors;
	}
	
	
	
}

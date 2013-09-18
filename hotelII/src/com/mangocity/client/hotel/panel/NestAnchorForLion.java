package com.mangocity.client.hotel.panel;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Anchor;
import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;
import com.mangocity.client.hotel.search.serviceImpl.HotelQueryAndShowImpl;

/**
 * 醒狮计划（8000返现活动）
 * @author wangjian
 *
 */

public class NestAnchorForLion extends Anchor {

	private GWTQueryCondition queryCondition;
	private int tabIndex;
	private NestAnchorForLion[] nestAnchors;
	final HotelQueryAndShowImpl hotelQueryAndShowImpl = HotelQueryAndShowImpl.getInstance(); 
	public NestAnchorForLion(String anchorText,GWTQueryCondition queryCondition,int tabIndex,NestAnchorForLion[] nestAnchors){
		super(anchorText);
		this.queryCondition = queryCondition;
		this.tabIndex = tabIndex;
		this.nestAnchors = nestAnchors;
	}
		
	public void setClickHandler(){
		this.addClickHandler(
				new ClickHandler(){
					public void onClick(ClickEvent event) {
						queryCondition.setPromoteType(tabIndex);
						queryCondition.setPageNo(1);
						writeCookies("promoteType",Integer.toString(tabIndex),null);
						hotelQueryAndShowImpl.setConditonAndHotelShow(queryCondition);
					}
				});
	}
	
	private void writeCookies(String name,String value,Date expires) {
		if(null!=name&&null!=value&&!"".equals(name)&&!"".equals(value)) {
			String cookieValue = Cookies.getCookie(name);
			if(null!=cookieValue&&!"".equals(cookieValue)) {
				Cookies.removeCookieNative(name,"/");
				Cookies.setCookie(name, value, expires, "", "/", false);
			}else {
				Cookies.setCookie(name, value, expires, "", "/", false);
			}
		}
	}
	
	public GWTQueryCondition getQueryCondition() {
		return queryCondition;
	}

	public void setQueryCondition(GWTQueryCondition queryCondition) {
		this.queryCondition = queryCondition;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public NestAnchorForLion[] getNestAnchors() {
		return nestAnchors;
	}

	public void setNestAnchors(NestAnchorForLion[] nestAnchors) {
		this.nestAnchors = nestAnchors;
	}
	
	
	
}

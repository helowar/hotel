package com.mangocity.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.mangocity.client.hotel.gwt.queryCondition.GWTQueryCondition;
import com.mangocity.client.hotel.search.serviceImpl.HotelResultShowImpl;
import com.mangocity.hotel.search.vo.CommodityVO;
import com.mangocity.hotel.search.vo.HotelPageForWebBean;
import com.mangocity.hotel.search.vo.HotelResultForWebVO;
import com.mangocity.hotel.search.vo.RoomTypeVO;

public class HotelInfoShow implements EntryPoint {
	
	private HotelListSearchServiceAsync hotelListSearchService ;
	private HotelResultShowImpl hotelResultShowImpl = new  HotelResultShowImpl();
	public void onModuleLoad() {
		init();
		String inDate = RootPanel.get("id_startDate").getElement().getPropertyString("value");
		String outDate = RootPanel.get("id_backDate").getElement().getPropertyString("value");
		String cityCode = RootPanel.get("id_cityCode").getElement().getPropertyString("value");
		String hotelId = RootPanel.get("id_hotelId").getElement().getPropertyString("value");
		String promoteType = RootPanel.get("promoteType").getElement().getPropertyString("value");
		GWTQueryCondition gwtQueryCondition = new GWTQueryCondition();
		gwtQueryCondition.setInDate(inDate);
		gwtQueryCondition.setOutDate(outDate);
		gwtQueryCondition.setCityCode(cityCode);
		//modify by alfred.query hotel infomation by hotelId.
		gwtQueryCondition.setHotelId(hotelId);
		gwtQueryCondition.setPromoteType(Integer.parseInt(promoteType));
		gwtQueryCondition.setFromChannel("1");//1Ϊweb
		
		
		//获取渠道编号 add by hushunqiang
		String projectCode = RootPanel.get("channelcode").getElement().getPropertyString("value");
		gwtQueryCondition.setProjectCode(projectCode);
		
		setHotelInfo(gwtQueryCondition);
		
	}
	
	private void init(){
		hotelListSearchService = GWT.create(HotelListSearchService.class);
	}
	
	private void setHotelInfo(GWTQueryCondition gwtQueryCondition){
		hotelListSearchService.searchHotelListInfo(gwtQueryCondition, new AsyncCallback<HotelPageForWebBean>(){
			  public void onFailure(Throwable caught){
			  }
			  public void  onSuccess(HotelPageForWebBean hotelpageForWebBean){
				  hotelResultShowImpl.setCommondityDivForOneHotel(hotelpageForWebBean);
			  }
		});
	}	
}

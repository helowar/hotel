package com.mangocity.hotel.search.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.search.service.IHotelQueryHandler;
import com.mangocity.hotel.search.service.assistant.Commodity;
import com.mangocity.hotel.search.service.assistant.CommoditySummary;
import com.mangocity.hotel.search.service.assistant.HotelInfo;
import com.mangocity.hotel.search.service.assistant.HotelSummaryInfo;
import com.mangocity.hotel.search.service.assistant.RoomTypeInfo;
import com.mangocity.hotel.search.service.assistant.RoomTypeSummaryInfo;
import com.mangocity.hotel.search.service.assistant.SaleInfo;
import com.mangocity.hotel.search.vo.HotelResultVO;
import com.mangocity.hotel.search.vo.RoomTypeVO;

/**
 * 
 * @author chenkeming
 *
 */
public class EmptyHotelQueryHandler implements IHotelQueryHandler {
	
	private long curHotelId;
	
	private List<HotelResultVO> list = new ArrayList<HotelResultVO>();
	
	private HotelResultVO curHotel; 
	
	private RoomTypeVO curRoom;
	
	public void startHandleQueryHotel(int hotelCount) {
		
	}
	
	public void endHandleQueryHotel() {
	}	

	public void endHandleCommodity(CommoditySummary curComSummary) {
		// TODO Auto-generated method stub

	}

	public void endHandleHotel(HotelSummaryInfo curHotelSumInfo) {
		// TODO Auto-generated method stub
	}

	public void endHandleRoomType(RoomTypeSummaryInfo curRoomTypeSumInfo) {
		// TODO Auto-generated method stub

	}

	public void startHandleCommodity(final Commodity commodity) {
		// TODO Auto-generated method stub

	}

	public void startHandleHotel(final HotelInfo hotelInfo) {
		// TODO Auto-generated method stub
		curHotelId = hotelInfo.getHotelId();
		HotelResultVO hotel = new HotelResultVO();
		hotel.setChnName(hotelInfo.getChnName());
		
		list.add(hotel);
		
		curHotel = hotel;
	}

	public void startHandleRoomType(final RoomTypeInfo roomTypeInfo) {
		// TODO Auto-generated method stub
		
		RoomTypeVO room = new RoomTypeVO();
			
		room.setRoomtypeName(roomTypeInfo.getRoomtypeName());
		
		curHotel.getRoomTypes().add(room);
		
		curRoom = room;
		
	}
	
	public void handleSaleInfo(List<SaleInfo> liSaleInfo) {
		
	}

	public List<HotelResultVO> getList() {
		return list;
	}

	public void setList(List<HotelResultVO> list) {
		this.list = list;
	}	


}

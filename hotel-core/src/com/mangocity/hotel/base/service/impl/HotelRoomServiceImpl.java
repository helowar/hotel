package com.mangocity.hotel.base.service.impl;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.HotelRoomDAO;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.service.HotelRoomService;

public class HotelRoomServiceImpl implements HotelRoomService {
	
	private HotelRoomDAO hotelRoomDAO;

	public List<HtlRoom> qryHtlRoomByRoomTypeSaleDateRange(long roomTypeID, Date beginDate, Date endDate) {
		return hotelRoomDAO.qryHtlRoomByRoomTypeSaleDateRange(roomTypeID, beginDate, endDate);
	}
	
	public HtlRoom getHtlRoomByRoomId(long roomId) {
		return hotelRoomDAO.qryHtlRoomByRoomId(roomId);
	}
	
	public HtlRoom getRoomByRoomTypeHotelIdSaleDate(long roomTypeId, long hotelId, Date ableSaleDate) {
		return hotelRoomDAO.qryRoomByRoomTypeHotelIdSaleDate(roomTypeId, hotelId, ableSaleDate);
	}
	
	 public List<HtlRoom> getHtlRoomByHotelIdRoomType(long roomType, long hotelId, Date beginDate, Date endDate) {
		 return hotelRoomDAO.qryHtlRoomByHotelIdRoomType(roomType, hotelId, beginDate, endDate);
	 }
	
	public void updateHtlRoom(HtlRoom htlRoom) {
		hotelRoomDAO.updateHtlRoom(htlRoom);
	}
	
	public List<HtlRoom> getHtlRooms(Long hotelId, Long roomTypeId, Date checkinDate, Date checkoutDate) {
		// TODO Auto-generated method stub
		return hotelRoomDAO.getHtlRooms(hotelId, roomTypeId, checkinDate, checkoutDate);
	}
	public void setHotelRoomDAO(HotelRoomDAO hotelRoomDAO) {
		this.hotelRoomDAO = hotelRoomDAO;
	}

	

}

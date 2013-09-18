package com.mangocity.webnew.util;

import java.util.Date;

import com.mangocity.hdl.hotel.dto.CheckHotelReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckHotelReservateExResponse;
import com.mangocity.hdl.hotel.dto.CheckReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckReservateExResponse;
import com.mangocity.hdl.hotel.dto.MGExReservItem;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.log.MyLog;

public class HotelGetHdlPrice {
	private static final MyLog log = MyLog.getLogger(HotelGetHdlPrice.class);
	/**
     * HDL webservice服务
     */
    private IHDLService hdlService;
    
    
    /**
	 * 去获得价格
	 * 
	 * @return
	 */
	public CheckReservateExResponse getPrice(Long hotelId, Long roomTypeId, String childRoomTypeId, int roomChannel,
			Date checkInDate, Date checkOutDate, int quantity) throws Exception {
		CheckReservateExRequest req = new CheckReservateExRequest();

		req.setHotelId(hotelId);
		req.setRoomTypeId(roomTypeId);
		req.setChildRoomTypeId(new Long(childRoomTypeId));
		req.setCheckInDate(DateUtil.dateToString(checkInDate));
		req.setCheckOutDate(DateUtil.dateToString(checkOutDate));
		req.setQuantity(quantity);
		req.setChainCode(null);
		req.setChannelType(roomChannel);
		int difdays = DateUtil.getDay(checkInDate, checkOutDate);
		for (int i = 0; i < difdays; i++) {
			MGExReservItem item = new MGExReservItem();
			item.setDayIndex(i);
			item.setBasePrice((float) 0.0);
			item.setSalePrice((float) 0.0);
			req.getReservItems().add(item);
		}
		return hdlService.checkReservate(req);
	}
	
	/**
	 * 获取中旅的价格
	 */
	public CheckHotelReservateExResponse checkHotelReservate(int channelType,Long hotelId,Long roomTypeId,
			Long childRoomTypeId,Date checkInDate, Date checkOutDate,int quantity)throws Exception{
		CheckHotelReservateExRequest req=new CheckHotelReservateExRequest();
		
		req.setChannelType(channelType);
		req.setChainCode(null);
		req.setHotelId(hotelId);
		req.setRoomTypes(String.valueOf(roomTypeId));
		req.setChildRoomTypes(String.valueOf(childRoomTypeId));
		req.setCheckInDate(DateUtil.dateToString(checkInDate));
		req.setCheckOutDate(DateUtil.dateToString(checkOutDate));
		req.setQuantity(quantity);
		req.setFrom(null);
		
		return hdlService.checkHotelReservate(req);
	}

	public IHDLService getHdlService() {
		return hdlService;
	}

	public void setHdlService(IHDLService hdlService) {
		this.hdlService = hdlService;
	}
}

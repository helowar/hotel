package com.mangocity.tmchotel.util;

import java.util.Date;

import com.mangocity.hdl.hotel.dto.CheckReservateExRequest;
import com.mangocity.hdl.hotel.dto.CheckReservateExResponse;
import com.mangocity.hdl.hotel.dto.MGExReservItem;
import com.mangocity.hdl.service.IHDLService;
import com.mangocity.util.DateUtil;
import com.mangocity.util.log.MyLog;

public class HotelGetHdlPrice {
	/**
     * HDL webservice服务
     */
    private IHDLService hdlService;
    
    
    private CheckReservateExResponse res = null;
    private static final MyLog log = MyLog.getLogger(HotelGetHdlPrice.class);
    
    
    
    /**
     *去获得价格
     * @return
     */
    public CheckReservateExResponse getPrice(Long hotelId ,Long roomTypeId, String childRoomTypeId, 
    		int roomChannel, Date checkInDate, Date checkOutDate,int quantity){
    	CheckReservateExRequest req = new CheckReservateExRequest();
    	try{
    		req.setHotelId(hotelId);
        	req.setRoomTypeId(roomTypeId);
        	req.setChildRoomTypeId(new Long(childRoomTypeId));
        	req.setCheckInDate(DateUtil.dateToString(checkInDate));
        	req.setCheckOutDate(DateUtil.dateToString(checkOutDate));
        	req.setQuantity(quantity);
        	req.setChainCode(null);
        	req.setChannelType(roomChannel);
        	int difdays = DateUtil.getDay(checkInDate,checkOutDate);
        	for (int i = 0; i < difdays; i++) {
                MGExReservItem item = new MGExReservItem();
                item.setDayIndex(i);
                item.setBasePrice((float) 0.0);
                item.setSalePrice((float) 0.0);
                req.getReservItems().add(item);
            }
        	res = hdlService.checkReservate(req);
	
    	}catch(Exception e){
    		log.error(e);
    	}
    	
    	
    	
    	
    	return res;
    }
    
    
    

	public IHDLService getHdlService() {
		return hdlService;
	}

	public void setHdlService(IHDLService hdlService) {
		this.hdlService = hdlService;
	}

	public CheckReservateExResponse getRes() {
		return res;
	}


	public void setRes(CheckReservateExResponse res) {
		this.res = res;
	}
}

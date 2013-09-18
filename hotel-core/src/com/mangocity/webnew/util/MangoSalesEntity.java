package com.mangocity.webnew.util;

import java.io.Serializable;

/**
 * 芒果酒店促销的信息
 * @author shengwei.zuo
 * 
 */
public class MangoSalesEntity implements Serializable {
    
	//酒店ID
	private Long   hotelId;
	
	//房型ID
    private Long   roomTypeId;
    
    //价格类型ID
    private Long   priceTypeId;
    
    //停止售卖的日期
    private String  salesEndDate;

	public Long getHotelId() {
		if(hotelId == null){
			hotelId = 0L;
		}
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		if(hotelId==null){
			hotelId = 0L;
		}
		this.hotelId = hotelId;
	}

	public Long getRoomTypeId() {
		if(roomTypeId == null){
			roomTypeId = 0L;
		}
		return roomTypeId;
	}

	public void setRoomTypeId(Long roomTypeId) {
		if(roomTypeId == null){
			roomTypeId =0L;
		}
		this.roomTypeId = roomTypeId;
	}

	public Long getPriceTypeId() {
		if(priceTypeId == null){
			priceTypeId = 0L;
		}
		return priceTypeId;
	}

	public void setPriceTypeId(Long priceTypeId) {
		if(priceTypeId==null){
			priceTypeId = 0L;
		}
		this.priceTypeId = priceTypeId;
	}

	public String getSalesEndDate() {
		return salesEndDate;
	}

	public void setSalesEndDate(String salesEndDate) {
		this.salesEndDate = salesEndDate;
	}

    
}

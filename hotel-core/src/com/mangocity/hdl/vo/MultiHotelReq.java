 
package com.mangocity.hdl.vo;


/**
 * 和各个合作方直连的多酒店查询接口参数类 
 * 
 * @author chenkeming
 *
 */
public class MultiHotelReq {

    /**
     * 渠道
     */
    protected int channelType;
    
    /**
     * 城市编码
     */
    private String cityId;    
    
    /**
     * 入住日期字符串
     */
    protected String checkInDate;
    
    /**
     * 离店日期字符串
     */
    protected String checkOutDate;
    
    /**
     * 是否显示房型详细信息，'Y':是,'N':否 -- 暂时ZHX使用
     */
    private String roomTypeDetailShowed;

	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getRoomTypeDetailShowed() {
		return roomTypeDetailShowed;
	}

	public void setRoomTypeDetailShowed(String roomTypeDetailShowed) {
		this.roomTypeDetailShowed = roomTypeDetailShowed;
	}


}

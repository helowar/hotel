package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 酒店最基本信息
 */
public class HtlHotelBase implements Serializable {

    /**
     * 酒店ID
     */
	private String hotelId;
	/**
	 * 酒店编码
	 */
    private String hotelCode;
    /**
     * 酒店名称
     */
    private String hotelName;
    /**
     * 酒店编码for渠道
     */
    private String hotelcodeforchannel;
    /**
     * 酒店名称for渠道
     */
    private String hotelnameforchannel;
    
    /**
     * 此酒店在mango和供应商是否都存在，M代表只有Mango有，C代表只有供应商有,MC代表两边都有
     */
    private String mangoChannelBoth = "M";
    /**
     * 房型列表
     */
    private List<HtlRoomBase> roomBaseLst = new ArrayList<HtlRoomBase>();
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getHotelCode() {
		return hotelCode;
	}
	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public String getHotelcodeforchannel() {
		return hotelcodeforchannel;
	}
	public void setHotelcodeforchannel(String hotelcodeforchannel) {
		this.hotelcodeforchannel = hotelcodeforchannel;
	}
	public String getHotelnameforchannel() {
		return hotelnameforchannel;
	}
	public void setHotelnameforchannel(String hotelnameforchannel) {
		this.hotelnameforchannel = hotelnameforchannel;
	}
	public List<HtlRoomBase> getRoomBaseLst() {
		return roomBaseLst;
	}
	public void setRoomBaseLst(List<HtlRoomBase> roomBaseLst) {
		this.roomBaseLst = roomBaseLst;
	}
	public String getMangoChannelBoth() {
		return mangoChannelBoth;
	}
	public void setMangoChannelBoth(String mangoChannelBoth) {
		this.mangoChannelBoth = mangoChannelBoth;
	}
    
    
}

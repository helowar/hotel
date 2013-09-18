package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * 房型最基本信息
 */
public class HtlRoomBase implements Serializable {

    /**
     * 房型ID
     */
	private String roomTypeId;
	/**
	 * 房型编码
	 */
    private String roomtypecode;
    /**
     * 房型编码for渠道
     */
    private String roomtypecodeforchannel;
    /**
     * 房型名称for渠道
     */
    private String roomtypenameforchannel;
    /**
     * 此酒店在mango和供应商是否都存在，M代表只有Mango有，C代表只有供应商有,MC代表两边都有
     */
    private String mangoChannelBoth = "M";
    
    /**
     * 房型名称forMango
     */
    private String roomtypename;
	public String getRoomTypeId() {
		return roomTypeId;
	}
	public void setRoomTypeId(String roomTypeId) {
		this.roomTypeId = roomTypeId;
	}
	public String getRoomtypecode() {
		return roomtypecode;
	}
	public void setRoomtypecode(String roomtypecode) {
		this.roomtypecode = roomtypecode;
	}
	public String getRoomtypecodeforchannel() {
		return roomtypecodeforchannel;
	}
	public void setRoomtypecodeforchannel(String roomtypecodeforchannel) {
		this.roomtypecodeforchannel = roomtypecodeforchannel;
	}
	public String getRoomtypenameforchannel() {
		return roomtypenameforchannel;
	}
	public void setRoomtypenameforchannel(String roomtypenameforchannel) {
		this.roomtypenameforchannel = roomtypenameforchannel;
	}
	public String getRoomtypename() {
		return roomtypename;
	}
	public void setRoomtypename(String roomtypename) {
		this.roomtypename = roomtypename;
	}
	public String getMangoChannelBoth() {
		return mangoChannelBoth;
	}
	public void setMangoChannelBoth(String mangoChannelBoth) {
		this.mangoChannelBoth = mangoChannelBoth;
	}
   
    
    
}

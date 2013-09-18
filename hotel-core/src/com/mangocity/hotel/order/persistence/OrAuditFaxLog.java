package com.mangocity.hotel.order.persistence;


import java.util.Date;

import com.mangocity.util.Entity;


/**
 * 日审
 * 
 * @author chenkeming
 *
 */
public class OrAuditFaxLog implements Entity {            

    private static final long serialVersionUID = 4997600461099189265L;

    /**
	 * 传真日志ID 
	 */
    private Long ID;

    /**
	 * 酒店ID
	 */
    private Long hotelId;
    
    /**
	 * 酒店名称
	 */
    private String hotelName;
    
    /**
	 * 日审日期
	 */
    private Date night;
    
    /**
	 * 传真号码
	 */
    private String fax;    

    /**
	 * 操作时间
	 */
    private Date workTime;

    /**
	 * 操作人
	 */
    private String workName;
    
    /**
	 * 发送状态
	 */
    private int sendState;
    
    private Long channelId;
    
    private String channelName;
    /**
     * 给酒店的备注
     */
    private String remarkToHotel;

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Date getNight() {
        return night;
    }

    public void setNight(Date night) {
        this.night = night;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public Date getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Date workTime) {
        this.workTime = workTime;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getRemarkToHotel() {
		return remarkToHotel;
	}

	public void setRemarkToHotel(String remarkToHotel) {
		this.remarkToHotel = remarkToHotel;
	}



}

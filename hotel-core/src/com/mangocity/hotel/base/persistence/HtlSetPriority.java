package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 */
public class HtlSetPriority extends CEntity implements Entity {

    /**
     * id
     */
    private Long ID;

    /**
     * 酒店ID
     */
    private long hotelId;

    /**
     * 房态查询级别
     */
    private String priSeach;

    /**
     * CC查询级别
     */
    private String priCc;

    /**
     * 网站查询级别
     */
    private String priWeb;
    /**
     * 酒店询房时段
     */
    private String priTime;
   
    
    public String getPriCc() {
        return priCc;
    }

    public void setPriCc(String priCc) {
        this.priCc = priCc;
    }

    public String getPriSeach() {
        return priSeach;
    }

    public void setPriSeach(String priSeach) {
        this.priSeach = priSeach;
    }

    public String getPriWeb() {
        return priWeb;
    }

    public void setPriWeb(String priWeb) {
        this.priWeb = priWeb;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

	public String getPriTime() {
		return priTime;
	}

	public void setPriTime(String priTime) {
		this.priTime = priTime;
	}

	

}

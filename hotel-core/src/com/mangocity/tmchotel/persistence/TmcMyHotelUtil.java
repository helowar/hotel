package com.mangocity.tmchotel.persistence;

/**
 * 
 * 酒店交叉销售 TMC-V2.0
 * add by shengwei.zuo 
 */

public class TmcMyHotelUtil implements java.io.Serializable{
	
    /**
	 * 酒店ID
	 */
    private Long hotelid;

    /**
	 * 酒店名称
	 */
    private String hotelname;

	public Long getHotelid() {
		return hotelid;
	}

	public void setHotelid(Long hotelid) {
		this.hotelid = hotelid;
	}

	public String getHotelname() {
		return hotelname;
	}

	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}
    


  
}
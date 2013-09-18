package com.mangocity.hotel.base.manage.assistant;

public class HotelAddressInfo {
	  /**
     * 省份代码
     */
    private String state;
    
    private String city;
    
    private String zone;
    
    private String chnAddress;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getChnAddress() {
		return chnAddress;
	}

	public void setChnAddress(String chnAddress) {
		this.chnAddress = chnAddress;
	}
    

}

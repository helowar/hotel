//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.12.25 at 10:58:34 ���� CST 
//


package com.mangocity.hdl.hotel.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "hotelIds",
    "checkInDate",
    "checkOutDate"
})
@XmlRootElement(name = "PriceCompareRequest")
public class PriceCompareRequest {
	/**
	 * 酒店ID
	 */
	@XmlElement(required = true)
    protected String hotelIds;
	
    @XmlElement(required = true)
    protected Date checkInDate;
    
    @XmlElement(required = true)
    protected Date checkOutDate;
    
    
	public String getHotelIds() {
		return hotelIds;
	}
	public void setHotelIds(String hotelIds) {
		this.hotelIds = hotelIds;
	}
	public Date getCheckInDate() {
		return checkInDate;
	}
	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}
	public Date getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
    

}

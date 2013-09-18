package com.mangocity.tmc.persistence;

import java.util.Date;
import java.util.GregorianCalendar;

public class HotelContractPrice implements java.io.Serializable,java.lang.Comparable {


    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Fields   
     private Long priceid;
     private Long hotelContractid;
     private Long hotelid;
     private Long roomtypeid;
     private String roomname;
     private Double price;
     private String isinclude;
     private String servicefeetype;
     private Double servicefee;
     private String breakfasttype;
     private Long breakfastnum;
     private String includenet;
     private Double netfee;
     private Date pricedate;
     private String priceweek;


     public int compareTo(Object otherPrice) 
     {
    	 int i = 0;
    	 final HotelContractPrice other = (HotelContractPrice) otherPrice;
    	 
    	 GregorianCalendar  arriveCalendar = new GregorianCalendar ();
		 arriveCalendar.setTime(this.getPricedate());
		
		GregorianCalendar  leaveCalendar = new GregorianCalendar ();
		leaveCalendar.setTime(other.getPricedate());
    	 
 		 i = arriveCalendar.compareTo(leaveCalendar);
    	 return i;
     }
    // Property accessors


    public Long getHotelid() {
        return this.hotelid;
    }
    
    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public Long getRoomtypeid() {
        return this.roomtypeid;
    }
    
    public void setRoomtypeid(Long roomtypeid) {
        this.roomtypeid = roomtypeid;
    }

    public String getRoomname() {
        return this.roomname;
    }
    
    public void setRoomname(String roomname) {
        this.roomname = roomname;
    }

    public Double getPrice() {
        return this.price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }

    public String getIsinclude() {
        return this.isinclude;
    }
    
    public void setIsinclude(String isinclude) {
        this.isinclude = isinclude;
    }

    public String getServicefeetype() {
        return this.servicefeetype;
    }
    
    public void setServicefeetype(String servicefeetype) {
        this.servicefeetype = servicefeetype;
    }

    public Double getServicefee() {
        return this.servicefee;
    }
    
    public void setServicefee(Double servicefee) {
        this.servicefee = servicefee;
    }

    public String getBreakfasttype() {
        return this.breakfasttype;
    }
    
    public void setBreakfasttype(String breakfasttype) {
        this.breakfasttype = breakfasttype;
    }

    public Long getBreakfastnum() {
        return this.breakfastnum;
    }
    
    public void setBreakfastnum(Long breakfastnum) {
        this.breakfastnum = breakfastnum;
    }

    public Date getPricedate() {
        return this.pricedate;
    }
    
    public void setPricedate(Date pricedate) {
        this.pricedate = pricedate;
    }

    public String getPriceweek() {
        return this.priceweek;
    }
    
    public void setPriceweek(String priceweek) {
        this.priceweek = priceweek;
    }

	public String getIncludenet() {
		return includenet;
	}

	public void setIncludenet(String includenet) {
		this.includenet = includenet;
	}

	public Double getNetfee() {
		return netfee;
	}

	public void setNetfee(Double netfee) {
		this.netfee = netfee;
	}


    public Long getHotelContractid() {
        return hotelContractid;
    }


    public void setHotelContractid(Long hotelContractid) {
        this.hotelContractid = hotelContractid;
    }


    public Long getPriceid() {
        return priceid;
    }


    public void setPriceid(Long priceid) {
        this.priceid = priceid;
    }
}
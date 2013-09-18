package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * TbhotelorderTraffic entity. @author MyEclipse Persistence Tools
 */

public class HotelorderTraffic implements Entity, Comparable<HotelorderTraffic> {

	// Fields

	private Long trafficid;
	private String place;
	private String traffictype;
	private String meetservice;
	private Date meettime;
	private String meetplane;
	private String sendservice;
	private Date sendtime;
	private String sendplane;
	private Double servicefee;
	private String isselected;
	private String status;
	private String creator;
	private Date createdate;
	private Long showindex;
	private Double trafficsingleprice;
	private Double trafficdoubleprice;
	private String creatorname;

	// Constructors

	/** default constructor */
	public HotelorderTraffic() {
	}

	/** full constructor */
	public HotelorderTraffic(String place,
			String traffictype, String meetservice, Date meettime,
			String meetplane, String sendservice, Date sendtime,
			String sendplane, Double servicefee, String isselected,
			String status, String creator, Date createdate, Long showindex,
			Double trafficsingleprice, Double trafficdoubleprice,
			String creatorname) {
		this.place = place;
		this.traffictype = traffictype;
		this.meetservice = meetservice;
		this.meettime = meettime;
		this.meetplane = meetplane;
		this.sendservice = sendservice;
		this.sendtime = sendtime;
		this.sendplane = sendplane;
		this.servicefee = servicefee;
		this.isselected = isselected;
		this.status = status;
		this.creator = creator;
		this.createdate = createdate;
		this.showindex = showindex;
		this.trafficsingleprice = trafficsingleprice;
		this.trafficdoubleprice = trafficdoubleprice;
		this.creatorname = creatorname;
	}

	// Property accessors

	public Long getTrafficid() {
		return this.trafficid;
	}

	public void setTrafficid(Long trafficid) {
		this.trafficid = trafficid;
	}

	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getTraffictype() {
		return this.traffictype;
	}

	public void setTraffictype(String traffictype) {
		this.traffictype = traffictype;
	}

	public String getMeetservice() {
		return this.meetservice;
	}

	public void setMeetservice(String meetservice) {
		this.meetservice = meetservice;
	}

	public Date getMeettime() {
		return this.meettime;
	}

	public void setMeettime(Date meettime) {
		this.meettime = meettime;
	}

	public String getMeetplane() {
		return this.meetplane;
	}

	public void setMeetplane(String meetplane) {
		this.meetplane = meetplane;
	}

	public String getSendservice() {
		return this.sendservice;
	}

	public void setSendservice(String sendservice) {
		this.sendservice = sendservice;
	}

	public Date getSendtime() {
		return this.sendtime;
	}

	public void setSendtime(Date sendtime) {
		this.sendtime = sendtime;
	}

	public String getSendplane() {
		return this.sendplane;
	}

	public void setSendplane(String sendplane) {
		this.sendplane = sendplane;
	}

	public Double getServicefee() {
		return this.servicefee;
	}

	public void setServicefee(Double servicefee) {
		this.servicefee = servicefee;
	}

	public String getIsselected() {
		return this.isselected;
	}

	public void setIsselected(String isselected) {
		this.isselected = isselected;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Long getShowindex() {
		return this.showindex;
	}

	public void setShowindex(Long showindex) {
		this.showindex = showindex;
	}

	public Double getTrafficsingleprice() {
		return this.trafficsingleprice;
	}

	public void setTrafficsingleprice(Double trafficsingleprice) {
		this.trafficsingleprice = trafficsingleprice;
	}

	public Double getTrafficdoubleprice() {
		return this.trafficdoubleprice;
	}

	public void setTrafficdoubleprice(Double trafficdoubleprice) {
		this.trafficdoubleprice = trafficdoubleprice;
	}

	public String getCreatorname() {
		return this.creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

    public Long getID() {
        return this.trafficid;
    }

    public int compareTo(HotelorderTraffic o) {
        
        if(null == o){
            return -1;
        }
        
        if(null == this.showindex){
            return 1;
        }
        
        if(null == o.getShowindex()){
            return -1;
        }
        
        return this.showindex.compareTo(o.getShowindex());
    }

}
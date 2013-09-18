package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * TbhotelorderFee entity. @author yong.zeng by 2009-7-22
 * 
 */

public class HotelorderFee implements Entity, Comparable<HotelorderFee> {

	// Fields
    private Long feeid;
	private String feetype;
	private Date begindate;
	private Date enddate;
	private Short feecount;
	private Double feeprice;
	private String status;
	private String creator;
	private Date createdate;
	private Long showindex;
	private String creatorname;
    
    private String dateStr;

	// Constructors

	/** default constructor */
	public HotelorderFee() {
	}

	/** full constructor */
	public HotelorderFee(String feetype,
			Date begindate, Date enddate, Short feecount, Double feeprice,
			String status, String creator, Date createdate, Long showindex,
			String creatorname) {
		this.feetype = feetype;
		this.begindate = begindate;
		this.enddate = enddate;
		this.feecount = feecount;
		this.feeprice = feeprice;
		this.status = status;
		this.creator = creator;
		this.createdate = createdate;
		this.showindex = showindex;
		this.creatorname = creatorname;
	}

	// Property accessors

	public Long getFeeid() {
		return this.feeid;
	}

	public void setFeeid(Long feeid) {
		this.feeid = feeid;
	}


	public String getFeetype() {
		return this.feetype;
	}

	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}

	public Date getBegindate() {
		return this.begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Short getFeecount() {
		return this.feecount;
	}

	public void setFeecount(Short feecount) {
		this.feecount = feecount;
	}

	public Double getFeeprice() {
		return this.feeprice;
	}

	public void setFeeprice(Double feeprice) {
		this.feeprice = feeprice;
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

	public String getCreatorname() {
		return this.creatorname;
	}

	public void setCreatorname(String creatorname) {
		this.creatorname = creatorname;
	}

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getID() {
        return this.feeid;
    }

    public int compareTo(HotelorderFee o) {
        
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
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;


/**
 *  Hotel 2.8.1
 *  转和约组
 *  @author guojun
 */

public class OrToContractgroup implements Entity{


    /**
	 * serial Version UID
	 */
    private static final long serialVersionUID = 1L;
    
    // Fields    
     private Long ID;
     private Long orderid;
     private String ordercd;
     private Long hotelid;
     private Long difficultReason;
     private Long difficultType;
     private Long difficultDep;
     private Date tocontracttime;
     private String contracter;
     private Date toroomcontime;
     private String roomcontroller;
     private String remark;
     private String roomresponse;
     private String confirst;
     private String consecond;
     
     private Date ccToRSCTime;
     private Date rscToCCTime;
     /**
      * 合约处理结果　房控重构
      */
     private Integer contactResult;
     /**
      * 合约转房控的处理人
      */
     private String contractToRoomcontroller;


    public String getContractToRoomcontroller() {
		return contractToRoomcontroller;
	}

	public void setContractToRoomcontroller(String contractToRoomcontroller) {
		this.contractToRoomcontroller = contractToRoomcontroller;
	}

	// Constructors
    /** default constructor */
    public OrToContractgroup() {
    }

    /** minimal constructor */
    public OrToContractgroup(Long orToContractgroupId, Long orderid) {
        this.ID = orToContractgroupId;
        this.orderid = orderid;
    }
    
    /** full constructor */
    public OrToContractgroup(Long orToContractgroupId, Long orderid, String ordercd,
            Long hotelid, Date tocontracttime, String contracter,
            Date toroomcontime, String roomcontroller, String remark, 
            String roomresponse, String confirst, String consecond) {
        this.ID = orToContractgroupId;
        this.orderid = orderid;
        this.ordercd = ordercd;
        this.hotelid = hotelid;
        this.tocontracttime = tocontracttime;
        this.contracter = contracter;
        this.toroomcontime = toroomcontime;
        this.roomcontroller = roomcontroller;
        this.remark = remark;
        this.roomresponse = roomresponse;
        this.confirst = confirst;
        this.consecond = consecond;
    }

   
    // Property accessors



    public Long getOrderid() {
        return this.orderid;
    }
    
    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public String getOrdercd() {
        return this.ordercd;
    }
    
    public void setOrdercd(String ordercd) {
        this.ordercd = ordercd;
    }

    public Long getHotelid() {
        return this.hotelid;
    }
    
    public void setHotelid(Long hotelid) {
        this.hotelid = hotelid;
    }

    public Date getTocontracttime() {
        return this.tocontracttime;
    }
    
    public void setTocontracttime(Date tocontracttime) {
        this.tocontracttime = tocontracttime;
    }

    public String getContracter() {
        return this.contracter;
    }
    
    public void setContracter(String contracter) {
        this.contracter = contracter;
    }

    public Date getToroomcontime() {
        return this.toroomcontime;
    }
    
    public void setToroomcontime(Date toroomcontime) {
        this.toroomcontime = toroomcontime;
    }

    public String getRoomcontroller() {
        return this.roomcontroller;
    }
    
    public void setRoomcontroller(String roomcontroller) {
        this.roomcontroller = roomcontroller;
    }

    public String getRoomresponse() {
        return this.roomresponse;
    }
    
    public void setRoomresponse(String roomresponse) {
        this.roomresponse = roomresponse;
    }

    public String getConfirst() {
        return this.confirst;
    }
    
    public void setConfirst(String confirst) {
        this.confirst = confirst;
    }

    public String getConsecond() {
        return this.consecond;
    }
    
    public void setConsecond(String consecond) {
        this.consecond = consecond;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public Long getDifficultDep() {
        return difficultDep;
    }

    public void setDifficultDep(Long difficultDep) {
        this.difficultDep = difficultDep;
    }

    public Long getDifficultReason() {
        return difficultReason;
    }

    public void setDifficultReason(Long difficultReason) {
        this.difficultReason = difficultReason;
    }

    public Long getDifficultType() {
        return difficultType;
    }

    public void setDifficultType(Long difficultType) {
        this.difficultType = difficultType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCcToRSCTime() {
        return ccToRSCTime;
    }

    public void setCcToRSCTime(Date ccToRSCTime) {
        this.ccToRSCTime = ccToRSCTime;
    }

    public Date getRscToCCTime() {
        return rscToCCTime;
    }

    public void setRscToCCTime(Date rscToCCTime) {
        this.rscToCCTime = rscToCCTime;
    }

	public Integer getContactResult() {
		return contactResult;
	}

	public void setContactResult(Integer contactResult) {
		this.contactResult = contactResult;
	}
   

}
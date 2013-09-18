package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class HtlHotelExt implements Entity, Serializable {

    private Long ID;

    // 扩展ID
    private long hotelExtID;

    // 房控负责人
    private String roomStateManager;

    // 不接受某国家入住的开始日期
    private Date refuseBeginDate;

    // 不接受某国家入住的结束日期
    private Date refuseEndDate;

    private HtlHotel htlHotel;

    /**
     * add by wuyun 2008-11-26 15:30 直联标志
     * 
     * @return
     */
    private String cooperateChannel;
    
    /**
     * add by xie yanhui 2011-7-19 11:30 酒店多渠道预定是否在外网销售，1:是,0:否 默认为0
     * 
     * @return
     */
    private String isOuterNetSale;

    /**
     * hotel 2.5.0 酒店订单是否允许修改,默认状态是允许修改 add by guojun 2009-02-01 15:47
     * 
     * @return
     */
    private Long isModify;

    /**
     * 是否激活为港中旅港澳直联酒店 1:是,0:否 默认为0 V2.8 add by chenjiajie 2009-03-16
     */
    private String isCTSHotel;

    /**
     * v2.8.1 by juesu.chen 2009-05-07 停止售卖的开始日期
     */
    private Date stopSellBeginDate;

    /**
     * v2.8.1 by juesu.chen 2009-05-07 停止售卖的结束日期
     */
    private Date stopSellEndDate;

    /**
     * v2.8.1 by juesu.chen 2009-05-07 停止售卖的勾选参数，'1'为有效
     */
    private String isStopSell;


    /**
     * v2.8.1 by zhijie.gu 2009-08-11 标示是否为青芒果酒店，'1'为是、'0'为不是
     */
    private String isGreenMangoHotel;
	
	    /**
     * v2.10 by shaojun.yang 2009-11-16 酒店免费服务备注信息
     */
    private String freeServiceRemark;
    /**
     * v2.10 by shaojun.yang 2009-11-16 酒店客房设施备注信息
     */
    private String roomFixtrueRemark;
    
    /**
     * v2.10 by shaojun.yang 2009-11-25 酒店信用卡注意事项
     */
    private String creditCardRemark;
    /**
     * v2.10 by shaojun.yang 2009-11-25 是否接受信用卡（1：接受；0：不接受）
     */
    private String isUseCreditCard;
	
	
	    /**
     * 本部优化，酒店停售原因
     */
    private String stopSellReason;
    /**
     * 本部优化，酒店停售原因备注
     */
    private String stopSellNote;

    public String getIsStopSell() {
        return isStopSell;
    }

    public void setIsStopSell(String isStopSell) {
        this.isStopSell = isStopSell;
    }

    public String getCooperateChannel() {
        return cooperateChannel;
    }

    public void setCooperateChannel(String cooperateChannel) {
        this.cooperateChannel = cooperateChannel;
    }

    public Date getRefuseBeginDate() {
        return refuseBeginDate;
    }

    public void setRefuseBeginDate(Date refuseBeginDate) {
        this.refuseBeginDate = refuseBeginDate;
    }

    public Date getRefuseEndDate() {
        return refuseEndDate;
    }

    public void setRefuseEndDate(Date refuseEndDate) {
        this.refuseEndDate = refuseEndDate;
    }

    public String getRoomStateManager() {
        return roomStateManager;
    }

    public void setRoomStateManager(String roomStateManager) {
        this.roomStateManager = roomStateManager;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public long getHotelExtID() {
        return hotelExtID;
    }

    public void setHotelExtID(long hotelExtID) {
        this.hotelExtID = hotelExtID;
    }

    public Long getIsModify() {
        return isModify;
    }

    public void setIsModify(Long isModify) {
        this.isModify = isModify;
    }

    public String getIsCTSHotel() {
        return isCTSHotel;
    }

    public void setIsCTSHotel(String isCTSHotel) {
        this.isCTSHotel = isCTSHotel;
    }

    public Date getStopSellBeginDate() {
        return stopSellBeginDate;
    }

    public void setStopSellBeginDate(Date stopSellBeginDate) {
        this.stopSellBeginDate = stopSellBeginDate;
    }

    public Date getStopSellEndDate() {
        return stopSellEndDate;
    }

    public String getStopSellNote() {
		return stopSellNote;
	}

	public void setStopSellNote(String stopSellNote) {
		this.stopSellNote = stopSellNote;
	}

	public void setStopSellEndDate(Date stopSellEndDate) {
        this.stopSellEndDate = stopSellEndDate;
    }

    public String getIsGreenMangoHotel() {
        return isGreenMangoHotel;
    }

    public void setIsGreenMangoHotel(String isGreenMangoHotel) {
        this.isGreenMangoHotel = isGreenMangoHotel;
    }

	public String getStopSellReason() {
		return stopSellReason;
	}

	public void setStopSellReason(String stopSellReason) {
		this.stopSellReason = stopSellReason;
	}
	
		public String getFreeServiceRemark() {
		return freeServiceRemark;
	}

	public void setFreeServiceRemark(String freeServiceRemark) {
		this.freeServiceRemark = freeServiceRemark;
	}

	public String getRoomFixtrueRemark() {
		return roomFixtrueRemark;
	}

	public void setRoomFixtrueRemark(String roomFixtrueRemark) {
		this.roomFixtrueRemark = roomFixtrueRemark;
	}

	public String getCreditCardRemark() {
		return creditCardRemark;
	}

	public void setCreditCardRemark(String creditCardRemark) {
		this.creditCardRemark = creditCardRemark;
	}

	public String getIsUseCreditCard() {
		return isUseCreditCard;
	}

	public void setIsUseCreditCard(String isUseCreditCard) {
		this.isUseCreditCard = isUseCreditCard;
	}

	public String getIsOuterNetSale() {
		return isOuterNetSale;
	}

	public void setIsOuterNetSale(String isOuterNetSale) {
		this.isOuterNetSale = isOuterNetSale;
	}
}

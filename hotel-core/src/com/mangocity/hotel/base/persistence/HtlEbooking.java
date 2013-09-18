package com.mangocity.hotel.base.persistence;

import java.io.Serializable;

import com.mangocity.util.Entity;

/**
 */
public class HtlEbooking implements Entity, Serializable {

    private HtlHotel htlHotel;

    private long ebookingID;

    /**
     * 是否是ebooking酒店
     */
    private String whetherEbooking;

    /**
     * 价格是否审核
     */
    private String whetherPrice;

    /**
     * 房态是否审核
     */
    private String whetherRoomType;
    
    /**
     * 是否有权限修改合同配额
     */
    private String whetherContack;
    /**
     * 面付是否自由加幅
     */
    private String whetherPayIncrease;

    public Long getID() {
        // TODO 自动生成方法存根
        return null;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public long getEbookingID() {
        return ebookingID;
    }

    public void setEbookingID(long ebookingID) {
        this.ebookingID = ebookingID;
    }

    public String getWhetherEbooking() {
        return whetherEbooking;
    }

    public void setWhetherEbooking(String whetherEbooking) {
        this.whetherEbooking = whetherEbooking;
    }

    public String getWhetherPrice() {
        return whetherPrice;
    }

    public void setWhetherPrice(String whetherPrice) {
        this.whetherPrice = whetherPrice;
    }

    public String getWhetherRoomType() {
        return whetherRoomType;
    }

    public void setWhetherRoomType(String whetherRoomType) {
        this.whetherRoomType = whetherRoomType;
    }

	public String getWhetherContack() {
		return whetherContack;
	}

	public void setWhetherContack(String whetherContack) {
		this.whetherContack = whetherContack;
	}

	public String getWhetherPayIncrease() {
		return whetherPayIncrease;
	}

	public void setWhetherPayIncrease(String whetherPayIncrease) {
		this.whetherPayIncrease = whetherPayIncrease;
	}

}

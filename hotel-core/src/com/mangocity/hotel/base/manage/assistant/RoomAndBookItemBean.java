package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlRoom;

/**
 * 主要是用来关系一个房间的配额+当天的预订条款,辅助显示按天调整的房态
 * 
 * @author xiaowumi
 * 
 */
public class RoomAndBookItemBean implements Serializable {
	
	private long roomTypeId;

    /**
     * 房间
     */
    private HtlRoom room;

    /**
     * 床型
     */
    private String bedType;

    /**
     * 临时配额
     */
    private List tempQuota;
    
    
    /**
     * 配额改造新的临时配额
     */
    private List tempQuotaNew;
    /**
     * 配额
     */
    private HtlQuota quota;

    /**
     * 预订条款
     */
    private HtlCreditAssure bookItemCreditAssure;

    /**
     * 关房原因
     */
    private List closeRoomReasonBean;

    /**
     * 判断该房型是否都关房 '1'是 '0'不是 add by zhineng.zhuang 2008-09-27
     */
    private String isAllClose;

    /**
     * 把所有的配额类型的配额相加，用于房态界面的显示
     */
    private int totalAvail;

    private int totalUsed;
    
    /**
     * 销售价格
     * add by shaojun.yang
     * 房控改造
     */
    private double salePrice;

    
    public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public int getTotalUsed() {
        return totalUsed;
    }

    public void setTotalUsed(int totalUsed) {
        this.totalUsed = totalUsed;
    }

    public HtlCreditAssure getBookItemCreditAssure() {
        return bookItemCreditAssure;
    }

    public void setBookItemCreditAssure(HtlCreditAssure bookItemCreditAssure) {
        this.bookItemCreditAssure = bookItemCreditAssure;
    }

    public HtlQuota getQuota() {
        return quota;
    }

    public void setQuota(HtlQuota quota) {
        this.quota = quota;
    }

    public HtlRoom getRoom() {
        return room;
    }

    public void setRoom(HtlRoom room) {
        this.room = room;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public List getTempQuota() {
        return tempQuota;
    }

    public void setTempQuota(List tempQuota) {
        this.tempQuota = tempQuota;
    }

    public List getCloseRoomReasonBean() {
        return closeRoomReasonBean;
    }

    public void setCloseRoomReasonBean(List closeRoomReasonBean) {
        this.closeRoomReasonBean = closeRoomReasonBean;
    }

    public String getIsAllClose() {
        return isAllClose;
    }

    public void setIsAllClose(String isAllClose) {
        this.isAllClose = isAllClose;
    }

    public int getTotalAvail() {
        return totalAvail;
    }

    public void setTotalAvail(int totalAvail) {
        this.totalAvail = totalAvail;
    }

	public List getTempQuotaNew() {
		return tempQuotaNew;
	}

	public void setTempQuotaNew(List tempQuotaNew) {
		this.tempQuotaNew = tempQuotaNew;
	}

	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

}

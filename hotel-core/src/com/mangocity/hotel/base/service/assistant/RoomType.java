package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hdl.constant.ChannelType;

/**
 * @author zhengxin 描述房型
 */
public class RoomType implements Serializable {
	
	private static final long serialVersionUID = -5144215744862222721L;

    /**
     * 房型ID
     */
    private String roomTypeId;

    /**
     * 子房型ID
     */
    private String childRoomTypeId;

    /**
     * 房型名称
     */
    private String roomTypeName;

    /**
     * 子房型名称
     */
    private String childRoomTypeName;

    /**
     * 门市价
     */
    private double roomPrice;

    /**
     * 推荐级别
     */
    private String recommend;

	/**
	 * 房间数量 add shengwei.zuo hotel2.9.3 2009-09-09
	 */
	private String canRoomNumber;

	/**
	 * 开关房标志
	 */
    private String closeFlag;

    /**
     * 开关房原因
     */
    private String reason;

    /**
     * 预订按钮显示条件，yud=4表示因为满房或不可超且配额为0, 因此不可预订
     */
    private int yud = -1;

    /**
     * 某个房型的渠道
     * 
     * @author chenkeming Mar 20, 2009 4:21:12 PM
     * @see ChannelType
     */
    private int roomChannel = 0;

    /**
     * 销售明细
     */
    private List<SaleItem> saleItems = new ArrayList<SaleItem>();

    private int totalCount = 0;

    /**
     * 子房型是否有优惠信息
     */
    private int hasPromo;

    /**
     * 子房型优惠信息内容
     */
    private String promoContent;

    /**
     * 子房型优惠起始日期字符串
     */
    private String promoBeginEnd;

    /**
     * 子房型是否有提示信息 add by shengwei.zuo 2009-08-18
     */
    private int hasTip;

    /**
     * 子房型提示信息内容 add by shengwei.zuo 2009-08-18
     */
    private String tipContent;
    
    /**
     * 单个价格类型在入住期间优惠立减总金额 add by chenjiajie 2009-10-15
     */
    private int benefitAmount = 0;
    
    /**
     * 面付现金返还金额 add by linpeng.fang
     */
    private int payCashReturnAmount;
    
    /**
     * 预付现金返现金额
     * @return
     */
    
    private int prePayCashReturnAmount;

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItem> roomItems) {
        this.saleItems = roomItems;
    }

    public int getYud() {
        return yud;
    }

    public void setYud(int yud) {
        this.yud = yud;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public String getChildRoomTypeName() {
        return childRoomTypeName;
    }

    public void setChildRoomTypeName(String childRoomTypeName) {
        this.childRoomTypeName = childRoomTypeName;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(String roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(String closeFlag) {
        this.closeFlag = closeFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getRoomChannel() {
        return roomChannel;
    }

    public void setRoomChannel(int roomChannel) {
        this.roomChannel = roomChannel;
    }

    /**
     * 该房型是否中旅房型
     * 
     * @author chenkeming Mar 20, 2009 5:27:42 PM
     * @return
     */
    public boolean isCtsHK() {
        return roomChannel == ChannelType.CHANNEL_CTS;
    }

    public int getHasPromo() {
        return hasPromo;
    }

    public void setHasPromo(int hasPromo) {
        this.hasPromo = hasPromo;
    }

    public String getPromoBeginEnd() {
        return promoBeginEnd;
    }

    public void setPromoBeginEnd(String promoBeginEnd) {
        this.promoBeginEnd = promoBeginEnd;
    }

    public String getPromoContent() {
        return promoContent;
    }

    public void setPromoContent(String promoContent) {
        this.promoContent = promoContent;
    }

    public int getHasTip() {
        return hasTip;
    }

    public void setHasTip(int hasTip) {
        this.hasTip = hasTip;
    }

    public String getTipContent() {
        return tipContent;
    }

    public void setTipContent(String tipContent) {
        this.tipContent = tipContent;
    }
	public String getCanRoomNumber() {
		return canRoomNumber;
	}


	public void setCanRoomNumber(String canRoomNumber) {
		this.canRoomNumber = canRoomNumber;
	}

	public int getBenefitAmount() {
		return benefitAmount;
	}

	public void setBenefitAmount(int benefitAmount) {
		this.benefitAmount = benefitAmount;
	}
	
	public int getPayCashReturnAmount() {
		return payCashReturnAmount;
	}

	public void setPayCashReturnAmount(int payCashReturnAmount) {
		this.payCashReturnAmount = payCashReturnAmount;
	}

	public int getPrePayCashReturnAmount() {
		return prePayCashReturnAmount;
	}

	public void setPrePayCashReturnAmount(int prePayCashReturnAmount) {
		this.prePayCashReturnAmount = prePayCashReturnAmount;
	}
}

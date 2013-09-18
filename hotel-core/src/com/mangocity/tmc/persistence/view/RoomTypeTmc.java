package com.mangocity.tmc.persistence.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hdl.constant.ChannelType;

/**
 * @author zhengxin
 * 描述房型
 */
public class RoomTypeTmc implements Serializable {
    
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
	 * 开关房标志?
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
	 * @author chenkeming Mar 20, 2009 4:21:12 PM
	 * @see ChannelType
	 */
	private int roomChannel = 0;
	
	/**
	 * 销售明细
	 */
	private List<SaleItemTmc>  saleItemsTmc = new ArrayList<SaleItemTmc>();
	
	
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


	public List<SaleItemTmc> getSaleItems() {
		return saleItemsTmc;
	}


	public void setSaleItems(List<SaleItemTmc> roomItems) {
		this.saleItemsTmc = roomItems;
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
}

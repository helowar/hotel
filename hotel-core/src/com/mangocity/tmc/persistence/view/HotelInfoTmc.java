package com.mangocity.tmc.persistence.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author xiaowumi 这个类主要用来显示CC查询酒店的结果的一条记录。
 */

public class HotelInfoTmc implements Serializable {
    
    private static final long serialVersionUID = -6806610717324239641L;
    
	// 酒店id
	private long hotelId;

	// 酒店中文名称
	private String hotelChnName;

	// 酒店英文名称
	private String hotelEngName;

	// 酒店星级
	private String hotelStar;
	
	/**
	 * 酒店数字星级
	 */
	private float hotelStar1;

	// 酒店类型
	private String hotelType;

	// 酒店中文地址
	private String chnAddress;

	/**
	 * 酒店介绍
	 */
	private String hotelIntroduce;
	
	/**
	 * 酒店详细介绍
	 */
	private String hotelChnIntroduce;
	
	/**
	 * 城市
	 */
	private String city;

	// 提示信息
	private String clueInfo;

	// 取消信息
	private String cancelMessage;

	// 地区
	private String bizZone;

	// 最低价
	private String lowestPrice;

	// 结算方式
	private String balanceMethod;

	// 酒店预付款的支付时限
	private Date advancePayTime;
	
	/*
	 * 兑换汇率
	 */
	private String rateValue;
	/*
	 * 合同币种
	 */
	private String currency;

	// 房间列表，是RoomType类的集合
	private List<RoomTypeTmc> roomTypesTmc = new ArrayList<RoomTypeTmc>();
	
	// 房型数量
	private int roomTypesSize;
	
	private String acceptCustom;
	 
	/**
	 * 是否400酒店
	 */
	private boolean flag400;
	
	// 主推类型
	private String commendType;	 
	 
	/**
	 *促销信息 
	 */
	// private int isSalesPromo;
	  
	/**
	 *最近用户评论 
	 */
	private int isUserComment;
	
	/**
	 * 房费需另缴税
	 */
	private int isTaxCharges;
	
	/**
	 * 房费需另缴税集合
	 * 用于房费需另缴税直接显示结果 v2.9.2 addby chenjuesu
	 */
	private List lstRoomTaxChargeTmc;
	
	/**
	 * add by wuyun 2008-11-26 15:30
	 * 酒店直联信息，0表示本部，其他值表示是直联酒店
	 */
	private String cooperateChannel;
	
	/**
	 * 中旅酒店标识
	 * @author chenkeming Mar 16, 2009 4:00:44 PM
	 */
	private boolean flagCtsHK = false;
    
    /**
     * 是否停止销售, true:停止, false:不停止
     * @author chenkeming May 12, 2009 1:25:26 PM
     */
    private boolean flagStopSell = false;
    
    /***************** 芒果网大礼包相关信息 begin ***************/
    
    /**
     * 酒店是否有芒果网大礼包优惠信息
     * 1: 有
     * 0: 无
     */
    private int hasPreSale;
    
    /**
     * 芒果网优惠信息名称
     */
    private String preSaleName;
    
    /**
     * 芒果网优惠信息内容
     */
    private String preSaleContent;
    
    /**
     * 芒果网优惠起止日期字符串
     */
    private String preSaleBeginEnd;
    
    /**
     * 芒果网优惠URL
     */
    private String preSaleURL;
    
    /***************** 芒果网大礼包相关信息 end ***************/
    
    /**
     * 用来传递总页数
     * @author chenkeming Nov 11, 2009 10:13:03 AM
     */
    private int totalPage = 0;
	
	
	public String getCooperateChannel() {
		return cooperateChannel;
	}

	public void setCooperateChannel(String cooperateChannel) {
		this.cooperateChannel = cooperateChannel;
	}

	public String getCancelMessage() {
		return cancelMessage;
	}

	public void setCancelMessage(String cancelMessage) {
		this.cancelMessage = cancelMessage;
	}

	public float getHotelStar1() {
		return hotelStar1;
	}

	public void setHotelStar1(float hotelStar1) {
		this.hotelStar1 = hotelStar1;
	}

	public String getChnAddress() {
		return chnAddress;
	}

	public void setChnAddress(String chnAddress) {
		this.chnAddress = chnAddress;
	}

	public String getClueInfo() {
		return clueInfo;
	}

	public void setClueInfo(String clueInfo) {
		this.clueInfo = clueInfo;
	}

	public String getAcceptCustom() {
		return acceptCustom;
	}

	public void setAcceptCustom(String acceptCustom) {
		this.acceptCustom = acceptCustom;
	}

	public String getHotelChnName() {
		return hotelChnName;
	}

	public void setHotelChnName(String hotelChnName) {
		this.hotelChnName = hotelChnName;
	}

	public String getHotelEngName() {
		return hotelEngName;
	}

	public void setHotelEngName(String hotelEngName) {
		this.hotelEngName = hotelEngName;
	}

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelIntroduce() {
		return hotelIntroduce;
	}

	public void setHotelIntroduce(String hotelIntroduce) {
		this.hotelIntroduce = hotelIntroduce;
	}

	public String getHotelStar() {
		return hotelStar;
	}

	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}

	public String getHotelType() {
		return hotelType;
	}

	public void setHotelType(String hotelType) {
		this.hotelType = hotelType;
	}

	public List<RoomTypeTmc> getRoomTypes() {
		return roomTypesTmc;
	}

	public void setRoomTypes(List<RoomTypeTmc> rooms) {
		this.roomTypesTmc = rooms;
	}

	public String getBizZone() {
		return bizZone;
	}

	public void setBizZone(String bizZone) {
		this.bizZone = bizZone;
	}

	public String getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public String getBalanceMethod() {
		return balanceMethod;
	}

	public void setBalanceMethod(String balanceMethod) {
		this.balanceMethod = balanceMethod;
	}

	public Date getAdvancePayTime() {
		return advancePayTime;
	}

	public void setAdvancePayTime(Date advancePayTime) {
		this.advancePayTime = advancePayTime;
	}

	public String getHotelChnIntroduce() {
		return hotelChnIntroduce;
	}

	public void setHotelChnIntroduce(String hotelChnIntroduce) {
		this.hotelChnIntroduce = hotelChnIntroduce;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRateValue() {
		return rateValue;
	}

	public void setRateValue(String rateValue) {
		this.rateValue = rateValue;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getRoomTypesSize() {
		return roomTypesSize;
	}

	public void setRoomTypesSize(int roomTypesSize) {
		this.roomTypesSize = roomTypesSize;
	}

	public String getCommendType() {
		return commendType;
	}

	public void setCommendType(String commendType) {
		this.commendType = commendType;
	}

	public boolean isFlag400() {
		return flag400;
	}

	public void setFlag400(boolean flag400) {
		this.flag400 = flag400;
	}

	/*public int getIsSalesPromo() {
		return isSalesPromo;
	}

	public void setIsSalesPromo(int isSalesPromo) {
		this.isSalesPromo = isSalesPromo;
	}*/

	public int getIsUserComment() {
		return isUserComment;
	}

	public void setIsUserComment(int isUserComment) {
		this.isUserComment = isUserComment;
	}

	public int getIsTaxCharges() {
		return isTaxCharges;
	}

	public void setIsTaxCharges(int isTaxCharges) {
		this.isTaxCharges = isTaxCharges;
	}

	public boolean isFlagCtsHK() {
		return flagCtsHK;
	}

	public void setFlagCtsHK(boolean flagCtsHK) {
		this.flagCtsHK = flagCtsHK;
	}

    public boolean isFlagStopSell() {
        return flagStopSell;
    }

    public void setFlagStopSell(boolean flagStopSell) {
        this.flagStopSell = flagStopSell;
    }

    public int getHasPreSale() {
        return hasPreSale;
    }

    public void setHasPreSale(int hasPreSale) {
        this.hasPreSale = hasPreSale;
    }

    public String getPreSaleBeginEnd() {
        return preSaleBeginEnd;
    }

    public void setPreSaleBeginEnd(String preSaleBeginEnd) {
        this.preSaleBeginEnd = preSaleBeginEnd;
    }

    public String getPreSaleContent() {
        return preSaleContent;
    }

    public void setPreSaleContent(String preSaleContent) {
        this.preSaleContent = preSaleContent;
    }

    public String getPreSaleName() {
        return preSaleName;
    }

    public void setPreSaleName(String preSaleName) {
        this.preSaleName = preSaleName;
    }

    public String getPreSaleURL() {
        return preSaleURL;
    }

    public void setPreSaleURL(String preSaleURL) {
        this.preSaleURL = preSaleURL;
    }

	public List getLstRoomTaxCharge() {
		return lstRoomTaxChargeTmc;
	}

	public void setLstRoomTaxCharge(List lstRoomTaxCharge) {
		this.lstRoomTaxChargeTmc = lstRoomTaxCharge;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

}

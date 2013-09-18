package com.mangocity.hotel.base.service.assistant;

import java.util.Date;

import com.mangocity.hdl.constant.ChannelType;

/**
 * 查询酒店存储过程返回结果集对象
 * 
 * @author neil
 */
public class HotelResultInfo {
    private float lowestPrice;// number(10,2) ,

    private long hotelId; // number(10) ,

    private String hotelChnName; // VARCHAR2(256) ,

    private String hotelEngName;// VARCHAR2(256) ,

    private String hotelStar;// VARCHAR2(2) ,

    private String hotelStar1; // VARCHAR2(20) ,

    private String hotelType;// VARCHAR2(256) ,

    private String chnAddress;// VARCHAR2(256) ,

    private String hotelIntroduce;// VARCHAR2(1024) ,

    private String hotelChnIntroduce;// VARCHAR2(1024) ,

    // 酒店推荐级别
    private String hotel_comm_type;

    private String clueInfo;// VARCHAR2(1024) ,

    private String bizZone;// VARCHAR2(20) ,

    private String city;// VARCHAR2(20) ,

    // 接受某国客人
    private String acceptCustom;

    // 是否400酒店
    private boolean flag400;

    // --价格类型
    private String childRoomTypeId;// number(10) ,

    private int comm_level;// number(2) ,
    
    //房间数量 add by shengwei.zuo hotel2.9.3 2009-09-09
    private Integer room_qty;

    private long room_type_id;// number(10) ,

    private String room_name;// VARCHAR2(256) ,

    // 实际销售价
    private double salesPrice;

    // 底价
    private double basePrice;

    // private double roomPrice; // number(10,2) ,

    private String price_type;// varchar2(256) ,

    private String close_flag;// varchar2(2) ,

    private String reason; // VARCHAR2(512) ,

    // --配额类型及支付方式
    private String quotaType;// varchar2(20) ,

    private String payMethod;// varchar2(20) ,

    // --房间配额 价格
    private int avail_qty;// number(4) ,

    private Date able_sale_date;// DATE ,

    private String currency;// varchar2(20) ,

    private String inc_breakfast_type;// varchar2(20) ,

    private String inc_breakfast_number;// _number varchar2(20) ,

    private String room_state;// VARCHAR2(64) ,

    private long quota_batch_id;// number(10),

    private String quota_pattern;// varchar2(20),

    private float salesroom_price;// number(10,2)

    private boolean readFlag = false;

    // 合同币种
    private String hotel_currency;

    // 合同结算方式
    private String hotel_balanceMethod;

    // 酒店提示信息
    private String alertMessage;

    /**
     * add by wuyun 2008-11-26 15:30 酒店直联信息，0表示本部，其他值表示是直联酒店
     */
    private String cooperateChannel;

    /**
     * 是否需要无条件担保, 是:1(true), 否:0(false)
     * 
     * @author chenkeming Feb 6, 2009 8:58:29 AM
     */
    private boolean needAssure;

    /**
     * 面付转预付, 必须:0, 允许:1, 不许:2
     * 
     * @author chenkeming Feb 6, 2009 8:59:16 AM
     */
    private int payToPrepay;

    /**
     * 最晚可预订日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private Date latestBookableDate;

    /**
     * 最晚可预订时间点 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private String latestBokableTime;
    
    
    /**hotel 2.9.3  
     * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 begin  
     */
    
    //最早可预订日期 
    private Date  firstBookableDate;
    
    //最早可预订时间
    private String firstBookableTime;

    /**hotel 2.9.3  
     * 新增时间段的预订规则 add by shengwei.zuo 2009-09-06 end  
     */

    
    /**
     * 必住最后日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private Date mustLastDate;

    /**
     * 必住最早日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private Date mustFirstDate;

    /**
     * 连住天数 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private long continueDay;

    /**
     * 最少限住天数 hotel2.9.2 add by xiaoyong.li 2009-07-28
     */
    private long minRestrictNights;

    /**
     * 最大限住天数 hotel2.9.2 add by xiaoyong.li 2009-07-28
     */
    private long maxRestrictNights;

    /**
     * 必住日期 hotel2.6 add by zhineng.zhuang 2009-02-16
     */
    private String mustInDate;

    /*
     * 必须连住日期的关系,或者 or 并且 V2.9.2 addby chenjuesu
     */
    private String mustInDatesRelation;

    /**
     * 该价格类型是否有预订条款, 供cc前台查酒店用
     * 
     * @author chenkeming Mar 5, 2009 8:55:39 AM
     */
    private boolean hasReserv = false;

    /**
     * 中旅标识
     * 
     * @author chenkeming Mar 16, 2009 10:12:27 AM
     */
    private boolean flagCtsHK = false;

    /**
     * 某个支付方式的渠道
     * 
     * @author chenkeming Mar 20, 2009 4:21:12 PM
     * @see ChannelType
     */
    private int roomChannel = 0;

    /**
     * 是否停止销售, true:停止, false:不停止
     * 
     * @author chenkeming May 12, 2009 1:22:49 PM
     */
    private boolean flagStopSell = false;
    /**
     * 是否停止销售, true:停止, false:不停止
     * 
     * @author chenkeming May 12, 2009 1:22:49 PM
     */
    private int stopSellReason ;
    /**
     * 是否停止销售, true:停止, false:不停止
     * 
     * @author chenkeming May 12, 2009 1:22:49 PM
     */
    private String stopSellNote ;

    /***************** 芒果网大礼包相关信息 begin ***************/

    /**
     * 酒店是否有芒果网大礼包优惠信息 1: 有 0: 无
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
     * 酒店所属省份 hotel2.9.2 add by chenjiajie 2009-08-18
     */
    private String state;

    /**
     * 酒店所属城区 hotel2.9.2 add by chenjiajie 2009-08-18
     */
    private String zone;
    
    /**
     * 优惠立减标志 1:有,0:无 add by chenjiajie 2009-10-15
     */
    private int hasBenefit;
    
    private String formulaId;
    
    private double commission;
    
    private double commissionrate;

    private int hasCashReturn;

    public long getContinueDay() {
        return continueDay;
    }

    public void setContinueDay(long continueDay) {
        this.continueDay = continueDay;
    }

    public String getLatestBokableTime() {
        return latestBokableTime;
    }

    public void setLatestBokableTime(String latestBokableTime) {
        this.latestBokableTime = latestBokableTime;
    }

    public Date getLatestBookableDate() {
        return latestBookableDate;
    }

    public void setLatestBookableDate(Date latestBookableDate) {
        this.latestBookableDate = latestBookableDate;
    }

    public Date getMustFirstDate() {
        return mustFirstDate;
    }

    public void setMustFirstDate(Date mustFirstDate) {
        this.mustFirstDate = mustFirstDate;
    }

    public String getMustInDate() {
        return mustInDate;
    }

    public void setMustInDate(String mustInDate) {
        this.mustInDate = mustInDate;
    }

    public Date getMustLastDate() {
        return mustLastDate;
    }

    public void setMustLastDate(Date mustLastDate) {
        this.mustLastDate = mustLastDate;
    }

    public String getCooperateChannel() {
        return cooperateChannel;
    }

    public void setCooperateChannel(String cooperateChannel) {
        this.cooperateChannel = cooperateChannel;
    }

    public Date getAble_sale_date() {
        return able_sale_date;
    }

    public void setAble_sale_date(Date able_sale_date) {
        this.able_sale_date = able_sale_date;
    }

    public int getAvail_qty() {
        return avail_qty;
    }

    public void setAvail_qty(int avail_qty) {
        this.avail_qty = avail_qty;
    }

    public String getBizZone() {
        return bizZone;
    }

    public void setBizZone(String bizZone) {
        this.bizZone = bizZone;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public String getChnAddress() {
        return chnAddress;
    }

    public void setChnAddress(String chnAddress) {
        this.chnAddress = chnAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClose_flag() {
        return close_flag;
    }

    public void setClose_flag(String close_flag) {
        this.close_flag = close_flag;
    }

    public String getClueInfo() {
        return clueInfo;
    }

    public void setClueInfo(String clueInfo) {
        this.clueInfo = clueInfo;
    }

    public int getComm_level() {
        return comm_level;
    }

    public void setComm_level(int comm_level) {
        this.comm_level = comm_level;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getHotelChnIntroduce() {
        return hotelChnIntroduce;
    }

    public void setHotelChnIntroduce(String hotelChnIntroduce) {
        this.hotelChnIntroduce = hotelChnIntroduce;
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

    public String getHotelStar1() {
        return hotelStar1;
    }

    public void setHotelStar1(String hotelStar1) {
        this.hotelStar1 = hotelStar1;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public String getInc_breakfast_number() {
        return inc_breakfast_number;
    }

    public void setInc_breakfast_number(String inc_breakfast_number) {
        this.inc_breakfast_number = inc_breakfast_number;
    }

    public String getInc_breakfast_type() {
        return inc_breakfast_type;
    }

    public void setInc_breakfast_type(String inc_breakfast_type) {
        this.inc_breakfast_type = inc_breakfast_type;
    }

    public float getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(float lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public long getQuota_batch_id() {
        return quota_batch_id;
    }

    public void setQuota_batch_id(long quota_batch_id) {
        this.quota_batch_id = quota_batch_id;
    }

    public String getQuota_pattern() {
        return quota_pattern;
    }

    public void setQuota_pattern(String quota_pattern) {
        this.quota_pattern = quota_pattern;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_state() {
        return room_state;
    }

    public void setRoom_state(String room_state) {
        this.room_state = room_state;
    }

    public long getRoom_type_id() {
        return room_type_id;
    }

    public void setRoom_type_id(long room_type_id) {
        this.room_type_id = room_type_id;
    }

    // public double getRoomPrice() {
    // return roomPrice;
    // }
    //
    // public void setRoomPrice(double roomPrice) {
    // this.roomPrice = roomPrice;
    // }

    public float getSalesroom_price() {
        return salesroom_price;
    }

    public void setSalesroom_price(float salesroom_price) {
        this.salesroom_price = salesroom_price;
    }

    public boolean isReadFlag() {
        return readFlag;
    }

    public void setReadFlag(boolean readFlag) {
        this.readFlag = readFlag;
    }

    public String getHotel_balanceMethod() {
        return hotel_balanceMethod;
    }

    public void setHotel_balanceMethod(String hotel_balanceMethod) {
        this.hotel_balanceMethod = hotel_balanceMethod;
    }

    public String getHotel_currency() {
        return hotel_currency;
    }

    public void setHotel_currency(String hotel_currency) {
        this.hotel_currency = hotel_currency;
    }

    public String getHotel_comm_type() {
        return hotel_comm_type;
    }

    public void setHotel_comm_type(String hotel_comm_type) {
        this.hotel_comm_type = hotel_comm_type;
    }

    public String getAcceptCustom() {
        return acceptCustom;
    }

    public void setAcceptCustom(String acceptCustom) {
        this.acceptCustom = acceptCustom;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public boolean isFlag400() {
        return flag400;
    }

    public void setFlag400(boolean flag400) {
        this.flag400 = flag400;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isNeedAssure() {
        return needAssure;
    }

    public void setNeedAssure(boolean needAssure) {
        this.needAssure = needAssure;
    }

    public int getPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(int payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

    public boolean isHasReserv() {
        return hasReserv;
    }

    public void setHasReserv(boolean hasReserv) {
        this.hasReserv = hasReserv;
    }

    public boolean isFlagCtsHK() {
        return flagCtsHK;
    }

    public void setFlagCtsHK(boolean flagCtsHK) {
        this.flagCtsHK = flagCtsHK;
    }

    public int getRoomChannel() {
        return roomChannel;
    }

    public void setRoomChannel(int roomChannel) {
        this.roomChannel = roomChannel;
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

    public String getMustInDatesRelation() {
        return mustInDatesRelation;
    }

    public void setMustInDatesRelation(String mustInDatesRelation) {
        this.mustInDatesRelation = mustInDatesRelation;
    }

    public long getMinRestrictNights() {
        return minRestrictNights;
    }

    public void setMinRestrictNights(long minRestrictNights) {
        this.minRestrictNights = minRestrictNights;
    }

    public long getMaxRestrictNights() {
        return maxRestrictNights;
    }

    public void setMaxRestrictNights(long maxRestrictNights) {
        this.maxRestrictNights = maxRestrictNights;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

	public Date getFirstBookableDate() {
		return firstBookableDate;
	}

	public void setFirstBookableDate(Date firstBookableDate) {
		this.firstBookableDate = firstBookableDate;
	}

	public String getFirstBookableTime() {
		return firstBookableTime;
	}

	public void setFirstBookableTime(String firstBookableTime) {
		this.firstBookableTime = firstBookableTime;
	}

	public Integer getRoom_qty() {
		return room_qty;
	}

	public void setRoom_qty(Integer room_qty) {
		this.room_qty = room_qty;
	}

	public int getHasBenefit() {
		return hasBenefit;
	}

	public void setHasBenefit(int hasBenefit) {
		this.hasBenefit = hasBenefit;
	}

	public int getStopSellReason() {
		return stopSellReason;
	}

	public void setStopSellReason(int stopSellReason) {
		this.stopSellReason = stopSellReason;
	}

	public String getStopSellNote() {
		return stopSellNote;
	}

	public void setStopSellNote(String stopSellNote) {
		this.stopSellNote = stopSellNote;
	}

	public int getHasCashReturn() {
		return hasCashReturn;
	}

	public void setHasCashReturn(int hasCashReturn) {
		this.hasCashReturn = hasCashReturn;
	}

	public String getFormulaId() {
		return formulaId;
	}

	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public double getCommissionrate() {
		return commissionrate;
	}

	public void setCommissionrate(double commissionrate) {
		this.commissionrate = commissionrate;
	}
}

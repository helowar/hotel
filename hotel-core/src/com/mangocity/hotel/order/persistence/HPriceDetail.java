package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.HEntity;

/**
 * 
 *  历史订单价格明细
 */
public class HPriceDetail implements HEntity {

    private static final long serialVersionUID = -2004857197136479022L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;

    /**
	 * 历史订单ID <fk> 和HOrder关联
	 */
    private HOrder orderH;

    /**
	 * 日期
	 */
    private Date night;

    /**
	 * 第几天,zero based
	 */
    private int dayIndex;

    /**
	 * 房态
	 */
    private String roomState;

    /**
	 * 配额数量
	 */
    private int quantity;

    /**
	 * 销售价
	 */
    private double salePrice;
    
    /**
	 * 底价
	 */
    private double basePrice;

    /**
	 * 门市价
	 */
    private double marketPrice;

    /**
	 * 早餐中文字符串
	 */
    private String breakfastStr;
    
    /**
	 * 每天的日期字符串,如"2/11 周3"
	 * @author chenkeming Feb 11, 2009 11:43:28 AM
	 */
    private String dateStr; 
    
    
    /**
	 * 当天是否有预订条款信息
	 * @author chenkeming Feb 18, 2009 11:48:08 AM
	 */
    private boolean hasReserv = false;

    /**
	 * 提前日期，时间
	 * @author chenkeming Feb 18, 2009 11:48:36 AM
	 */
    private String beforeTime;    
    
    /**
	 * 提前多少天
	 * @author chenkeming Feb 18, 2009 11:48:36 AM
	 */
    private String beforeDayNum;    
    
    /**
     * 连住多少晚
     * @author chenkeming Feb 18, 2009 11:49:08 AM
     */
    private String continueDay;
    
    /**
     * 必住日期
     * @author chenkeming Feb 18, 2009 11:50:16 AM
     */
    private String mustDate;
    
    /**
     * 担保条件
     * @author chenkeming Feb 18, 2009 11:50:32 AM
     */
    private String assureCond;
    
    /**
     * 担保类型
     * @author chenkeming Feb 18, 2009 11:50:48 AM
     */
    private String assureType;
    
    /**
     * 结算方式
     * @author chenkeming Feb 18, 2009 11:51:06 AM
     */
    private String balanceMode;
    
    /**
     * 付款时限
     * @author chenkeming Feb 18, 2009 11:51:25 AM
     */
    private String prepayTime;    


    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public Date getNight() {
        return night;
    }

    public void setNight(Date night) {
        this.night = night;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRoomState() {
        return roomState;
    }

    public void setRoomState(String roomState) {
        this.roomState = roomState;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public HOrder getOrderH() {
        return orderH;
    }

    public void setOrderH(HOrder orderH) {
        this.orderH = orderH;
    }

    public String getBreakfastStr() {
        return breakfastStr;
    }

    public void setBreakfastStr(String breakfastStr) {
        this.breakfastStr = breakfastStr;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getAssureCond() {
        return assureCond;
    }

    public void setAssureCond(String assureCond) {
        this.assureCond = assureCond;
    }

    public String getAssureType() {
        return assureType;
    }

    public void setAssureType(String assureType) {
        this.assureType = assureType;
    }

    public String getBalanceMode() {
        return balanceMode;
    }

    public void setBalanceMode(String balanceMode) {
        this.balanceMode = balanceMode;
    }

    public String getBeforeDayNum() {
        return beforeDayNum;
    }

    public void setBeforeDayNum(String beforeDayNum) {
        this.beforeDayNum = beforeDayNum;
    }

    public String getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(String beforeTime) {
        this.beforeTime = beforeTime;
    }

    public String getContinueDay() {
        return continueDay;
    }

    public void setContinueDay(String continueDay) {
        this.continueDay = continueDay;
    }

    public boolean isHasReserv() {
        return hasReserv;
    }

    public void setHasReserv(boolean hasReserv) {
        this.hasReserv = hasReserv;
    }

    public String getMustDate() {
        return mustDate;
    }

    public void setMustDate(String mustDate) {
        this.mustDate = mustDate;
    }

    public String getPrepayTime() {
        return prepayTime;
    }

    public void setPrepayTime(String prepayTime) {
        this.prepayTime = prepayTime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

}

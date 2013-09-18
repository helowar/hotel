package com.mangocity.hotel.base.manage.assistant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.mangocity.hotel.base.persistence.CEntity;

/**
 * 用户录入的酒店房间价格
 * 
 * @author zhengxin
 * 
 */
public class InputPrice extends CEntity implements Serializable {

    /**
     * 房型ID
     */
    private Long roomTypeId;

    /**
     * 子房型ID,也就是价格类型
     */
    private Long childRoomTypeId;

    /**
     * 配额类型
     */
    private String quotaType;

    /**
     * 价格ID
     */
    private Long priceID;

    /**
     * 销售日期
     */
    private Date ableSaleDate;

    /**
     * 支付方式
     */
    private String payMethod;

    /**
     * 底价
     */
    private double basePrice;

    /**
     * 净价
     */
    private double advicePrice;

    /**
     * 币种
     */
    private String currency;

    /**
     * 门市价
     */
    private double roomPrice;

    /**
     * 酒店提供的销售价(正式约定，与芒果卖价相同，如果酒店提供的价不含服务费， 则酒店业务部人员自己计算之后算成芒果卖价，这样就与网格价相同)
     */
    private double salePrice;

    /**
     * 佣金
     */
    private double calculatedCommission;

    /**
     * 酒店提供的佣金百分比
     */
    private double commissionRate;

    /**
     * 是否是固定销售价
     */
    private boolean fixed;

    /**
     * 售价是否含服务费
     */
    private boolean includeServiceCharge;

    /**
     * 服务费税率
     */
    private double serviceCharge;

    /**
     * 佣金计算是否计算含早价
     */
    private boolean includeBreakfastPrice;

    /**
     * 佣金计否含税
     */
    private boolean commissionIncludeTax;

    /**
     * 佣金税率
     */
    private boolean commissionTaxRate;

    /**
     * 佣金计算是否含服务费
     */
    private boolean commissionIncludeServiceCharge;

    /**
     * 早餐单价
     */
    private double breakfastPrice;

    /**
     * 含早类型
     */
    private String breakfastType;

    /**
     * 含早数量
     */
    private String breakfastNum;

    private int iBreakfastNum;

    /**
     * 房间日期
     */
    private Date roomDate;

    /**
     * 计算公式ID
     */
    private String formulaId;

    /**
     * 酒店id
     */
    private long hotelId;

    /**
     * 酒店房型价格类型组合str
     * 
     * @return
     */
    private String roomTypeIdStr;

    /**
     * 酒店房型价格类型中文名称组合
     * 
     * @return
     */
    private String roomTypeNameStr;

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        BigDecimal bd = new BigDecimal(basePrice);
        this.basePrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public String getBreakfastNum() {
        return breakfastNum;
    }

    public void setBreakfastNum(String breakfastNum) {
        this.breakfastNum = breakfastNum;
        this.iBreakfastNum = Integer.valueOf(breakfastNum).intValue();
    }

    public double getBreakfastPrice() {
        return breakfastPrice;
    }

    public void setBreakfastPrice(double breakfastPrice) {
        BigDecimal bd = new BigDecimal(breakfastPrice);
        this.breakfastPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public String getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(String breakfastType) {
        this.breakfastType = breakfastType;
    }

    public double getCalculatedCommission() {
        return calculatedCommission;
    }

    public void setCalculatedCommission(double commission) {
        BigDecimal bd = new BigDecimal(commission);
        this.calculatedCommission = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getFormulaId() {
        return formulaId;
    }

    public void setFormulaId(String formula) {
        this.formulaId = formula;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payway) {
        this.payMethod = payway;
    }

    public Date getRoomDate() {
        return roomDate;
    }

    public void setRoomDate(Date roomDate) {
        this.roomDate = roomDate;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        BigDecimal bd = new BigDecimal(roomPrice);
        this.roomPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        BigDecimal db = new BigDecimal(salePrice);
        this.salePrice = db.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getAdvicePrice() {
        return advicePrice;
    }

    public void setAdvicePrice(double advicePrice) {
        BigDecimal db = new BigDecimal(advicePrice);
        this.advicePrice = db.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public Long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(Long childRoomTypeID) {
        this.childRoomTypeId = childRoomTypeID;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeID) {
        this.roomTypeId = roomTypeID;
    }

    public boolean isCommissionIncludeServiceCharge() {
        return commissionIncludeServiceCharge;
    }

    public void setCommissionIncludeServiceCharge(boolean commissionIncludeServiceCharge) {
        this.commissionIncludeServiceCharge = commissionIncludeServiceCharge;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(double commissionRate) {
        BigDecimal bd = new BigDecimal(commissionRate);
        this.commissionRate = bd.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public boolean isIncludeBreakfastPrice() {
        return includeBreakfastPrice;
    }

    public void setIncludeBreakfastPrice(boolean includeBreakfastPrice) {
        this.includeBreakfastPrice = includeBreakfastPrice;
    }

    public boolean isIncludeServiceCharge() {
        return includeServiceCharge;
    }

    public void setIncludeServiceCharge(boolean salePriceIncludeServiceCharge) {
        this.includeServiceCharge = salePriceIncludeServiceCharge;
    }

    public boolean isCommissionIncludeTax() {
        return commissionIncludeTax;
    }

    public void setCommissionIncludeTax(boolean commissionIncludeTax) {
        this.commissionIncludeTax = commissionIncludeTax;
    }

    public boolean isCommissionTaxRate() {
        return commissionTaxRate;
    }

    public void setCommissionTaxRate(boolean commissionTaxRate) {
        this.commissionTaxRate = commissionTaxRate;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Long getPriceID() {
        return priceID;
    }

    public void setPriceID(Long priceID) {
        this.priceID = priceID;
    }

    public Date getAbleSaleDate() {
        return ableSaleDate;
    }

    public void setAbleSaleDate(Date ableSaleDate) {
        this.ableSaleDate = ableSaleDate;
    }

    public String getQuotaType() {
        return quotaType;
    }

    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public int getIBreakfastNum() {
        return iBreakfastNum;
    }

    public void setIBreakfastNum(int breakfastNum) {
        iBreakfastNum = breakfastNum;
    }

    public String getRoomTypeIdStr() {
        return roomTypeIdStr;
    }

    public void setRoomTypeIdStr(String roomTypeIdStr) {
        this.roomTypeIdStr = roomTypeIdStr;

        String[] kk = roomTypeIdStr.split("&&");
        if (null != kk && 1 < kk.length) {
            this.roomTypeId = Long.valueOf(kk[0]).longValue();
            this.childRoomTypeId = Long.valueOf(kk[1]).longValue();

        }
    }

    public String getRoomTypeNameStr() {
        return roomTypeNameStr;
    }

    /*
     * public void setRoomTypeNameStr(String roomTypeNameStr) { this.roomTypeNameStr =
     * roomTypeNameStr; }
     */

}

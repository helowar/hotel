package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.constant.HotelCalcuAssuAmoType;
import com.mangocity.util.Entity;

/**
 * 
 *  预订规则
 *  @author chenkeming
 */
public class OrReservation implements Entity {

    private static final long serialVersionUID = 7653269118174239229L;

    /**
	 * ID <pk>
	 */
    private Long ID;

    /**
	 * 预付付款时限
	 */
    private Date advancePayTime;

    /**
	 * 酒店结算方式
	 */
    private String balanceMode;

    /**
	 * 最晚担保时间
	 */
    private String lateSuretyTime;
    
    /**
	 * 是否向酒店出具担保函
	 */
    private boolean assureLetter = false;
    
    /**
	 * 保存酒店方面的预定条款是否需要担保, 但CC真正操作时自己决定(CC操作的结果保存在OrOrder表里)
	 */
    private boolean needCredit = false;
    
    /**
	 * 担保金额
	 */
    private double reservSuretyPrice;

    /**
	 * 预订担保注意事项
	 */
    private String creditRemark;
    
    /**
	 * 取消/修改Item, hotel2.6和OrAssureItemEvery关联 原来和OrAssureItem关联
	 */
    private List<OrAssureItemEvery> assureList = new ArrayList<OrAssureItemEvery>();    
    
    /**
	 * 担保明细
	 */
    private List<OrGuaranteeItem> guarantees = new ArrayList<OrGuaranteeItem>();    
    
    /**
	 * 是否有促销信息
	 * @author chenkeming Feb 18, 2009 2:03:36 PM
	 */
    private boolean hasPresale = false;
    
    /**
	 * 是否有房费另缴税信息
	 * @author chenkeming Feb 18, 2009 2:03:54 PM
	 */
    private boolean hasTaxCharge = false;
    
    /**
	 * 是否无条件
	 * @author chenkeming Feb 18, 2009 2:04:46 PM
	 */
    private boolean unCondition = false;
    
    /**
	 * 是否有超时担保
	 * @author chenkeming Feb 18, 2009 2:04:46 PM
	 */
    private boolean overTimeAssure = false;
    
    /**
	 * 是否有超房数担保
	 * @author chenkeming Feb 18, 2009 2:06:10 PM
	 */
    private boolean roomsAssure = false;
    
    /**
	 * 超房数担保房间数
	 * @author chenkeming Feb 18, 2009 2:06:36 PM
	 */
    private int rooms;
    
    /**
	 * 是否有超间夜担保
	 * @author lixiaoyong 2009-7-31
	 */
    private boolean nightsAssure = false;
    
    /**
	 * 是否有超间夜担保
	 * @author lixiaoyong 2009-7-31
	 */
    private int nights;
    
    /**
	 * 修改字段定义
	 * @author chenkeming Feb 18, 2009 2:32:13 PM
	 */
    private String modifyField;
    
    /**
	 * 预定条款计算规则
	 * @author chenkeming Feb 18, 2009 2:33:03 PM
	 * @see HotelCalcuAssuAmoType
	 */
    private String clauseRule = "3";
    
    /**
	 * 预订条款字符串(提前多少天预订等信息)
	 * @author chenkeming Feb 18, 2009 2:42:45 PM
	 */
    private String clauseStr;
    
    /**
	 * 订单首日金额,用于计算修改取消扣款
	 * @author chenkeming Feb 19, 2009 9:02:44 AM
	 */
    private double firstPrice = 0.0;
    
    /**
	 * 用于保存下新单时取消修改条款id和日期序号
	 */
    private String cancelModifyStr;
    
    /**
	 * 如果是预付,预先要给酒店的部分订金
	 * @author chenkeming Mar 6, 2009 1:03:03 PM
	 */
    private double payToHotelAdv = 0.0;
    
    /*
	 * 是否是 必须面付转预付 add by shengwei.zuo 2009-05-13 hotel 2.6 
	 */
    private boolean isMustPayToPrepay;
    
    /**
	 * 预付酒店付款时限类型
	 * 注意：该字段并没有保存到数据库中，只是为了网站的判断
	 * add by wuyun
	 * v2.6
	 * 2009-06-16
	 * @return
	 */
    private String prepayLimitType;
            
    public String getPrepayLimitType() {
        return prepayLimitType;
    }

    public void setPrepayLimitType(String prepayLimitType) {
        this.prepayLimitType = prepayLimitType;
    }

    public Date getAdvancePayTime() {
        return advancePayTime;
    }

    public void setAdvancePayTime(Date advancePayTime) {
        this.advancePayTime = advancePayTime;
    }

    public boolean isAssureLetter() {
        return assureLetter;
    }

    public void setAssureLetter(boolean assureLetter) {
        this.assureLetter = assureLetter;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long attribute186) {
        this.ID = attribute186;
    }

    public String getBalanceMode() {
        return balanceMode;
    }

    public void setBalanceMode(String balanceMode) {
        this.balanceMode = balanceMode;
    }

    public String getLateSuretyTime() {
        return lateSuretyTime;
    }

    public void setLateSuretyTime(String lateSuretyTime) {
        this.lateSuretyTime = lateSuretyTime;
    }

    public boolean isNeedCredit() {
        return needCredit;
    }

    public void setNeedCredit(boolean needCredit) {
        this.needCredit = needCredit;
    }

    public String getCreditRemark() {
        return creditRemark;
    }

    public void setCreditRemark(String creditRemark) {
        this.creditRemark = creditRemark;
    }

    public double getReservSuretyPrice() {
        return reservSuretyPrice;
    }

    public void setReservSuretyPrice(double reservSuretyPrice) {
        this.reservSuretyPrice = reservSuretyPrice;
    }

    public boolean isHasPresale() {
        return hasPresale;
    }

    public void setHasPresale(boolean hasPresale) {
        this.hasPresale = hasPresale;
    }

    public boolean isHasTaxCharge() {
        return hasTaxCharge;
    }

    public void setHasTaxCharge(boolean hasTaxCharge) {
        this.hasTaxCharge = hasTaxCharge;
    }

    public boolean isOverTimeAssure() {
        return overTimeAssure;
    }

    public void setOverTimeAssure(boolean overTimeAssure) {
        this.overTimeAssure = overTimeAssure;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public boolean isRoomsAssure() {
        return roomsAssure;
    }

    public void setRoomsAssure(boolean roomsAssure) {
        this.roomsAssure = roomsAssure;
    }

    public String getClauseRule() {
        return clauseRule;
    }

    public void setClauseRule(String clauseRule) {
        this.clauseRule = clauseRule;
    }

    public String getModifyField() {
        return modifyField;
    }

    public void setModifyField(String modifyField) {
        this.modifyField = modifyField;
    }

    public String getClauseStr() {
        return clauseStr;
    }

    public void setClauseStr(String clauseStr) {
        this.clauseStr = clauseStr;
    }

    public List<OrGuaranteeItem> getGuarantees() {
        return guarantees;
    }

    public void setGuarantees(List<OrGuaranteeItem> guarantees) {
        this.guarantees = guarantees;
    }

    public double getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(double firstPrice) {
        this.firstPrice = firstPrice;
    }

    public boolean isUnCondition() {
        return unCondition;
    }

    public void setUnCondition(boolean unCondition) {
        this.unCondition = unCondition;
    }

    public List<OrAssureItemEvery> getAssureList() {
        return assureList;
    }

    public void setAssureList(List<OrAssureItemEvery> assureList) {
        this.assureList = assureList;
    }
    
    /**
	 * 是否可能担保
	 * @author chenkeming Feb 20, 2009 10:52:21 AM
	 * @return
	 */
    public boolean isCanAssure() {
        return unCondition || overTimeAssure || roomsAssure||nightsAssure;
    }

    public String getCancelModifyStr() {
        return cancelModifyStr;
    }

    public void setCancelModifyStr(String cancelModifyStr) {
        this.cancelModifyStr = cancelModifyStr;
    }

    public double getPayToHotelAdv() {
        return payToHotelAdv;
    }

    public void setPayToHotelAdv(double payToHotelAdv) {
        this.payToHotelAdv = payToHotelAdv;
    }

    public boolean isMustPayToPrepay() {
        return isMustPayToPrepay;
    }

    public void setMustPayToPrepay(boolean isMustPayToPrepay) {
        this.isMustPayToPrepay = isMustPayToPrepay;
    }

    public boolean isNightsAssure() {
        return nightsAssure;
    }

    public void setNightsAssure(boolean nightsAssure) {
        this.nightsAssure = nightsAssure;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

}

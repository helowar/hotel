package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.HEntity;

/**
 * 
 *  历史订单预订规则
 *  @author chenkeming
 */
public class HReservation implements HEntity {

    private static final long serialVersionUID = -5244474780424428988L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;

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
	 * 保存酒店方面的预定条款是否需要无条件担保, 但CC真正操作时自己决定(CC操作的结果保存在OrOrder表里)
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
	 * 取消/修改Item, hotel2.6和HAssureItemEvery关联
	 */
    private List<HAssureItemEvery> assureListH = new ArrayList<HAssureItemEvery>();
    
    /**
	 * 担保明细
	 */
    private List<HGuaranteeItem> guaranteesH = new ArrayList<HGuaranteeItem>();    
    
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
	 * 修改字段定义
	 * @author chenkeming Feb 18, 2009 2:32:13 PM
	 */
    private String modifyField;
    
    /**
	 * 预定条款计算规则
	 * @author chenkeming Feb 18, 2009 2:33:03 PM
	 */
    private String clauseRule;
    
    /**
	 * 预订条款字符串(提前多少天预订等信息)
	 * @author chenkeming Feb 18, 2009 2:42:45 PM
	 */
    private String clauseStr;
    
    /**
	 * 单日最高房价,用于计算修改取消扣款
	 * @author chenkeming Feb 19, 2009 9:02:44 AM
	 */
    private double highest;
    
    /**
	 * 订单首日金额,用于计算修改取消扣款
	 * @author chenkeming Feb 19, 2009 9:02:44 AM
	 */
    private double firstPrice;    
    
    /**
	 * 是否可能担保
	 * @author chenkeming Feb 20, 2009 10:52:21 AM
	 * @return
	 */
    public boolean isCanAssure() {
        return unCondition || overTimeAssure || roomsAssure;
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

    public double getHighest() {
        return highest;
    }

    public void setHighest(double highest) {
        this.highest = highest;
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

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public List<HAssureItemEvery> getAssureListH() {
        return assureListH;
    }

    public void setAssureListH(List<HAssureItemEvery> assureListH) {
        this.assureListH = assureListH;
    }

    public List<HGuaranteeItem> getGuaranteesH() {
        return guaranteesH;
    }

    public void setGuaranteesH(List<HGuaranteeItem> guaranteesH) {
        this.guaranteesH = guaranteesH;
    }

}

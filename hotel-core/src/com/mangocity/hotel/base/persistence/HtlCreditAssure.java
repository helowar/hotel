package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * 信用卡担保
 */

public class HtlCreditAssure extends CEntity implements Entity, Serializable {

    private static final long serialVersionUID = -4353413158811970910L;

    private Long ID;

    /**
     * 合同ID
     */
    private Integer contractId;

    /**
     * 有效起止日期
     */
    private Date beginDate;

    private Date endDate;

    /**
     * 房型
     */
    private String roomType;

    /**
     * 担保条件
     */
    private String assureConditions;

    /**
     * 担保类型
     */
    private String assureType;

    /**
     * 是否向酒店出具担保函
     */
    private boolean assureLetter;

    /**
     * 是否允许取消和修改
     */
    private boolean allowModify;

    /**
     * 最晚担保时间
     */
    private String lastAssureTime;

    /**
     * 提前天数
     */
    private String aheadDays;

    /**
     * 结算方法
     */
    private String balanceMethod;

    /**
     * 预付金额类型
     */
    private String prepayMoneyType;

    /**
     * 付款时限类型
     */
    private String timeLimitType;

    /**
     * 付款时限
     */
    private String timeLimit;

    /**
     * 预订注意事项
     */
    private String remark;

    /**
     * 取消条款明细
     * 
     * @return
     */
    private List<HtlAssureItem> htlAssureItem = new ArrayList<HtlAssureItem>();

    private List<HtlCreditAssureDate> htlCreditAssureDate = new ArrayList<HtlCreditAssureDate>();

    private List<HtlAssureCardItem> htlAssureCardItem = new ArrayList<HtlAssureCardItem>();

    private int lastModiDateNum;

    private String lastModiDate;

    private int lastCancelNum;

    private String lastCancelDate;

    // 付款时限的提前天数
    private int timeLimitNum;

    // 付款时限的提前日期
    private String timeLimitDate;

    // 付款时限的订单酒店确认时间后几天内付款
    private int timeLimitDateNum;

    private long hotelId;

    /**
     * 提前预订时间
     */
    private String aheadTimer;

    /**
     * 连住晚数
     */
    private String continueNight;

    /**
     * 必住日期
     * 
     */
    private String mustDate;

    /**
     * 必须日期之前 must_before_date
     */
    private Date mustBeforeDate;

    /**
     * must_before_time 必须时间之前
     */
    private String mustBeforeTime;

    // Constructors
    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifyBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 预付条款的提前时间点 add by zhineng.zhuang
     */
    private String prepayAheadTime;

    /**
     * wuyun 用来存放CC查询需要显示的预订条款信息字符串
     * 
     * @return
     */
    private String assureString;

    public String getAssureString() {
        return assureString;
    }

    public void setAssureString(String assureString) {
        this.assureString = assureString;
    }

    public Integer getContractId() {
        return this.contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getAssureType() {
        return this.assureType;
    }

    public void setAssureType(String assureType) {
        this.assureType = assureType;
    }

    public boolean isAllowModify() {
        return this.allowModify;
    }

    public void setAllowModify(boolean allowModify) {
        this.allowModify = allowModify;
    }

    public String getLastAssureTime() {
        return this.lastAssureTime;
    }

    public void setLastAssureTime(String lastAssureTime) {
        this.lastAssureTime = lastAssureTime;
    }

    public String getAheadDays() {
        return this.aheadDays;
    }

    public void setAheadDays(String aheadDays) {
        this.aheadDays = aheadDays;
    }

    public String getBalanceMethod() {
        return this.balanceMethod;
    }

    public void setBalanceMethod(String balanceMethod) {
        this.balanceMethod = balanceMethod;
    }

    public String getTimeLimit() {
        return this.timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public boolean isAssureLetter() {
        return assureLetter;
    }

    public void setAssureLetter(boolean assureLetter) {
        this.assureLetter = assureLetter;
    }

    public String getTimeLimitType() {
        return timeLimitType;
    }

    public void setTimeLimitType(String timeLimitType) {
        this.timeLimitType = timeLimitType;
    }

    public String getLastCancelDate() {
        return lastCancelDate;
    }

    public void setLastCancelDate(String lastCancelDate) {
        this.lastCancelDate = lastCancelDate;
    }

    public int getLastCancelNum() {
        return lastCancelNum;
    }

    public void setLastCancelNum(int lastCancelNum) {
        this.lastCancelNum = lastCancelNum;
    }

    public String getLastModiDate() {
        return lastModiDate;
    }

    public void setLastModiDate(String lastModiDate) {
        this.lastModiDate = lastModiDate;
    }

    public int getLastModiDateNum() {
        return lastModiDateNum;
    }

    public void setLastModiDateNum(int lastModiDateNum) {
        this.lastModiDateNum = lastModiDateNum;
    }

    public String getTimeLimitDate() {
        return timeLimitDate;
    }

    public void setTimeLimitDate(String timeLimitDate) {
        this.timeLimitDate = timeLimitDate;
    }

    public int getTimeLimitNum() {
        return timeLimitNum;
    }

    public void setTimeLimitNum(int timeLimitNum) {
        this.timeLimitNum = timeLimitNum;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getAheadTimer() {
        return aheadTimer;
    }

    public void setAheadTimer(String aheadTimer) {
        this.aheadTimer = aheadTimer;
    }

    public String getContinueNight() {
        return continueNight;
    }

    public void setContinueNight(String continueNight) {
        this.continueNight = continueNight;
    }

    public String getMustDate() {
        return mustDate;
    }

    public void setMustDate(String mustDate) {
        this.mustDate = mustDate;
    }

    public Date getMustBeforeDate() {
        return mustBeforeDate;
    }

    public void setMustBeforeDate(Date mustBeforeDate) {
        this.mustBeforeDate = mustBeforeDate;
    }

    public String getMustBeforeTime() {
        return mustBeforeTime;
    }

    public void setMustBeforeTime(String mustBeforeTime) {
        this.mustBeforeTime = mustBeforeTime;
    }

    public List<HtlAssureItem> getHtlAssureItem() {
        return htlAssureItem;
    }

    public void setHtlAssureItem(List<HtlAssureItem> htlAssureItem) {
        this.htlAssureItem = htlAssureItem;
    }

    public List<HtlCreditAssureDate> getHtlCreditAssureDate() {
        return htlCreditAssureDate;
    }

    public void setHtlCreditAssureDate(List<HtlCreditAssureDate> htlCreditAssureDate) {
        this.htlCreditAssureDate = htlCreditAssureDate;
    }

    public List<HtlAssureCardItem> getHtlAssureCardItem() {
        return htlAssureCardItem;
    }

    public void setHtlAssureCardItem(List<HtlAssureCardItem> htlAssureCardItem) {
        this.htlAssureCardItem = htlAssureCardItem;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getPrepayMoneyType() {
        return prepayMoneyType;
    }

    public void setPrepayMoneyType(String prepayMoneyType) {
        this.prepayMoneyType = prepayMoneyType;
    }

    public String getAssureConditions() {
        return assureConditions;
    }

    public void setAssureConditions(String assureConditions) {
        this.assureConditions = assureConditions;
    }

    public int getTimeLimitDateNum() {
        return timeLimitDateNum;
    }

    public void setTimeLimitDateNum(int timeLimitDateNum) {
        this.timeLimitDateNum = timeLimitDateNum;
    }

    public String getPrepayAheadTime() {
        return prepayAheadTime;
    }

    public void setPrepayAheadTime(String prepayAheadTime) {
        this.prepayAheadTime = prepayAheadTime;
    }

}
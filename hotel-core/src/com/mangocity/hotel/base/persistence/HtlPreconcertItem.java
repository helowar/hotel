package com.mangocity.hotel.base.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.util.Entity;

/**
 * HtlPreconcertItem generated by MyEclipse Persistence Tools
 */

public class HtlPreconcertItem extends CEntity implements Entity {

    // Fields

    /**
	 * 
	 */

    private Long ID;

    private Long hotelId;

    private Long reservationTemplateId;

    private String reservationName;

    private Long contractId;

    private Date validDate;

    private Long priceTypeId;

    private String payToPrepay;

    private String attention;

    private String createBy;

    private String createById;

    private Date createTime;

    private String modifyBy;

    private String modifyById;

    private Date modifyTime;

    private String delBy;

    private String delById;

    private Date delTime;

    private String active;

    private String beginDt;

    private String endDt;

    private String isFirstQuery;

    private String validDt;

    private List<HtlPreconcertItem> dataList = new ArrayList<HtlPreconcertItem>();

    // add by zhineng.zhuang hotel 2.6 加关联
    /**
     * 预订条款
     */
    private List<HtlReservation> htlReservationList;

    /**
     * 每日的担保条款
     */
    private List<HtlAssure> htlAssureList;

    /**
     *每日的预付条款
     */
    private List<HtlPrepayEveryday> htlPrepayEverydayList;

    // Constructors

    /** default constructor */
    public HtlPreconcertItem() {
    }

    /** full constructor */
    public HtlPreconcertItem(Long hotelId, Long reservationTemplateId, String reservationName,
        Long contractId, Date validDate, Long priceTypeId, String payToPrepay, String attention,
        String createBy, String createById, Date createTime, String modifyBy, String modifyById,
        Date modifyTime, String delBy, String delById, Date delTime, String active) {
        this.hotelId = hotelId;
        this.reservationTemplateId = reservationTemplateId;
        this.reservationName = reservationName;
        this.contractId = contractId;
        this.validDate = validDate;
        this.priceTypeId = priceTypeId;
        this.payToPrepay = payToPrepay;
        this.attention = attention;
        this.createBy = createBy;
        this.createById = createById;
        this.createTime = createTime;
        this.modifyBy = modifyBy;
        this.modifyById = modifyById;
        this.modifyTime = modifyTime;
        this.delBy = delBy;
        this.delById = delById;
        this.delTime = delTime;
        this.active = active;
    }

    // Property accessors

    public Long getHotelId() {
        return this.hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getReservationTemplateId() {
        return this.reservationTemplateId;
    }

    public void setReservationTemplateId(Long reservationTemplateId) {
        this.reservationTemplateId = reservationTemplateId;
    }

    public String getReservationName() {
        return this.reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public Long getContractId() {
        return this.contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Date getValidDate() {
        return this.validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public Long getPriceTypeId() {
        return this.priceTypeId;
    }

    public void setPriceTypeId(Long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public String getPayToPrepay() {
        return this.payToPrepay;
    }

    public void setPayToPrepay(String payToPrepay) {
        this.payToPrepay = payToPrepay;
    }

    public String getAttention() {
        return this.attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateById() {
        return this.createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyBy() {
        return this.modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public String getModifyById() {
        return this.modifyById;
    }

    public void setModifyById(String modifyById) {
        this.modifyById = modifyById;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDelBy() {
        return this.delBy;
    }

    public void setDelBy(String delBy) {
        this.delBy = delBy;
    }

    public String getDelById() {
        return this.delById;
    }

    public void setDelById(String delById) {
        this.delById = delById;
    }

    public Date getDelTime() {
        return this.delTime;
    }

    public void setDelTime(Date delTime) {
        this.delTime = delTime;
    }

    public String getActive() {
        return this.active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getBeginDt() {
        return beginDt;
    }

    public void setBeginDt(String beginDt) {
        this.beginDt = beginDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public List<HtlPreconcertItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<HtlPreconcertItem> dataList) {
        this.dataList = dataList;
    }

    public List<HtlAssure> getHtlAssureList() {
        return htlAssureList;
    }

    public void setHtlAssureList(List<HtlAssure> htlAssureList) {
        this.htlAssureList = htlAssureList;
    }

    public List<HtlPrepayEveryday> getHtlPrepayEverydayList() {
        return htlPrepayEverydayList;
    }

    public void setHtlPrepayEverydayList(List<HtlPrepayEveryday> htlPrepayEverydayList) {
        this.htlPrepayEverydayList = htlPrepayEverydayList;
    }

    public List<HtlReservation> getHtlReservationList() {
        return htlReservationList;
    }

    public void setHtlReservationList(List<HtlReservation> htlReservationList) {
        this.htlReservationList = htlReservationList;
    }

    public String getIsFirstQuery() {
        return isFirstQuery;
    }

    public void setIsFirstQuery(String isFirstQuery) {
        this.isFirstQuery = isFirstQuery;
    }

    public String getValidDt() {
        return validDt;
    }

    public void setValidDt(String validDt) {
        this.validDt = validDt;
    }

    public Long getID() {
        // TODO Auto-generated method stub
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}
package com.mangocity.hdl.hotel.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExOrder complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExOrder", propOrder = { "orderid", "adultnum", "arrivaltime", "arrivaltraffic",
    "bedtype", "checkindate", "checkoutdate", "createdate", "creditcardexpires", "creditcardname",
    "creditcardno", "creditcardtype", "currency", "customerfax", "email", "exchangerate",
    "fellownames", "flight", "guaranteetype", "hotelcode", "hotelcodeforchannel", "hotelid",
    "hotelname", "hotelnotes", "isguarantee", "latestarrivaltime", "linkman", "hotelconfirm",
    "hotelconfirmid", "localtotalamout", "orderstate", "mobile", "noshowreason", "nosmoking",
    "ordercd", "ordercdhotel", "paymethod", "paysatus", "pricetypecode", "rateplancode",
    "roomquantity", "roomtypecode", "roomtypecodeforchannel", "roomtypeid", "roomtypename",
    "specialrequest", "telephone", "title", "totalamount", "exOrderItems", "specialnote",
    "createtime", "modifiedtime", "firstDayPrice" })
public class MGExOrder {

    protected Long orderid;

    protected int adultnum;

    protected String arrivaltime;

    protected String arrivaltraffic;

    protected Integer bedtype;

    @XmlElement(required = true)
    protected String checkindate;

    @XmlElement(required = true)
    protected String checkoutdate;

    protected String createdate;

    protected String creditcardexpires;

    protected String creditcardname;

    protected String creditcardno;

    protected String creditcardtype;

    protected String currency;

    protected String customerfax;

    protected String email;

    protected float exchangerate;

    protected String fellownames;

    protected String flight;

    protected String guaranteetype;

    protected String hotelcode;

    protected String hotelcodeforchannel;

    protected Long hotelid;

    protected String hotelname;

    protected String hotelnotes;

    protected String isguarantee;

    protected String latestarrivaltime;

    @XmlElement(required = true)
    protected String linkman;

    protected Integer hotelconfirm;

    protected String hotelconfirmid;

    protected float localtotalamout;

    @XmlElement(required = true)
    protected String orderstate;

    protected String mobile;

    protected String noshowreason;

    protected String nosmoking;

    protected String ordercd;

    protected String ordercdhotel;

    @XmlElement(required = true)
    protected String paymethod;

    protected String paysatus;

    protected int pricetypecode;

    protected String rateplancode;

    protected int roomquantity;

    protected Integer roomtypecode;

    protected String roomtypecodeforchannel;

    protected Integer roomtypeid;

    protected String roomtypename;

    protected String specialrequest;

    protected String telephone;

    protected String title;

    protected float totalamount;

    @XmlElement(nillable = true)
    protected List<MGExOrderItem> exOrderItems;

    protected String specialnote;

    protected String createtime;

    protected String modifiedtime;

    protected float firstDayPrice;

    /**
     * Gets the value of the orderid property.
     * 
     * @return possible object is {@link Long }
     * 
     */
    public Long getOrderid() {
        return orderid;
    }

    /**
     * Sets the value of the orderid property.
     * 
     * @param value
     *            allowed object is {@link Long }
     * 
     */
    public void setOrderid(Long value) {
        this.orderid = value;
    }

    /**
     * Gets the value of the adultnum property.
     * 
     */
    public int getAdultnum() {
        return adultnum;
    }

    /**
     * Sets the value of the adultnum property.
     * 
     */
    public void setAdultnum(int value) {
        this.adultnum = value;
    }

    /**
     * Gets the value of the arrivaltime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getArrivaltime() {
        return arrivaltime;
    }

    /**
     * Sets the value of the arrivaltime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setArrivaltime(String value) {
        this.arrivaltime = value;
    }

    /**
     * Gets the value of the arrivaltraffic property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getArrivaltraffic() {
        return arrivaltraffic;
    }

    /**
     * Sets the value of the arrivaltraffic property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setArrivaltraffic(String value) {
        this.arrivaltraffic = value;
    }

    /**
     * Gets the value of the bedtype property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getBedtype() {
        return bedtype;
    }

    /**
     * Sets the value of the bedtype property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setBedtype(Integer value) {
        this.bedtype = value;
    }

    /**
     * Gets the value of the checkindate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCheckindate() {
        return checkindate;
    }

    /**
     * Sets the value of the checkindate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCheckindate(String value) {
        this.checkindate = value;
    }

    /**
     * Gets the value of the checkoutdate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCheckoutdate() {
        return checkoutdate;
    }

    /**
     * Sets the value of the checkoutdate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCheckoutdate(String value) {
        this.checkoutdate = value;
    }

    /**
     * Gets the value of the createdate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCreatedate() {
        return createdate;
    }

    /**
     * Sets the value of the createdate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCreatedate(String value) {
        this.createdate = value;
    }

    /**
     * Gets the value of the creditcardexpires property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCreditcardexpires() {
        return creditcardexpires;
    }

    /**
     * Sets the value of the creditcardexpires property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCreditcardexpires(String value) {
        this.creditcardexpires = value;
    }

    /**
     * Gets the value of the creditcardname property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCreditcardname() {
        return creditcardname;
    }

    /**
     * Sets the value of the creditcardname property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCreditcardname(String value) {
        this.creditcardname = value;
    }

    /**
     * Gets the value of the creditcardno property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCreditcardno() {
        return creditcardno;
    }

    /**
     * Sets the value of the creditcardno property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCreditcardno(String value) {
        this.creditcardno = value;
    }

    /**
     * Gets the value of the creditcardtype property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCreditcardtype() {
        return creditcardtype;
    }

    /**
     * Sets the value of the creditcardtype property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCreditcardtype(String value) {
        this.creditcardtype = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the customerfax property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCustomerfax() {
        return customerfax;
    }

    /**
     * Sets the value of the customerfax property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCustomerfax(String value) {
        this.customerfax = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the exchangerate property.
     * 
     */
    public float getExchangerate() {
        return exchangerate;
    }

    /**
     * Sets the value of the exchangerate property.
     * 
     */
    public void setExchangerate(float value) {
        this.exchangerate = value;
    }

    /**
     * Gets the value of the fellownames property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFellownames() {
        return fellownames;
    }

    /**
     * Sets the value of the fellownames property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFellownames(String value) {
        this.fellownames = value;
    }

    /**
     * Gets the value of the flight property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFlight() {
        return flight;
    }

    /**
     * Sets the value of the flight property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFlight(String value) {
        this.flight = value;
    }

    /**
     * Gets the value of the guaranteetype property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGuaranteetype() {
        return guaranteetype;
    }

    /**
     * Sets the value of the guaranteetype property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGuaranteetype(String value) {
        this.guaranteetype = value;
    }

    /**
     * Gets the value of the hotelcode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getHotelcode() {
        return hotelcode;
    }

    /**
     * Sets the value of the hotelcode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setHotelcode(String value) {
        this.hotelcode = value;
    }

    /**
     * Gets the value of the hotelcodeforchannel property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getHotelcodeforchannel() {
        return hotelcodeforchannel;
    }

    /**
     * Sets the value of the hotelcodeforchannel property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setHotelcodeforchannel(String value) {
        this.hotelcodeforchannel = value;
    }

    /**
     * Gets the value of the hotelid property.
     * 
     * @return possible object is {@link Long }
     * 
     */
    public Long getHotelid() {
        return hotelid;
    }

    /**
     * Sets the value of the hotelid property.
     * 
     * @param value
     *            allowed object is {@link Long }
     * 
     */
    public void setHotelid(Long value) {
        this.hotelid = value;
    }

    /**
     * Gets the value of the hotelname property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getHotelname() {
        return hotelname;
    }

    /**
     * Sets the value of the hotelname property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setHotelname(String value) {
        this.hotelname = value;
    }

    /**
     * Gets the value of the hotelnotes property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getHotelnotes() {
        return hotelnotes;
    }

    /**
     * Sets the value of the hotelnotes property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setHotelnotes(String value) {
        this.hotelnotes = value;
    }

    /**
     * Gets the value of the isguarantee property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIsguarantee() {
        return isguarantee;
    }

    /**
     * Sets the value of the isguarantee property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setIsguarantee(String value) {
        this.isguarantee = value;
    }

    /**
     * Gets the value of the latestarrivaltime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLatestarrivaltime() {
        return latestarrivaltime;
    }

    /**
     * Sets the value of the latestarrivaltime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLatestarrivaltime(String value) {
        this.latestarrivaltime = value;
    }

    /**
     * Gets the value of the linkman property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLinkman() {
        return linkman;
    }

    /**
     * Sets the value of the linkman property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLinkman(String value) {
        this.linkman = value;
    }

    /**
     * Gets the value of the hotelconfirm property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getHotelconfirm() {
        return hotelconfirm;
    }

    /**
     * Sets the value of the hotelconfirm property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setHotelconfirm(Integer value) {
        this.hotelconfirm = value;
    }

    /**
     * Gets the value of the hotelconfirmid property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getHotelconfirmid() {
        return hotelconfirmid;
    }

    /**
     * Sets the value of the hotelconfirmid property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setHotelconfirmid(String value) {
        this.hotelconfirmid = value;
    }

    /**
     * Gets the value of the localtotalamout property.
     * 
     */
    public float getLocaltotalamout() {
        return localtotalamout;
    }

    /**
     * Sets the value of the localtotalamout property.
     * 
     */
    public void setLocaltotalamout(float value) {
        this.localtotalamout = value;
    }

    /**
     * Gets the value of the orderstate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOrderstate() {
        return orderstate;
    }

    /**
     * Sets the value of the orderstate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOrderstate(String value) {
        this.orderstate = value;
    }

    /**
     * Gets the value of the mobile property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the value of the mobile property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMobile(String value) {
        this.mobile = value;
    }

    /**
     * Gets the value of the noshowreason property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getNoshowreason() {
        return noshowreason;
    }

    /**
     * Sets the value of the noshowreason property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setNoshowreason(String value) {
        this.noshowreason = value;
    }

    /**
     * Gets the value of the nosmoking property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getNosmoking() {
        return nosmoking;
    }

    /**
     * Sets the value of the nosmoking property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setNosmoking(String value) {
        this.nosmoking = value;
    }

    /**
     * Gets the value of the ordercd property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOrdercd() {
        return ordercd;
    }

    /**
     * Sets the value of the ordercd property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOrdercd(String value) {
        this.ordercd = value;
    }

    /**
     * Gets the value of the ordercdhotel property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOrdercdhotel() {
        return ordercdhotel;
    }

    /**
     * Sets the value of the ordercdhotel property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOrdercdhotel(String value) {
        this.ordercdhotel = value;
    }

    /**
     * Gets the value of the paymethod property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPaymethod() {
        return paymethod;
    }

    /**
     * Sets the value of the paymethod property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPaymethod(String value) {
        this.paymethod = value;
    }

    /**
     * Gets the value of the paysatus property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPaysatus() {
        return paysatus;
    }

    /**
     * Sets the value of the paysatus property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPaysatus(String value) {
        this.paysatus = value;
    }

    /**
     * Gets the value of the pricetypecode property.
     * 
     */
    public int getPricetypecode() {
        return pricetypecode;
    }

    /**
     * Sets the value of the pricetypecode property.
     * 
     */
    public void setPricetypecode(int value) {
        this.pricetypecode = value;
    }

    /**
     * Gets the value of the rateplancode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRateplancode() {
        return rateplancode;
    }

    /**
     * Sets the value of the rateplancode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRateplancode(String value) {
        this.rateplancode = value;
    }

    /**
     * Gets the value of the roomquantity property.
     * 
     */
    public int getRoomquantity() {
        return roomquantity;
    }

    /**
     * Sets the value of the roomquantity property.
     * 
     */
    public void setRoomquantity(int value) {
        this.roomquantity = value;
    }

    /**
     * Gets the value of the roomtypecode property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getRoomtypecode() {
        return roomtypecode;
    }

    /**
     * Sets the value of the roomtypecode property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setRoomtypecode(Integer value) {
        this.roomtypecode = value;
    }

    /**
     * Gets the value of the roomtypecodeforchannel property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoomtypecodeforchannel() {
        return roomtypecodeforchannel;
    }

    /**
     * Sets the value of the roomtypecodeforchannel property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoomtypecodeforchannel(String value) {
        this.roomtypecodeforchannel = value;
    }

    /**
     * Gets the value of the roomtypeid property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getRoomtypeid() {
        return roomtypeid;
    }

    /**
     * Sets the value of the roomtypeid property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setRoomtypeid(Integer value) {
        this.roomtypeid = value;
    }

    /**
     * Gets the value of the roomtypename property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoomtypename() {
        return roomtypename;
    }

    /**
     * Sets the value of the roomtypename property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoomtypename(String value) {
        this.roomtypename = value;
    }

    /**
     * Gets the value of the specialrequest property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSpecialrequest() {
        return specialrequest;
    }

    /**
     * Sets the value of the specialrequest property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSpecialrequest(String value) {
        this.specialrequest = value;
    }

    /**
     * Gets the value of the telephone property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Sets the value of the telephone property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTelephone(String value) {
        this.telephone = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the totalamount property.
     * 
     */
    public float getTotalamount() {
        return totalamount;
    }

    /**
     * Sets the value of the totalamount property.
     * 
     */
    public void setTotalamount(float value) {
        this.totalamount = value;
    }

    /**
     * Gets the value of the exOrderItems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the exOrderItems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getExOrderItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link MGExOrderItem }
     * 
     * 
     */
    public List<MGExOrderItem> getExOrderItems() {
        if (null == exOrderItems) {
            exOrderItems = new ArrayList<MGExOrderItem>();
        }
        return this.exOrderItems;
    }

    /**
     * Gets the value of the specialnote property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSpecialnote() {
        return specialnote;
    }

    /**
     * Sets the value of the specialnote property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSpecialnote(String value) {
        this.specialnote = value;
    }

    /**
     * Gets the value of the createtime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCreatetime() {
        return createtime;
    }

    /**
     * Sets the value of the createtime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCreatetime(String value) {
        this.createtime = value;
    }

    /**
     * Gets the value of the modifiedtime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getModifiedtime() {
        return modifiedtime;
    }

    /**
     * Sets the value of the modifiedtime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setModifiedtime(String value) {
        this.modifiedtime = value;
    }

    /**
     * Gets the value of the firstDayPrice property.
     * 
     */
    public float getFirstDayPrice() {
        return firstDayPrice;
    }

    /**
     * Sets the value of the firstDayPrice property.
     * 
     */
    public void setFirstDayPrice(float value) {
        this.firstDayPrice = value;
    }

}

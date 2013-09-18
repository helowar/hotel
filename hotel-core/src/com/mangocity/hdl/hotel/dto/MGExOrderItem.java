package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExOrderItem complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre> </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExOrderItem", propOrder = { "number", "orderid", "ordercdforchannel", "roomno",
    "quantity", "checkindate", "checkoutdate", "hotelconfirm", "hotelconfirmid", "guests",
    "noteresult", "orderstate", "sum", "quitdate", "baseprice", "saleprice", "baserate",
    "specialnote", "totalcharges", "createtime", "modifiedtime" })
public class MGExOrderItem {

    protected int number;

    protected Long orderid;

    protected String ordercdforchannel;

    protected Integer roomno;

    protected int quantity;

    @XmlElement(required = true)
    protected String checkindate;

    @XmlElement(required = true)
    protected String checkoutdate;

    protected Integer hotelconfirm;

    protected String hotelconfirmid;

    protected String guests;

    protected Integer noteresult;

    protected String orderstate;

    protected float sum;

    protected String quitdate;

    protected Float baseprice;

    protected Float saleprice;

    protected Float baserate;

    protected String specialnote;

    protected Float totalcharges;

    protected String createtime;

    protected String modifiedtime;

    /**
     * Gets the value of the number property.
     * 
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     */
    public void setNumber(int value) {
        this.number = value;
    }

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
     * Gets the value of the ordercdforchannel property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOrdercdforchannel() {
        return ordercdforchannel;
    }

    /**
     * Sets the value of the ordercdforchannel property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOrdercdforchannel(String value) {
        this.ordercdforchannel = value;
    }

    /**
     * Gets the value of the roomno property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getRoomno() {
        return roomno;
    }

    /**
     * Sets the value of the roomno property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setRoomno(Integer value) {
        this.roomno = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     */
    public void setQuantity(int value) {
        this.quantity = value;
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
     * Gets the value of the guests property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGuests() {
        return guests;
    }

    /**
     * Sets the value of the guests property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGuests(String value) {
        this.guests = value;
    }

    /**
     * Gets the value of the noteresult property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getNoteresult() {
        return noteresult;
    }

    /**
     * Sets the value of the noteresult property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setNoteresult(Integer value) {
        this.noteresult = value;
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
     * Gets the value of the sum property.
     * 
     */
    public float getSum() {
        return sum;
    }

    /**
     * Sets the value of the sum property.
     * 
     */
    public void setSum(float value) {
        this.sum = value;
    }

    /**
     * Gets the value of the quitdate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getQuitdate() {
        return quitdate;
    }

    /**
     * Sets the value of the quitdate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setQuitdate(String value) {
        this.quitdate = value;
    }

    /**
     * Gets the value of the baseprice property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getBaseprice() {
        return baseprice;
    }

    /**
     * Sets the value of the baseprice property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setBaseprice(Float value) {
        this.baseprice = value;
    }

    /**
     * Gets the value of the saleprice property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getSaleprice() {
        return saleprice;
    }

    /**
     * Sets the value of the saleprice property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setSaleprice(Float value) {
        this.saleprice = value;
    }

    /**
     * Gets the value of the baserate property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getBaserate() {
        return baserate;
    }

    /**
     * Sets the value of the baserate property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setBaserate(Float value) {
        this.baserate = value;
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
     * Gets the value of the totalcharges property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getTotalcharges() {
        return totalcharges;
    }

    /**
     * Sets the value of the totalcharges property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setTotalcharges(Float value) {
        this.totalcharges = value;
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

}

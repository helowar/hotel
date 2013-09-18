package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExAuditOrderItem complex type.
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
@XmlType(name = "MGExAuditOrderItem", propOrder = { "basePrice", "baseRate", "checkinDate",
    "checkoutDate", "guests", "hotelConfirm", "hotelConfirmId", "modifiedTime", "noteResult",
    "number", "ordercdForChannel", "orderState", "quantity", "quitDate", "roomNo", "salePrice",
    "specialNote", "sum", "totalCharges" })
public class MGExAuditOrderItem {

    protected float basePrice;

    protected float baseRate;

    @XmlElement(required = true)
    protected String checkinDate;

    protected String checkoutDate;

    protected String guests;

    protected int hotelConfirm;

    protected String hotelConfirmId;

    protected String modifiedTime;

    protected int noteResult;

    protected int number;

    protected String ordercdForChannel;

    protected int orderState;

    protected int quantity;

    protected String quitDate;

    protected String roomNo;

    protected float salePrice;

    protected String specialNote;

    protected float sum;

    protected float totalCharges;

    /**
     * Gets the value of the basePrice property.
     * 
     */
    public float getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the value of the basePrice property.
     * 
     */
    public void setBasePrice(float value) {
        this.basePrice = value;
    }

    /**
     * Gets the value of the baseRate property.
     * 
     */
    public float getBaseRate() {
        return baseRate;
    }

    /**
     * Sets the value of the baseRate property.
     * 
     */
    public void setBaseRate(float value) {
        this.baseRate = value;
    }

    /**
     * Gets the value of the checkinDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCheckinDate() {
        return checkinDate;
    }

    /**
     * Sets the value of the checkinDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCheckinDate(String value) {
        this.checkinDate = value;
    }

    /**
     * Gets the value of the checkoutDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCheckoutDate() {
        return checkoutDate;
    }

    /**
     * Sets the value of the checkoutDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCheckoutDate(String value) {
        this.checkoutDate = value;
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
     * Gets the value of the hotelConfirm property.
     * 
     */
    public int getHotelConfirm() {
        return hotelConfirm;
    }

    /**
     * Sets the value of the hotelConfirm property.
     * 
     */
    public void setHotelConfirm(int value) {
        this.hotelConfirm = value;
    }

    /**
     * Gets the value of the hotelConfirmId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getHotelConfirmId() {
        return hotelConfirmId;
    }

    /**
     * Sets the value of the hotelConfirmId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setHotelConfirmId(String value) {
        this.hotelConfirmId = value;
    }

    /**
     * Gets the value of the modifiedTime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getModifiedTime() {
        return modifiedTime;
    }

    /**
     * Sets the value of the modifiedTime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setModifiedTime(String value) {
        this.modifiedTime = value;
    }

    /**
     * Gets the value of the noteResult property.
     * 
     */
    public int getNoteResult() {
        return noteResult;
    }

    /**
     * Sets the value of the noteResult property.
     * 
     */
    public void setNoteResult(int value) {
        this.noteResult = value;
    }

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
     * Gets the value of the ordercdForChannel property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOrdercdForChannel() {
        return ordercdForChannel;
    }

    /**
     * Sets the value of the ordercdForChannel property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOrdercdForChannel(String value) {
        this.ordercdForChannel = value;
    }

    /**
     * Gets the value of the orderState property.
     * 
     */
    public int getOrderState() {
        return orderState;
    }

    /**
     * Sets the value of the orderState property.
     * 
     */
    public void setOrderState(int value) {
        this.orderState = value;
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
     * Gets the value of the quitDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getQuitDate() {
        return quitDate;
    }

    /**
     * Sets the value of the quitDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setQuitDate(String value) {
        this.quitDate = value;
    }

    /**
     * Gets the value of the roomNo property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoomNo() {
        return roomNo;
    }

    /**
     * Sets the value of the roomNo property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoomNo(String value) {
        this.roomNo = value;
    }

    /**
     * Gets the value of the salePrice property.
     * 
     */
    public float getSalePrice() {
        return salePrice;
    }

    /**
     * Sets the value of the salePrice property.
     * 
     */
    public void setSalePrice(float value) {
        this.salePrice = value;
    }

    /**
     * Gets the value of the specialNote property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSpecialNote() {
        return specialNote;
    }

    /**
     * Sets the value of the specialNote property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSpecialNote(String value) {
        this.specialNote = value;
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
     * Gets the value of the totalCharges property.
     * 
     */
    public float getTotalCharges() {
        return totalCharges;
    }

    /**
     * Sets the value of the totalCharges property.
     * 
     */
    public void setTotalCharges(float value) {
        this.totalCharges = value;
    }

}

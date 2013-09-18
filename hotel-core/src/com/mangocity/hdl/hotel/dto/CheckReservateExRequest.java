package com.mangocity.hdl.hotel.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
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
@XmlType(name = "", propOrder = { "channelType", "hotelId", "chainCode", "roomTypeId",
    "childRoomTypeId", "checkInDate", "checkOutDate", "from", "quantity", "reservItems" })
@XmlRootElement(name = "CheckReservateExRequest")
public class CheckReservateExRequest {

    protected int channelType;

    protected long hotelId;

    protected String chainCode;

    protected long roomTypeId;

    protected long childRoomTypeId;

    @XmlElement(required = true)
    protected String checkInDate;

    @XmlElement(required = true)
    protected String checkOutDate;

    // 试预定时数据来源,来自网站还是非网站.
    protected String from;

    protected int quantity;

    @XmlElement(nillable = true)
    protected List<MGExReservItem> reservItems;

    /**
     * Gets the value of the channelType property.
     * 
     */
    public int getChannelType() {
        return channelType;
    }

    /**
     * Sets the value of the channelType property.
     * 
     */
    public void setChannelType(int value) {
        this.channelType = value;
    }

    /**
     * Gets the value of the hotelId property.
     * 
     */
    public long getHotelId() {
        return hotelId;
    }

    /**
     * Sets the value of the hotelId property.
     * 
     */
    public void setHotelId(long value) {
        this.hotelId = value;
    }

    /**
     * Gets the value of the chainCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getChainCode() {
        return chainCode;
    }

    /**
     * Sets the value of the chainCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setChainCode(String value) {
        this.chainCode = value;
    }

    /**
     * Gets the value of the roomTypeId property.
     * 
     */
    public long getRoomTypeId() {
        return roomTypeId;
    }

    /**
     * Sets the value of the roomTypeId property.
     * 
     */
    public void setRoomTypeId(long value) {
        this.roomTypeId = value;
    }

    /**
     * Gets the value of the childRoomTypeId property.
     * 
     */
    public long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    /**
     * Sets the value of the childRoomTypeId property.
     * 
     */
    public void setChildRoomTypeId(long value) {
        this.childRoomTypeId = value;
    }

    /**
     * Gets the value of the checkInDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCheckInDate() {
        return checkInDate;
    }

    /**
     * Sets the value of the checkInDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCheckInDate(String value) {
        this.checkInDate = value;
    }

    /**
     * Gets the value of the checkOutDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * Sets the value of the checkOutDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCheckOutDate(String value) {
        this.checkOutDate = value;
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
     * Gets the value of the reservItems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the reservItems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getReservItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link MGExReservItem }
     * 
     * 
     */
    public List<MGExReservItem> getReservItems() {
        if (null == reservItems) {
            reservItems = new ArrayList<MGExReservItem>();
        }
        return this.reservItems;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}

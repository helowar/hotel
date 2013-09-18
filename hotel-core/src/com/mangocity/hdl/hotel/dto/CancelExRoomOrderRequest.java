package com.mangocity.hdl.hotel.dto;

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
@XmlType(name = "", propOrder = { "channelType", "channelCode", "hotelId", "orderId",
    "cancelReason", "cancelMessage", "chainCode" })
@XmlRootElement(name = "CancelExRoomOrderRequest")
public class CancelExRoomOrderRequest {

    protected int channelType;

    @XmlElement(required = true)
    protected String channelCode;

    protected long hotelId;

    protected long orderId;

    protected String cancelReason;

    protected String cancelMessage;

    protected String chainCode;

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
     * Gets the value of the channelCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * Sets the value of the channelCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setChannelCode(String value) {
        this.channelCode = value;
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
     * Gets the value of the orderId property.
     * 
     */
    public long getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     */
    public void setOrderId(long value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the cancelReason property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCancelReason() {
        return cancelReason;
    }

    /**
     * Sets the value of the cancelReason property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCancelReason(String value) {
        this.cancelReason = value;
    }

    /**
     * Gets the value of the cancelMessage property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCancelMessage() {
        return cancelMessage;
    }

    /**
     * Sets the value of the cancelMessage property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCancelMessage(String value) {
        this.cancelMessage = value;
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

}

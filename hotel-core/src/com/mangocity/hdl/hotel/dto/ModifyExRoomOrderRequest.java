package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "channelType", "hotelId", "chainCode", "roomOrder" })
@XmlRootElement(name = "ModifyExRoomOrderRequest")
public class ModifyExRoomOrderRequest {

    protected int channelType;

    protected long hotelId;

    protected String chainCode;

    @XmlElement(required = true)
    protected MGExOrder roomOrder;

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
     * Gets the value of the roomOrder property.
     * 
     * @return possible object is {@link MGExOrder }
     * 
     */
    public MGExOrder getRoomOrder() {
        return roomOrder;
    }

    /**
     * Sets the value of the roomOrder property.
     * 
     * @param value
     *            allowed object is {@link MGExOrder }
     * 
     */
    public void setRoomOrder(MGExOrder value) {
        this.roomOrder = value;
    }

}

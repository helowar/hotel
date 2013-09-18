package com.mangocity.hdl.hotel.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "channelCode", "hotelCode", "roomTypeCodes", "chainCode" })
@XmlRootElement(name = "QueryRoomTypeInfoExRequest")
public class QueryRoomTypeInfoExRequest {

    protected int channelCode;

    @XmlElement(required = true)
    protected String hotelCode;

    @XmlElement(required = true)
    protected List<String> roomTypeCodes;

    protected String chainCode;

    /**
     * Gets the value of the channelCode property.
     * 
     */
    public int getChannelCode() {
        return channelCode;
    }

    /**
     * Sets the value of the channelCode property.
     * 
     */
    public void setChannelCode(int value) {
        this.channelCode = value;
    }

    /**
     * Gets the value of the hotelCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getHotelCode() {
        return hotelCode;
    }

    /**
     * Sets the value of the hotelCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setHotelCode(String value) {
        this.hotelCode = value;
    }

    /**
     * Gets the value of the roomTypeCodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the roomTypeCodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getRoomTypeCodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     * 
     * 
     */
    public List<String> getRoomTypeCodes() {
        if (null == roomTypeCodes) {
            roomTypeCodes = new ArrayList<String>();
        }
        return this.roomTypeCodes;
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

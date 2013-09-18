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
@XmlType(name = "", propOrder = { "channelCode", "hotelId", "chainCode" })
@XmlRootElement(name = "QueryHotelInfoExRequest")
public class QueryHotelInfoExRequest {

    protected int channelCode;

    @XmlElement(type = Long.class)
    protected List<Long> hotelId;

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
     * Gets the value of the hotelId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the hotelId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getHotelId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Long }
     * 
     * 
     */
    public List<Long> getHotelId() {
        if (null == hotelId) {
            hotelId = new ArrayList<Long>();
        }
        return this.hotelId;
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

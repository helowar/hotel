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
 * &lt;complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "channelCode", "hotelId", "chainCode", "orderCode", "auditDate" })
@XmlRootElement(name = "DailyAuditExRequest")
public class DailyAuditExRequest {

    protected int channelCode;

    protected long hotelId;

    protected String chainCode;

    @XmlElement(nillable = true)
    protected List<String> orderCode;

    @XmlElement(required = true)
    protected String auditDate;

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
     * Gets the value of the orderCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the orderCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getOrderCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     * 
     * 
     */
    public List<String> getOrderCode() {
        if (null == orderCode) {
            orderCode = new ArrayList<String>();
        }
        return this.orderCode;
    }

    /**
     * Gets the value of the auditDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAuditDate() {
        return auditDate;
    }

    /**
     * Sets the value of the auditDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAuditDate(String value) {
        this.auditDate = value;
    }

}

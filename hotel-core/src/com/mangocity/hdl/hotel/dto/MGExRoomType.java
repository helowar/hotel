package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExRoomType", propOrder = { "roomTypeCode", "maxAdults", "maxExtraMeal",
    "maxExtraBed", "webPayDiscount", "roomTypeName", "acreage", "bedType", "roomQty",
    "descriptions", "roomEquipment" })
public class MGExRoomType {

    @XmlElement(required = true)
    protected String roomTypeCode;

    protected int maxAdults;

    protected int maxExtraMeal;

    protected int maxExtraBed;

    protected String webPayDiscount;

    @XmlElement(required = true)
    protected String roomTypeName;

    protected int acreage;

    protected String bedType;

    protected int roomQty;

    protected String descriptions;

    protected int roomEquipment;

    /**
     * Gets the value of the roomTypeCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    /**
     * Sets the value of the roomTypeCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoomTypeCode(String value) {
        this.roomTypeCode = value;
    }

    /**
     * Gets the value of the maxAdults property.
     * 
     */
    public int getMaxAdults() {
        return maxAdults;
    }

    /**
     * Sets the value of the maxAdults property.
     * 
     */
    public void setMaxAdults(int value) {
        this.maxAdults = value;
    }

    /**
     * Gets the value of the maxExtraMeal property.
     * 
     */
    public int getMaxExtraMeal() {
        return maxExtraMeal;
    }

    /**
     * Sets the value of the maxExtraMeal property.
     * 
     */
    public void setMaxExtraMeal(int value) {
        this.maxExtraMeal = value;
    }

    /**
     * Gets the value of the maxExtraBed property.
     * 
     */
    public int getMaxExtraBed() {
        return maxExtraBed;
    }

    /**
     * Sets the value of the maxExtraBed property.
     * 
     */
    public void setMaxExtraBed(int value) {
        this.maxExtraBed = value;
    }

    /**
     * Gets the value of the webPayDiscount property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getWebPayDiscount() {
        return webPayDiscount;
    }

    /**
     * Sets the value of the webPayDiscount property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setWebPayDiscount(String value) {
        this.webPayDiscount = value;
    }

    /**
     * Gets the value of the roomTypeName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoomTypeName() {
        return roomTypeName;
    }

    /**
     * Sets the value of the roomTypeName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoomTypeName(String value) {
        this.roomTypeName = value;
    }

    /**
     * Gets the value of the acreage property.
     * 
     */
    public int getAcreage() {
        return acreage;
    }

    /**
     * Sets the value of the acreage property.
     * 
     */
    public void setAcreage(int value) {
        this.acreage = value;
    }

    /**
     * Gets the value of the bedType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getBedType() {
        return bedType;
    }

    /**
     * Sets the value of the bedType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setBedType(String value) {
        this.bedType = value;
    }

    /**
     * Gets the value of the roomQty property.
     * 
     */
    public int getRoomQty() {
        return roomQty;
    }

    /**
     * Sets the value of the roomQty property.
     * 
     */
    public void setRoomQty(int value) {
        this.roomQty = value;
    }

    /**
     * Gets the value of the descriptions property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDescriptions() {
        return descriptions;
    }

    /**
     * Sets the value of the descriptions property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDescriptions(String value) {
        this.descriptions = value;
    }

    /**
     * Gets the value of the roomEquipment property.
     * 
     */
    public int getRoomEquipment() {
        return roomEquipment;
    }

    /**
     * Sets the value of the roomEquipment property.
     * 
     */
    public void setRoomEquipment(int value) {
        this.roomEquipment = value;
    }

}

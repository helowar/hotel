package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExCancelPolicy complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExCancelPolicy", propOrder = { "cardAheadDay", "cardAheadTime", "cardAmount",
    "cardCancelAheadTime", "cardDeductType", "cardOperateType", "channeltype", "childRoomTypeCode",
    "childRoomTypeName", "enddate", "exCancelPolicyId", "hotelcodeforchannel", "modifyTime",
    "rateplancode", "rateplanname", "roomtypeidforchannel", "startdate", "type" })
public class MGExCancelPolicy {

    protected String cardAheadDay;

    protected String cardAheadTime;

    protected Float cardAmount;

    protected String cardCancelAheadTime;

    protected String cardDeductType;

    protected String cardOperateType;

    protected Integer channeltype;

    protected String childRoomTypeCode;

    protected String childRoomTypeName;

    protected String enddate;

    protected Integer exCancelPolicyId;

    protected String hotelcodeforchannel;

    protected String modifyTime;

    protected String rateplancode;

    protected String rateplanname;

    protected String roomtypeidforchannel;

    protected String startdate;

    protected Integer type;

    /**
     * Gets the value of the cardAheadDay property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCardAheadDay() {
        return cardAheadDay;
    }

    /**
     * Sets the value of the cardAheadDay property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCardAheadDay(String value) {
        this.cardAheadDay = value;
    }

    /**
     * Gets the value of the cardAheadTime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCardAheadTime() {
        return cardAheadTime;
    }

    /**
     * Sets the value of the cardAheadTime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCardAheadTime(String value) {
        this.cardAheadTime = value;
    }

    /**
     * Gets the value of the cardAmount property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getCardAmount() {
        return cardAmount;
    }

    /**
     * Sets the value of the cardAmount property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setCardAmount(Float value) {
        this.cardAmount = value;
    }

    /**
     * Gets the value of the cardCancelAheadTime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCardCancelAheadTime() {
        return cardCancelAheadTime;
    }

    /**
     * Sets the value of the cardCancelAheadTime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCardCancelAheadTime(String value) {
        this.cardCancelAheadTime = value;
    }

    /**
     * Gets the value of the cardDeductType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCardDeductType() {
        return cardDeductType;
    }

    /**
     * Sets the value of the cardDeductType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCardDeductType(String value) {
        this.cardDeductType = value;
    }

    /**
     * Gets the value of the cardOperateType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCardOperateType() {
        return cardOperateType;
    }

    /**
     * Sets the value of the cardOperateType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCardOperateType(String value) {
        this.cardOperateType = value;
    }

    /**
     * Gets the value of the channeltype property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getChanneltype() {
        return channeltype;
    }

    /**
     * Sets the value of the channeltype property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setChanneltype(Integer value) {
        this.channeltype = value;
    }

    /**
     * Gets the value of the childRoomTypeCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getChildRoomTypeCode() {
        return childRoomTypeCode;
    }

    /**
     * Sets the value of the childRoomTypeCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setChildRoomTypeCode(String value) {
        this.childRoomTypeCode = value;
    }

    /**
     * Gets the value of the childRoomTypeName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getChildRoomTypeName() {
        return childRoomTypeName;
    }

    /**
     * Sets the value of the childRoomTypeName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setChildRoomTypeName(String value) {
        this.childRoomTypeName = value;
    }

    /**
     * Gets the value of the enddate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getEnddate() {
        return enddate;
    }

    /**
     * Sets the value of the enddate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEnddate(String value) {
        this.enddate = value;
    }

    /**
     * Gets the value of the exCancelPolicyId property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getExCancelPolicyId() {
        return exCancelPolicyId;
    }

    /**
     * Sets the value of the exCancelPolicyId property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setExCancelPolicyId(Integer value) {
        this.exCancelPolicyId = value;
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
     * Gets the value of the modifyTime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getModifyTime() {
        return modifyTime;
    }

    /**
     * Sets the value of the modifyTime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setModifyTime(String value) {
        this.modifyTime = value;
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
     * Gets the value of the rateplanname property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRateplanname() {
        return rateplanname;
    }

    /**
     * Sets the value of the rateplanname property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRateplanname(String value) {
        this.rateplanname = value;
    }

    /**
     * Gets the value of the roomtypeidforchannel property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoomtypeidforchannel() {
        return roomtypeidforchannel;
    }

    /**
     * Sets the value of the roomtypeidforchannel property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoomtypeidforchannel(String value) {
        this.roomtypeidforchannel = value;
    }

    /**
     * Gets the value of the startdate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getStartdate() {
        return startdate;
    }

    /**
     * Sets the value of the startdate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setStartdate(String value) {
        this.startdate = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setType(Integer value) {
        this.type = value;
    }

}

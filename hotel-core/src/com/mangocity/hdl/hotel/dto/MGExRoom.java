package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExRoom", propOrder = { "ableQty", "channeltype", "enddate", "exRoomId",
    "hotelcodeforchannel", "modifyTime", "quotaPattern", "quotaType", "roomState",
    "roomtypeidforchannel", "startdate", "totalQty" })
public class MGExRoom {

    protected Integer ableQty;

    protected Integer channeltype;

    protected String enddate;

    protected Integer exRoomId;

    protected String hotelcodeforchannel;

    protected String modifyTime;

    protected String quotaPattern;

    protected String quotaType;

    protected String roomState;

    protected String roomtypeidforchannel;

    protected String startdate;

    protected Integer totalQty;

    /**
     * Gets the value of the ableQty property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getAbleQty() {
        return ableQty;
    }

    /**
     * Sets the value of the ableQty property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setAbleQty(Integer value) {
        this.ableQty = value;
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
     * Gets the value of the exRoomId property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getExRoomId() {
        return exRoomId;
    }

    /**
     * Sets the value of the exRoomId property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setExRoomId(Integer value) {
        this.exRoomId = value;
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
     * Gets the value of the quotaPattern property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getQuotaPattern() {
        return quotaPattern;
    }

    /**
     * Sets the value of the quotaPattern property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setQuotaPattern(String value) {
        this.quotaPattern = value;
    }

    /**
     * Gets the value of the quotaType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getQuotaType() {
        return quotaType;
    }

    /**
     * Sets the value of the quotaType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setQuotaType(String value) {
        this.quotaType = value;
    }

    /**
     * Gets the value of the roomState property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoomState() {
        return roomState;
    }

    /**
     * Sets the value of the roomState property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoomState(String value) {
        this.roomState = value;
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
     * Gets the value of the totalQty property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getTotalQty() {
        return totalQty;
    }

    /**
     * Sets the value of the totalQty property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setTotalQty(Integer value) {
        this.totalQty = value;
    }

}

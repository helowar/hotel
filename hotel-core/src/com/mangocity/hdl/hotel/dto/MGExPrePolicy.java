package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExPrePolicy complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;MGExPrePolicy&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;balanceMethod&quot; type=&quot;{http://
 *         www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;channeltype&quot; type=&quot;{http://www.w
 *         3.org/2001/XMLSchema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;childRoomTypeCode&quot; type=&quot;{http://www.w
 *         3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;childRoomTypeName&quot; type=&quot;{http://www.w
 *         3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;enddate&quot; type=&quot;{http://www.w3.org/2001
 *         /XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;exGuaranteeId&quot; type=&quot;{http://www.w3.or
 *         g/2001/XMLSchema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;hotelcodeforchannel&quot; type=&quot;{http://www.w
 *         3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;modifyTime&quot; type=&quot;{http://www.w3.org/20
 *         01/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;prepayAheadTime&quot; type=&quot;{http://www.w3
 *         .org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;prepayMoneyType&quot; type=&quot;{http://www.w3
 *         .org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplancode&quot; type=&quot;{http://www.
 *         w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplanname&quot; type=&quot;{http://www.w
 *         3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;roomtypeidforchannel&quot; type=&quot;{http:/
 *         /www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;startdate&quot; type=&quot;{http://www.w3.org/20
 *         01/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;timeLimit&quot; type=&quot;{http://www.w3.org/2
 *         001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;timeLimitType&quot; type=&quot;{http://www.w3.or
 *         g/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExPrePolicy", propOrder = { "balanceMethod", "channeltype", "childRoomTypeCode",
    "childRoomTypeName", "enddate", "exGuaranteeId", "hotelcodeforchannel", "modifyTime",
    "prepayAheadTime", "prepayMoneyType", "rateplancode", "rateplanname", "roomtypeidforchannel",
    "startdate", "timeLimit", "timeLimitType" })
public class MGExPrePolicy {

    protected String balanceMethod;

    protected Integer channeltype;

    protected String childRoomTypeCode;

    protected String childRoomTypeName;

    protected String enddate;

    protected Integer exGuaranteeId;

    protected String hotelcodeforchannel;

    protected String modifyTime;

    protected String prepayAheadTime;

    protected String prepayMoneyType;

    protected String rateplancode;

    protected String rateplanname;

    protected String roomtypeidforchannel;

    protected String startdate;

    protected String timeLimit;

    protected String timeLimitType;

    /**
     * Gets the value of the balanceMethod property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getBalanceMethod() {
        return balanceMethod;
    }

    /**
     * Sets the value of the balanceMethod property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setBalanceMethod(String value) {
        this.balanceMethod = value;
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
     * Gets the value of the exGuaranteeId property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getExGuaranteeId() {
        return exGuaranteeId;
    }

    /**
     * Sets the value of the exGuaranteeId property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setExGuaranteeId(Integer value) {
        this.exGuaranteeId = value;
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
     * Gets the value of the prepayAheadTime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPrepayAheadTime() {
        return prepayAheadTime;
    }

    /**
     * Sets the value of the prepayAheadTime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPrepayAheadTime(String value) {
        this.prepayAheadTime = value;
    }

    /**
     * Gets the value of the prepayMoneyType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPrepayMoneyType() {
        return prepayMoneyType;
    }

    /**
     * Sets the value of the prepayMoneyType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPrepayMoneyType(String value) {
        this.prepayMoneyType = value;
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
     * Gets the value of the timeLimit property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTimeLimit() {
        return timeLimit;
    }

    /**
     * Sets the value of the timeLimit property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTimeLimit(String value) {
        this.timeLimit = value;
    }

    /**
     * Gets the value of the timeLimitType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTimeLimitType() {
        return timeLimitType;
    }

    /**
     * Sets the value of the timeLimitType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTimeLimitType(String value) {
        this.timeLimitType = value;
    }

}

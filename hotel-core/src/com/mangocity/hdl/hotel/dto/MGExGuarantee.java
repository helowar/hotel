package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExGuarantee complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;MGExGuarantee&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;assureConditions&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;assureType&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;channeltype&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;childRoomTypeCode&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;childRoomTypeName&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;enddate&quot; type=&quot;{http:
 *         //www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;exGuaranteeId&quot; type=&quot;{http
 *         ://www.w3.org/2001/XMLSchema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;hotelcodeforchannel&quot; type=&quot;{
 *         http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;lastAssureTime&quot; type=&quot;{
 *         http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplancode&quot; type=&quot;{
 *         http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplanname&quot; type=&quot;{
 *         http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;roomtypeidforchannel&quot; 
 *         type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;startdate&quot; type=&quot;{
 *         http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExGuarantee", propOrder = { "assureConditions", "assureType", "channeltype",
    "childRoomTypeCode", "childRoomTypeName", "enddate", "exGuaranteeId", "hotelcodeforchannel",
    "lastAssureTime", "rateplancode", "rateplanname", "roomtypeidforchannel", "startdate" })
public class MGExGuarantee {

    protected String assureConditions;

    protected String assureType;

    protected Integer channeltype;

    protected String childRoomTypeCode;

    protected String childRoomTypeName;

    protected String enddate;

    protected Integer exGuaranteeId;

    protected String hotelcodeforchannel;

    protected String lastAssureTime;

    protected String rateplancode;

    protected String rateplanname;

    protected String roomtypeidforchannel;

    protected String startdate;

    /**
     * Gets the value of the assureConditions property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAssureConditions() {
        return assureConditions;
    }

    /**
     * Sets the value of the assureConditions property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAssureConditions(String value) {
        this.assureConditions = value;
    }

    /**
     * Gets the value of the assureType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAssureType() {
        return assureType;
    }

    /**
     * Sets the value of the assureType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAssureType(String value) {
        this.assureType = value;
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
     * Gets the value of the lastAssureTime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLastAssureTime() {
        return lastAssureTime;
    }

    /**
     * Sets the value of the lastAssureTime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLastAssureTime(String value) {
        this.lastAssureTime = value;
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

}

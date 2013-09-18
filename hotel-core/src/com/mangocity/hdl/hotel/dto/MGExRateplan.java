package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExRateplan complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;MGExRateplan&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;channelcode&quot; type=&quot;{http://www.w3.or
 *         g/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;exRateplanId&quot; type=&quot;{http://www.w3.
 *         org/2001/XMLSchema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;guarateedescs&quot; type=&quot;{http://www.w3.or
 *         g/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;guarateetype&quot; type=&quot;{http://www.w3.org/
 *         2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;hotelcodeforchannel&quot; type=&quot;{http://www.
 *         w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;lastmodiftime&quot; type=&quot;{http://www.w3.or
 *         g/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplancode&quot; type=&quot;{http://www.w3.org
 *         /2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplanname&quot; type=&quot;{http://www.w3.org
 *         /2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;supplycode&quot; type=&quot;{http://www.w3.org/2
 *         001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExRateplan", propOrder = { "channelcode", "exRateplanId", "guarateedescs",
    "guarateetype", "hotelcodeforchannel", "lastmodiftime", "rateplancode", "rateplanname",
    "supplycode" })
public class MGExRateplan {

    protected String channelcode;

    protected Integer exRateplanId;

    protected String guarateedescs;

    protected String guarateetype;

    protected String hotelcodeforchannel;

    protected String lastmodiftime;

    protected String rateplancode;

    protected String rateplanname;

    protected String supplycode;

    /**
     * Gets the value of the channelcode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getChannelcode() {
        return channelcode;
    }

    /**
     * Sets the value of the channelcode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setChannelcode(String value) {
        this.channelcode = value;
    }

    /**
     * Gets the value of the exRateplanId property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getExRateplanId() {
        return exRateplanId;
    }

    /**
     * Sets the value of the exRateplanId property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setExRateplanId(Integer value) {
        this.exRateplanId = value;
    }

    /**
     * Gets the value of the guarateedescs property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGuarateedescs() {
        return guarateedescs;
    }

    /**
     * Sets the value of the guarateedescs property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGuarateedescs(String value) {
        this.guarateedescs = value;
    }

    /**
     * Gets the value of the guarateetype property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGuarateetype() {
        return guarateetype;
    }

    /**
     * Sets the value of the guarateetype property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGuarateetype(String value) {
        this.guarateetype = value;
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
     * Gets the value of the lastmodiftime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLastmodiftime() {
        return lastmodiftime;
    }

    /**
     * Sets the value of the lastmodiftime property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLastmodiftime(String value) {
        this.lastmodiftime = value;
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
     * Gets the value of the supplycode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSupplycode() {
        return supplycode;
    }

    /**
     * Sets the value of the supplycode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSupplycode(String value) {
        this.supplycode = value;
    }

}

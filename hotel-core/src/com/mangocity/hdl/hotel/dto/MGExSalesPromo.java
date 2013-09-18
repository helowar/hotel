package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExSalesPromo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;MGExSalesPromo&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;beginDate&quot; type=&quot;{http://www.w3.org/2001/XMLSc
 *         hema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;channeltype&quot; type=&quot;{http://www.w3.org/2001/XML
 *         Schema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;endDate&quot; type=&quot;{http://www.w3.org/2001/XMLSche
 *         ma}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;exPromotionId&quot; type=&quot;{http://www.w3.org/2001/
 *         XMLSchema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;modifyTime&quot; type=&quot;{http://www.w3.org/2001/XMLS
 *         chema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplancode&quot; type=&quot;{http://www.w3.org/2001/X
 *         MLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplanname&quot; type=&quot;{http://www.w3.org/2001/XML
 *         Schema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;roomtypeidforchannel&quot; type=&quot;{http://www.w3.org/
 *         2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;salePromoCont&quot; type=&quot;{http://www.w3.org/2001/XM
 *         LSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExSalesPromo", propOrder = { "beginDate", "channeltype", "endDate",
    "exPromotionId", "modifyTime", "rateplancode", "rateplanname", "roomtypeidforchannel",
    "salePromoCont" })
public class MGExSalesPromo {

    protected String beginDate;

    protected Integer channeltype;

    protected String endDate;

    protected Integer exPromotionId;

    protected String modifyTime;

    protected String rateplancode;

    protected String rateplanname;

    protected String roomtypeidforchannel;

    protected String salePromoCont;

    /**
     * Gets the value of the beginDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the value of the beginDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setBeginDate(String value) {
        this.beginDate = value;
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
     * Gets the value of the endDate property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the exPromotionId property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getExPromotionId() {
        return exPromotionId;
    }

    /**
     * Sets the value of the exPromotionId property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setExPromotionId(Integer value) {
        this.exPromotionId = value;
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
     * Gets the value of the salePromoCont property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSalePromoCont() {
        return salePromoCont;
    }

    /**
     * Sets the value of the salePromoCont property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSalePromoCont(String value) {
        this.salePromoCont = value;
    }

}

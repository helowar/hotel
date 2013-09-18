package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExPrice complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;MGExPrice&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;addScope&quot; type=&quot;{http://www.w3.org/200
 *         1/XMLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;basePrice&quot; type=&quot;{http://www.w3.org/20
 *         01/XMLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;canAddScope&quot; type=&quot;{http://www.w3.org/
 *         2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;channelcode&quot; type=&quot;{http://www.w3.org/2
 *         001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;commission&quot; type=&quot;{http://www.w3.org/20
 *         01/XMLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;commissionRate&quot; type=&quot;{http://www.w3.or
 *         g/2001/XMLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;currency&quot; type=&quot;{http://www.w3.org/2001
 *         /XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;enddate&quot; type=&quot;{http://www.w3.org/2001
 *         /XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;exPriceId&quot; type=&quot;{http://www.w3.org/20
 *         01/XMLSchema}int&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;hotelcodeforchannel&quot; type=&quot;{http://ww
 *         w.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;incBreakfastNumber&quot; type=&quot;{http://www
 * w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;incBreakfastPrice&quot; type=&quot;{http://www.w
 *         3.org/2001/XMLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;incBreakfastType&quot; type=&quot;{http://www.w3
 *         .org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;mangosaleprice&quot; type=&quot;{http://www.w3.o
 *         rg/2001/XMLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;marketprice&quot; type=&quot;{http://www.w3.org/
 *         2001/XMLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;modifyTime&quot; type=&quot;{http://www.w3.org/2
 *         001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;paymenttype&quot; type=&quot;{http://www.w3.org/
 *         2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;price&quot; type=&quot;{http://www.w3.org/2001/X
 *         MLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplancode&quot; type=&quot;{http://www.w3.org
 *         /2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;rateplanname&quot; type=&quot;{http://www.w3.or
 *         g/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;roomtypecode&quot; type=&quot;{http://www.w3.or
 *         g/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;roomtypename&quot; type=&quot;{http://www.w3.or
 *         g/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;serviceChargeRate&quot; type=&quot;{http://www.w
 *         3.org/2001/XMLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;servicecharge&quot; type=&quot;{http://www.w3.o
 *         rg/2001/XMLSchema}float&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;startdate&quot; type=&quot;{http://www.w3.org/2
 *         001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;supplycode&quot; type=&quot;{http://www.w3.org/
 *         2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExPrice", propOrder = { "addScope", "basePrice", "canAddScope", "channelcode",
    "commission", "commissionRate", "currency", "enddate", "exPriceId", "hotelcodeforchannel",
    "incBreakfastNumber", "incBreakfastPrice", "incBreakfastType", "mangosaleprice", "marketprice",
    "modifyTime", "paymenttype", "price", "rateplancode", "rateplanname", "roomtypecode",
    "roomtypename", "serviceChargeRate", "servicecharge", "startdate", "supplycode" })
public class MGExPrice {

    protected Float addScope;

    protected Float basePrice;

    protected String canAddScope;

    protected String channelcode;

    protected Float commission;

    protected Float commissionRate;

    protected String currency;

    protected String enddate;

    protected Integer exPriceId;

    protected String hotelcodeforchannel;

    protected String incBreakfastNumber;

    protected Float incBreakfastPrice;

    protected String incBreakfastType;

    protected Float mangosaleprice;

    protected Float marketprice;

    protected String modifyTime;

    protected String paymenttype;

    protected Float price;

    protected String rateplancode;

    protected String rateplanname;

    protected String roomtypecode;

    protected String roomtypename;

    protected Float serviceChargeRate;

    protected Float servicecharge;

    protected String startdate;

    protected String supplycode;

    /**
     * Gets the value of the addScope property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getAddScope() {
        return addScope;
    }

    /**
     * Sets the value of the addScope property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setAddScope(Float value) {
        this.addScope = value;
    }

    /**
     * Gets the value of the basePrice property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the value of the basePrice property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setBasePrice(Float value) {
        this.basePrice = value;
    }

    /**
     * Gets the value of the canAddScope property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCanAddScope() {
        return canAddScope;
    }

    /**
     * Sets the value of the canAddScope property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCanAddScope(String value) {
        this.canAddScope = value;
    }

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
     * Gets the value of the commission property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getCommission() {
        return commission;
    }

    /**
     * Sets the value of the commission property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setCommission(Float value) {
        this.commission = value;
    }

    /**
     * Gets the value of the commissionRate property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getCommissionRate() {
        return commissionRate;
    }

    /**
     * Sets the value of the commissionRate property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setCommissionRate(Float value) {
        this.commissionRate = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCurrency(String value) {
        this.currency = value;
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
     * Gets the value of the exPriceId property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getExPriceId() {
        return exPriceId;
    }

    /**
     * Sets the value of the exPriceId property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setExPriceId(Integer value) {
        this.exPriceId = value;
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
     * Gets the value of the incBreakfastNumber property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIncBreakfastNumber() {
        return incBreakfastNumber;
    }

    /**
     * Sets the value of the incBreakfastNumber property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setIncBreakfastNumber(String value) {
        this.incBreakfastNumber = value;
    }

    /**
     * Gets the value of the incBreakfastPrice property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getIncBreakfastPrice() {
        return incBreakfastPrice;
    }

    /**
     * Sets the value of the incBreakfastPrice property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setIncBreakfastPrice(Float value) {
        this.incBreakfastPrice = value;
    }

    /**
     * Gets the value of the incBreakfastType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIncBreakfastType() {
        return incBreakfastType;
    }

    /**
     * Sets the value of the incBreakfastType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setIncBreakfastType(String value) {
        this.incBreakfastType = value;
    }

    /**
     * Gets the value of the mangosaleprice property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getMangosaleprice() {
        return mangosaleprice;
    }

    /**
     * Sets the value of the mangosaleprice property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setMangosaleprice(Float value) {
        this.mangosaleprice = value;
    }

    /**
     * Gets the value of the marketprice property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getMarketprice() {
        return marketprice;
    }

    /**
     * Sets the value of the marketprice property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setMarketprice(Float value) {
        this.marketprice = value;
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
     * Gets the value of the paymenttype property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPaymenttype() {
        return paymenttype;
    }

    /**
     * Sets the value of the paymenttype property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPaymenttype(String value) {
        this.paymenttype = value;
    }

    /**
     * Gets the value of the price property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setPrice(Float value) {
        this.price = value;
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
     * Gets the value of the roomtypecode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoomtypecode() {
        return roomtypecode;
    }

    /**
     * Sets the value of the roomtypecode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoomtypecode(String value) {
        this.roomtypecode = value;
    }

    /**
     * Gets the value of the roomtypename property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRoomtypename() {
        return roomtypename;
    }

    /**
     * Sets the value of the roomtypename property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRoomtypename(String value) {
        this.roomtypename = value;
    }

    /**
     * Gets the value of the serviceChargeRate property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getServiceChargeRate() {
        return serviceChargeRate;
    }

    /**
     * Sets the value of the serviceChargeRate property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setServiceChargeRate(Float value) {
        this.serviceChargeRate = value;
    }

    /**
     * Gets the value of the servicecharge property.
     * 
     * @return possible object is {@link Float }
     * 
     */
    public Float getServicecharge() {
        return servicecharge;
    }

    /**
     * Sets the value of the servicecharge property.
     * 
     * @param value
     *            allowed object is {@link Float }
     * 
     */
    public void setServicecharge(Float value) {
        this.servicecharge = value;
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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.13 at 04:34:51 ���� CST 
//


package zhx.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * ������Ϣ
 * 
 * <p>Java class for RateAmountMessageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RateAmountMessageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StatusApplicationControl" type="{}StatusApplicationControlType" minOccurs="0"/>
 *         &lt;element name="Rates" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Rate" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;extension base="{}RateUploadType">
 *                           &lt;attribute name="RateChangeIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="LocatorID" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RateAmountMessageType", propOrder = {
    "statusApplicationControl",
    "rates"
})
public class RateAmountMessageType {

    @XmlElement(name = "StatusApplicationControl")
    protected StatusApplicationControlType statusApplicationControl;
    @XmlElement(name = "Rates")
    protected RateAmountMessageType.Rates rates;
    @XmlAttribute(name = "LocatorID")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger locatorID;

    /**
     * Gets the value of the statusApplicationControl property.
     * 
     * @return
     *     possible object is
     *     {@link StatusApplicationControlType }
     *     
     */
    public StatusApplicationControlType getStatusApplicationControl() {
        return statusApplicationControl;
    }

    /**
     * Sets the value of the statusApplicationControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusApplicationControlType }
     *     
     */
    public void setStatusApplicationControl(StatusApplicationControlType value) {
        this.statusApplicationControl = value;
    }

    /**
     * Gets the value of the rates property.
     * 
     * @return
     *     possible object is
     *     {@link RateAmountMessageType.Rates }
     *     
     */
    public RateAmountMessageType.Rates getRates() {
        return rates;
    }

    /**
     * Sets the value of the rates property.
     * 
     * @param value
     *     allowed object is
     *     {@link RateAmountMessageType.Rates }
     *     
     */
    public void setRates(RateAmountMessageType.Rates value) {
        this.rates = value;
    }

    /**
     * Gets the value of the locatorID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLocatorID() {
        return locatorID;
    }

    /**
     * Sets the value of the locatorID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLocatorID(BigInteger value) {
        this.locatorID = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Rate" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;extension base="{}RateUploadType">
     *                 &lt;attribute name="RateChangeIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *               &lt;/extension>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "rate"
    })
    public static class Rates {

        @XmlElement(name = "Rate", required = true)
        protected List<RateAmountMessageType.Rates.Rate> rate;

        /**
         * Gets the value of the rate property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the rate property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRate().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RateAmountMessageType.Rates.Rate }
         * 
         * 
         */
        public List<RateAmountMessageType.Rates.Rate> getRate() {
            if (rate == null) {
                rate = new ArrayList<RateAmountMessageType.Rates.Rate>();
            }
            return this.rate;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;extension base="{}RateUploadType">
         *       &lt;attribute name="RateChangeIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" />
         *     &lt;/extension>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Rate
            extends RateUploadType
        {

            @XmlAttribute(name = "RateChangeIndicator")
            protected Boolean rateChangeIndicator;

            /**
             * Gets the value of the rateChangeIndicator property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public Boolean isRateChangeIndicator() {
                return rateChangeIndicator;
            }

            /**
             * Sets the value of the rateChangeIndicator property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setRateChangeIndicator(Boolean value) {
                this.rateChangeIndicator = value;
            }

        }

    }

}

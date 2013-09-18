//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.13 at 04:34:51 ���� CST 
//


package zhx.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="TransactionName" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="Header" type="{}HeaderType"/>
 *         &lt;element name="DestinationSystemCodes" type="{}DestinationSystemCodesType" minOccurs="0"/>
 *         &lt;element name="HotelVendorRS">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;sequence>
 *                     &lt;element name="Success" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                     &lt;element name="Warnings" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                     &lt;element ref="{}Vendor"/>
 *                     &lt;element name="Vendors">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element ref="{}Vendor"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IdentityInfo" type="{}IdentityInfoType"/>
 *         &lt;element ref="{}Source"/>
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
    "transactionName",
    "header",
    "destinationSystemCodes",
    "hotelVendorRS",
    "identityInfo",
    "source"
})
@XmlRootElement(name = "OTResponse")
public class OTResponseHVRS {

    @XmlElement(name = "TransactionName", required = true)
    protected Object transactionName;
    @XmlElement(name = "Header", required = true)
    protected HeaderType header;
    @XmlElement(name = "DestinationSystemCodes")
    protected DestinationSystemCodesType destinationSystemCodes;
    @XmlElement(name = "HotelVendorRS", required = true)
    protected OTResponseHVRS.HotelVendorRS hotelVendorRS;
    @XmlElement(name = "IdentityInfo", required = true)
    protected IdentityInfoType identityInfo;
    @XmlElement(name = "Source", required = true)
    protected Source source;

    /**
     * Gets the value of the transactionName property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getTransactionName() {
        return transactionName;
    }

    /**
     * Sets the value of the transactionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setTransactionName(Object value) {
        this.transactionName = value;
    }

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderType }
     *     
     */
    public HeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderType }
     *     
     */
    public void setHeader(HeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the destinationSystemCodes property.
     * 
     * @return
     *     possible object is
     *     {@link DestinationSystemCodesType }
     *     
     */
    public DestinationSystemCodesType getDestinationSystemCodes() {
        return destinationSystemCodes;
    }

    /**
     * Sets the value of the destinationSystemCodes property.
     * 
     * @param value
     *     allowed object is
     *     {@link DestinationSystemCodesType }
     *     
     */
    public void setDestinationSystemCodes(DestinationSystemCodesType value) {
        this.destinationSystemCodes = value;
    }

    /**
     * Gets the value of the hotelVendorRS property.
     * 
     * @return
     *     possible object is
     *     {@link OTResponseHVRS.HotelVendorRS }
     *     
     */
    public OTResponseHVRS.HotelVendorRS getHotelVendorRS() {
        return hotelVendorRS;
    }

    /**
     * Sets the value of the hotelVendorRS property.
     * 
     * @param value
     *     allowed object is
     *     {@link OTResponseHVRS.HotelVendorRS }
     *     
     */
    public void setHotelVendorRS(OTResponseHVRS.HotelVendorRS value) {
        this.hotelVendorRS = value;
    }

    /**
     * Gets the value of the identityInfo property.
     * 
     * @return
     *     possible object is
     *     {@link IdentityInfoType }
     *     
     */
    public IdentityInfoType getIdentityInfo() {
        return identityInfo;
    }

    /**
     * Sets the value of the identityInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentityInfoType }
     *     
     */
    public void setIdentityInfo(IdentityInfoType value) {
        this.identityInfo = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link Source }
     *     
     */
    public Source getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link Source }
     *     
     */
    public void setSource(Source value) {
        this.source = value;
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
     *       &lt;choice>
     *         &lt;sequence>
     *           &lt;element name="Success" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *           &lt;element name="Warnings" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
     *           &lt;element ref="{}Vendor"/>
     *           &lt;element name="Vendors">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element ref="{}Vendor"/>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *         &lt;/sequence>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "success",
        "warnings",
        "vendor",
        "vendors"
    })
    public static class HotelVendorRS {

        @XmlElement(name = "Success")
        protected Object success;
        @XmlElement(name = "Warnings")
        protected Object warnings;
        @XmlElement(name = "Vendor")
        protected Vendor vendor;
        @XmlElement(name = "Vendors")
        protected OTResponseHVRS.HotelVendorRS.Vendors vendors;

        /**
         * Gets the value of the success property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getSuccess() {
            return success;
        }

        /**
         * Sets the value of the success property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setSuccess(Object value) {
            this.success = value;
        }

        /**
         * Gets the value of the warnings property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getWarnings() {
            return warnings;
        }

        /**
         * Sets the value of the warnings property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setWarnings(Object value) {
            this.warnings = value;
        }

        /**
         * Gets the value of the vendor property.
         * 
         * @return
         *     possible object is
         *     {@link Vendor }
         *     
         */
        public Vendor getVendor() {
            return vendor;
        }

        /**
         * Sets the value of the vendor property.
         * 
         * @param value
         *     allowed object is
         *     {@link Vendor }
         *     
         */
        public void setVendor(Vendor value) {
            this.vendor = value;
        }

        /**
         * Gets the value of the vendors property.
         * 
         * @return
         *     possible object is
         *     {@link OTResponseHVRS.HotelVendorRS.Vendors }
         *     
         */
        public OTResponseHVRS.HotelVendorRS.Vendors getVendors() {
            return vendors;
        }

        /**
         * Sets the value of the vendors property.
         * 
         * @param value
         *     allowed object is
         *     {@link OTResponseHVRS.HotelVendorRS.Vendors }
         *     
         */
        public void setVendors(OTResponseHVRS.HotelVendorRS.Vendors value) {
            this.vendors = value;
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
         *         &lt;element ref="{}Vendor"/>
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
            "vendor"
        })
        public static class Vendors {

            @XmlElement(name = "Vendor", required = true)
            protected Vendor vendor;

            /**
             * Gets the value of the vendor property.
             * 
             * @return
             *     possible object is
             *     {@link Vendor }
             *     
             */
            public Vendor getVendor() {
                return vendor;
            }

            /**
             * Sets the value of the vendor property.
             * 
             * @param value
             *     allowed object is
             *     {@link Vendor }
             *     
             */
            public void setVendor(Vendor value) {
                this.vendor = value;
            }

        }

    }

}
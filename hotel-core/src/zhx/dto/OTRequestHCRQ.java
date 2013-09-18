//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.13 at 04:34:51 ���� CST 
//


package zhx.dto;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="HotelCancelRQ">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="HotelReservations">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}HotelReservation" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="CancelReason" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                 &lt;/sequence>
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
    "hotelCancelRQ",
    "identityInfo",
    "source"
})
@XmlRootElement(name = "OTRequest")
public class OTRequestHCRQ {

    @XmlElement(name = "TransactionName", required = true)
    protected Object transactionName;
    @XmlElement(name = "Header", required = true)
    protected HeaderType header;
    @XmlElement(name = "DestinationSystemCodes")
    protected DestinationSystemCodesType destinationSystemCodes;
    @XmlElement(name = "HotelCancelRQ", required = true)
    protected OTRequestHCRQ.HotelCancelRQ hotelCancelRQ;
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
     * Gets the value of the hotelCancelRQ property.
     * 
     * @return
     *     possible object is
     *     {@link OTRequestHCRQ.HotelCancelRQ }
     *     
     */
    public OTRequestHCRQ.HotelCancelRQ getHotelCancelRQ() {
        return hotelCancelRQ;
    }

    /**
     * Sets the value of the hotelCancelRQ property.
     * 
     * @param value
     *     allowed object is
     *     {@link OTRequestHCRQ.HotelCancelRQ }
     *     
     */
    public void setHotelCancelRQ(OTRequestHCRQ.HotelCancelRQ value) {
        this.hotelCancelRQ = value;
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
     *       &lt;sequence>
     *         &lt;element name="HotelReservations">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}HotelReservation" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="CancelReason" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
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
        "hotelReservations",
        "cancelReason"
    })
    public static class HotelCancelRQ {

        @XmlElement(name = "HotelReservations", required = true)
        protected OTRequestHCRQ.HotelCancelRQ.HotelReservations hotelReservations;
        @XmlElement(name = "CancelReason", required = true)
        protected Object cancelReason;

        /**
         * Gets the value of the hotelReservations property.
         * 
         * @return
         *     possible object is
         *     {@link OTRequestHCRQ.HotelCancelRQ.HotelReservations }
         *     
         */
        public OTRequestHCRQ.HotelCancelRQ.HotelReservations getHotelReservations() {
            return hotelReservations;
        }

        /**
         * Sets the value of the hotelReservations property.
         * 
         * @param value
         *     allowed object is
         *     {@link OTRequestHCRQ.HotelCancelRQ.HotelReservations }
         *     
         */
        public void setHotelReservations(OTRequestHCRQ.HotelCancelRQ.HotelReservations value) {
            this.hotelReservations = value;
        }

        /**
         * Gets the value of the cancelReason property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getCancelReason() {
            return cancelReason;
        }

        /**
         * Sets the value of the cancelReason property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setCancelReason(Object value) {
            this.cancelReason = value;
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
         *         &lt;element ref="{}HotelReservation" maxOccurs="unbounded"/>
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
            "hotelReservation"
        })
        public static class HotelReservations {

            @XmlElement(name = "HotelReservation", required = true)
            protected List<HotelReservation> hotelReservation;

            /**
             * Gets the value of the hotelReservation property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the hotelReservation property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getHotelReservation().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link HotelReservation }
             * 
             * 
             */
            public List<HotelReservation> getHotelReservation() {
                if (hotelReservation == null) {
                    hotelReservation = new ArrayList<HotelReservation>();
                }
                return this.hotelReservation;
            }

        }

    }

}

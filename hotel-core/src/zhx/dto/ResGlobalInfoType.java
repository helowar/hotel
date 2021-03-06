//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.09.13 at 04:34:51 ���� CST 
//


package zhx.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResGlobalInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResGlobalInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Total" type="{}TotalType"/>
 *         &lt;element name="Fees" type="{}FeesType" minOccurs="0"/>
 *         &lt;element name="HotelReservationIDs">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="HotelReservationID">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="ResID_Type" type="{}StringLength1to64" />
 *                           &lt;attribute name="ResID_Value" type="{}StringLength1to16" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
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
@XmlType(name = "ResGlobalInfoType", propOrder = {
    "total",
    "fees",
    "hotelReservationIDs"
})
public class ResGlobalInfoType {

    @XmlElement(name = "Total", required = true)
    protected TotalType total;
    @XmlElement(name = "Fees")
    protected FeesType fees;
    @XmlElement(name = "HotelReservationIDs", required = true)
    protected ResGlobalInfoType.HotelReservationIDs hotelReservationIDs;

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link TotalType }
     *     
     */
    public TotalType getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link TotalType }
     *     
     */
    public void setTotal(TotalType value) {
        this.total = value;
    }

    /**
     * Gets the value of the fees property.
     * 
     * @return
     *     possible object is
     *     {@link FeesType }
     *     
     */
    public FeesType getFees() {
        return fees;
    }

    /**
     * Sets the value of the fees property.
     * 
     * @param value
     *     allowed object is
     *     {@link FeesType }
     *     
     */
    public void setFees(FeesType value) {
        this.fees = value;
    }

    /**
     * Gets the value of the hotelReservationIDs property.
     * 
     * @return
     *     possible object is
     *     {@link ResGlobalInfoType.HotelReservationIDs }
     *     
     */
    public ResGlobalInfoType.HotelReservationIDs getHotelReservationIDs() {
        return hotelReservationIDs;
    }

    /**
     * Sets the value of the hotelReservationIDs property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResGlobalInfoType.HotelReservationIDs }
     *     
     */
    public void setHotelReservationIDs(ResGlobalInfoType.HotelReservationIDs value) {
        this.hotelReservationIDs = value;
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
     *         &lt;element name="HotelReservationID">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="ResID_Type" type="{}StringLength1to64" />
     *                 &lt;attribute name="ResID_Value" type="{}StringLength1to16" />
     *               &lt;/restriction>
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
        "hotelReservationID"
    })
    public static class HotelReservationIDs {

        @XmlElement(name = "HotelReservationID", required = true)
        protected ResGlobalInfoType.HotelReservationIDs.HotelReservationID hotelReservationID;

        /**
         * Gets the value of the hotelReservationID property.
         * 
         * @return
         *     possible object is
         *     {@link ResGlobalInfoType.HotelReservationIDs.HotelReservationID }
         *     
         */
        public ResGlobalInfoType.HotelReservationIDs.HotelReservationID getHotelReservationID() {
            return hotelReservationID;
        }

        /**
         * Sets the value of the hotelReservationID property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResGlobalInfoType.HotelReservationIDs.HotelReservationID }
         *     
         */
        public void setHotelReservationID(ResGlobalInfoType.HotelReservationIDs.HotelReservationID value) {
            this.hotelReservationID = value;
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
         *       &lt;attribute name="ResID_Type" type="{}StringLength1to64" />
         *       &lt;attribute name="ResID_Value" type="{}StringLength1to16" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class HotelReservationID {

            @XmlAttribute(name = "ResID_Type")
            protected String resIDType;
            @XmlAttribute(name = "ResID_Value")
            protected String resIDValue;

            /**
             * Gets the value of the resIDType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getResIDType() {
                return resIDType;
            }

            /**
             * Sets the value of the resIDType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setResIDType(String value) {
                this.resIDType = value;
            }

            /**
             * Gets the value of the resIDValue property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getResIDValue() {
                return resIDValue;
            }

            /**
             * Sets the value of the resIDValue property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setResIDValue(String value) {
                this.resIDValue = value;
            }

        }

    }

}

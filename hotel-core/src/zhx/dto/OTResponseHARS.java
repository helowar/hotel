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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="HotelAvailRS">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="Errors">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Error">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                     &lt;attribute name="ErrorDesc" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;sequence>
 *                     &lt;element name="Success" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                     &lt;element name="Warnings" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                     &lt;element name="RespPageInfo">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="TotalPageNum" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                               &lt;element name="TotalNum" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                               &lt;element name="CurrentPage" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="RoomStays">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element ref="{}RoomStay" maxOccurs="unbounded"/>
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
    "hotelAvailRS",
    "identityInfo",
    "source",
    "errors"
})
@XmlRootElement(name = "OTResponse")
public class OTResponseHARS {

    @XmlElement(name = "TransactionName", required = true)
    protected Object transactionName;
    @XmlElement(name = "Header", required = true)
    protected HeaderType header;
    @XmlElement(name = "DestinationSystemCodes")
    protected DestinationSystemCodesType destinationSystemCodes;
    @XmlElement(name = "HotelAvailRS", required = true)
    protected OTResponseHARS.HotelAvailRS hotelAvailRS;
    @XmlElement(name = "IdentityInfo", required = true)
    protected IdentityInfoType identityInfo;
    @XmlElement(name = "Source", required = true)
    protected Source source;   
    @XmlElement(name = "Errors")
    protected OTResponseHARS.Errors errors;
    
    /**
     * Gets the value of the errors property.
     * 
     * @return
     *     possible object is
     *     {@link OTResponseHARS.Errors }
     *     
     */
    public OTResponseHARS.Errors getErrors() {
        return errors;
    }

    /**
     * Sets the value of the errors property.
     * 
     * @param value
     *     allowed object is
     *     {@link OTResponseHARS.Errors }
     *     
     */
    public void setErrors(OTResponseHARS.Errors value) {
        this.errors = value;
    }

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
     * Gets the value of the hotelAvailRS property.
     * 
     * @return
     *     possible object is
     *     {@link OTResponseHARS.HotelAvailRS }
     *     
     */
    public OTResponseHARS.HotelAvailRS getHotelAvailRS() {
        return hotelAvailRS;
    }

    /**
     * Sets the value of the hotelAvailRS property.
     * 
     * @param value
     *     allowed object is
     *     {@link OTResponseHARS.HotelAvailRS }
     *     
     */
    public void setHotelAvailRS(OTResponseHARS.HotelAvailRS value) {
        this.hotelAvailRS = value;
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
     *         &lt;element name="Error">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="ErrorDesc" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
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
        "error"
    })
    public static class Errors {

        @XmlElement(name = "Error", required = true)
        protected OTResponseHARS.Errors.Error error;

        /**
         * Gets the value of the error property.
         * 
         * @return
         *     possible object is
         *     {@link OTResponseHARS.Errors.Error }
         *     
         */
        public OTResponseHARS.Errors.Error getError() {
            return error;
        }

        /**
         * Sets the value of the error property.
         * 
         * @param value
         *     allowed object is
         *     {@link OTResponseHARS.Errors.Error }
         *     
         */
        public void setError(OTResponseHARS.Errors.Error value) {
            this.error = value;
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
         *       &lt;attribute name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="ErrorDesc" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Error {

            @XmlAttribute(name = "ErrorCode")
            @XmlSchemaType(name = "anySimpleType")
            protected String errorCode;
            @XmlAttribute(name = "ErrorDesc")
            @XmlSchemaType(name = "anySimpleType")
            protected String errorDesc;

            /**
             * Gets the value of the errorCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getErrorCode() {
                return errorCode;
            }

            /**
             * Sets the value of the errorCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setErrorCode(String value) {
                this.errorCode = value;
            }

            /**
             * Gets the value of the errorDesc property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getErrorDesc() {
                return errorDesc;
            }

            /**
             * Sets the value of the errorDesc property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setErrorDesc(String value) {
                this.errorDesc = value;
            }

        }

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
     *         &lt;element name="Errors">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Error">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                           &lt;attribute name="ErrorDesc" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;sequence>
     *           &lt;element name="Success" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *           &lt;element name="Warnings" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
     *           &lt;element name="RespPageInfo">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="TotalPageNum" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *                     &lt;element name="TotalNum" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *                     &lt;element name="CurrentPage" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *           &lt;element name="RoomStays">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element ref="{}RoomStay" maxOccurs="unbounded"/>
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
        "errors",
        "success",
        "warnings",
        "respPageInfo",
        "roomStays"
    })
    public static class HotelAvailRS {

        @XmlElement(name = "Errors")
        protected OTResponseHARS.HotelAvailRS.Errors errors;
        @XmlElement(name = "Success")
        protected Object success;
        @XmlElement(name = "Warnings")
        protected Object warnings;
        @XmlElement(name = "RespPageInfo")
        protected OTResponseHARS.HotelAvailRS.RespPageInfo respPageInfo;
        @XmlElement(name = "RoomStays")
        protected OTResponseHARS.HotelAvailRS.RoomStays roomStays;

        /**
         * Gets the value of the errors property.
         * 
         * @return
         *     possible object is
         *     {@link OTResponseHARS.HotelAvailRS.Errors }
         *     
         */
        public OTResponseHARS.HotelAvailRS.Errors getErrors() {
            return errors;
        }

        /**
         * Sets the value of the errors property.
         * 
         * @param value
         *     allowed object is
         *     {@link OTResponseHARS.HotelAvailRS.Errors }
         *     
         */
        public void setErrors(OTResponseHARS.HotelAvailRS.Errors value) {
            this.errors = value;
        }

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
         * Gets the value of the respPageInfo property.
         * 
         * @return
         *     possible object is
         *     {@link OTResponseHARS.HotelAvailRS.RespPageInfo }
         *     
         */
        public OTResponseHARS.HotelAvailRS.RespPageInfo getRespPageInfo() {
            return respPageInfo;
        }

        /**
         * Sets the value of the respPageInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link OTResponseHARS.HotelAvailRS.RespPageInfo }
         *     
         */
        public void setRespPageInfo(OTResponseHARS.HotelAvailRS.RespPageInfo value) {
            this.respPageInfo = value;
        }

        /**
         * Gets the value of the roomStays property.
         * 
         * @return
         *     possible object is
         *     {@link OTResponseHARS.HotelAvailRS.RoomStays }
         *     
         */
        public OTResponseHARS.HotelAvailRS.RoomStays getRoomStays() {
            return roomStays;
        }

        /**
         * Sets the value of the roomStays property.
         * 
         * @param value
         *     allowed object is
         *     {@link OTResponseHARS.HotelAvailRS.RoomStays }
         *     
         */
        public void setRoomStays(OTResponseHARS.HotelAvailRS.RoomStays value) {
            this.roomStays = value;
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
         *         &lt;element name="Error">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                 &lt;attribute name="ErrorDesc" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
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
            "error"
        })
        public static class Errors {

            @XmlElement(name = "Error", required = true)
            protected OTResponseHARS.HotelAvailRS.Errors.Error error;

            /**
             * Gets the value of the error property.
             * 
             * @return
             *     possible object is
             *     {@link OTResponseHARS.HotelAvailRS.Errors.Error }
             *     
             */
            public OTResponseHARS.HotelAvailRS.Errors.Error getError() {
                return error;
            }

            /**
             * Sets the value of the error property.
             * 
             * @param value
             *     allowed object is
             *     {@link OTResponseHARS.HotelAvailRS.Errors.Error }
             *     
             */
            public void setError(OTResponseHARS.HotelAvailRS.Errors.Error value) {
                this.error = value;
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
             *       &lt;attribute name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *       &lt;attribute name="ErrorDesc" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Error {

                @XmlAttribute(name = "ErrorCode")
                @XmlSchemaType(name = "anySimpleType")
                protected String errorCode;
                @XmlAttribute(name = "ErrorDesc")
                @XmlSchemaType(name = "anySimpleType")
                protected String errorDesc;

                /**
                 * Gets the value of the errorCode property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getErrorCode() {
                    return errorCode;
                }

                /**
                 * Sets the value of the errorCode property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setErrorCode(String value) {
                    this.errorCode = value;
                }

                /**
                 * Gets the value of the errorDesc property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getErrorDesc() {
                    return errorDesc;
                }

                /**
                 * Sets the value of the errorDesc property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setErrorDesc(String value) {
                    this.errorDesc = value;
                }

            }

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
         *         &lt;element name="TotalPageNum" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
         *         &lt;element name="TotalNum" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
         *         &lt;element name="CurrentPage" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
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
            "totalPageNum",
            "totalNum",
            "currentPage"
        })
        public static class RespPageInfo {

            @XmlElement(name = "TotalPageNum", required = true)
            protected Object totalPageNum;
            @XmlElement(name = "TotalNum", required = true)
            protected Object totalNum;
            @XmlElement(name = "CurrentPage", required = true)
            protected Object currentPage;

            /**
             * Gets the value of the totalPageNum property.
             * 
             * @return
             *     possible object is
             *     {@link Object }
             *     
             */
            public Object getTotalPageNum() {
                return totalPageNum;
            }

            /**
             * Sets the value of the totalPageNum property.
             * 
             * @param value
             *     allowed object is
             *     {@link Object }
             *     
             */
            public void setTotalPageNum(Object value) {
                this.totalPageNum = value;
            }

            /**
             * Gets the value of the totalNum property.
             * 
             * @return
             *     possible object is
             *     {@link Object }
             *     
             */
            public Object getTotalNum() {
                return totalNum;
            }

            /**
             * Sets the value of the totalNum property.
             * 
             * @param value
             *     allowed object is
             *     {@link Object }
             *     
             */
            public void setTotalNum(Object value) {
                this.totalNum = value;
            }

            /**
             * Gets the value of the currentPage property.
             * 
             * @return
             *     possible object is
             *     {@link Object }
             *     
             */
            public Object getCurrentPage() {
                return currentPage;
            }

            /**
             * Sets the value of the currentPage property.
             * 
             * @param value
             *     allowed object is
             *     {@link Object }
             *     
             */
            public void setCurrentPage(Object value) {
                this.currentPage = value;
            }

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
         *         &lt;element ref="{}RoomStay" maxOccurs="unbounded"/>
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
            "roomStay"
        })
        public static class RoomStays {

            @XmlElement(name = "RoomStay", required = true)
            protected List<RoomStay> roomStay;

            /**
             * Gets the value of the roomStay property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the roomStay property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getRoomStay().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link RoomStay }
             * 
             * 
             */
            public List<RoomStay> getRoomStay() {
                if (roomStay == null) {
                    roomStay = new ArrayList<RoomStay>();
                }
                return this.roomStay;
            }

        }

    }

}

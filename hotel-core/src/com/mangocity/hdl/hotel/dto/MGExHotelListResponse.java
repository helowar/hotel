
package com.mangocity.hdl.hotel.dto;

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
 *         &lt;element name="result" type="{http://www.mangocity.com/hdl/hotel/dto}MGExResult"/>
 *         &lt;element name="hotelList" type="{http://www.mangocity.com/hdl/hotel/dto}MGExHotelListReturn" maxOccurs="unbounded" minOccurs="0"/>
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
    "result",
    "hotelList"
})
@XmlRootElement(name = "MGExHotelListResponse")
public class MGExHotelListResponse {

    @XmlElement(required = true)
    protected MGExResult result;
    @XmlElement(nillable = true)
    protected List<MGExHotelListReturn> hotelList;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link MGExResult }
     *     
     */
    public MGExResult getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link MGExResult }
     *     
     */
    public void setResult(MGExResult value) {
        this.result = value;
    }

    /**
     * Gets the value of the hotelList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hotelList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHotelList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MGExHotelListReturn }
     * 
     * 
     */
    public List<MGExHotelListReturn> getHotelList() {
        if (hotelList == null) {
            hotelList = new ArrayList<MGExHotelListReturn>();
        }
        return this.hotelList;
    }

}

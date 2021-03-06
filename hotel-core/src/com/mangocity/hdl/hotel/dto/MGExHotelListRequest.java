
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
 *         &lt;element name="keyWord" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="channelCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hotelList" type="{http://www.mangocity.com/hdl/hotel/dto}MGExHotelList" maxOccurs="unbounded" minOccurs="0"/>
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
    "keyWord",
    "channelCode",
    "hotelList"
})
@XmlRootElement(name = "MGExHotelListRequest")
public class MGExHotelListRequest {

    protected String keyWord;
    protected int channelCode;
    @XmlElement(nillable = true)
    protected List<MGExHotelList> hotelList;

    /**
     * Gets the value of the keyWord property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyWord() {
        return keyWord;
    }

    /**
     * Sets the value of the keyWord property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyWord(String value) {
        this.keyWord = value;
    }

    /**
     * Gets the value of the channelCode property.
     * 
     */
    public int getChannelCode() {
        return channelCode;
    }

    /**
     * Sets the value of the channelCode property.
     * 
     */
    public void setChannelCode(int value) {
        this.channelCode = value;
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
     * {@link MGExHotelList }
     * 
     * 
     */
    public List<MGExHotelList> getHotelList() {
        if (hotelList == null) {
            hotelList = new ArrayList<MGExHotelList>();
        }
        return this.hotelList;
    }

}

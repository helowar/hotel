
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
 *         &lt;element name="result" type="{http://www.mangocity.com/hdl/hotel/dto}Result" minOccurs="0"/>
 *         &lt;element name="roomTypes" type="{http://www.mangocity.com/hdl/hotel/dto}CheckRoomTypeResponse" maxOccurs="unbounded" minOccurs="0"/>
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
    "hotelId",
    "roomTypes",
    "priceTypeResponse"
})
@XmlRootElement(name = "CheckHotelReservateExResponse")
public class CheckHotelReservateExResponse {

    protected Result result;
    
    protected String hotelId;
    
    //用来存放每天的配额信息（暂时只有中旅使用到这个属性）
    @XmlElement(nillable = true)
    protected List<CheckRoomTypeResponse> roomTypes;
    
  //用来存放每天的价格和是否可订的信息
    @XmlElement(nillable = true)
    protected List<CheckPriceTypeResponse> priceTypeResponse;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link Result }
     *     
     */
    public Result getResult() {
    	if(null == result){
    		result = new Result();
    	}
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link Result }
     *     
     */
    public void setResult(Result value) {
        this.result = value;
    }

    /**
     * Gets the value of the roomTypes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roomTypes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoomTypes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CheckRoomTypeResponse }
     * 
     * 
     */
    public List<CheckRoomTypeResponse> getRoomTypes() {
        if (roomTypes == null) {
            roomTypes = new ArrayList<CheckRoomTypeResponse>();
        }
        return this.roomTypes;
    }

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public void setRoomTypes(List<CheckRoomTypeResponse> roomTypes) {
		this.roomTypes = roomTypes;
	}

	public List<CheckPriceTypeResponse> getPriceTypeResponse() {
		return priceTypeResponse;
	}

	public void setPriceTypeResponse(List<CheckPriceTypeResponse> priceTypeResponse) {
		this.priceTypeResponse = priceTypeResponse;
	}

}

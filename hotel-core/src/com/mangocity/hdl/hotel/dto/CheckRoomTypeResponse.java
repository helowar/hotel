
package com.mangocity.hdl.hotel.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CheckRoomTypeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CheckRoomTypeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="result" type="{http://www.mangocity.com/hdl/hotel/dto}Result" minOccurs="0"/>
 *         &lt;element name="priceTypes" type="{http://www.mangocity.com/hdl/hotel/dto}CheckPriceTypeResponse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CheckRoomTypeResponse", propOrder = {
    "result",
    "roomTypeId",
    "roomTypeIdDesc",
    "roomTypeRespItems"
})
public class CheckRoomTypeResponse {

    protected Result result;
    
    protected String roomTypeId;//价格类型ID
    
    protected String roomTypeIdDesc; // 房型描述
    
    protected List<CheckRoomTypeResponseItem> roomTypeRespItems;
    
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

	public String getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(String roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public String getRoomTypeIdDesc() {
		return roomTypeIdDesc;
	}

	public void setRoomTypeIdDesc(String roomTypeIdDesc) {
		this.roomTypeIdDesc = roomTypeIdDesc;
	}

	public List<CheckRoomTypeResponseItem> getRoomTypeRespItems() {
		return roomTypeRespItems;
	}

	public void setRoomTypeRespItems(List<CheckRoomTypeResponseItem> roomTypeRespItems) {
		this.roomTypeRespItems = roomTypeRespItems;
	}

}

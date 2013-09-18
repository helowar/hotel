
package com.mangocity.hdl.hotel.dto;

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
 *         &lt;element name="channelType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hotelId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="chainCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="checkInDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="checkOutDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="from" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "channelType",
    "hotelId",
    "chainCode",
    "checkInDate",
    "checkOutDate",
    "from",
    "quantity",
    "roomTypes",
    "childRoomTypes"
})
@XmlRootElement(name = "CheckHotelReservateExRequest")
public class CheckHotelReservateExRequest {
	
	//渠道类型
	@XmlElement(required = true)
    protected int channelType;
	
	//酒店ID
    @XmlElement(required = true)
    protected long hotelId;
    
    protected String chainCode;
    
    //离店日期
    @XmlElement(required = true)
    protected String checkInDate;
    
    //入住日期
    @XmlElement(required = true)
    protected String checkOutDate;
    
    protected String from;
    
    //预订房间数量
    protected int quantity;   
    
    //房型数据，格式：123#13#12
    protected String roomTypes;
    
    //价格类型数据，格式：123#13#12
    protected String childRoomTypes;

    /**
     * Gets the value of the channelType property.
     * 
     */
    public int getChannelType() {
        return channelType;
    }

    /**
     * Sets the value of the channelType property.
     * 
     */
    public void setChannelType(int value) {
        this.channelType = value;
    }

    /**
     * Gets the value of the hotelId property.
     * 
     */
    public long getHotelId() {
        return hotelId;
    }

    /**
     * Sets the value of the hotelId property.
     * 
     */
    public void setHotelId(long value) {
        this.hotelId = value;
    }

    /**
     * Gets the value of the chainCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChainCode() {
        return chainCode;
    }

    /**
     * Sets the value of the chainCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChainCode(String value) {
        this.chainCode = value;
    }

    /**
     * Gets the value of the checkInDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckInDate() {
        return checkInDate;
    }

    /**
     * Sets the value of the checkInDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckInDate(String value) {
        this.checkInDate = value;
    }

    /**
     * Gets the value of the checkOutDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * Sets the value of the checkOutDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckOutDate(String value) {
        this.checkOutDate = value;
    }

    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrom(String value) {
        this.from = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     */
    public void setQuantity(int value) {
        this.quantity = value;
    }

	public String getRoomTypes() {
		return roomTypes;
	}

	public void setRoomTypes(String roomTypes) {
		this.roomTypes = roomTypes;
	}

	public String getChildRoomTypes() {
		return childRoomTypes;
	}

	public void setChildRoomTypes(String childRoomTypes) {
		this.childRoomTypes = childRoomTypes;
	}

}

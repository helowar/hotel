
package com.mangocity.hdl.hotel.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CheckPriceTypeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CheckPriceTypeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="result" type="{http://www.mangocity.com/hdl/hotel/dto}Result" minOccurs="0"/>
 *         &lt;element name="payMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reservItems" type="{http://www.mangocity.com/hdl/hotel/dto}MGExReservItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="flagSecondConfirm" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CheckPriceTypeResponse", propOrder = {
    "result",
    "payMethod",
    "roomTypeId",
    "roomTypeIdDesc",
    "childRoomTypeId",
    "childRoomTypeIdDesc",
    "reservItems",
    "flagSecondConfirm"
})
public class CheckPriceTypeResponse {

    protected Result result;
    
    protected String payMethod;
    
    protected String roomTypeId;//价格类型ID
    
    protected String roomTypeIdDesc; // 房型描述
    
    protected String childRoomTypeId;
    
    protected String childRoomTypeIdDesc;
    
    @XmlElement(nillable = true)
    protected List<MGExReservItem> reservItems;
    
    //是否需要二次确认，只有如家集团使用到这个属性
    protected boolean flagSecondConfirm;

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
     * Gets the value of the payMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayMethod() {
        return payMethod;
    }

    /**
     * Sets the value of the payMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayMethod(String value) {
        this.payMethod = value;
    }

    /**
     * Gets the value of the reservItems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reservItems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReservItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MGExReservItem }
     * 
     * 
     */
    public List<MGExReservItem> getReservItems() {
        if (reservItems == null) {
            reservItems = new ArrayList<MGExReservItem>();
        }
        return this.reservItems;
    }

    /**
     * Gets the value of the flagSecondConfirm property.
     * 
     */
    public boolean isFlagSecondConfirm() {
        return flagSecondConfirm;
    }

    /**
     * Sets the value of the flagSecondConfirm property.
     * 
     */
    public void setFlagSecondConfirm(boolean value) {
        this.flagSecondConfirm = value;
    }

	public String getChildRoomTypeId() {
		return childRoomTypeId;
	}

	public void setChildRoomTypeId(String childRoomTypeId) {
		this.childRoomTypeId = childRoomTypeId;
	}

	public String getChildRoomTypeIdDesc() {
		return childRoomTypeIdDesc;
	}

	public void setChildRoomTypeIdDesc(String childRoomTypeIdDesc) {
		this.childRoomTypeIdDesc = childRoomTypeIdDesc;
	}

	public void setReservItems(List<MGExReservItem> reservItems) {
		this.reservItems = reservItems;
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

}

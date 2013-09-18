package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "result" })
@XmlRootElement(name = "ModifyExRoomOrderResponse")
public class ModifyExRoomOrderResponse {

    @XmlElement(required = true)
    protected MGExResult result;

    /**
     * Gets the value of the result property.
     * 
     * @return possible object is {@link MGExResult }
     * 
     */
    public MGExResult getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *            allowed object is {@link MGExResult }
     * 
     */
    public void setResult(MGExResult value) {
        this.result = value;
    }

}

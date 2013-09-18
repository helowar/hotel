package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "result" })
@XmlRootElement(name = "CancelExRoomOrderResponse")
public class CancelExRoomOrderResponse {

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

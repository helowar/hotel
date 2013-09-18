package com.mangocity.hdl.hotel.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "infoStr" })
@XmlRootElement(name = "SendTestInfoResponse")
public class SendTestInfoResponse {

    @XmlElement(required = true)
    protected String infoStr;

    /**
     * Gets the value of the infoStr property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInfoStr() {
        return infoStr;
    }

    /**
     * Sets the value of the infoStr property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInfoStr(String value) {
        this.infoStr = value;
    }

}

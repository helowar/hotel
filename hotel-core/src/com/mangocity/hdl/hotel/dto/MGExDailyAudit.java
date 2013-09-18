package com.mangocity.hdl.hotel.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for MGExDailyAudit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MGExDailyAudit", propOrder = { "orderCode", "ordercd", "orderid", "orderItem" })
public class MGExDailyAudit {

    @XmlElement(required = true)
    protected String orderCode;

    // 芒果网内部的cd
    protected String ordercd;

    // 芒果网内部的id
    protected String orderid;

    @XmlElement(nillable = true)
    protected List<MGExAuditOrderItem> orderItem;

    /**
     * Gets the value of the orderCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * Sets the value of the orderCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setOrderCode(String value) {
        this.orderCode = value;
    }

    /**
     * Gets the value of the orderItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the orderItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getOrderItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link MGExAuditOrderItem }
     * 
     * 
     */
    public List<MGExAuditOrderItem> getOrderItem() {
        if (null == orderItem) {
            orderItem = new ArrayList<MGExAuditOrderItem>();
        }
        return this.orderItem;
    }

    public String getOrdercd() {
        return ordercd;
    }

    public void setOrdercd(String ordercd) {
        this.ordercd = ordercd;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

}

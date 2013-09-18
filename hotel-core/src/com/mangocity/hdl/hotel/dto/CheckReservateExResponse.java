package com.mangocity.hdl.hotel.dto;

import java.util.ArrayList;
import java.util.List;
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
 * &lt;complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "result", "reason", "htmlRow", "payMethod", "reservItems", "flagSecondConfirm" })
@XmlRootElement(name = "CheckReservateExResponse")
public class CheckReservateExResponse {

    protected int result;

    protected String reason;

    /**
     * ajax调用时页面上对应的行
     */
    protected String htmlRow;

    /**
     * 付款方式
     */
    protected String payMethod;

    @XmlElement(nillable = true)
    protected List<MGExReservItem> reservItems;
    
    protected boolean flagSecondConfirm;

    /**
     * Gets the value of the result property.
     * 
     */
    public int getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     */
    public void setResult(int value) {
        this.result = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setReason(String value) {
        this.reason = value;
    }

    /**
     * Gets the value of the reservItems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the reservItems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getReservItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link MGExReservItem }
     * 
     * 
     */
    public List<MGExReservItem> getReservItems() {
        if (null == reservItems) {
            reservItems = new ArrayList<MGExReservItem>();
        }
        return this.reservItems;
    }

    public String getHtmlRow() {
        return htmlRow;
    }

    public void setHtmlRow(String htmlRow) {
        this.htmlRow = htmlRow;
    }
    
    public boolean getFlagSecondConfirm() {
		return flagSecondConfirm;
	}

}

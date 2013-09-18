package com.mangocity.hdl.hotel.dto;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "roomTypeInfos" })
@XmlRootElement(name = "QueryRoomTypeInfoExResponse")
public class QueryRoomTypeInfoExResponse {

    @XmlElement(nillable = true)
    protected List<MGExRoomType> roomTypeInfos;

    /**
     * Gets the value of the roomTypeInfos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the roomTypeInfos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getRoomTypeInfos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link MGExRoomType }
     * 
     * 
     */
    public List<MGExRoomType> getRoomTypeInfos() {
        if (null == roomTypeInfos) {
            roomTypeInfos = new ArrayList<MGExRoomType>();
        }
        return this.roomTypeInfos;
    }

}

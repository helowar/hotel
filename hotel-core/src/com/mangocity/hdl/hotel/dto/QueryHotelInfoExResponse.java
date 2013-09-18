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
@XmlType(name = "", propOrder = { "hotels" })
@XmlRootElement(name = "QueryHotelInfoExResponse")
public class QueryHotelInfoExResponse {

    @XmlElement(nillable = true)
    protected List<MGExHotel> hotels;

    /**
     * Gets the value of the hotels property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the hotels property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getHotels().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link MGExHotel }
     * 
     * 
     */
    public List<MGExHotel> getHotels() {
        if (null == hotels) {
            hotels = new ArrayList<MGExHotel>();
        }
        return this.hotels;
    }

}

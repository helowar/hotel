package com.mangocity.hotel.order.persistence;

import com.mangocity.hotel.base.persistence.CEntity;
import com.mangocity.util.Entity;


/**
 * 日审信息设置酒店
 * @author chenkeming
 *
 */
public class HtlAuditInfoHotel extends CEntity implements Entity {
    
    
    //	 Fields
    
    //  ID
    private Long ID;
    //  渠道ID
    private Long auditInfoId;
    //  酒店ID
    private Long hotelId;
            

    // Property accessors
    public Long getID() {
        return this.ID;
    }

    public void setID(Long roomTypeId) {
        this.ID = roomTypeId;
    }

    public Long getAuditInfoId() {
        return auditInfoId;
    }

    public void setAuditInfoId(Long auditInfoId) {
        this.auditInfoId = auditInfoId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }        

}

package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.persistence.CEntity;
import com.mangocity.util.Entity;


/**
 * 日审信息设置
 * @author chenkeming
 *
 */
public class HtlAuditInfo extends CEntity  implements Entity {

    // Fields

    // ID
    private Long ID;

    // 名称
    private String channelName;

    
    // 关联的酒店
    private List lstAuditInfoHotel = new ArrayList();
    
    private List lstSetup = new ArrayList();
    

    // Property accessors
    public Long getID() {
        return this.ID;
    }

    public void setID(Long roomTypeId) {
        this.ID = roomTypeId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List getLstAuditInfoHotel() {
        return lstAuditInfoHotel;
    }

    public void setLstAuditInfoHotel(List lstAuditInfoHotel) {
        this.lstAuditInfoHotel = lstAuditInfoHotel;
    }

    public List getLstSetup() {
        return lstSetup;
    }

    public void setLstSetup(List lstSetup) {
        this.lstSetup = lstSetup;
    }

}

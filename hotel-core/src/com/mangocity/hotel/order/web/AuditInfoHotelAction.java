package com.mangocity.hotel.order.web;

import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.order.persistence.HtlAuditInfoHotel;

/**
 * 审核信息的酒店action
 * 
 * @author chenkeming
 * 
 */
public class AuditInfoHotelAction extends PersistenceAction {

    private String[] hotelID;

    private String selIDs;

    protected Class getEntityClass() {
        return HtlAuditInfoHotel.class;
    }

    public String delete() {
        super.delete();
        return super.forwardMsgBox("删除酒店成功！", "refreshSelf()");
    }

    /**
     * 
     *批量删除
     * 
     */
    public String volumeDel() {
        try {
            hotelID = selIDs.split(",");
            for (int i = 0; i < hotelID.length; i++) {
                super.getEntityManager().remove(getEntityClass(), Long.valueOf(hotelID[i]));
            }
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
        }
        return super.forwardMsgBox("批量删除酒店成功！", "refreshSelf()");
    }

    public String[] getHotelID() {
        return hotelID;
    }

    public void setHotelID(String[] hotelID) {
        this.hotelID = hotelID;
    }

    public String getSelIDs() {
        return selIDs;
    }

    public void setSelIDs(String selIDs) {
        this.selIDs = selIDs;
    }
}

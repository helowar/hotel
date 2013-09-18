package com.mangocity.hotel.order.web;

import com.mangocity.hotel.base.web.webwork.PersistenceAction;
import com.mangocity.hotel.order.persistence.HotelFax;
import com.mangocity.hotel.order.service.IAuditService;

/**
 */
public class AuditOperateAction extends PersistenceAction {

    protected Class getEntityClass() {
        return HotelFax.class;
    }

    private IAuditService auditService;

    public void setAuditService(IAuditService auditService) {
        this.auditService = auditService;
    }

    private String FORWARD;

    private Long auditHotelID;

    private Long[] hotelID;

    /**
     * 查询日审的酒店调转名称
     */
    private static final String LISTHOTEL = "listHotel";

    protected static final String DELETED = "deleted";

    public String allFroward() {
        FORWARD = "addHotel";
        return FORWARD;
    }

    /**
     * 查询日审的酒店
     * 
     * @return
     */
    public String listHotel() {

        return LISTHOTEL;
    }

    /**
     * public String delete() {
     * 
     * //Map params = super.getParams(); HotelFax aHotel = new HotelFax(); if (auditHotelID == null)
     * return super.forwardError("auditHotelID不能为空!,请传入auditHotelID参数!");
     * aHotel.setID(auditHotelID); auditService.deleteAuditFax(aHotel);
     * 
     * return DELETED; }
     **/
    public Long[] getHotelID() {
        return hotelID;
    }

    public void setHotelID(Long[] hotelID) {
        this.hotelID = hotelID;
    }

    public Long getAuditHotelID() {
        return auditHotelID;
    }

    public void setAuditHotelID(Long auditHotelID) {
        this.auditHotelID = auditHotelID;
    }

}
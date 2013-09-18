package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.persistence.HtlPreconcertItemBatch;
import com.mangocity.hotel.base.web.webwork.PersistenceAction;

/**
 * 酒店预定条款查询日志
 * 
 * @author lihaibo add by 2009-3-31 hotelV2.6
 */
public class ClauseDoLogAction extends PersistenceAction {

    // 酒店ID;
    private long hotelId;

    private long roomTypeId;

    private HtlPreconcertItemBatch htlPreconcertItemBatch;

    protected Class getEntityClass() {

        return HtlPreconcertItemBatch.class;

    }

    // 页面跳转
    public String allForword() {

        super.setEntity(super.populateEntity());
        this.setHtlPreconcertItemBatch((HtlPreconcertItemBatch) this.getEntity());

        return "queryDoLogPage";

    }

    public String forwardToOneRoomType() {
        return "queryDoLogPageInOneRoomType";
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public HtlPreconcertItemBatch getHtlPreconcertItemBatch() {
        return htlPreconcertItemBatch;
    }

    public void setHtlPreconcertItemBatch(HtlPreconcertItemBatch htlPreconcertItemBatch) {
        this.htlPreconcertItemBatch = htlPreconcertItemBatch;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

}

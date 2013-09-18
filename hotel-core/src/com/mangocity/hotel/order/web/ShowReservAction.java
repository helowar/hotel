package com.mangocity.hotel.order.web;

import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.order.persistence.OrPriceDetail;
import com.mangocity.hotel.order.service.IOrderEditService;

/**
 * 查看预订条款信息action
 * 
 * @author chenkeming Mar 5, 2009 9:37:30 AM
 */
public class ShowReservAction extends GenericAction {

    private static final long serialVersionUID = 3634210700893539789L;

    private long hotelId;

    private String checkinDate;

    private String checkoutDate;

    private String childRoomTypeId;

    private IOrderEditService orderEditService;

    private OrPriceDetail priceDetail;

    public String showReservCC() {
        priceDetail = orderEditService.getHotelQueryReserv(hotelId, checkinDate, checkoutDate,
            childRoomTypeId);
        return "success";
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(String childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public IOrderEditService getOrderEditService() {
        return orderEditService;
    }

    public void setOrderEditService(IOrderEditService orderEditService) {
        this.orderEditService = orderEditService;
    }

    public OrPriceDetail getPriceDetail() {
        return priceDetail;
    }

    public void setPriceDetail(OrPriceDetail priceDetail) {
        this.priceDetail = priceDetail;
    }
}

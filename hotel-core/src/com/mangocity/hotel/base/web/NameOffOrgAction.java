package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.manage.HotelManageGroup;
import com.mangocity.hotel.base.persistence.HtlNameplate;
import com.mangocity.hotel.base.persistence.HtlNameplateOffshootOrgan;
import com.mangocity.hotel.base.web.webwork.GenericAction;

/**
 */
public class NameOffOrgAction extends GenericAction {

    private long hotel_group_id;

    private long htlNameplateID;

    private long nameplateOffshootOrganID;

    private HtlNameplateOffshootOrgan htlNameplateOffshootOrgan;

    private HotelManageGroup hotelManageGroup;

    private String FORWARD;

    public String allForward() {
        if (0 < nameplateOffshootOrganID) {
            htlNameplateOffshootOrgan = hotelManageGroup
                .queryNameplateOffshootOrgan(nameplateOffshootOrganID);
        }

        FORWARD = "query";
        return FORWARD;
    }

    public String saveOrUpdate() {
        HtlNameplate htlNameplate = new HtlNameplate();
        htlNameplate.setID(htlNameplateID);
        htlNameplateOffshootOrgan.setNameplate(htlNameplate);
        hotelManageGroup.saveOrUpdateNameplateOffshootOrgan(htlNameplateOffshootOrgan);

        if (0 < hotel_group_id) {
            FORWARD = "return";
        } else {
            FORWARD = SUCCESS;
        }

        return FORWARD;
    }

    public String delHtlNameplateOffshootOrgan() {

        hotelManageGroup.delNameplateOffshootOrgan(nameplateOffshootOrganID);
        FORWARD = "return";
        return FORWARD;
    }

    public String getFORWARD() {
        return FORWARD;
    }

    public void setFORWARD(String forward) {
        FORWARD = forward;
    }

    public long getHotel_group_id() {
        return hotel_group_id;
    }

    public void setHotel_group_id(long hotel_group_id) {
        this.hotel_group_id = hotel_group_id;
    }

    public long getHtlNameplateID() {
        return htlNameplateID;
    }

    public void setHtlNameplateID(long htlNameplateID) {
        this.htlNameplateID = htlNameplateID;
    }

    public HtlNameplateOffshootOrgan getHtlNameplateOffshootOrgan() {
        return htlNameplateOffshootOrgan;
    }

    public void setHtlNameplateOffshootOrgan(HtlNameplateOffshootOrgan htlNameplateOffshootOrgan) {
        this.htlNameplateOffshootOrgan = htlNameplateOffshootOrgan;
    }

    public long getNameplateOffshootOrganID() {
        return nameplateOffshootOrganID;
    }

    public void setNameplateOffshootOrganID(long nameplateOffshootOrganID) {
        this.nameplateOffshootOrganID = nameplateOffshootOrganID;
    }

    public HotelManageGroup getHotelManageGroup() {
        return hotelManageGroup;
    }

    public void setHotelManageGroup(HotelManageGroup hotelManageGroup) {
        this.hotelManageGroup = hotelManageGroup;
    }

}

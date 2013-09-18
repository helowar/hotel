package com.mangocity.hotel.base.web;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.manage.HotelManageGroup;
import com.mangocity.hotel.base.persistence.HtlNameplate;
import com.mangocity.hotel.base.persistence.HtlNameplateChinaHQ;
import com.mangocity.hotel.base.web.webwork.GenericAction;

/**
 */
public class NameplateAction extends GenericAction {

    private long hotel_group_id;

    private HtlNameplate htlNameplate;

    private HtlNameplateChinaHQ htlNameplateChinaHQ;

    private List lisHtlNameplateOffshootOrgan = new ArrayList();

    private String FORWARD;

    private String refor;

    private HotelManageGroup hotelManageGroup;

    private long htlNameplateID;

    private long nameplateOffshootOrganID;

    public String allForward() {
        if (0 < htlNameplateID) {
            htlNameplate = hotelManageGroup.queryNameplate(htlNameplateID);
            htlNameplateChinaHQ = hotelManageGroup.queryHotelNameplateChinaHQ(htlNameplateID);
            lisHtlNameplateOffshootOrgan = hotelManageGroup
                .queryLisHtlNameplateOffshootOrgan(htlNameplateID);

        }

        FORWARD = "query";

        return FORWARD;
    }

    public String saveOrUpdate() {
        /*
         * HtlHotelGroup hotelGroup = new HtlHotelGroup(); hotelGroup.setID(hotel_group_id);
         * htlNameplate.setHotelGroup(hotelGroup);
         */
        htlNameplate.setHotel_group_id(hotel_group_id);
        htlNameplate.setChinaHqAddress(htlNameplateChinaHQ.getAddress());
        hotelManageGroup.saveOrUpdateNameplate(htlNameplate);

        if (null != hotelManageGroup) {
            htlNameplateChinaHQ.setNameplate(htlNameplate);
            hotelManageGroup.saveOrUpdateNameplateChinaHQ(htlNameplateChinaHQ);
        }
        if (null != refor && 0 < refor.length()) {
            FORWARD = refor;
        } else {
            FORWARD = "success";
        }
        return FORWARD;
    }

    public String delNameplate() {
        hotelManageGroup.delNameplate(htlNameplateID);
        FORWARD = "success";
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

    public HtlNameplate getHtlNameplate() {
        return htlNameplate;
    }

    public void setHtlNameplate(HtlNameplate htlNameplate) {
        this.htlNameplate = htlNameplate;
    }

    public HtlNameplateChinaHQ getHtlNameplateChinaHQ() {
        return htlNameplateChinaHQ;
    }

    public void setHtlNameplateChinaHQ(HtlNameplateChinaHQ htlNameplateChinaHQ) {
        this.htlNameplateChinaHQ = htlNameplateChinaHQ;
    }

    public List getLisHtlNameplateOffshootOrgan() {
        return lisHtlNameplateOffshootOrgan;
    }

    public void setLisHtlNameplateOffshootOrgan(List lisHtlNameplateOffshootOrgan) {
        this.lisHtlNameplateOffshootOrgan = lisHtlNameplateOffshootOrgan;
    }

    public String getRefor() {
        return refor;
    }

    public void setRefor(String refor) {
        this.refor = refor;
    }

    public HotelManageGroup getHotelManageGroup() {
        return hotelManageGroup;
    }

    public void setHotelManageGroup(HotelManageGroup hotelManageGroup) {
        this.hotelManageGroup = hotelManageGroup;
    }

    public long getHtlNameplateID() {
        return htlNameplateID;
    }

    public void setHtlNameplateID(long htlNameplateID) {
        this.htlNameplateID = htlNameplateID;
    }

    public long getNameplateOffshootOrganID() {
        return nameplateOffshootOrganID;
    }

    public void setNameplateOffshootOrganID(long nameplateOffshootOrganID) {
        this.nameplateOffshootOrganID = nameplateOffshootOrganID;
    }

}

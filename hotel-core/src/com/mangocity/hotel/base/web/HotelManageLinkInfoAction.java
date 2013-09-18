package com.mangocity.hotel.base.web;

import com.mangocity.hotel.base.manage.HotelManageGroup;
import com.mangocity.hotel.base.persistence.CEntityEvent;
import com.mangocity.hotel.base.persistence.HtlContactInfo;
import com.mangocity.hotel.base.persistence.HtlGroupOffshootOrgan;
import com.mangocity.hotel.base.persistence.HtlHotelGroup;
import com.mangocity.hotel.base.web.webwork.GenericAction;

/**
 */
public class HotelManageLinkInfoAction extends GenericAction {

    private long hotel_group_id;

    private String FORWARD;

    private String refor;

    private HtlContactInfo htlContactInfo;

    private HtlGroupOffshootOrgan htlGroupOffshootOrgan;

    private HotelManageGroup hotelManageGroup;

    private long contactInfoID;

    /* ----------重 构 的 新 方 法 开 始---------- */

    public String allForward() {
        if (0 < contactInfoID) {
            htlGroupOffshootOrgan = hotelManageGroup.queryHtlGroupOffshootOrgan(contactInfoID);

            // htlContactInfo = hotelManageGroup.queryHMLinkInfo(contactInfoID);
        }

        FORWARD = "query";

        return FORWARD;
    }

    public String saveOrUpdate() {

        // htlContactInfo.setHotel_group_id(hotel_group_id);
        // hotelManageGroup.saveOrUpdateHMLinkInfo(htlContactInfo);
        HtlHotelGroup hotelGroup = new HtlHotelGroup();
        hotelGroup.setID(hotel_group_id);
        if (null != super.getOnlineRoleUser()) {
            hotelGroup = (HtlHotelGroup) CEntityEvent.setCEntity(hotelGroup, super
                .getOnlineRoleUser().getName(), super.getOnlineRoleUser().getLoginName());
        }
        htlGroupOffshootOrgan.setHotelGroup(hotelGroup);
        hotelManageGroup.saveOrUpdateHtlGroupOffshootOrgan(htlGroupOffshootOrgan);

        if (0 < hotel_group_id) {
            FORWARD = "return";
        } else {
            FORWARD = SUCCESS;
        }

        return FORWARD;
    }

    /* ----------重 构 的 新 方 法 结 束---------- */

    public String addForward() {
        if ("addhmlup".equals(refor)) {
            FORWARD = refor;
        } else {
            FORWARD = "addhml";
        }
        return FORWARD;
    }

    public String upForward() {
        htlContactInfo = hotelManageGroup.queryHMLinkInfo(contactInfoID);
        FORWARD = "update";
        return FORWARD;
    }

    public String addHMLinkInfo() {
        // htlContactInfo.setHotel_group_id(hotel_group_id);
        hotelManageGroup.createHMLinkInfo(htlContactInfo);
        return SUCCESS;
    }

    public String upHMLinkInfo() {
        // htlContactInfo.setHotel_group_id(hotel_group_id);
        hotelManageGroup.upHMLinkInfo(htlContactInfo);
        return SUCCESS;
    }

    public String delHMLinkInfo() {
        hotelManageGroup.delHtlGroupOffshootOrgan(contactInfoID);
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

    public HtlContactInfo getHtlContactInfo() {
        return htlContactInfo;
    }

    public void setHtlContactInfo(HtlContactInfo htlContactInfo) {
        this.htlContactInfo = htlContactInfo;
    }

    public HotelManageGroup getHotelManageGroup() {
        return hotelManageGroup;
    }

    public void setHotelManageGroup(HotelManageGroup hotelManageGroup) {
        this.hotelManageGroup = hotelManageGroup;
    }

    public long getContactInfoID() {
        return contactInfoID;
    }

    public void setContactInfoID(long contactInfoID) {
        this.contactInfoID = contactInfoID;
    }

    public String getRefor() {
        return refor;
    }

    public void setRefor(String refor) {
        this.refor = refor;
    }

    public HtlGroupOffshootOrgan getHtlGroupOffshootOrgan() {
        return htlGroupOffshootOrgan;
    }

    public void setHtlGroupOffshootOrgan(HtlGroupOffshootOrgan htlGroupOffshootOrgan) {
        this.htlGroupOffshootOrgan = htlGroupOffshootOrgan;
    }

}

package com.mangocity.hotel.outsytem.interf.impl;

import java.io.Serializable;
import java.util.List;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlBookSetup;
import com.mangocity.hotel.base.persistence.HtlCtct;
import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.hotel.base.persistence.HtlTrafficInfo;
import com.mangocity.hotel.base.util.BizRuleCheck;
import com.mangocity.hotel.outsytem.interf.AddSysOuterHotelTMCinterface;
import com.mangocity.util.DateUtil;

/**
 */
public class AddSysOuterHotelTMCinterfaceImpl implements
AddSysOuterHotelTMCinterface, Serializable {

    private HotelManage hotelManage;

    public String AddSysOuterHotelInfo(HtlHotel htlHotel) {

        List lsCtct = htlHotel.getHtlCtct();
        for (int i = 0; i < lsCtct.size(); i++) {
            HtlCtct ct = (HtlCtct) lsCtct.get(i);
            ct.setHtlHotel(htlHotel);
        }
        List lsTraffic = htlHotel.getHtlTrafficInfo();
        for (int i = 0; i < lsTraffic.size(); i++) {
            HtlTrafficInfo ti = (HtlTrafficInfo) lsTraffic.get(i);
            ti.setHtlHotel(htlHotel);
        }
        List lisBookSetup = htlHotel.getHtlBookSetup();
        for (int i = 0; i < lisBookSetup.size(); i++) {
            HtlBookSetup bs = (HtlBookSetup) lisBookSetup.get(i);
            new BizRuleCheck();
            bs.setActive(BizRuleCheck.getTrueString());
            bs.setCreateTime(DateUtil.getSystemDate());
            bs.setCreateById("TMC");
            bs.setCreateBy("TMC");
            bs.setHtlHotel(htlHotel);
        }
        htlHotel.setHtlTrafficInfo(lsTraffic);
        htlHotel.setHtlCtct(lsCtct);
        htlHotel.setHtlBookSetup(lisBookSetup);
        htlHotel.setActive(BizRuleCheck.getHotelActive());
        htlHotel.setHotelSystemSign("02");
        long resultInt = hotelManage.saveOrUpdateHotel(htlHotel);
        if (0 != resultInt) {
            return "ERROR";
        } else {
            return "SUCCEED";
        }

    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

}

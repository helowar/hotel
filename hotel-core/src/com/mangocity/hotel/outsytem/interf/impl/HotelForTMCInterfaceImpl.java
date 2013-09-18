package com.mangocity.hotel.outsytem.interf.impl;

import java.io.Serializable;

import com.mangocity.hotel.base.manage.HotelManage;
import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.outsytem.interf.HotelForTMCInterface;
import com.mangocity.hotel.outsytem.interf.assistant.CreditAssureQueryBean;

/**
 */
public class HotelForTMCInterfaceImpl implements HotelForTMCInterface, Serializable {

    private HotelManage hotelManage;

    public HtlCreditAssure queryCreditAssureInfo(CreditAssureQueryBean creditAssureQueryBean) {

        return hotelManage.qryCreditAssureForTMC(creditAssureQueryBean.getHotelId(),
            creditAssureQueryBean.getQueryDate(), creditAssureQueryBean.getRoomTypeId());
    }

    public HotelManage getHotelManage() {
        return hotelManage;
    }

    public void setHotelManage(HotelManage hotelManage) {
        this.hotelManage = hotelManage;
    }

}

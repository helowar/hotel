package com.mangocity.hotel.outsytem.interf;

import com.mangocity.hotel.base.persistence.HtlCreditAssure;
import com.mangocity.hotel.outsytem.interf.assistant.CreditAssureQueryBean;

/**
 */
public interface HotelForTMCInterface {

    // 查询预定担保信息
    public HtlCreditAssure queryCreditAssureInfo(CreditAssureQueryBean creditAssureQueryBean);

}

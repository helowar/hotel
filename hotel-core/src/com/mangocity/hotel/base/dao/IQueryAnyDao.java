package com.mangocity.hotel.base.dao;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;

/**
 */
public interface IQueryAnyDao {

    // 通过业务主键找到一个房间
    public HtlRoom findRoomByBizKey(Long roomTypeId, Long hotelId, String ableSaleDate);

    // 通过业务主键找到一个配额对象
    public HtlQuota findQuotaByBizKey(String ableSaleDate, String shareType, String quotaType,
        Long hotelId, Long contractId);

    // 通过唯一的逻辑主键配额id找出当天配额对象的所有cutoffDay记录
    public List findAllCutoffDayQuotaByQuotaId(Long quotaId);

    public List queryHotelByCCWithPrice(HotelQueryCondition hotelQueryCondition);

    public List queryHotelByCCWithoutPrice(HotelQueryCondition hotelQueryCondition);

    public HtlPrice findPriceByBizKey(Long roomId, int memberTypeId);

    public List queryPriceHistory(String queryID, Map map);
}

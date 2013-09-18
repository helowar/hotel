package com.mangocity.hotel.base.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.dao.IQueryAnyDao;
import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.hotel.base.persistence.HtlQuota;
import com.mangocity.hotel.base.persistence.HtlRoom;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.util.dao.DAOIbatisImpl;

/**
 * 用Ibatis来查询一些记录
 * 
 * @author xiaowumi
 * 
 */
public class QueryAnyDaoImpl extends DAOIbatisImpl implements IQueryAnyDao {

    // 通过业务主键找到一个房间
    public HtlRoom findRoomByBizKey(Long roomTypeId, Long hotelId, String ableSaleDate) {
        HashMap map = new HashMap();
        map.put("ableSaleDate", ableSaleDate);
        map.put("roomTypeId", roomTypeId);
        map.put("hotelId", hotelId);
        return (HtlRoom) super.getSqlMapClientTemplate().queryForObject("findRoomByBizKey", map);
    }

    // 通过业务主键找到一个配额对象
    public HtlQuota findQuotaByBizKey(String ableSaleDate, String shareType, String quotaType,
        Long hotelId, Long contractId) {
        HashMap map = new HashMap();
        map.put("ableSaleDate", ableSaleDate);
        map.put("quotaType", quotaType);
        map.put("shareType", shareType);
        map.put("hotelId", hotelId);
        map.put("contractId", contractId);
        return (HtlQuota) super.getSqlMapClientTemplate().queryForObject("findQuotaByBizKeyIbatis",
            map);
    }

    // 通过唯一的逻辑主键配额id找出当天配额对象的所有cutoffDay记录
    public List findAllCutoffDayQuotaByQuotaId(Long quotaId) {
        return super.getSqlMapClientTemplate().queryForList("findAllCutoffDayQuotaByQuotaId",
            quotaId);
    }

    public List queryHotelByCCWithPrice(HotelQueryCondition hotelQueryCondition) {

        return super.getSqlMapClientTemplate().queryForList("queryHotelByCCWithPrice",
            hotelQueryCondition);
    }

    public List queryHotelByCCWithoutPrice(HotelQueryCondition hotelQueryCondition) {
        Map params = new HashMap();
        params.put("cityId", hotelQueryCondition.getCityId());
        params.put("bizZone", hotelQueryCondition.getBizZone());
        params.put("chnAddress", hotelQueryCondition.getChnAddress());
        return super.queryByPagination("queryHotelByCCWithoutPrice", params, hotelQueryCondition
            .getPageSize());
    }

    public HtlPrice findPriceByBizKey(Long roomId, int memberTypeId) {
        Map params = new HashMap();
        params.put("roomId", roomId);
        params.put("memberTypeId", memberTypeId);
        return (HtlPrice) super.getSqlMapClientTemplate().queryForObject("findPriceByBizKey",
            params);
    }

    public List queryPriceHistory(String queryID, Map map) {
        return super.queryForList(queryID, map);
    }
}

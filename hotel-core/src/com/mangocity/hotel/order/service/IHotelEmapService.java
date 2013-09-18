package com.mangocity.hotel.order.service;

import java.sql.SQLException;
import java.util.List;

import com.mangocity.hotel.base.service.assistant.HotelEmapResultInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.mgis.domain.valueobject.LatLng;

/**
 */
public interface IHotelEmapService {


    /****
     * 查询酒店的修改实现
     * 
     * @param condition查询条件
     * @throws SQLException 
     */
    public List queryHotels(HotelQueryCondition condition, Double longitude, Double latitude,
        int distance, LatLng leftTopLatLng, LatLng leftBottomLatLng, LatLng rightTopLatLng,
        LatLng rightBottomLatLng, boolean isAllFlag, 
        List<HotelEmapResultInfo> allHotelList) throws SQLException;

}

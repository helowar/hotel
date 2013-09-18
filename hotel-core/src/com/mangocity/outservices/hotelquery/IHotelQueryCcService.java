package com.mangocity.outservices.hotelquery;

import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.service.assistant.HotelInfo;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;

public interface IHotelQueryCcService extends IHotelQueryOutService {
    /* ===============================CC查询接口begin=============================== */
    /**
     * cc查询酒店
     * @param condition CC查询条件
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     */
    public List<HotelInfo> queryHotelsForCc(HotelQueryCondition condition,Map params) throws Exception;
    
    /**
     * cc单独查询分页信息页数，页码，总记录数
     * @param condition CC查询条件
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     */
    public List<HotelInfo> queryHotelPagesForCc(HotelQueryCondition condition,Map params) throws Exception;
    /* ===============================CC查询接口end=============================== */
}

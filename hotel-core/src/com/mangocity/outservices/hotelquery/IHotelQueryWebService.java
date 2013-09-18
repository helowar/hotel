package com.mangocity.outservices.hotelquery;

import java.util.Map;

import com.mangocity.hweb.persistence.HotelInfoForWeb;
import com.mangocity.hweb.persistence.HotelPageForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebBean;

public interface IHotelQueryWebService extends IHotelQueryOutService {
    /* ===============================网站查询接口begin=============================== */
    /**
     * 查询散客网站酒店
     * @param queryBean 网站查询条件
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     * @throws Exception
     */
    public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean queryBean,Map params) throws Exception;
    
    /**
     * 网站根据酒店ID查询某家酒店的信息
     * @param hotelId 酒店ID
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     * @throws Exception
     */
    public HotelInfoForWeb queryHotelInfoForWeb(Long hotelId,Map params) throws Exception;
    
    /**
     * 网站查询主题酒店
     * @param qureyBean 网站查询条件
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     * @throws Exception
     */
    public HotelPageForWebBean queryThemeHotelsForWeb(QueryHotelForWebBean qureyBean,Map params) throws Exception;
    
    /**
     * 网站单独查询分页信息页数，页码，总记录数
     * @param queryBean 网站查询条件
     * @param params 参数列表(key,value)见下方
     * <ul>
     *  <li>哪个渠道调用查询(KEY_QUERY_CHANNEL,CHANNEL_*)</li>
     * </ul>
     * @return
     * @throws Exception
     */
    public HotelPageForWebBean queryHotelPagesForWeb(QueryHotelForWebBean queryBean,Map params) throws Exception;
    /* ===============================网站查询接口end=============================== */
}

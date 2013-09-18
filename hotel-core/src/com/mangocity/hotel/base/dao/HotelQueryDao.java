package com.mangocity.hotel.base.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hotel.base.persistence.HtlAssure;
import com.mangocity.hotel.base.persistence.HtlFavourableclause;
import com.mangocity.hotel.base.service.assistant.HotelQueryCondition;

/**
 * 酒店查询Dao接口
 * @author chenjiajie
 *
 */
public interface HotelQueryDao extends Serializable {

    /**
     * Map中的key 查询结果集
     */
    public static final String KEY_RESULT_LIST = "keyResultList";
    
    /**
     * Map中的key 查询结果集数量
     */
    public static final String KEY_TOTAL_SIZE = "keyTotalSize";
    
    /**
     * CC查询数据库，并把数据库返回的游标缓存到List中
     * @param condition
     * @return Map 包括记录数和返回数据
     */
    public Map queryHotelResultListForCC(HotelQueryCondition condition) throws SQLException;
    
    /**
     * 查询传入的所有价格类型的连住优惠信息
     * @param allPriceTypeList
     * @return 
     */
    public Map<String,List<HtlFavourableclause>> queryAllFavourableList(String allPriceTypeList);
    
    /**
     * 生成连住优惠信息Map的key
     * @param hotelId
     * @param priceTypeId
     * @return
     */
    public String generateFavourableListKey(Long hotelId,Long priceTypeId);
    
    /**
     * 查询传入的所有价格类型在某一天的担保信息
     * @param hotelIdList
     * @param allPriceTypeList
     * @param beginDate check-in date
     * @return
     */
    public Map<String,List<List<HtlAssure>>> queryAllAssureListByBeginDate(String hotelIdList, String allPriceTypeList, Date beginDate);
    
    /**
     * 生成连住优惠信息Map的key
     * @param hotelId
     * @param priceTypeId
     * @return
     */
    public String generateAssureListKey(Long hotelId,Long priceTypeId,Date beginDate);
    
    /**
     * 据酒店和价格类型查询优惠信息
     * @param hotelId
     * @param priceTypeId
     * @param beginDate
     * @param endDate
     * @return
     */
    List<HtlFavourableclause> queryFavourableByHotelIdAndPriceTypeId(Long hotelId,String priceTypeId,Date checkInDate,Date checkOutDate);
}

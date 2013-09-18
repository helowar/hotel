package com.mangocity.hweb.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mangocity.hweb.persistence.QueryHotelForWebBean;
import com.mangocity.hweb.persistence.QueryHotelForWebResult;
import com.mangocity.webnew.persistence.HtlChannelClickLog;

public interface HWebHotelDAO {
	
	/**
     * 预订条款查询某一个酒店价格明细
     * @param hotelID
     * @param beginDate
     * @param endDate
     * @param beginDateExt
     * @param endDateExt
     * @param roomTypeID
     * @param childRoomTypeID
     * @param payMethod
     * @return
     */
    public List<QueryHotelForWebResult> queryPriceDetailForWeb(Long hotelID, Date beginDate, Date endDate, 
            Date beginDateExt, Date endDateExt, Long roomTypeID, Long childRoomTypeID, String payMethod);
    
    /**
     * 得到净价
     */
    public List queryAdvicePriceForWeb(Long hotelID, Date beginDate,
            Date endDate, Date beginDateExt, Date endDateExt, Long roomTypeID, Long childRoomTypeID,
            String payMethod);
    
    /**
     * 网站查询数据库，并把数据库返回的游标缓存到List中
     * @param queryBean
     * @return Map 包括记录数和返回数据
     */
    public Map queryHotelResultListForWeb(QueryHotelForWebBean queryBean);
    
    /**
     * 查询主题酒店
     * @param queryBean
     * @return Map 包括记录数和返回数据
     */
    public Map queryThemeHotelsForWeb(QueryHotelForWebBean queryBean);
    
    /**
     * 保存渠道日志
     * 
     * @param channelClickLog
     */
    public void saveChannelLog(HtlChannelClickLog channelClickLog);
    
    /**
     * 更新渠道日志
     * 
     * @param channelLogId
     */
    public void updateChannelLog(long channelLogId);

}

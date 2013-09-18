package hk.com.cts.ctcp.hotel.service;

import java.util.List;

import com.mangocity.hotel.base.persistence.ExMapping;

/**
 */
public interface IExMappingService {

    /**
     * 根据渠道、酒店ID，房型id、子房型id获取直连价格计划编码信息 add by shizhongwen 时间:Mar 17, 2009 2:24:21 PM
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @return
     */
    public ExMapping getMapping(long channelType, long hotelId, long roomTypeId,
        long childRoomTypeId);

    /**
     * 根据渠道、芒果酒店id列表获取直连编码信息 add by shizhongwen 时间:Mar 17, 2009 2:24:57 PM
     * 
     * @param channelType
     * @param hotelIds
     * @return
     */
    public List getMapping(long channelType, List<Long> hotelIds);

    /**
     * 根据渠道、芒果酒店Id,房型Id获取直连房型编码信息 add by shizhongwen 时间:Mar 17, 2009 2:25:13 PM
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @return
     */
    public ExMapping getMapping(long channelType, long hotelId, long roomTypeId);

    /**
     * 根据渠道、芒果酒店id、香港房型id获取芒果网的房型编码 add by shizhongwen 时间:Mar 19, 2009 3:30:26 PM
     * 
     * @param channelType
     * @param hotelId
     * @param hkrooomTypeId
     * @return
     */
    public List<ExMapping> getMapping(long channelType, long hotelId, String hkrooomTypeId);

    /**
     * 根据渠道、芒果酒店id、香港房型id、香港价格类型Id获取芒果网的子房型ID add by shizhongwen 时间:Mar 19, 2009 3:30:26 PM
     * 
     * @param channelType
     * @param hotelId
     * @param hkrooomTypeId
     * @return
     */
    public List getMappingOfNation(long channelType, long hotelId, String hkrooomTypeId,
        String sNation);

    /**
     * 根据渠道、酒店ID，芒果房型id、香港子房型id获取芒果子房型id add by shizhongwen 时间:Mar 23, 2009 5:17:52 PM
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @param hkchildRoomTypeId
     * @return
     */
    public ExMapping getMapping(long channelType, long hotelId, long roomTypeId,
        String hkchildRoomTypeId);
}

package hk.com.cts.ctcp.hotel.service.impl;

import hk.com.cts.ctcp.hotel.dao.IExDao;
import hk.com.cts.ctcp.hotel.service.IExMappingService;

import java.util.List;

import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.util.hotel.constant.HotelMappingType;

/**
 */
public class ExMappingServiceImpl implements IExMappingService {

    private IExDao exDao;

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
        long childRoomTypeId) {
        String hsql = " from ExMapping where channeltype=?  and hotelid=?"
            +" and roomTypeId=? and priceTypeId=? and type=?" ;
        return (ExMapping) exDao.find(hsql, new Object[]{channelType,hotelId,roomTypeId+"",childRoomTypeId+"",HotelMappingType.PRICE_TYPE});
    }

    /**
     * 根据渠道、酒店ID，房型id、香港子房型id获取芒果子房型id add by shizhongwen 时间:Mar 23, 2009 5:17:52 PM
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @param hkchildRoomTypeId
     * @return
     */
    public ExMapping getMapping(long channelType, long hotelId, long roomTypeId,
        String hkchildRoomTypeId) {
        String hsql = " from ExMapping where channeltype= ?" + " and hotelid=? and roomTypeId= ?"
            + " and rateplancode=?" + " and type=?";

        Object[] values = new Object[5];
        values[0] = Long.valueOf(channelType);
        values[1] = Long.valueOf(hotelId);
        values[2] = "" + roomTypeId;
        values[3] = hkchildRoomTypeId;
        values[4] = HotelMappingType.PRICE_TYPE;
        return (ExMapping) exDao.find(hsql, values);
    }

    /**
     * 根据渠道、芒果酒店id列表获取直连编码信息 add by shizhongwen 时间:Mar 17, 2009 2:24:57 PM
     * 
     * @param channelType
     * @param hotelIds
     * @return
     */
    public List getMapping(long channelType, List<Long> hotelIds) {
        if (null == hotelIds || 0 >= hotelIds.size()) {
            return null;
        }
        String hsql = " from ExMapping where channeltype=? and type=? "+ " and (";
        Object[] values=new Object[hotelIds.size()+2];
        values[0]=channelType;
        values[1]=HotelMappingType.HOTEL_TYPE;
        for (int i = 0; i < hotelIds.size(); i++) {
            Long hotelId = hotelIds.get(i);
            if (0 < i) {
                hsql += " or ";
            }
            hsql += "hotelid=?" ;
            values[i+2]=hotelId;
        }
        hsql += ")";
        return exDao.query(hsql, values);
    }

    public void setExDao(IExDao exDao) {
        this.exDao = exDao;
    }

    /**
     * 根据渠道、芒果酒店Id,房型Id获取直连房型编码信息 add by shizhongwen 时间:Mar 17, 2009 2:25:13 PM
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @return
     */
    public ExMapping getMapping(long channelType, long hotelId, long roomTypeId) {
        String hsql = " from ExMapping where channeltype=? and hotelid=? and roomTypeId=? and type=?";
        return (ExMapping) exDao.find(hsql, new Object[]{channelType,hotelId,roomTypeId+"",HotelMappingType.ROOM_TYPE});
    }

    /**
     * 根据渠道、芒果酒店id、香港房型id获取芒果网的房型编码 add by shizhongwen 时间:Mar 19, 2009 3:30:26 PM
     * 
     * @param channelType
     * @param hotelId
     * @param hkrooomTypeId
     * @return
     */
    public List<ExMapping> getMapping(long channelType, long hotelId, String hkrooomTypeId) {
        String hsql = " from ExMapping where channeltype= ?"
            + " and hotelid=? and roomtypecodeforchannel=? and type=?";
        Object[] values = new Object[4];
        values[0] = channelType;
        values[1] = hotelId;
        values[2] = hkrooomTypeId;
        values[3] = HotelMappingType.ROOM_TYPE;
        return exDao.query(hsql, values);
    }

    /**
     * 根据渠道、芒果酒店id、香港房型id、香港价格类型Id获取芒果网的子房型ID add by shizhongwen 时间:Mar 19, 2009 3:30:26 PM
     * 
     * @param channelType
     * @param hotelId
     * @param hkrooomTypeId
     * @return
     */
    public List getMappingOfNation(long channelType, long hotelId, String hkrooomTypeId,
        String sNation) {
        String hsql = " from ExMapping where channeltype= ?"
            + " and hotelid=? and roomtypecodeforchannel= ?" + " and rateplancode=?"
            + " and type=?";

        Object[] values = new Object[5];
        values[0] = Long.valueOf(channelType);
        values[1] = Long.valueOf(hotelId);
        values[2] = hkrooomTypeId;
        values[3] = sNation;
        values[4] = HotelMappingType.PRICE_TYPE;
        return exDao.query(hsql, values);
    }

}

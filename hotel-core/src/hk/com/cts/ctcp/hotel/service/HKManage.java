package hk.com.cts.ctcp.hotel.service;

import hk.com.cts.ctcp.hotel.webservice.enquiryservice.ClassNationAmtData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.ClassQtyData;
import hk.com.cts.ctcp.hotel.webservice.enquiryservice.NationAmtData;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomAmtResponse;
import hk.com.cts.ctcp.hotel.webservice.response.HKRoomQtyResponse;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.ExMapping;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;

/**
 */
public interface HKManage {

    /**
     * 根据酒店id 找到香港酒店编码 add by shizhongwen 时间:Mar 17, 2009 4:37:00 PM
     * 
     * @param hotelId
     * @return
     */
    public String getHKHotelCode(Long hotelId);
    
    /**
     * B2B同步价格时候 将价格写入数据库 add by xuyiwen 2011-1-18
     * @param items
     * @param hotelId
     * @param priceTypeId
     * @param inDate
     * @param outDate
     */
    void  updateB2BCTSHotelPrice(List<QueryHotelForWebSaleItems> items,long hotelId,long priceTypeId,long roomTypeId);

    /**
     * 根据酒店id 找到相应香港对应属性类ExMapping add by shizhongwen 时间:Mar 17, 2009 4:38:21 PM
     * 
     * @param hotelId
     * @return
     */
    public ExMapping getHKExMapping(Long hotelId);

    /**
     * 根据酒店id,芒果房型id获取香港对应的房型Id add by shizhongwen 时间:Mar 23, 2009 4:24:12 PM
     * 
     * @param hotelId
     * @param roomTypeId
     * @return
     */
    public String getHKRoomTypeId(Long hotelId, long roomTypeId);

    /**
     * 取得酒店返回列表信息 add by shizhongwen 时间:Mar 17, 2009 4:51:18 PM
     * 
     * @param classQtyDatalist
     * @return
     *  by chenkeming@2009-04-17 修改获取dayIndex逻辑
     */
    public List<HKRoomQtyResponse> getHKRoomQtyList(Long hotelId,
			List<ClassQtyData> classQtyDatalist, String sDateFm,
			String roomTypes, int nDays);
    
    /**
     * @param hotelId
     * @param classQtyDatalist
     * @param roomTypeId
     * @return
     */
    public List<Date> getHKRoomQtyListForWeb(Long hotelId,
			List<ClassQtyData> classQtyDatalist,
			long roomTypeId);

    /**
     * 取得酒店所有房型价格列表 add by shizhongwen 时间:Mar 18, 2009 7:44:01 PM
     * 
     * @param hotelId
     * @param classQtyDatalist
     * @return
     */
    public List<HKRoomAmtResponse> getHKRoomAmtList(Long hotelId,
        List<ClassNationAmtData> classNationDatalist, Date dateFm, 
        String roomTypes, int nDays, String childRoomTypes);

    /**
     * 根据渠道、酒店id、香港房型id获取芒果网的房型编码 add by shizhongwen 时间:Mar 19, 2009 3:20:00 PM
     * 
     * @param hotelId
     * @param roomTypeId
     * @return
     */
    public List<String> getRoomIdMappingByHK(long hotelId, String hkrooomTypeId);

    /**
     * 根据渠道、芒果酒店id、香港房型id、香港价格类型Id获取芒果网的子房型ID add by shizhongwen 时间:Mar 19, 2009 3:53:51 PM
     * 
     * @param hotelId
     * @param hkrooomTypeId
     * @param sNation
     * @return
     */
    public List<String[]> getPriceMappingByHK(long hotelId, String hkrooomTypeId, String sNation);

    /**
     * 更新HK酒店价格 add by shizhongwen 时间:Mar 23, 2009 3:02:59 PM
     * 
     * @param hotelId
     * @param baseAmt
     * @param listAmt
     * @param start_date
     * @param childRoomTypeId
     * @param roomTypeId
     * @return String 0:成功,-1:失败
     */
    public String updateHKHotelPrice(Long hotelId, float baseAmt, float listAmt, String start_date,
        long childRoomTypeId, long roomTypeId);

    /**
     * 更新HK酒店房间数 add by shizhongwen 时间:Mar 23, 2009 5:48:04 PM
     * 
     * @param hotelId
     * @param qty
     * @param roomTypeId
     * @param datetime
     * @return
     */
    public String updateHKhotelRoom(Long hotelId, int qty, Long roomTypeId, String datetime);

    /**
     * 根据渠道、酒店ID，芒果房型id、香港子房型id获取芒果子房型id add by shizhongwen 时间:Mar 23, 2009 5:17:52 PM
     * 
     * @param channelType
     * @param hotelId
     * @param roomTypeId
     * @param hkchildRoomTypeId
     * @return
     */
    public String getPriceMappingByHK(long hotelId, long roomTypeId, String hkchildRoomTypeId);

    /**
     * 根据芒果酒店id，芒果房型id,List<NationAmtData>封装HKRoomAmtResponse add by shizhongwen 时间:Mar 23, 2009
     * 5:31:31 PM
     * 
     * @param hotelId
     * @param roomTypeId
     * @param nationDatalist
     * @return
     */
    public List<HKRoomAmtResponse> getHKRoomNationAmtList(Long hotelId, long roomTypeId,
        List<NationAmtData> nationDatalist);

    /**
     * 根据酒店ID，房型id、子房型id获取直连香港酒店价格id add by shizhongwen 时间:Mar 25, 2009 4:19:37 PM
     * 
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @return
     */
    public String getHKChildRoomId(long hotelId, long roomTypeId, long childRoomTypeId);
}

package hk.com.cts.ctcp.hotel.dao;

import java.util.List;

import com.mangocity.util.dao.DAO;

/**
 */
public interface IExDao extends DAO {

    /**
     * 根据sql语句更新记录 add by shizhongwen 时间:Mar 17, 2009 2:41:34 PM
     * 
     * @param hsql
     * @param values
     */
    public void update(final String hsql, final Object[] values);

    /**
     * 更新HK酒店价格 add by shizhongwen 时间:Mar 23, 2009 11:36:00 AM
     * 
     * @param hotelId
     * @param baseAmt
     * @param listAmt
     * @param start_date
     * @param childRoomTypeId
     * @param roomTypeId
     */
    public String updateHKHotelPrice(Long hotelId, float baseAmt, float listAmt, String start_date,
        Long childRoomTypeId, Long roomTypeId);

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
     * 
     * 获取中旅订单信息
     * 
     * @return
     */
    public List getHKOrderInfos();
    
    /**
     * 
     * 获取芒果中旅单信息
     * 
     * @return
     */
    public List getMangoHKOrderInfos();
    
    /**
     * 
     * 获取芒果订单编号
     * 
     * @return
     */
    public List getMangoOrderCd(String orderId);
    
    /**
     * 
     * 获取度假中旅单信息
     * 
     * @return
     */
    public List getPkgHKOrderInfos();
    
    /**
     * 
     * 获取度假订单编号
     * 
     * @return
     */
    public List getPkgOrderCd(String orderId);
    
    /**
     * 
     * 获取中旅房型信息
     * 
     * @return
     */
    public List getHKRoomInfos();
    
    /**
     * 
     * 获取芒果酒店名称
     * 
     * @return
     */
    public List getMangoHotelName(Integer hotelId);
    
    /**
     * 
     * 获取中旅价格信息
     * 
     * @return
     */
    public List getHKPriceInfos();
    
    /**
     * 
     * 获取芒果房型名称
     * 
     * @return
     */
    public List getMangoRoomName(Integer roomTypeId);
    
    /**
     * sql插入
     */
    public void insertlog(String sql);
}

package com.mangocity.hotel114.manage;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.mangocity.hotelweb.persistence.HotelInfoForWeb;
import com.mangocity.hotelweb.persistence.HotelInfoForWebBean;
import com.mangocity.hotelweb.persistence.HotelPageForWebBean;
import com.mangocity.hotelweb.persistence.QueryHotelFactorageResult;
import com.mangocity.hotelweb.persistence.QueryHotelForWebBean;
import com.mangocity.hotelweb.persistence.QueryHotelForWebSaleItems;
import com.mangocity.hotelweb.persistence.QueryHotelForWebServiceIntroduction;

/**
 */
public interface Hotel114ManageWeb {
    /**
     * 查询酒店预定信息
     * 
     * @param List
     *            酒店查询条件对象
     * @return hotel_id;
     * @throws SQLException 
     * 
     */
    public HotelPageForWebBean queryHotelsForWeb(QueryHotelForWebBean qureyBean)
    throws SQLException;

    // public HotelPageForWebBean queryHotelForWeb(QueryHotelForWebBean qureyBean);
    /**
     * 查询预定的酒店价格列表 按周排序
     */
    public List<QueryHotelForWebSaleItems> queryPriceForWeb(long childRoomTypeId, Date beginDate,
        Date endDate, double minPrice, double maxPrice, String payMethod, String quotaType);

    /**
     * 查询某家酒店的信息
     * @throws SQLException 
     */

    public HotelInfoForWebBean queryHotelInfoBeanForWeb(
        QueryHotelForWebBean queryBean) throws SQLException;

    /**
     * 查询某家酒店的服务信息
     */
    public QueryHotelForWebServiceIntroduction queryServiceIntroductionForWeb(long hotelId);

    /**
     * 根据酒店ID查询某家酒店的信息
     */
    public HotelInfoForWeb queryHotelInfoForWeb(long hotelId);

    // 根据会员ID查询渠道供应商信息
    public QueryHotelFactorageResult queryHotelFactorageForWeb(long memberId);

}

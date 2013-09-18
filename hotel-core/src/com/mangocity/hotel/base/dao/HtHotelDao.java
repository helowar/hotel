package com.mangocity.hotel.base.dao;

import java.util.List;

import com.mangocity.hotel.base.persistence.HtlHotel;
import com.mangocity.util.dao.GenericDAO;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 1.0
*@since 1.0
*/
public interface HtHotelDao extends GenericDAO
{

    /**
     * 列出所有有效的酒店
     * @param status 酒店状态
     * @return List
     */
    public List<HtlHotel> lstAllHotels(String status);
    
    /**
     * 根据酒店ID字符串得到酒店中文名称数组
     * @param hotelIds 酒店ID字符串
     * @return
     */
    public Object[] getHotelNamesByIds(String hotelIds);

}
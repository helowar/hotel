package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.persistence.HtlPrice;
import com.mangocity.util.dao.GenericDAO;

/**
*@author chenhuizhong e-mail:huizong.chen@mangocity.com
*@version 1.0
*@since 1.0
*/
public interface HtlPriceDao extends GenericDAO
{

    /**
     * 通过业务主键找到一个价格
     * @param hotelId
     * @param roomTypeId
     * @param childRoomTypeId
     * @param quotaType
     * @param ableSaleDate
     * @param payMethod
     * @return HtlPrice
     */
    public abstract HtlPrice findPriceBizKey(long hotelId, long roomTypeId,
            long childRoomTypeId, String quotaType, Date ableSaleDate,String payMethod);

    /**
     * 更新价格
     * @param lstPrice
     */
    public abstract void saveOrUpdatePrice(List<HtlPrice> lstPrice);
    
    /**
     * 根据酒店ID批量更新价格
     * @param hotelId
     * @param contractId
     * @param quotaType
     * @param currency
     * @param week
     * @return String
     */
    public String saveOrUpdatePrice(long hotelId, long contractId,
            String quotaType, String currency, String week) ;
    
    /**
     * 调用存储过程更新价格
     */
    public void saveOrUpdatePrice();
}
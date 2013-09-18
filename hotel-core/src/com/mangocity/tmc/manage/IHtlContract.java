package com.mangocity.tmc.manage;

import java.util.Date;
import java.util.List;

import com.mangocity.tmc.persistence.HotelContractPrice;

/**
 * 商旅会员查询合同类接口
 * @author:shizhongwen
 * 创建日期:Aug 7, 2009,4:44:17 PM
 * 描述：
 */
public interface IHtlContract {
    
    /**
     * 根据协议酒店合同id, 协议酒店id, 预订开始和结束日期查询间夜价格
     * @param hotelcontractid
     * @param hotelid
     * @param roomtypeid
     * @param beginDate 开始日
     * @param endDate  离店日
     * @return
     */
    public List<HotelContractPrice> queryHotelcontractPrice(long hotelcontractid, long hotelid, long roomtypeid, Date beginDate, Date endDate);
}

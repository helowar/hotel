package com.mangocity.hotel.base.dao;

import java.util.Date;
import java.util.List;

/**
 */
public interface IChangePriceDao {

    /**
     * 取得当天这个酒店的变价工单列表
     * 
     * @param hotelid
     *            酒店ID
     * @param sysDate
     *            当前日期
     * @return 得当天这个酒店的变价工单列表
     */
    public List getTodayChangePrice(long hotelid, Date sysDate);

    /**
     * 得到变价工单历史记录
     * 
     * @param taskCode
     *            工单CD
     * @return
     */
    public List getChangePriceLog(String taskCode);
}

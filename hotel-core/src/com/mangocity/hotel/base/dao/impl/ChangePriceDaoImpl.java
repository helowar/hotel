package com.mangocity.hotel.base.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.dao.IChangePriceDao;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 */
public class ChangePriceDaoImpl extends DAOHibernateImpl implements IChangePriceDao, Serializable {

    /**
     * 取得当天这个酒店的变价工单列表
     * 
     * @param hotelid
     *            酒店ID
     * @param sysDate
     *            当前日期
     * @return 得当天这个酒店的变价工单列表
     */
    public List getTodayChangePrice(long hotelid, Date sysDate) {
        List lstChangePrice = super.queryByNamedQuery("getTodayChangePrice", new Object[] {
            hotelid, sysDate });
        return lstChangePrice;
    }

    /**
     * 得到变价工单历史记录
     * 
     * @param taskCode
     *            工单CD
     * @return
     */
    public List getChangePriceLog(String taskCode) {
        List lstChangePriceLog = super.queryByNamedQuery("getChangePriceLog",
            new Object[] { taskCode });
        return lstChangePriceLog;
    }

}

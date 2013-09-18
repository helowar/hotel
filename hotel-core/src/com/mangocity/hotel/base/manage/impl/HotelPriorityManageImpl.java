package com.mangocity.hotel.base.manage.impl;

import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.base.manage.HotelPriorityManage;
import com.mangocity.hotel.base.persistence.HtlSetPriority;
import com.mangocity.util.dao.DAOHibernateImpl;

/**
 */
public class HotelPriorityManageImpl extends DAOHibernateImpl implements HotelPriorityManage {

    /*
     * 找回酒店优先级信息 (non-Javadoc)
     * 
     * @see com.mangocity.hotel.base.manage.HotelPriorityManage#findHotelPriority(long)
     */
    public HtlSetPriority findHotelPriority(long hotel_id) {
        String hsql = "from HtlSetPriority where hotelId = ?";
        List priResult = new ArrayList();
        priResult = super.query(hsql, hotel_id);
        if (null != priResult && 0 < priResult.size()) {
            return (HtlSetPriority) priResult.get(0);
        }
        return null;
    }

    /*
     * 更新酒店优先级信息 (non-Javadoc)
     * 
     * @see com.mangocity.hotel.base.manage.HotelPriorityManage#updatePriority
     * (com.mangocity.hotel.base.persistence.HtlSetPriority)
     */
    public int updatePriority(HtlSetPriority htlPri) {
        super.saveOrUpdate(htlPri);
        return 0;
    }

}

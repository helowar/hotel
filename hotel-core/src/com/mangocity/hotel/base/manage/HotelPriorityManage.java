package com.mangocity.hotel.base.manage;

import com.mangocity.hotel.base.persistence.HtlSetPriority;

/**
 */
public interface HotelPriorityManage {

    /*
     * 查找酒店优先级信息
     */
    public HtlSetPriority findHotelPriority(long hotel_id);

    /*
     * 更新酒店优先级信息
     */
    public int updatePriority(HtlSetPriority htlPri);

}

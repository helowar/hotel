package com.mangocity.hotel.base.manage;

import java.util.List;

/**
 */
public interface ChangePriceWarningManage {

    /*
     * 返回变价时间`
     */
    public List findPriceDate(long hotel_id);

}

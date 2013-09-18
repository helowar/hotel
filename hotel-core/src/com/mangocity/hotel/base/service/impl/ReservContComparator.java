package com.mangocity.hotel.base.service.impl;

import java.util.Comparator;

import com.mangocity.hotel.base.persistence.HtlReservCont;

/**
 * 用于对连住日期类HtlReservCont的List按照连住日期排序
 * 
 * @author wuyun v2.6 2009-06-15
 */
public class ReservContComparator implements Comparator {

    /**
     * 需要判断传入的参数是否为空
     */
    public int compare(Object arg0, Object arg1) {
        int flag = 0;
        if (null == arg0) {
            flag = 0;
        } else if (null == arg1) {
            flag = 1;
        } else {
            HtlReservCont item0 = (HtlReservCont) arg0;
            HtlReservCont item1 = (HtlReservCont) arg1;
            if (item0.getContinueDate().before(item1.getContinueDate())) {
                flag = 0;
            } else {
                flag = 1;
            }
        }
        return flag;

    }

}

package com.mangocity.hotel.order.web;

/**
 * 预订房型的Action
 * 
 */
public class ReserveAction extends OrderAction {

    public String hello(String name) {
        return "你好！" + name;
    }

    public String hello1(String name) {

        if (null != order) {
            return order.getHotelName();
        }
        return "";
    }
}
package com.mangocity.hotel.order.web;

import java.util.List;

/**
 * 查看价格的ACTION
 * 
 * @author zhengxin
 * 
 */
public class ViewPriceAction extends OrderAction {

    private static final String VIEW_PRICE = "view_price";

    /**
     * 不重复的房间记录，一天一条
     */
    private List roomItems;

    /**
     * 对于日期的周的描述 格式是MM/DD 周 W
     */
    private List dateStrList;

    public String view() {

        order = super.getOrder();

        // dateStrList = super.getDateStrList(order.getCheckinDate(), order
        // .getCheckoutDate());

        // roomItems = super.getUniqueRoomItems(order);

        return VIEW_PRICE;
    }

    public List getDateStrList() {
        return dateStrList;
    }

    public void setDateStrList(List dateStrList) {
        this.dateStrList = dateStrList;
    }

    public List getRoomItems() {
        return roomItems;
    }

    public void setRoomItems(List roomItems) {
        this.roomItems = roomItems;
    }
}

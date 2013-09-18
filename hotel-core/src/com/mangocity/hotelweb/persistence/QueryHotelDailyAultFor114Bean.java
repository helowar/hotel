package com.mangocity.hotelweb.persistence;

import java.io.Serializable;

/**
 */
public class QueryHotelDailyAultFor114Bean implements Serializable {

    private String ordercd;

    private String ordernum;// 订单编号 该订单号是由会员管理系统生成的"流水号"

    private String hotel;// ：酒店名称

    private String room;// ：;//房型

    private String money;// 总价

    private String price;// 单价

    private String quantityreal;// 数量

    private String checkin;// ：入住日期

    private String inroom;// : 入住人姓名

    private String checktime;

    /**
     * @return 返回 price。
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price
     *            要设置的 price。
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * @return 返回 quantityreal。
     */
    public String getQuantityreal() {
        return quantityreal;
    }

    /**
     * @param quantityreal
     *            要设置的 quantityreal。
     */
    public void setQuantityreal(String quantityreal) {
        this.quantityreal = quantityreal;
    }

    /**
     * @return 返回 checkin。
     */
    public String getCheckin() {
        return checkin;
    }

    /**
     * @param checkin
     *            要设置的 checkin。
     */
    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    /**
     * @return 返回 checktime。
     */
    public String getChecktime() {
        return checktime;
    }

    /**
     * @param checktime
     *            要设置的 checktime。
     */
    public void setChecktime(String checktime) {
        this.checktime = checktime;
    }

    /**
     * @return 返回 hotel。
     */
    public String getHotel() {
        return hotel;
    }

    /**
     * @param hotel
     *            要设置的 hotel。
     */
    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    /**
     * @return 返回 inroom。
     */
    public String getInroom() {
        return inroom;
    }

    /**
     * @param inroom
     *            要设置的 inroom。
     */
    public void setInroom(String inroom) {
        this.inroom = inroom;
    }

    /**
     * @return 返回 money。
     */
    public String getMoney() {
        return money;
    }

    /**
     * @param money
     *            要设置的 money。
     */
    public void setMoney(String money) {
        this.money = money;
    }

    /**
     * @return 返回 ordercd。
     */
    public String getOrdercd() {
        return ordercd;
    }

    /**
     * @param ordercd
     *            要设置的 ordercd。
     */
    public void setOrdercd(String ordercd) {
        this.ordercd = ordercd;
    }

    /**
     * @return 返回 ordernum。
     */
    public String getOrdernum() {
        return ordernum;
    }

    /**
     * @param ordernum
     *            要设置的 ordernum。
     */
    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    /**
     * @return 返回 room。
     */
    public String getRoom() {
        return room;
    }

    /**
     * @param room
     *            要设置的 room。
     */
    public void setRoom(String room) {
        this.room = room;
    }

}

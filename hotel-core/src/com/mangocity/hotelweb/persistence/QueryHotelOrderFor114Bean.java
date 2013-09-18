package com.mangocity.hotelweb.persistence;

import java.io.Serializable;

/**
 */
public class QueryHotelOrderFor114Bean implements Serializable {

    private String id;

    private String ordercd;

    private String ordernum;// 订单编号 该订单号是由会员管理系统生成的"流水号"

    private String hotel;// ：酒店名称

    private String room;// ：;//房型

    private String quantity;// ：房间数量

    private String amount;// ：总金额

    private String checkin;// ：入住日期

    private String checkout;// ：退房日期

    private String addtime;// ：下单时间

    private String modifytime;// ：修改时间

    private String status;// ：订单状态

    private String contactman;// : 联系人

    private String contactphone;// :联系电话

    private String hotelconfirm;// : 酒店是否已确认

    private String inroom;// : 入住人姓名

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
     * @return 返回 addtime。
     */
    public String getAddtime() {
        return addtime;
    }

    /**
     * @param addtime
     *            要设置的 addtime。
     */
    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    /**
     * @return 返回 amount。
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount
     *            要设置的 amount。
     */
    public void setAmount(String amount) {
        this.amount = amount;
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
     * @return 返回 checkout。
     */
    public String getCheckout() {
        return checkout;
    }

    /**
     * @param checkout
     *            要设置的 checkout。
     */
    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    /**
     * @return 返回 contactman。
     */
    public String getContactman() {
        return contactman;
    }

    /**
     * @param contactman
     *            要设置的 contactman。
     */
    public void setContactman(String contactman) {
        this.contactman = contactman;
    }

    /**
     * @return 返回 contactphone。
     */
    public String getContactphone() {
        return contactphone;
    }

    /**
     * @param contactphone
     *            要设置的 contactphone。
     */
    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
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
     * @return 返回 hotelconfirm。
     */
    public String getHotelconfirm() {
        return hotelconfirm;
    }

    /**
     * @param hotelconfirm
     *            要设置的 hotelconfirm。
     */
    public void setHotelconfirm(String hotelconfirm) {
        this.hotelconfirm = hotelconfirm;
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
     * @return 返回 modifytime。
     */
    public String getModifytime() {
        return modifytime;
    }

    /**
     * @param modifytime
     *            要设置的 modifytime。
     */
    public void setModifytime(String modifytime) {
        this.modifytime = modifytime;
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
     * @return 返回 quantity。
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     *            要设置的 quantity。
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    /**
     * @return 返回 status。
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            要设置的 status。
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

package com.mangocity.hotel.base.persistence;

import com.mangocity.util.Entity;

/**
 */
public class HtlTrafficInfo extends CEntity implements Entity {

    private Long ID;

    private long trafficID;

    // private long hotelID;
    // 到达位置
    private String arriveAddress;

    // 距离
    private String distance;

    // 车程时间
    private String byCarTime;

    // 到达站点名称
    private String arriveName;

    private HtlHotel htlHotel;

    public String getArriveAddress() {
        return arriveAddress;
    }

    public void setArriveAddress(String arriveAddress) {
        this.arriveAddress = arriveAddress;
    }

    public String getArriveName() {
        return arriveName;
    }

    public void setArriveName(String arriveName) {
        this.arriveName = arriveName;
    }

    public String getByCarTime() {
        return byCarTime;
    }

    public void setByCarTime(String byCarTime) {
        this.byCarTime = byCarTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public HtlHotel getHtlHotel() {
        return htlHotel;
    }

    public void setHtlHotel(HtlHotel htlHotel) {
        this.htlHotel = htlHotel;
    }

    public long getTrafficID() {
        return trafficID;
    }

    public void setTrafficID(long trafficID) {
        this.trafficID = trafficID;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

}

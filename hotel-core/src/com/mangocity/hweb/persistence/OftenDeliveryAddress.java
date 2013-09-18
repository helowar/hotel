package com.mangocity.hweb.persistence;

import java.io.Serializable;

/**
 */
public class OftenDeliveryAddress implements Serializable {

    /**
     * 配送城市ID
     */
    // private String deliveryCityID;
    /**
     * 配送城区ID
     */
    // private String deliveryZoneID;
    /**
     * 配送地址
     */
    private String deliveryAddress;

    /**
     * 城市+城区字符串
     */
    // private String cityZone;
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

}

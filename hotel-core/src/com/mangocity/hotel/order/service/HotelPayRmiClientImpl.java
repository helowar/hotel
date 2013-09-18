package com.mangocity.hotel.order.service;

import java.io.Serializable;

/**
 */
public class HotelPayRmiClientImpl implements Serializable {

    /**
     * 给财务rmi客户端
     * 
     * @author chenkeming Mar 13, 2009 5:10:19 PM
     */
    private IPayOutInterface payServ;

    public IPayOutInterface getPayServ() {
        return payServ;
    }

    public void setPayServ(IPayOutInterface payServ) {
        this.payServ = payServ;
    }

}

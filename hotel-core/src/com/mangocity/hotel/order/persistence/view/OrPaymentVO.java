package com.mangocity.hotel.order.persistence.view;


import java.io.Serializable;

import com.mangocity.hotel.order.persistence.OrPayment;

/**
 * 
 *  支付记录
 *  
 *  @author chenkeming
 */
public class OrPaymentVO implements Serializable {

    private OrPayment payment = null;
    
    private String name;    

    public OrPaymentVO(String name) {        
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrPayment getPayment() {
        return payment;
    }

    public void setPayment(OrPayment payment) {
        this.payment = payment;
    }
}

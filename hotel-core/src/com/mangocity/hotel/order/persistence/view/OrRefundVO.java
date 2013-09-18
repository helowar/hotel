package com.mangocity.hotel.order.persistence.view;


import java.io.Serializable;

import com.mangocity.hotel.order.persistence.OrRefund;

/**
 * 
 *  退款记录
 *  
 *  @author chenkeming
 */
public class OrRefundVO implements Serializable {

    private OrRefund refund = null;
    
    private String name;    

    public OrRefundVO(String name) {        
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrRefund getRefund() {
        return refund;
    }

    public void setRefund(OrRefund refund) {
        this.refund = refund;
    }


}

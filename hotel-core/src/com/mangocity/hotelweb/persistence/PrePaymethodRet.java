package com.mangocity.hotelweb.persistence;

import java.io.Serializable;

/**
 * 全额预付返回结果
 * 
 * @author neil
 * 
 */
public class PrePaymethodRet implements Serializable {

    // 定义实际需要支付的金额
    private double paySum;

    private boolean payBool;

    public boolean isPayBool() {
        return payBool;
    }

    public void setPayBool(boolean payBool) {
        this.payBool = payBool;
    }

    public double getPaySum() {
        return paySum;
    }

    public void setPaySum(double paySum) {
        this.paySum = paySum;
    }

}

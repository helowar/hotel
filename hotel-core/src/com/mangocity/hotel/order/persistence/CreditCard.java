/**
 *  Modified by chenkeming@2007.05.08
 *  
 *  订单信用卡资料表
 */
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 */
public class CreditCard implements Entity {

    // ID
    private Long ID;

    // 订单ID <fk> 和Order关联
    private Order order;
    
    // 信用卡种
    private String cardType;

    // 信用卡号
    private String cardNo;

    // 有效期
    private Date validPeriod;
    
    // 持卡人姓名
    private String ownerName;
    
    // 持卡人证件类型
    private String ownerIdType;

    // 持卡人证件号码
    private String ownerId;
    
    // 验证码
    private String verifyNo;
    
    // 是否保留该信用卡
    private String isSave;
    
    // 是否有效
    private String active;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long creditCardId) {
        this.ID = creditCardId;
    }

    public String getIsSave() {
        return isSave;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerIdType() {
        return ownerIdType;
    }

    public void setOwnerIdType(String ownerIdType) {
        this.ownerIdType = ownerIdType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(Date validPeriod) {
        this.validPeriod = validPeriod;
    }

    public String getVerifyNo() {
        return verifyNo;
    }

    public void setVerifyNo(String verifyNo) {
        this.verifyNo = verifyNo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}

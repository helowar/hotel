/**
 * 
 *  配送信息
 */
package com.mangocity.hotel.order.persistence;

import com.mangocity.util.Entity;

/**
 */
public class Fulfillment implements Entity {

    // 配送ID <pk>
    private Long ID;

    // 是否索要发票
    private String isInvoice;

    // 发票抬头
    private String invoiceTitle;

    // 发票邮寄地址
    private String invoiceAddress;

    // 配送收费
    private double fee;

    // 配送地址
    private String address;

    // 配送单位
    private String department;

    // 配送联系人
    private String linkman;

    // 配送联系人电话
    private String linkPhone;
    
    // 配送邮编
    private String postNo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long fulfillId) {
        this.ID = fulfillId;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(String isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getPostNo() {
        return postNo;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }


}

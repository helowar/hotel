package com.mangocity.hotelweb.persistence;

import java.io.Serializable;

/**
 */
public class HotelFormIpsBean implements Serializable {
    // 订单类型orderType
    private String orderType;

    // 订单编号orderCode
    private String orderCode;

    // 会员memberid
    private String member;

    // 支付方式payType
    private String payType;

    // 卡类型cardType
    private String cardType;

    // 支付金额transactAmount
    private String transactAmount;

    // 交易日期transactDate
    private String transactDate;

    // 支付成功返回的URL successUrl
    private String successUrl;

    // 支付失败返回的URL failureUrl
    private String failureUrl;

    // 数字签名signature
    private String signature;

    // 服务器端返回类型serverReturnType
    private String serverReturnType;

    // 服务器端返回地址serverLocation
    private String serverLocation;

    // 业务系统保留数据attachment
    private String attachment;

    private String targetUrl;// 目标支付服务器URL

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTransactAmount() {
        return transactAmount;
    }

    public void setTransactAmount(String transactAmount) {
        this.transactAmount = transactAmount;
    }

    public String getTransactDate() {
        return transactDate;
    }

    public void setTransactDate(String transactDate) {
        this.transactDate = transactDate;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getServerReturnType() {
        return serverReturnType;
    }

    public void setServerReturnType(String serverReturnType) {
        this.serverReturnType = serverReturnType;
    }

    public String getServerLocation() {
        return serverLocation;
    }

    public void setServerLocation(String serverLocation) {
        this.serverLocation = serverLocation;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}

package com.mangocity.hotel.base.util;

import java.io.Serializable;

/**
 */
public class CreditCardWrapper implements Serializable {

    /**
     * id 系统ID memberid 会员表ID credittype 信用卡类型 currencytype 币种 creditcardno 卡号 oversea 是否国际卡
     * expiredate 有效期 cardholder 持卡人 certificate 有效证件类型 certificateno 证件号 verifycode 3维验证码 operator
     * 操作员 createdate 创建时间 modifidate 修改时间 deletedate 删除时间 availability 是否有效卡 passverify 是否通过验证
     * translastdate 交易日期 notes 备注 active 是否生效 four
     * 
     */

    private long id = 0L;

    private long memberid = 0L;

    private String credittype = "";

    private String currencytype = "";

    private String creditcardno = "";

    private String oversea;

    private String expiredate = "";

    private String cardholder = "";

    private String certificate = "";

    private String certificateno = "";

    private String verifycode = "";

    private String operator = "";

    private java.util.Date createdate;

    private java.util.Date modifidate;

    private java.util.Date deletedate;

    private String availability;

    private String passverify;

    private java.util.Date translastdate;

    private String notes = "";

    private String active;

    private String four = "";

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificateno() {
        return certificateno;
    }

    public void setCertificateno(String certificateno) {
        this.certificateno = certificateno;
    }

    public java.util.Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(java.util.Date createdate) {
        this.createdate = createdate;
    }

    public String getCreditcardno() {
        return creditcardno;
    }

    public void setCreditcardno(String creditcardno) {
        this.creditcardno = creditcardno;
    }

    public String getCredittype() {
        return credittype;
    }

    public void setCredittype(String credittype) {
        this.credittype = credittype;
    }

    public String getCurrencytype() {
        return currencytype;
    }

    public void setCurrencytype(String currencytype) {
        this.currencytype = currencytype;
    }

    public java.util.Date getDeletedate() {
        return deletedate;
    }

    public void setDeletedate(java.util.Date deletedate) {
        this.deletedate = deletedate;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMemberid() {
        return memberid;
    }

    public void setMemberid(long memberid) {
        this.memberid = memberid;
    }

    public java.util.Date getModifidate() {
        return modifidate;
    }

    public void setModifidate(java.util.Date modifidate) {
        this.modifidate = modifidate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOversea() {
        return oversea;
    }

    public void setOversea(String oversea) {
        this.oversea = oversea;
    }

    public String getPassverify() {
        return passverify;
    }

    public void setPassverify(String passverify) {
        this.passverify = passverify;
    }

    public java.util.Date getTranslastdate() {
        return translastdate;
    }

    public void setTranslastdate(java.util.Date translastdate) {
        this.translastdate = translastdate;
    }

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

}

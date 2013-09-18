package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.HEntity;

/**
 * 
 *  历史订单114用信用卡实体类
 *  
 *  @author chenkeming
 */
public class HCreditCardTemp implements HEntity {

    private static final long serialVersionUID = -2228263142901552697L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;    
    
    /**
	 * 该114订单用到的会员card IDs
	 */
    private String cardIds = "";

    /**
	 * 历史订单ID <fk> 和HOrder关联
	 */
    private HOrder orderH;

    /**
	 * 会员ID
	 */
    private Long memberId;

    /**
	 * 类型
	 */
    private String creditType;

    /**
	 * 币种
	 */
    private String currencyType;

    /**
	 * 卡号
	 */
    private String creditCardNo;

    /**
	 * 国内/国外
	 */
    private boolean oversea;

    /**
	 * 有效期
	 */
    private String expireDate;        

    /**
	 * 持卡人
	 */
    private String cardHolder;
    
    /**
	 * 有效证件类型
	 */
    private String certificate;
    
    /**
	 * 有效证件号码
	 */
    private String certificateNo;
    
    /**
	 * 验证码
	 */
    private String verifyCode;
    
    /**
	 * 操作员
	 */
    private String operator;
    
    /**
	 * 创建日期
	 */
    private Date createDate;
    
    /**
	 * 修改日期
	 */
    private String modifiDate;
    
    private char passVerify;
    
    /**
	 * 最后交易日期
	 */
    private Date transLastDate;
    
    private boolean active;
    
    /**
	 * 密码后四位
	 */
    private String four;
    
    /**
	 * 最后交易日期
	 */
    private boolean availability;
    
    /**
	 * 操作员
	 */
    private String notes;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardIds() {
        return cardIds;
    }

    public void setCardIds(String cardIds) {
        this.cardIds = cardIds;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getModifiDate() {
        return modifiDate;
    }

    public void setModifiDate(String modifiDate) {
        this.modifiDate = modifiDate;
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
    
    public boolean isOversea() {
        return oversea;
    }

    public void setOversea(boolean oversea) {
        this.oversea = oversea;
    }

    public char getPassVerify() {
        return passVerify;
    }

    public void setPassVerify(char passVerify) {
        this.passVerify = passVerify;
    }

    public Date getTransLastDate() {
        return transLastDate;
    }

    public void setTransLastDate(Date transLastDate) {
        this.transLastDate = transLastDate;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public HOrder getOrderH() {
        return orderH;
    }

    public void setOrderH(HOrder orderH) {
        this.orderH = orderH;
    }
    
}

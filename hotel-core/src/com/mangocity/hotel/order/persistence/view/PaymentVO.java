package com.mangocity.hotel.order.persistence.view;

import java.util.Date;

import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.util.Entity;

/**
 * 
 *  支付记录
 *  
 *  @author chenkeming
 */
public class PaymentVO implements Entity {

    private static final long serialVersionUID = 4608722732545637258L;

    /**
	 * ID <pk>
	 */
    private Long ID;

    /**
	 * 支付类型（现金，积分，信用卡，在线支付）
	 * 
	 * @see PrepayType
	 */
    private int payType;

    /**
	 * 支付金额
	 */
    private double money;
    
    /**
	 * 支付是否成功
	 */
    private boolean paySucceed;
    
    /**
	 * 币种
	 */
    private String currencyType;

    /**
	 * 支付号
	 */
    private String prepayBillNo;
    
    /**
	 * 最后操作人
	 */
    private String operator;
    
    /**
	 * 最后操作时间
	 */
    private Date operateTime;
    
    /**
	 * 操作备注
	 */
    private String notes;    
    
    /**
	 * 创建人
	 */
    private String creator;
    
    /**
	 * 创建时间
	 */
    private Date createTime;
    
    /**
	 * 确认收款人
	 */
    private String confirmer;
    
    /**
	 * 确认收款时间
	 */
    private Date confirmTime;    
    

    /** getter and setter  begin */
    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean isPaySucceed() {
        return paySucceed;
    }

    public void setPaySucceed(boolean paySucceed) {
        this.paySucceed = paySucceed;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPrepayBillNo() {
        return prepayBillNo;
    }

    public void setPrepayBillNo(String prepayBillNo) {
        this.prepayBillNo = prepayBillNo;
    }

    public String getConfirmer() {
        return confirmer;
    }

    public void setConfirmer(String confirmer) {
        this.confirmer = confirmer;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    /** getter and setter  end */
    
    /**
	 * 是否需要信用卡
	 */
    public boolean isNeedCreditCard() {
        return payType == PrepayType.CreditCardInt || 
                payType == PrepayType.CreditCardDom;
    }
    
    /**
	 * 是否需要选择银行
	 * @return
	 */
    public boolean isNeedSelectBank() {
        return payType == PrepayType.Bank;
    }
    
    /**
	 * 是否积分支付
	 * @return
	 */
    public boolean isPoints() {
        return payType == PrepayType.Points;
    }
    
}

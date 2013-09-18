
package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.util.Entity;

/**
 * 
 *  支付记录
 *  
 *  @author chenkeming
 */
public class OrPayment implements Entity {

    private static final long serialVersionUID = -327410491604130754L;

    /**
	 * ID <pk>
	 */
    private Long ID;

    /**
	 * 订单ID <fk> 和OrOrder关联
	 */
    private OrOrder order;

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
	 * 支付号  用于存储流水号 add by shengwei.zuo 2009-12-1 网站改版
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

    public OrOrder getOrder() {
        return order;
    }

    public void setOrder(OrOrder order) {
        this.order = order;
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

    
    /**
	 * 是否IPS支付(国内)
	 * @return
	 */
    public boolean isIpsr() {
        return payType == PrepayType.IPSDom;
    }

    /**
	 * 是否IPS支付(国际)
	 * @return
	 */
    public boolean isIps() {
        return payType == PrepayType.IPSInt;
    }
    
    /**
	 * 是否CMB支付
	 * @return
	 */
    public boolean isCmb() {
        return payType == PrepayType.CMBInt;
    }
    
    /**
	 * 是否网站在线支付方式
	 * @return
	 */
    public boolean isWebWay() {
		return payType >= PrepayType.IPSDom && payType != PrepayType.Coupon
				&& payType != PrepayType.BALANCEPAYInt
				&& payType != PrepayType.UNTIONPAYPHONE;
    }
    
    /**
     * 是否代金券支付方式 hotel2.9.3 add by chenjiajie 2009-09-05
     * @return
     */
    public boolean isCoupon(){
    	return payType == PrepayType.Coupon;
    }
    
    /**
	 * 是否账户余额支付 TMC-V2.0 add by shengwei.zuo  2010-4-1
	 * @return
	 */
    public boolean isBalance() {
        return payType == PrepayType.BALANCEPAYInt;
    }
    
    /**
	 * 是否银联电话支付方式
	 * @return
	 */
    public boolean isUnionPayPhone() {
		return payType == PrepayType.UNTIONPAYPHONE;
    }
}

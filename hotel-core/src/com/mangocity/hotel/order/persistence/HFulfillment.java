package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.hotel.order.constant.FulfillTaskType;
import com.mangocity.util.HEntity;

/**
 * 
 *  历史订单配送信息
 *  
 *  @author chenkeming
 *  
 */
public class HFulfillment implements HEntity {

    private static final long serialVersionUID = -1654475702005733300L;

    /**
	 * 主键 <pk>
	 */
    private Long hisID;    
    
    /**
	 * 任务类型
	 * @see FulfillTaskType
	 */
    private int fulfillTaskType;

    /**
	 * 配送方式
	 */
    private String deliveryType;
     
    /**
	 * 配送城市ID
	 */
    private String deliveryCityID;
    
    /**
	 * 配送城区ID
	 */
    private String deliveryZoneID;
                  
    /**
	 * 应收金额
	 */
    private double account;
    
    /**
	 * 配送地址
	 */
    private String deliveryAddress;
    
    /**
	 * 配送日期
	 */
    private Date deliveryDate;
  
    /**
	 * 配送开始时间
	 */
    private String deliveryTime;
    
    /**
	 * 配送时间
	 */
    private String deliveryTimeEnd;
    
    /**
	 * 收件人姓名
	 */
    private String addresseeName;
    
    /**
	 * 收件人电话
	 */
    private String addresseePhone;
                                          
    /**
	 * 是否要发票
	 */
    private boolean isInvoice = false;
    
    /**
	 * 发票项目
	 */
    private String fulfillSelInvoice; 
    
    /**
	 * 发票抬头
	 */
    private String fulfillInvoiceTitle = "";
                              
    /**
	 * 发票金额
	 */
    private double fulfillInvoiceFee;    
    
    /**
	 * 邮编
	 */
    private String fulfillPostCode; 
     
    /**
	 * 备注
	 */
    private String fulfillNote;     
     
    /**
	 * 收款单位编号
	 */
    private String unitCD; 
    
    /**
	 * 收款联系人
	 */
    private String fulfillPayLinkman;
    
    /**
	 * 收款联系电话
	 */
    private String fulfillPayPhone;

    /**
	 * 收款详细地址
	 */
    private String fulfillPayAddress;
    
    /**
	 * 收款日期
	 */
    private Date fulfillPayDate;
    
    /**
	 * 收款开始时间
	 */
    private String fulfillPayTime;
    
    /**
	 * 收款结束时间
	 */
    private String fulfillPayTimeEnd;
    
    /**
	 * 是否配送入住凭证
	 */
    private boolean fulfillConfirm = false;
    
    /**
	 * 是否已经生成过配送单
	 */
    private boolean isFulfillCheck = false;
    
    /**
	 * 配送状态
	 */
    private String deliveryState = "";
    
    /**
	 * 配送单编号
	 */
    private String fulfillmentCD;

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public String getAddresseeName() {
        return addresseeName;
    }

    public void setAddresseeName(String addresseeName) {
        this.addresseeName = addresseeName;
    }

    public String getAddresseePhone() {
        return addresseePhone;
    }

    public void setAddresseePhone(String addresseePhone) {
        this.addresseePhone = addresseePhone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryCityID() {
        return deliveryCityID;
    }

    public void setDeliveryCityID(String deliveryCityID) {
        this.deliveryCityID = deliveryCityID;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryTimeEnd() {
        return deliveryTimeEnd;
    }

    public void setDeliveryTimeEnd(String deliveryTimeEnd) {
        this.deliveryTimeEnd = deliveryTimeEnd;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryZoneID() {
        return deliveryZoneID;
    }

    public void setDeliveryZoneID(String deliveryZoneID) {
        this.deliveryZoneID = deliveryZoneID;
    }

    public boolean isFulfillConfirm() {
        return fulfillConfirm;
    }

    public void setFulfillConfirm(boolean fulfillConfirm) {
        this.fulfillConfirm = fulfillConfirm;
    }

    public double getFulfillInvoiceFee() {
        return fulfillInvoiceFee;
    }

    public void setFulfillInvoiceFee(double fulfillInvoiceFee) {
        this.fulfillInvoiceFee = fulfillInvoiceFee;
    }

    public String getFulfillInvoiceTitle() {
        return fulfillInvoiceTitle;
    }

    public void setFulfillInvoiceTitle(String fulfillInvoiceTitle) {
        this.fulfillInvoiceTitle = fulfillInvoiceTitle;
    }

    public String getFulfillmentCD() {
        return fulfillmentCD;
    }

    public void setFulfillmentCD(String fulfillmentCD) {
        this.fulfillmentCD = fulfillmentCD;
    }

    public String getFulfillNote() {
        return fulfillNote;
    }

    public void setFulfillNote(String fulfillNote) {
        this.fulfillNote = fulfillNote;
    }

    public String getFulfillPayAddress() {
        return fulfillPayAddress;
    }

    public void setFulfillPayAddress(String fulfillPayAddress) {
        this.fulfillPayAddress = fulfillPayAddress;
    }

    public Date getFulfillPayDate() {
        return fulfillPayDate;
    }

    public void setFulfillPayDate(Date fulfillPayDate) {
        this.fulfillPayDate = fulfillPayDate;
    }

    public String getFulfillPayLinkman() {
        return fulfillPayLinkman;
    }

    public void setFulfillPayLinkman(String fulfillPayLinkman) {
        this.fulfillPayLinkman = fulfillPayLinkman;
    }

    public String getFulfillPayPhone() {
        return fulfillPayPhone;
    }

    public void setFulfillPayPhone(String fulfillPayPhone) {
        this.fulfillPayPhone = fulfillPayPhone;
    }

    public String getFulfillPayTime() {
        return fulfillPayTime;
    }

    public void setFulfillPayTime(String fulfillPayTime) {
        this.fulfillPayTime = fulfillPayTime;
    }

    public String getFulfillPayTimeEnd() {
        return fulfillPayTimeEnd;
    }

    public void setFulfillPayTimeEnd(String fulfillPayTimeEnd) {
        this.fulfillPayTimeEnd = fulfillPayTimeEnd;
    }

    public String getFulfillPostCode() {
        return fulfillPostCode;
    }

    public void setFulfillPostCode(String fulfillPostCode) {
        this.fulfillPostCode = fulfillPostCode;
    }

    public String getFulfillSelInvoice() {
        return fulfillSelInvoice;
    }

    public void setFulfillSelInvoice(String fulfillSelInvoice) {
        this.fulfillSelInvoice = fulfillSelInvoice;
    }

    public int getFulfillTaskType() {
        return fulfillTaskType;
    }

    public void setFulfillTaskType(int fulfillTaskType) {
        this.fulfillTaskType = fulfillTaskType;
    }

    public Long getHisID() {
        return hisID;
    }

    public void setHisID(Long hisID) {
        this.hisID = hisID;
    }

    public boolean isFulfillCheck() {
        return isFulfillCheck;
    }

    public boolean getIsFulfillCheck() {
        return isFulfillCheck;
    }
    
    public void setIsFulfillCheck(boolean isFulfillCheck) {
        this.isFulfillCheck = isFulfillCheck;
    }

    public boolean isInvoice() {
        return isInvoice;
    }

    public boolean getIsInvoice() {
        return isInvoice;
    }
    
    public void setIsInvoice(boolean isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getUnitCD() {
        return unitCD;
    }

    public void setUnitCD(String unitCD) {
        this.unitCD = unitCD;
    }

}

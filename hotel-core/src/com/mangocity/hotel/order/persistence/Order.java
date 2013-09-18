/**
 * 
 *  订单
 */
package com.mangocity.hotel.order.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.constant.PayState;
import com.mangocity.util.DateUtil;
import com.mangocity.util.Entity;
import com.mangocity.util.hotel.constant.PayMethod;


/**
 */
public class Order implements Entity {

    // 订单ID <pk>
    private Long ID;
    
    // 订单CD
    private String orderCD;

    // 源订单CD
    private String originCD;
    
    // 酒店ID
    private Long hotelId;

    // 酒店名称
    private String hotelName;

    // 会员ID
    private Long memberId;

    // 会员中文名
    private String memberChName;

    // 会员英文名
    private String memberEnName;
    
    /**
	 * 会员类型
	 */
    private String memberType;

    // 订单类型
    private String type;
    
    // 订单来源
    private String source;

    // 销售渠道
    private String channel;
    
    // 订单状态
    private String orderState;

    // 信用卡担保状态
    private String suretyState;
    
    // 付款状态
    private String payState;
    
    // 日审状态
    private String auditState;
    
    // 房型ID
    private Long roomTypeId;

    /**
	 * 子房型
	 */
    private Long childRoomTypeId;
    
    // 房型名称
    private String roomTypeName;
    

    // 房间数量
    private int roomQuantity;

    // 实际入住间夜数
    private int factRoomNum;
    
    // 房间日期
    private Date checkinDate;

    // 退房时间
    private Date checkoutDate;

    // 实际退房时间
    private Date factCheckout;
    
    // 紧急程度
    private String emergencyLevel;

    // 订单总金额
    private double amount;
    
    // 已经支付金额
    private double payAmount;
    
    // 担保金额
    private double suretyPrice;
    
    /**
	 * 最晚取消时间
	 */
    private Date   allowCancelTime;
    
    // 信用卡扣款金额
    private double penalty;
    
    // 退款金额
    private double refund;
    
    // 配送单号
    private String fulfillmentCD;
    
    // 付款类型 网上支付、积分、现金、POS机等
    private String prepayType;
    
    // 支付币种
    private String paymentCurrency;
    
    // 付款方式(面付,预付)
    private String payMethod;
    
    // 付款时限
    private Date prepayLimitDate;
    
    // 撤单原因
    private String cancelReason;
    
    // 担保币种
    private String suretyCurrency;
    
    // 订单创建时间
    private Date createDate;
    
    // 订单创建人ID
    private String creator;
    
    // 创建人姓名
    private String creatorName;
    
    // 订单修改人ID
    private String modifier;
    
    // 最后修改人姓名
    private String modifierName;
    
    // 最后修改人角色
    private String modifierRole;
    
    // 跟单人ID
    private String owner;
    
    // 跟单人姓名
    private String ownerName;
    
    // 订单最后修改时间
    private Date modifiedTime;
    
    // 酒店确认标志
    private String hotelConfirm;
    
    // 客户确认标志
    private String customerConfirm;
    
    // 酒店确认号
    private String hotelConfirmId;
    
    // 客人确认方式
    private String confirmType;
    
    // 和RoomDetail关联
    private List roomItems = new ArrayList();
    
    // 和QuotaDetail关联
    private List quotaList = new ArrayList();
    
    // 和Payment关联
    private List paymentList = new ArrayList();
    
    // 和CreditCard关联
    private List creditCardList = new ArrayList();
    
    // 和Audit关联
    private List auditList = new ArrayList();
    
    // 和HandleLog关联
    private List logList = new ArrayList();
    
    // 和FellowInfo关联
    private List fellowList = new ArrayList();
    
    // 配送ID <fk> 和Fulfillment关联
    private Fulfillment fulfill;
    
    // 备注ID <fk> 和Remark关联
    private Remark remark;
    
    // 预订规则ID <fk> 和Reservation关联
    private Reservation reservation;
    
    // 联系信息ID <fk> 和Linkman关联
    private Linkman linkman;    
    
    

    
    /**
     * 是否预付订单
     * @return
     */
    public boolean isPrepayOrder() {
        return PayMethod.PRE_PAY.equals(this.payMethod);
    }
    
    /**
	 * 是否担保单
	 * @return
	 */
    public boolean isGuaranteeOrder(){
        return !isPrepayOrder() && null != getSuretyState();
    }
    
    /**
	 * 是否需要配送
	 * @return
	 */
    public boolean isNeedFulfill(){
//		if (!isPrepayOrder())
//			return false;
//		
//		/**
//		 * 如果是现金和POS刷卡方式，则需要配送
//		 */
//		if (PrepayType.Cash.equals(this.prepayType) || PrepayType.POS.equals(this.prepayType))
//			return true;
        
        
        return false;
        
    }
    
    /**
     * 预付单客户是否已付款
     * @return
     */
    public boolean isPayCustomer() {
        return isPrepayOrder() && PayState.HAVE_PAY.equals(this.payState);
    }
    
    /**
     * 订单状态是否已入住及之后的状态
     * @return
     */
    public boolean isAfterCheckin() {
        return false;
//		return (OrderState.CHECK_IN.equals(this.orderState) 
//				|| OrderState.FINISH.equals(this.orderState)
//				|| OrderState.CANCEL.equals(this.orderState)
//				|| OrderState.FONT_CANCEL.equals(this.orderState));
        
    }
    
    /**
	 * 判断订单入住日期是否今天
	 * @return
	 */
    public boolean isTodayCheckin() {
        return 0 == DateUtil.compare(this.checkinDate,new Date());
    }
        
    public Linkman getLinkman() {
        return linkman;
    }

    public void setLinkman(Linkman linkman) {
        this.linkman = linkman;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(String confirmType) {
        this.confirmType = confirmType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCustomerConfirm() {
        return customerConfirm;
    }

    public void setCustomerConfirm(String customerConfirm) {
        this.customerConfirm = customerConfirm;
    }

    public String getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(String emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public Date getFactCheckout() {
        return factCheckout;
    }

    public void setFactCheckout(Date factCheckout) {
        this.factCheckout = factCheckout;
    }

    public int getFactRoomNum() {
        return factRoomNum;
    }

    public void setFactRoomNum(int factRoomNum) {
        this.factRoomNum = factRoomNum;
    }

    public String getFulfillmentCD() {
        return fulfillmentCD;
    }

    public void setFulfillmentCD(String fulfillmentCD) {
        this.fulfillmentCD = fulfillmentCD;
    }

    public String getHotelConfirm() {
        return hotelConfirm;
    }

    public void setHotelConfirm(String hotelConfirm) {
        this.hotelConfirm = hotelConfirm;
    }

    public String getHotelConfirmId() {
        return hotelConfirmId;
    }

    public void setHotelConfirmId(String hotelConfirmId) {
        this.hotelConfirmId = hotelConfirmId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getMemberChName() {
        return memberChName;
    }

    public void setMemberChName(String memberChName) {
        this.memberChName = memberChName;
    }

    public String getMemberEnName() {
        return memberEnName;
    }

    public void setMemberEnName(String memberEnName) {
        this.memberEnName = memberEnName;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getModifierRole() {
        return modifierRole;
    }

    public void setModifierRole(String modifierRole) {
        this.modifierRole = modifierRole;
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long orderId) {
        this.ID = orderId;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOriginCD() {
        return originCD;
    }

    public void setOriginCD(String originCD) {
        this.originCD = originCD;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public Date getPrepayLimitDate() {
        return prepayLimitDate;
    }

    public void setPrepayLimitDate(Date prepayLimitDate) {
        this.prepayLimitDate = prepayLimitDate;
    }

    public String getPrepayType() {
        return prepayType;
    }

    public void setPrepayType(String prepayType) {
        this.prepayType = prepayType;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public int getRoomQuantity() {
        return roomQuantity;
    }

    public void setRoomQuantity(int roomQuantity) {
        this.roomQuantity = roomQuantity;
    }

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSuretyCurrency() {
        return suretyCurrency;
    }

    public void setSuretyCurrency(String suretyCurrency) {
        this.suretyCurrency = suretyCurrency;
    }

    public double getSuretyPrice() {
        return suretyPrice;
    }

    public void setSuretyPrice(double suretyPrice) {
        this.suretyPrice = suretyPrice;
    }

    public String getSuretyState() {
        return suretyState;
    }

    public void setSuretyState(String suretyState) {
        this.suretyState = suretyState;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List getRoomItems() {
        return roomItems;
    }

    public void setRoomItems(List roomItems) {
        this.roomItems = roomItems;
    }

    public Fulfillment getFulfill() {
        return fulfill;
    }

    public void setFulfill(Fulfillment fulfill) {
        this.fulfill = fulfill;
    }

    public List getQuotaList() {
        return quotaList;
    }

    public void setQuotaList(List quotaList) {
        this.quotaList = quotaList;
    }

    public List getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List paymentList) {
        this.paymentList = paymentList;
    }

    public List getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(List creditCardList) {
        this.creditCardList = creditCardList;
    }

    public List getAuditList() {
        return auditList;
    }

    public void setAuditList(List auditList) {
        this.auditList = auditList;
    }

    public Remark getRemark() {
        return remark;
    }

    public void setRemark(Remark remark) {
        this.remark = remark;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation rule) {
        this.reservation = rule;
    }

    public List getLogList() {
        return logList;
    }

    public void setLogList(List logList) {
        this.logList = logList;
    }

    public List getFellowList() {
        return fellowList;
    }

    public void setFellowList(List fellowList) {
        this.fellowList = fellowList;
    }

    public Long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(Long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public Date getAllowCancelTime() {
        return allowCancelTime;
    }

    public void setAllowCancelTime(Date allowCancelTime) {
        this.allowCancelTime = allowCancelTime;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }


}

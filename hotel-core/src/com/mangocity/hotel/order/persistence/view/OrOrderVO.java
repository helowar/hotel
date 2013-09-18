package com.mangocity.hotel.order.persistence.view;


import java.util.Date;
import java.util.List;

import com.mangocity.hotel.base.constant.HraOrderType;
import com.mangocity.hotel.order.constant.ConfirmType;
import com.mangocity.hotel.order.constant.EmergencyLevel;
import com.mangocity.hotel.order.constant.GuaranteeState;
import com.mangocity.hotel.order.constant.OrderSource;
import com.mangocity.hotel.order.constant.OrderState;
import com.mangocity.hotel.order.constant.OrderType;
import com.mangocity.hotel.order.constant.PrepayType;
import com.mangocity.hotel.order.persistence.OrFulfillment;
import com.mangocity.util.Entity;
import com.mangocity.util.hotel.constant.BedType;


/**
 * 
 *  订单
 *  
 *  @author chenkeming
 */
public class OrOrderVO implements Entity {

    private static final long serialVersionUID = 4869863159739661802L;

    /**
	 * 订单ID <pk>
	 */
    private Long ID;
    
    /**
	 * 订单CD
	 */
    private String orderCD;

    /**
	 * 订单CD -- 给酒店发传真用
	 */
    private String orderCDHotel;
    
    /**
	 * 源订单CD
	 */
    private String originCD;
    
    /**
	 * 酒店ID
	 */
    private Long hotelId;

    /**
	 * 酒店名称
	 */
    private String hotelName;

    /**
	 * 会员ID
	 */
    private Long memberId;
 
    /**
	 * 会员名字(包括中英文)
	 */
    private String memberName;    

    /**
	 * 订单类型 :(1-mango, 2-114)
	 * 
	 * @see OrderType
	 */
    private int type;
    
    /**
	 * 订单来源: 网站，CCmango，香港组
	 * 
	 * @see OrderSource
	 */
    private String source;

    /**
	 * 销售渠道（是指代理公司，如114南京公司等，不是指城市，这点与1期的114城市有区别）<br>
	 * 暂时用不着，暂保留
	 */
    private int channel;
    
    /**
	 * 订单状态（前台订单未提交，已提交订单，已提交中台，中台处理完毕，已开始日审，日审完毕，已日结
	 * 
	 * 如果封装的是B2bModifyOrderInfo实体对象，则订单状态 0 表示修改已处理 1 修改未处理， 2 取消未处理  3 取消已处理
	 * 
	 * @see OrderState
	 */
    private int orderState;

    /**
	 * 信用卡担保状态
	 * 
	 * @see GuaranteeState
	 */
    private int suretyState;
    
    /**
	 * 付款状态
	 */
    private int payState;
    
    /**
	 * 日审状态：noshow，提前退房，正常退房，延住
	 */
    private int auditState;
    
    /**
	 * 房型ID
	 */
    private Long roomTypeId;
    
    /**
	 * 房型名称
	 */
    private String roomTypeName;

    /**
	 * 子房型
	 */
    private Long childRoomTypeId;
        
    /**
	 * 房间数量
	 */
    private int roomQuantity;
        
    /**
	 * 入住日期
	 */
    private Date checkinDate;

    /**
	 * 退房时间
	 */
    private Date checkoutDate;
    
    /**
	 * 紧急程度
	 * 
	 * @see EmergencyLevel
	 */
    private int emergencyLevel;
    
    /**
	 * 已经支付金额
	 */
    private double payAmount;
    
    /**
	 * 担保金额
	 */
    private double suretyPrice;
    
    /**
	 * 最晚取消时间
	 */
    private Date   allowCancelTime;
    
    /**
	 * 信用卡扣款金额
	 */
    private double penalty;
    
    /**
	 * 退款金额
	 */
    private double refund;
    
    /**
	 * 配送单号
	 */
    private String fulfillmentCD;
    
    /**
	 * 付款类型 网上支付、积分、现金、POS机等
	 */
    private int prepayType;
    
    /**
	 * 币种类型
	 */
    private int paymentCurrency;
    
    /**
	 * 面付/预付<br>
	 * 为了和酒店本部一致，这里采用字符串:<br>
	 * 1. "pay" : 面付<br>
	 * 2. "pre_pay" : 预付<br>
	 * 
	 * @see PayMethod类
	 */
    private String payMethod;
    
    /**
	 * 预付时限
	 */
    private Date prepayLimitDate;
    
    /**
	 * 撤单原因 
	 */
    private int cancelReason;
    
    /**
	 * 担保币种
	 */
    private int suretyCurrency;
    
    /**
	 * 订单创建时间
	 */
    private Date createDate;
    
    /**
	 * 创建人登陆工号
	 */
    private Long creator;
    
    /**
	 * 创建人中英文姓名
	 */
    private String creatorName;
    
    /**
	 * 修改人登陆工号
	 */
    private Long modifier;
    
    /**
	 * 最后修改人中英文姓名
	 */
    private String modifierName;
    
    /**
	 * 最后修改人所属部门，角色等
	 */
    private String modifierRole;
    
    /**
	 * 分配给中台操作人员
	 */
    private String assignTo;
    
    /**
	 * 中英文，如 (丁争 Terry)便于查询，既能查中文又能查英文）
	 */
    private String assignToName;
    
    /**
	 * 订单最后修改时间
	 */
    private Date modifiedTime;
    
    /**
	 * 酒店确认标志
	 */
    private boolean hotelConfirm;
    
    /**
	 * 客户确认标志
	 */
    private boolean customerConfirm;    
    
    /**
	 * 客人确认方式
	 * 
	 * @see ConfirmType
	 */
    private int confirmType;
    
    /**
	 * 总金额
	 */
    private double sum;
    
    /**
	 * 是否虚假订单（现在没用，保留）
	 */
    private boolean illusive = false;
    
    /**
	 * （现在没用，保留，用于unicall一个电话呼入）
	 */
    private String taskId;
    
    /**
	 * 撤单原因为其他时，为手工输入
	 */
    private String cancelMessage;
    
    /**
	 * 撤单原因为客人原因时
	 */
    private String guestCancelMessage;    
    
    /**
	 * VIP级别。<br>
	 * 从会员里面来 0表示非VIP
	 */
    private int vipLevel;
    
    /**
	 * 是否锁定
	 */
    private boolean isLock;
    
    /**
	 * 当前订单锁定人
	 */
    private Long locker;
    
    /**
	 * 代理商号，如果为N则表示不是代理商定单<br>　
	 * member表中有代理商编号的都认为是代理商
	 */
    private String agentCode;
    
    /**
	 * 是否手工订单
	 */
    private boolean isManualOrder;
    
    /**
	 * 订单中台分类类型（预付单、担保单、114单、散客单，香港，疑难）
	 * 
	 * @see HraOrderType
	 */
    private int hraOrderType;
    
    /**
	 * 对换比率ID
	 */
    private int rateId;
    
    /**
	 * 转换为人民币后的支付总金额
	 */
    private double sumRmb;
    
    /**
	 * 预留日期
	 */
    private Date reservedDate;
    
    /**
	 * 联系人
	 */
    private String linkMan;
    
    /**
	 * 联系人称呼
	 */
    private String title;
    
    /**
	 * 手机
	 */
    private String mobile;
    
    /**
	 * 电话号码
	 */
    private String telephone;
    
    /**
	 * 传真
	 */
    private String customerFax;
    
    /**
	 * 电子邮箱
	 */
    private String email;
    
    /**
	 * 去酒店坐什么交通工具（如飞机）
	 */
    private String arrivalTraffic;
    
    /**
	 * 车次（航班）
	 */
    private String flight;
    
    /**
	 * 最早到店时间
	 */
    private String arrivalTime;
    
    /**
	 * 最晚到店时间
	 */
    private String latestArrivalTime;
    
    /**
	 * 中英文，包括所有入住人。便于查询
	 */
    private String fellowNames;
    
    /**
	 * 已发送酒店确认传真（包括修改传真）确认和修改其实是一个概念，<br>
	 * 都是要发送传真等酒店确认。修改是重新确认
	 */
    private boolean sendedHotelFax;
    
    /**
	 * 已满足配额
	 */
    private boolean quotaOk;
    
    /**
	 * 会员所在的省份（用于114区分不同省份）
	 */
    private String memberState;
    
    /**
	 * 114序列号
	 */
    private String sequence114;
    
    /**
	 * 114订单前台取消标识
	 */
    private boolean frontCancel;
    
    /**
	 * 是否需要信用卡担保
	 */
    private boolean isCreditAssured;

    /**
	 * 会员要求使用的卡IDS可多选，现在只有114有设置该值
	 */
    private String creditCardIdsSelect;
    
    /**
	 * 确认金额
	 */
    private double confirmTotal;
    
    /**
	 * 结算金额
	 */
    private double balanceTotal;
    
    /**
	 * 面付转预付标识
	 */
    private boolean payToPrepay;
    
    /**
	 * 预付金额
	 */
    private double prepayTotalRmb;
    
    /**
	 * 是否需要退款
	 */
    private boolean needRefund;
    
    /**
	 * 退款原因
	 */
    private String refundMessage;
    
    /**
	 * 是否已退款
	 */
    private boolean hasRefund;
    
    /**
	 * 退款时限
	 */
    private Date refundLimitTime;
    
    /**
	 * 是否单结酒店（单结酒店的，如果已给酒店钱，那么不能退款）
	 */
    private boolean singleBalance;
    
    /**
	 * 是否需要付款给供应商
	 */
    private boolean payToHotel;
    
    /**
	 * 已付款给供应商（单结的酒店需要汇款至酒店）
	 */
    private boolean payToHotelOk;
    
    /**
	 * 退款单领导审批已通过
	 */
    private boolean refundBillAuditPass;
    
    /**
	 * 退款单财务执行完毕
	 */
    private boolean refundDoneForFinance;
    
    /**
	 * 酒店给我们退款状态
	 */
    private int refundStateForHotel;
    
    /**
	 * 用于酒店回传绑定ID，收到的酒店传真JPG图片
	 */
    private String fax;
    
    /**
	 * 支付比率　一个月只出现几张这种单，有面付也有预付
	 */
    private String paymentRate;
    
    /**
	 * 预付完成
	 */
    private boolean hasPrepayed;
    
    /**
	 * 已口头确认酒店
	 */
    private boolean hotelConfirmTel;
    
    /**
	 * 已书面确认酒店
	 */
    private boolean hotelConfirmFax;
    
    /**
	 * 已收回传
	 */
    private boolean hotelConfirmFaxReturn;
    
    /**
	 * 已发送客户确认(包括修改)
	 */
    private boolean sendedMemberFax;
    
    /**
	 * 客人特殊要求
	 */
    private String specialRequest;        
    
    /**
	 * 是否属于被交接班订单
	 */
    private boolean inNextTeam;
    
    /**
	 * 会员CD
	 */
    private String memberCd;
    
    /**
	 * noshow原因
	 */
    private String noshowReason;
    
    /**
	 * 特别说明
	 */
    private String specialNote;
    
    /**
	 * 日审操作状态(未操作/已保存/待审核/完成)
	 */
    private int auditOpState;
    
    /**
	 * 是否继续保持在中台
	 */
    private boolean isStayInMid;
    
    /**
	 * 床型
	 * 
	 * @see BedType
	 */
    private int bedType;
    
    /**
	 * 子房型名称
	 */
    private String childRoomTypeName;
    
    /**
	 * 酒店星级
	 */
    private float hotelStar;
    
    /**
	 * 城市
	 */
    private String city;
    
    /**
	 * 订单提交到中台的时间
	 */
    private Date toMidTime;
    
    /**
	 * 对于面付转预付单，当给酒店发传真时，该字段的值决定
	 * 显示底价还是销售价
	 */
    private boolean showBasePrice;
    
    /**
	 * 配额是否可退
	 */
    private boolean quotaCanReturn;
    
    /**
	 * 酒店确认号
	 */
    private String hotelConfirmNo;
        
    /**
	 * 配送ID <fk> 和OrFulfillment关联
	 */
    private OrFulfillment fulfill;
    
    /**
	 * 和OrPayment关联
	 */
    private List paymentList;        
    
    
    /** getter and setter begin */

    public boolean isStayInMid() {
        return this.isStayInMid;
        /*return (orderState >= OrderState.SUBMIT_TO_MID &&
				!(hotelConfirmTel && hotelConfirmFax && sendedMemberFax
						&& quotaOk));*/
    }    
    
    public boolean getIsStayInMid() {
        return this.isStayInMid;
    }
    
    public void setIsStayInMid(boolean isStayInMid) {
        this.isStayInMid = isStayInMid;
    }

    public void setStayInMid(boolean isStayInMid) {
        this.isStayInMid = isStayInMid;
    }    
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public boolean isShowBasePrice() {
        return showBasePrice;
    }

    public void setShowBasePrice(boolean showBasePrice) {
        this.showBasePrice = showBasePrice;
    }

    public Date getToMidTime() {
        return toMidTime;
    }

    public void setToMidTime(Date toMidTime) {
        this.toMidTime = toMidTime;
    }

    public float getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(float hotelStar) {
        this.hotelStar = hotelStar;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public boolean isQuotaCanReturn() {
        return quotaCanReturn;
    }

    public void setQuotaCanReturn(boolean quotaCanReturn) {
        this.quotaCanReturn = quotaCanReturn;
    }

    public Date getAllowCancelTime() {
        return allowCancelTime;
    }

    public void setAllowCancelTime(Date allowCancelTime) {
        this.allowCancelTime = allowCancelTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivalTraffic() {
        return arrivalTraffic;
    }

    public void setArrivalTraffic(String arrivalTraffic) {
        this.arrivalTraffic = arrivalTraffic;
    }

    public String getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public String getAssignToName() {
        return assignToName;
    }

    public void setAssignToName(String assignToName) {
        this.assignToName = assignToName;
    }

    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    public double getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(double balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    public int getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(int cancelReason) {
        this.cancelReason = cancelReason;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
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

    public Long getChildRoomTypeId() {
        return childRoomTypeId;
    }

    public void setChildRoomTypeId(Long childRoomTypeId) {
        this.childRoomTypeId = childRoomTypeId;
    }

    public double getConfirmTotal() {
        return confirmTotal;
    }

    public void setConfirmTotal(double confirmTotal) {
        this.confirmTotal = confirmTotal;
    }

    public int getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(int confirmType) {
        this.confirmType = confirmType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreditCardIdsSelect() {
        return creditCardIdsSelect;
    }

    public void setCreditCardIdsSelect(String creditCardIdsSelect) {
        this.creditCardIdsSelect = creditCardIdsSelect;
    }

    public boolean isCustomerConfirm() {
        return customerConfirm;
    }

    public void setCustomerConfirm(boolean customerConfirm) {
        this.customerConfirm = customerConfirm;
    }

    public String getCustomerFax() {
        return customerFax;
    }

    public void setCustomerFax(String customerFax) {
        this.customerFax = customerFax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmergencyLevel() {
        return emergencyLevel;
    }

    public void setEmergencyLevel(int emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFellowNames() {
        return fellowNames;
    }

    public void setFellowNames(String fellowNames) {
        this.fellowNames = fellowNames;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public boolean isFrontCancel() {
        return frontCancel;
    }

    public void setFrontCancel(boolean frontCancel) {
        this.frontCancel = frontCancel;
    }

    public String getFulfillmentCD() {
        return fulfillmentCD;
    }

    public void setFulfillmentCD(String fulfillmentCD) {
        this.fulfillmentCD = fulfillmentCD;
    }

    public boolean isHasRefund() {
        return hasRefund;
    }

    public void setHasRefund(boolean hasRefund) {
        this.hasRefund = hasRefund;
    }


    public boolean isHotelConfirm() {
        return hotelConfirm;
    }

    public void setHotelConfirm(boolean hotelConfirm) {
        this.hotelConfirm = hotelConfirm;
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

    public int getHraOrderType() {
        return hraOrderType;
    }

    public void setHraOrderType(int hraOrderType) {
        this.hraOrderType = hraOrderType;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long id) {
        ID = id;
    }

    public boolean isIllusive() {
        return illusive;
    }

    public boolean getIsIllusive() {
        return illusive;
    }
    
    public void setIllusive(boolean illusive) {
        this.illusive = illusive;
    }
    
    public void setIsIllusive(boolean illusive) {
        this.illusive = illusive;
    }

    public boolean isCreditAssured() {
        return isCreditAssured;
    }

    public boolean getIsCreditAssured() {
        return isCreditAssured;
    }
    
    public void setCreditAssured(boolean isCreditAssured) {
        this.isCreditAssured = isCreditAssured;
    }
    
    public void setIsCreditAssured(boolean isCreditAssured) {
        this.isCreditAssured = isCreditAssured;
    }

    public boolean isLock() {
        return isLock;
    }
    
    public boolean getIsLock() {
        return isLock;
    }

    public void setLock(boolean isLock) {
        this.isLock = isLock;
    }
    
    public void setIsLock(boolean isLock) {
        this.isLock = isLock;
    }

    public boolean isManualOrder() {
        return isManualOrder;
    }
    
    public boolean getIsManualOrder() {
        return isManualOrder;
    }

    public void setManualOrder(boolean isManualOrder) {
        this.isManualOrder = isManualOrder;
    }
    
    public void setIsManualOrder(boolean isManualOrder) {
        this.isManualOrder = isManualOrder;
    }

    public String getLatestArrivalTime() {
        return latestArrivalTime;
    }

    public void setLatestArrivalTime(String latestArrivalTime) {
        this.latestArrivalTime = latestArrivalTime;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public Long getLocker() {
        return locker;
    }

    public void setLocker(Long locker) {
        this.locker = locker;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }


    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
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

    public boolean isNeedRefund() {
        return needRefund;
    }

    public void setNeedRefund(boolean needRefund) {
        this.needRefund = needRefund;
    }

    public String getOrderCD() {
        return orderCD;
    }

    public void setOrderCD(String orderCD) {
        this.orderCD = orderCD;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getOriginCD() {
        return originCD;
    }

    public void setOriginCD(String originCD) {
        this.originCD = originCD;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public int getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(int paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public List getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List paymentList) {
        this.paymentList = paymentList;
    }

    public String getPaymentRate() {
        return paymentRate;
    }

    public void setPaymentRate(String paymentRate) {
        this.paymentRate = paymentRate;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public int getPayState() {
        return payState;
    }

    public void setPayState(int payState) {
        this.payState = payState;
    }

    public boolean isPayToHotel() {
        return payToHotel;
    }

    public void setPayToHotel(boolean payToHotel) {
        this.payToHotel = payToHotel;
    }

    public boolean isPayToHotelOk() {
        return payToHotelOk;
    }

    public void setPayToHotelOk(boolean payToHotelOk) {
        this.payToHotelOk = payToHotelOk;
    }

    public boolean isPayToPrepay() {
        return payToPrepay;
    }

    public void setPayToPrepay(boolean payToPrepay) {
        this.payToPrepay = payToPrepay;
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

    public double getPrepayTotalRmb() {
        return prepayTotalRmb;
    }

    public void setPrepayTotalRmb(double prepayTotalRmb) {
        this.prepayTotalRmb = prepayTotalRmb;
    }

    public int getPrepayType() {
        return prepayType;
    }

    public void setPrepayType(int prepayType) {
        this.prepayType = prepayType;
    }

    public boolean isQuotaOk() {
        return quotaOk;
    }

    public void setQuotaOk(boolean quotaOk) {
        this.quotaOk = quotaOk;
    }

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public boolean isRefundBillAuditPass() {
        return refundBillAuditPass;
    }

    public void setRefundBillAuditPass(boolean refundBillAuditPass) {
        this.refundBillAuditPass = refundBillAuditPass;
    }

    public boolean isRefundDoneForFinance() {
        return refundDoneForFinance;
    }

    public void setRefundDoneForFinance(boolean refundDoneForFinance) {
        this.refundDoneForFinance = refundDoneForFinance;
    }

    public Date getRefundLimitTime() {
        return refundLimitTime;
    }

    public void setRefundLimitTime(Date refundLimitTime) {
        this.refundLimitTime = refundLimitTime;
    }

    public String getRefundMessage() {
        return refundMessage;
    }

    public void setRefundMessage(String refundMessage) {
        this.refundMessage = refundMessage;
    }

    public int getRefundStateForHotel() {
        return refundStateForHotel;
    }

    public void setRefundStateForHotel(int refundStateForHotel) {
        this.refundStateForHotel = refundStateForHotel;
    }

    public Date getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(Date reservedDate) {
        this.reservedDate = reservedDate;
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

    public boolean isSendedHotelFax() {
        return sendedHotelFax;
    }

    public void setSendedHotelFax(boolean sendedHotelFax) {
        this.sendedHotelFax = sendedHotelFax;
    }

    public String getSequence114() {
        return sequence114;
    }

    public void setSequence114(String sequence114) {
        this.sequence114 = sequence114;
    }

    public boolean isSingleBalance() {
        return singleBalance;
    }

    public void setSingleBalance(boolean singleBalance) {
        this.singleBalance = singleBalance;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getSumRmb() {
        return sumRmb;
    }

    public void setSumRmb(double sumRmb) {
        this.sumRmb = sumRmb;
    }

    public int getSuretyCurrency() {
        return suretyCurrency;
    }

    public void setSuretyCurrency(int suretyCurrency) {
        this.suretyCurrency = suretyCurrency;
    }

    public double getSuretyPrice() {
        return suretyPrice;
    }

    public void setSuretyPrice(double suretyPrice) {
        this.suretyPrice = suretyPrice;
    }

    public int getSuretyState() {
        return suretyState;
    }

    public void setSuretyState(int suretyState) {
        this.suretyState = suretyState;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }
    
    public String getOrderCDHotel() {
        return orderCDHotel;
    }

    public void setOrderCDHotel(String orderCDHotel) {
        this.orderCDHotel = orderCDHotel;
    }
    
    public boolean isHasPrepayed() {
        return hasPrepayed;
    }

    public void setHasPrepayed(boolean hasPrepayed) {
        this.hasPrepayed = hasPrepayed;
    }
    
    public boolean isHotelConfirmFax() {
        return hotelConfirmFax;
    }

    public void setHotelConfirmFax(boolean hotelConfirmFax) {
        this.hotelConfirmFax = hotelConfirmFax;
    }

    public boolean isHotelConfirmTel() {
        return hotelConfirmTel;
    }

    public void setHotelConfirmTel(boolean hotelConfirmTel) {
        this.hotelConfirmTel = hotelConfirmTel;
    }

    public boolean isHotelConfirmFaxReturn() {
        return hotelConfirmFaxReturn;
    }

    public void setHotelConfirmFaxReturn(boolean hotelConfirmFaxReturn) {
        this.hotelConfirmFaxReturn = hotelConfirmFaxReturn;
    }

    public boolean isSendedMemberFax() {
        return sendedMemberFax;
    }

    public void setSendedMemberFax(boolean sendedMemberFax) {
        this.sendedMemberFax = sendedMemberFax;
    }
    
    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }
    
    public boolean isInNextTeam() {
        return inNextTeam;
    }

    public void setInNextTeam(boolean inNextTeam) {
        this.inNextTeam = inNextTeam;
    }
    
    public String getMemberCd() {
        return memberCd;
    }

    public void setMemberCd(String memberCd) {
        this.memberCd = memberCd;
    }
    
    public int getAuditOpState() {
        return auditOpState;
    }

    public void setAuditOpState(int auditOpState) {
        this.auditOpState = auditOpState;
    }

    public String getNoshowReason() {
        return noshowReason;
    }

    public void setNoshowReason(String noshowReason) {
        this.noshowReason = noshowReason;
    }

    public String getSpecialNote() {
        return specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }
    
    public int getBedType() {
        return bedType;
    }

    public void setBedType(int bedType) {
        this.bedType = bedType;
    }
    
    public String getChildRoomTypeName() {
        return childRoomTypeName;
    }

    public void setChildRoomTypeName(String childRoomTypeName) {
        this.childRoomTypeName = childRoomTypeName;
    }

    public OrFulfillment getFulfill() {
        return fulfill;
    }

    public String getGuestCancelMessage() {
        return guestCancelMessage;
    }

    public void setGuestCancelMessage(String guestCancelMessage) {
        this.guestCancelMessage = guestCancelMessage;
    }

    public void setFulfill(OrFulfillment fulfill) {
        this.fulfill = fulfill;
    }
    
    /** end of getter and setter */
    
    /**
	 * 获取订单的预付金额明细
	 */
    public static String getPrepayDetail(OrOrderVO order) {
//		StringBuffer buffer = new StringBuffer("");		
//
//		List list = order.getPaymentList();
//		for(int i=0; i<list.size(); i++) {
//			PaymentVO payment = (PaymentVO)list.get(i);
//			if(i == 0) {
//				buffer.append("(其中");
//			}
//			if(i < 0) {
//				buffer.append(",");
//			}
//			buffer.append(PrepayType.payStrMap.get(payment.getPayType()))
//				.append(payment.getMoney()).append("元");
//		}	
//		if(list.size() > 0) {
//			buffer.append(")");
//		}
//		
//		return buffer.toString();

        StringBuffer buffer = new StringBuffer("");
        OrFulfillment fulfill = order.getFulfill();
        double leftFee = order.getLeftFee();
        double ptMoney = getOrderPoint(order);
        double prepayNotFul = getPrepayNotFul(order);
        buffer.append("(配送费: ")
        .append((null != fulfill ? fulfill.getAccount() : 0.0))
        .append("元, 订单金额: ")
        .append(order.getSumRmb())
//		.append("元, 预付应付金额: ")
//		.append(order.getPrepayTotalRmb())
        .append("元, 积分支付: ")
        .append(ptMoney)
        .append("元, 预付金额: ")
        .append(prepayNotFul)
        .append("元, 退房手续费: ")
        .append(leftFee)
        .append(")");
            
        return buffer.toString();
    }
    
    /**
	 * 获取订单退房费
	 */
    public double getLeftFee() {
        if(orderState == OrderState.REFUND_SUCCESS) {
            return getPrepayTotalRmb() - getRefund(); 
        }
        return 0.0;        
    }
    
    private static double getOrderPoint(OrOrderVO order) {
        List list = order.getPaymentList();
        for(int i=0; i<list.size(); i++) {
            PaymentVO payment = (PaymentVO)list.get(i);
            if(payment.getPayType() == PrepayType.Points) {
                return payment.getMoney();
            }
        }    
        return 0.0;
    }
    
    private static double getPrepayFul(OrOrderVO order) {
        double total = 0.0;
        List list = order.getPaymentList();
        for(int i=0; i<list.size(); i++) {
            PaymentVO payment = (PaymentVO)list.get(i);
            if(payment.getPayType() == PrepayType.Cash || 
                    payment.getPayType() == PrepayType.POS) {
                total += payment.getMoney();
            }
        }
        return total;    
    }
    
    private static double getPrepayNotFul(OrOrderVO order) {
        double total = 0.0;
        List list = order.getPaymentList();
        for(int i=0; i<list.size(); i++) {
            PaymentVO payment = (PaymentVO)list.get(i);
            int payType = payment.getPayType();
            if(payType != PrepayType.Cash && 
                    payType != PrepayType.POS && 
                    payType != PrepayType.Points || 
                    (payType > PrepayType.POS && payment.isPaySucceed())) {                
                total += payment.getMoney();
            }
        }
        return total;    
    }
    
    /**
	 * 获取订单的配送金额明细
	 */
    public String getFeeDetail() {
        StringBuffer buffer = new StringBuffer("");        
        double leftFee = getLeftFee();
        double ptMoney = getOrderPoint(this);
        double prepayNotFul = getPrepayNotFul(this);
        double fulFee = getPrepayFul(this) + (null != fulfill ? 
                fulfill.getAccount() : 0.0) - leftFee;
        buffer.append("配送应收金额: ")
        .append(fulFee)
        .append("RMB (配送费: ")
        .append((null != fulfill ? fulfill.getAccount() : 0.0))
        .append("元, 订单金额: ")
        .append(getSumRmb())
//		.append("元, 预付应付金额: ")
//		.append(getPrepayTotalRmb())
        .append("元, 积分支付: ")
        .append(ptMoney)
        .append("元, 预付金额: ")
        .append(prepayNotFul)
        .append("元, 退房手续费: ")
        .append(leftFee)
        .append(")");
            
        return buffer.toString();
    }
    
    /**
	 * 获取配送应收金额
	 * @return
	 */
    public double getFeeReceive() {
        double leftFee = getLeftFee();
        double fulFee = getPrepayFul(this) + (null != fulfill ? 
                fulfill.getAccount() : 0.0) - leftFee;
        return fulFee;
    }

    /**
	 * 发票项目
	 * @return
	 */
    public String getFulItem() {
        return "代酒店收费";
    }
    
    private static String getCancelReasonByCode(int reason) {    
        if(1 == reason) {
            return "已下新单";
        }
        if(2 == reason) {
            return "客人原因";
        }        
        if(3 == reason) {
            return "回复慢";
        }
        if(4 == reason) {
            return "比竞争对手贵";
        }
        if(5 == reason) {
            return "价格倒挂";
        }
        if(6 == reason) {
            return "酒店设施原因";
        }
        if(7 == reason) {
            return "酒店设施原因NR";
        }
        if(8 == reason) {
            return "TC重复下单";
        }        
        if(9 == reason) {
            return "TC错误下单";
        }
        if(10 == reason) {
            return "满房";
        }
        if(11 == reason) {
            return "满房NR";
        }
        if(12 == reason) {
            return "酒店调价";
        }
        if(13 == reason) {
            return "酒店调价NR";
        }
        if(14 == reason) {
            return "No-show取消";
        }        
        if(15 == reason) {
            return "酒店查不到合同";
        }
        if(16 == reason) {
            return "测试单";
        }
        if(17 == reason) {
            return "其他";
        }
        return "";        
    }
    
    /**
	 * 获取取消原因字符串
	 * @return
	 */
    public String getCancelReasonStr() {
        if(orderState != OrderState.CANCEL) {
            return "";    
        }
        
        return getCancelReasonByCode(getCancelReason());        
    }
    
    private static String getCancelNoteStr(OrOrderVO order) {
        int reason = order.getCancelReason();        
        if(2 == reason) {
            String guest = order.getGuestCancelMessage();
            if(guest.equals("1")) {
                return "行程改变";
            }
            if(guest.equals("2")) {
                return "无法提供担保";
            }
            if(guest.equals("3")) {
                return "通过其他途径预定";
            }
            if(guest.equals("4")) {
                return "酒店位置不合适";
            }
            if(guest.equals("5")) {
                return "天气原因";
            }
            if(guest.equals("6")) {
                return "客人重复/错误下单";
            }
            if(guest.equals("7")) {
                return "网站无效单";
            }
            if(guest.equals("8")) {
                return "不愿告知原因";
            }
            if(guest.equals("9")) {
                return "其他";
            }
        }
        
        if(17 == reason) {
            return order.getCancelMessage();
        }
        
        return "";
    }
    
    /**
	 * 获取取消备注字符串
	 * @return
	 */
    public String getCancelNote() {
        return getCancelNoteStr(this);
    }

    public String getHotelConfirmNo() {
        return hotelConfirmNo;
    }

    public void setHotelConfirmNo(String hotelConfirmNo) {
        this.hotelConfirmNo = hotelConfirmNo;
    }
    
}

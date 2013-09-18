package com.mangocity.hotel.orderrecord.model;

import java.util.Date;
/**
 * 订单日志
 * @author zhouna
 *
 */
public class OrderRecord {
	private Long recordId;//记录id
	private Long actionId;//动作id
	private String source;//订单来源
	private String projectCode;//客源渠道
	private String supplierChannel;//供应商渠道
	private Long hotelId;//酒店id
	private Long roomTypeId;//房型id
	private Long priceTypeId;//价格类型
	private Double oneRoomPrice;//单间售价
	private Double basePrice;//单间底价
	/**
	 * 对于网站：
	 * 5：订单填写页
	 * 6：支付页
	 * 7：完成页
	 * 8：在线支付页
	 * 9：跳网关页：
	 * 
	 */
	private Integer currentStep;//当前步骤
	private String ororderCd;//订单CD
	private Double orderPriceSumConctract;//订单总额（合同币种）
	private Double orderPriceSumPay;//订单总额（RMB）
	private Double realPaymoney;//现金支付金额
	private String pointMoney;//使用积分额
	private String ulmMoney;//使用代金券金额
	private String payMethod;//支付方式
	private String contractCurrency;//订单币种
	private String payCurrency;//支付币种
	private Double returnCashMoney;//返现金额（RMB）
	private Integer isNeedAssue;//是否需要担保；
	private Long memberId;//会员id
	private String memberCd;//会员cd(会员三期)
	private String customerIp;//客人ip；
	private Integer bedType;//床型
	private Date checkinDate;//入住时间
	private Date checkoutDate;//离店时间
	private String roomQuantity;//预订房间数
	private String arriveTime;//到店时间
	private String lastArriveTime;//最晚到店时间
	private Date createDate;//创建日期
	private String creatorName;//创建者
	private String creatorId;//创建者工号
	private String apacheSessionId;//apache会话ID

	public String getApacheSessionId() {
		return apacheSessionId;
	}
	public void setApacheSessionId(String apacheSessionId) {
		this.apacheSessionId = apacheSessionId;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getSupplierChannel() {
		return supplierChannel;
	}
	public void setSupplierChannel(String supplierChannel) {
		this.supplierChannel = supplierChannel;
	}
	public Long getHotelId() {
		return hotelId;
	}
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}
	public Long getRoomTypeId() {
		return roomTypeId;
	}
	public void setRoomTypeId(Long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}
	public Long getPriceTypeId() {
		return priceTypeId;
	}
	public void setPriceTypeId(Long priceTypeId) {
		this.priceTypeId = priceTypeId;
	}
	public Double getOneRoomPrice() {
		return oneRoomPrice;
	}
	public void setOneRoomPrice(Double oneRoomPrice) {
		this.oneRoomPrice = oneRoomPrice;
	}
	public Double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}
	public Integer getCurrentStep() {
		return currentStep;
	}
	public void setCurrentStep(Integer currentStep) {
		this.currentStep = currentStep;
	}
	public String getOrorderCd() {
		return ororderCd;
	}
	public void setOrorderCd(String ororderCd) {
		this.ororderCd = ororderCd;
	}
	public Double getOrderPriceSumConctract() {
		return orderPriceSumConctract;
	}
	public void setOrderPriceSumConctract(Double orderPriceSumConctract) {
		this.orderPriceSumConctract = orderPriceSumConctract;
	}
	public Double getOrderPriceSumPay() {
		return orderPriceSumPay;
	}
	public void setOrderPriceSumPay(Double orderPriceSumPay) {
		this.orderPriceSumPay = orderPriceSumPay;
	}
	public Double getRealPaymoney() {
		return realPaymoney;
	}
	public void setRealPaymoney(Double realPaymoney) {
		this.realPaymoney = realPaymoney;
	}
	public String getPointMoney() {
		return pointMoney;
	}
	public void setPointMoney(String pointMoney) {
		this.pointMoney = pointMoney;
	}
	public String getUlmMoney() {
		return ulmMoney;
	}
	public void setUlmMoney(String ulmMoney) {
		this.ulmMoney = ulmMoney;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	public String getContractCurrency() {
		return contractCurrency;
	}
	public void setContractCurrency(String contractCurrency) {
		this.contractCurrency = contractCurrency;
	}
	public String getPayCurrency() {
		return payCurrency;
	}
	public void setPayCurrency(String payCurrency) {
		this.payCurrency = payCurrency;
	}
	public Double getReturnCashMoney() {
		return returnCashMoney;
	}
	public void setReturnCashMoney(Double returnCashMoney) {
		this.returnCashMoney = returnCashMoney;
	}
	public Integer getIsNeedAssue() {
		return isNeedAssue;
	}
	public void setIsNeedAssue(Integer isNeedAssue) {
		this.isNeedAssue = isNeedAssue;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getMemberCd() {
		return memberCd;
	}
	public void setMemberCd(String memberCd) {
		this.memberCd = memberCd;
	}
	public String getCustomerIp() {
		return customerIp;
	}
	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}
	public Integer getBedType() {
		return bedType;
	}
	public void setBedType(Integer bedType) {
		this.bedType = bedType;
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
	public String getRoomQuantity() {
		return roomQuantity;
	}
	public void setRoomQuantity(String roomQuantity) {
		this.roomQuantity = roomQuantity;
	}
	public String getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}
	public String getLastArriveTime() {
		return lastArriveTime;
	}
	public void setLastArriveTime(String lastArriveTime) {
		this.lastArriveTime = lastArriveTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}

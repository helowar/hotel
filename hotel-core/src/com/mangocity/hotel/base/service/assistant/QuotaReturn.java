package com.mangocity.hotel.base.service.assistant;

import java.io.Serializable;
import java.util.Date;

/**
 */
public class QuotaReturn implements Serializable {

	private static final long serialVersionUID = -7956358542333020040L;

	// 酒店id
	private long hotelId;

	// 房型id
	private long roomTypeId;

	// 价格类型
	private long childRoomTypeId;

	// 床型ID
	private long bedId;

	// 支付方式
	private String payMethod;

	// 会员类型(TMC,CC,TP)
	private int memberType;

	// 被扣配额的所属日期
	private Date quotaDate;

	// 扣配额日期
	private Date useQuotaDate;

	// 扣配额数量
	private int quotaNum;

	// 所扣配额类型( 包房配额2，普通配额1，临时配额3,呼出配额4)
	private String quotaType;

	// 配额是否存在共享的
	private Long quotaShare;

	// 配额模式(如果为S-I在店每天都扣，如果为C-I进店只扣第一天)
	private String quotaPattern;

	// 配额是否可退 0为不可退 1为可退
	private boolean takebackQuota;
	
    // 底价
    private double basePrice;

    // 门市价
    private double salesroomPrice;

    // 销售价
    private double salePrice;

    // 房态
    private String roomState;
    
	// 操作部门,可以为空
	private String operatorDept;
	
	// 操作人(会员名称)
	private String operatorName;
	
	// 操作ID(会员ID)
	private String operatorId;

	// 扣退配额成功标志(0扣成功 1扣失败 2退成功 3退失败)
	private int sign;

	public long getHotelId() {
		return hotelId;
	}

	public void setHotelId(long hotelId) {
		this.hotelId = hotelId;
	}

	public long getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public long getChildRoomTypeId() {
		return childRoomTypeId;
	}

	public void setChildRoomTypeId(long childRoomTypeId) {
		this.childRoomTypeId = childRoomTypeId;
	}

	public long getBedId() {
		return bedId;
	}

	public void setBedId(long bedId) {
		this.bedId = bedId;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public int getMemberType() {
		return memberType;
	}

	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}

	public Date getQuotaDate() {
		return quotaDate;
	}

	public void setQuotaDate(Date quotaDate) {
		this.quotaDate = quotaDate;
	}

	public Date getUseQuotaDate() {
		return useQuotaDate;
	}

	public void setUseQuotaDate(Date useQuotaDate) {
		this.useQuotaDate = useQuotaDate;
	}

	public int getQuotaNum() {
		return quotaNum;
	}

	public void setQuotaNum(int quotaNum) {
		this.quotaNum = quotaNum;
	}

	public String getQuotaType() {
		return quotaType;
	}

	public void setQuotaType(String quotaType) {
		this.quotaType = quotaType;
	}

	public String getQuotaPattern() {
		return quotaPattern;
	}

	public void setQuotaPattern(String quotaPattern) {
		this.quotaPattern = quotaPattern;
	}

	public boolean isTakebackQuota() {
		return takebackQuota;
	}

	public void setTakebackQuota(boolean takebackQuota) {
		this.takebackQuota = takebackQuota;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}



	public Long getQuotaShare() {
		return quotaShare;
	}

	public void setQuotaShare(Long quotaShare) {
		this.quotaShare = quotaShare;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public double getSalesroomPrice() {
		return salesroomPrice;
	}

	public void setSalesroomPrice(double salesroomPrice) {
		this.salesroomPrice = salesroomPrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public String getRoomState() {
		return roomState;
	}

	public void setRoomState(String roomState) {
		this.roomState = roomState;
	}

	public String getOperatorDept() {
		return operatorDept;
	}

	public void setOperatorDept(String operatorDept) {
		this.operatorDept = operatorDept;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

}

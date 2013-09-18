package com.mangocity.hotel.dreamweb.ordercancel.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mangocity.hotel.order.persistence.OrHandleLog;

public class OrderCancel implements Serializable {

	/**
	 *  自动生成的可序列号版本id
	 */
	private static final long serialVersionUID = -6599508133565460300L;
	
	//数据库记录id
	private Long Id;
	
	// 订单id
	private Long orderId;
	
	// 订单cd
	private String orderCD;
	
	// 酒店id
	private Long hotelId;
	
	// 酒店名称
	private String hotelName;
	
	// 会员id
	private Long memberId;
	
	// 会员cd
	private String memberCD;
	
	// 会员名称
	private String memberName;
	
	// 原因代码
	private String reasonCD;
	
	// 原因内容
	private String reason;
	
	// 创建时间
	private Date createDate;
	
	// 修改时间
	private Date modifyDate;
	
    /**
	 * 和OrHandleLog关联
	 */
    private List<OrHandleLog> logList;

	/**
	 * @return the orderId
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the orderCD
	 */
	public String getOrderCD() {
		return orderCD;
	}

	/**
	 * @param orderCD the orderCD to set
	 */
	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}

	/**
	 * @return the hotelId
	 */
	public Long getHotelId() {
		return hotelId;
	}

	/**
	 * @param hotelId the hotelId to set
	 */
	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	/**
	 * @return the hotelName
	 */
	public String getHotelName() {
		return hotelName;
	}

	/**
	 * @param hotelName the hotelName to set
	 */
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	/**
	 * @return the memberId
	 */
	public Long getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return the memberCD
	 */
	public String getMemberCD() {
		return memberCD;
	}

	/**
	 * @param memberCD the memberCD to set
	 */
	public void setMemberCD(String memberCD) {
		this.memberCD = memberCD;
	}

	/**
	 * @return the memberName
	 */
	public String getMemberName() {
		return memberName;
	}

	/**
	 * @param memberName the memberName to set
	 */
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	/**
	 * @return the reasonCD
	 */
	public String getReasonCD() {
		return reasonCD;
	}

	/**
	 * @param reasonCD the reasonCD to set
	 */
	public void setReasonCD(String reasonCD) {
		this.reasonCD = reasonCD;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the modifyDate
	 */
	public Date getModifyDate() {
		return modifyDate;
	}

	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return Id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Id = id;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
    public List<OrHandleLog> getLogList() {
    	if(null == logList) {
    		logList = new ArrayList<OrHandleLog>();
    	}
        return logList;
    }

    public void setLogList(List<OrHandleLog> logList) {
        this.logList = logList;
    }
}

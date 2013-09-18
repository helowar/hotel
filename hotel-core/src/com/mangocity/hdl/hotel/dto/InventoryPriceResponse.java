package com.mangocity.hdl.hotel.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "result",
    "message",
    "hotelId",
    "availableDate",
    "MemberRate",
    "roomTypeId",
    "ratePlanId",
    "roomInvStatusDateCode",
    "availableAmount"
})
@XmlRootElement(name = "InventoryPriceResponse")
public class InventoryPriceResponse  implements Serializable{
	
	private static final long serialVersionUID = 4023894214727423667L;
	
	/**
	 * 接口访问返回状态(1正常 0异常)
	 */
	private int result = 1;
	
	/**
	 * 异常返回信息
	 */
	private String message;

	/**
	 * 艺龙酒店ID
	 */
	private String hotelId; 
	
	
	/**
	 * 可售日期
	 */
	private Date availableDate;
	
	
	/**
	 * 会员价(售价)预订酒店价格，返回-1代表此房无价，无价和满房都不能进行预订
	 */
	private BigDecimal MemberRate;
	
	
	/**
	 * 艺龙房型ID
	 */
	private String roomTypeId;
	
	/**
	 * 艺龙价格类型ID	
	 */
	private String ratePlanId;
	
	/**
	 * 每一天能否预订的标记
	 * 1不可订、2可订
	 */
	private Integer roomInvStatusDateCode;
	
	/**
	 * 配额
	 */
	private Integer availableAmount;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}

	public Date getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(Date availableDate) {
		this.availableDate = availableDate;
	}

	public BigDecimal getMemberRate() {
		return MemberRate;
	}

	public void setMemberRate(BigDecimal memberRate) {
		MemberRate = memberRate;
	}

	public String getRoomTypeId() {
		return roomTypeId;
	}

	public void setRoomTypeId(String roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public String getRatePlanId() {
		return ratePlanId;
	}

	public void setRatePlanId(String ratePlanId) {
		this.ratePlanId = ratePlanId;
	}

	public Integer getRoomInvStatusDateCode() {
		return roomInvStatusDateCode;
	}

	public void setRoomInvStatusDateCode(Integer roomInvStatusDateCode) {
		this.roomInvStatusDateCode = roomInvStatusDateCode;
	}

	public Integer getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(Integer availableAmount) {
		this.availableAmount = availableAmount;
	}
	
}

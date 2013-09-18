package com.mangocity.hotel.base.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * 返现活动与酒店映射
 * @author xuyiwen
 *
 */
public class HtlFavourableHotel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -632692275052967665L;

	private Long id;
	
	/**
	 * 返现活动ID
	 */
	private Long favId;
	
	/**
	 * 酒店ID
	 */
	private Long hotelId;
	
	/**
	 * 用于软删除
	 */
	private int htlFlag;
	
	private Date createTime;
	
	private Date modifyTime;

	public Long getFavId() {
		return favId;
	}

	public void setFavId(Long favId) {
		this.favId = favId;
	}

	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public int getHtlFlag() {
		return htlFlag;
	}

	public void setHtlFlag(int htlFlag) {
		this.htlFlag = htlFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}

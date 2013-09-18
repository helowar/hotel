package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * 为电商统计网站来源订单用 与表HTL_INDEXPAGE_HBCOUNT关联
 * @author xuyiwen
 *
 */
public class HtlIndexPageHbCount {
	private Integer id;
	
	/**
	 * 日期
	 */
	private Date dateTime;
	
	/**
	 * 当天的订单量
	 */
	private Integer countNum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getCountNum() {
		return countNum;
	}

	public void setCountNum(Integer countNum) {
		this.countNum = countNum;
	}
	
	
}

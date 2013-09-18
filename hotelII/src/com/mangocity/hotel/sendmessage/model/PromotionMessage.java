package com.mangocity.hotel.sendmessage.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author liting 促销信息对象。机票、酒店订单完成页发送促销短信需求。
 */
public class PromotionMessage implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * 发送的产品，酒店为hotel，机票是flight
	 */
	private String product;

	/*
	 * 发送状态，0不用发送，1发送
	 */
	private int status;

	/**
	 * 发送信息内容
	 */
	private String content;

	/*
	 * 活动专辑代码号，5位数字
	 */
	private String funtionCode;

	/**
	 * 活动简单说明
	 */
	private String funtionRemark;
	
	/*
	 * 活动开始日期
	 */
	private Date beginDate;

	
	/*
	 * 活动结束日期
	 */
	private Date endDate;


	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFuntionCode() {
		return funtionCode;
	}

	public void setFuntionCode(String funtionCode) {
		this.funtionCode = funtionCode;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFuntionRemark() {
		return funtionRemark;
	}

	public void setFuntionRemark(String funtionRemark) {
		this.funtionRemark = funtionRemark;
	}

}

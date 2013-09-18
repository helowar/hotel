package com.mangocity.hotel.order.persistence;

import java.util.Date;

/**
 * 从callcenter接收到的短信 
 * @author MyEclipse Persistence Tools
 */

public class OrSMSRecv implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	private Long sequenceid;
	private Date recvtime;
	private Long status;//0未处理，1成功，-1失败
	private Date lastupdate;
	private String fromno;
	private String tomobile;
	private String content;
	private String operator;
	private String type;
	private String timechar;
	private String orderid;
	public Long getSequenceid() {
		return this.sequenceid;
	}

	public void setSequenceid(Long sequenceid) {
		this.sequenceid = sequenceid;
	}

	public Date getRecvtime() {
		return this.recvtime;
	}

	public void setRecvtime(Date recvtime) {
		this.recvtime = recvtime;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Date getLastupdate() {
		return this.lastupdate;
	}

	public void setLastupdate(Date lastupdate) {
		this.lastupdate = lastupdate;
	}

	public String getFromno() {
		return this.fromno;
	}

	public void setFromno(String fromno) {
		this.fromno = fromno;
	}

	public String getTomobile() {
		return this.tomobile;
	}

	public void setTomobile(String tomobile) {
		this.tomobile = tomobile;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTimechar() {
		return this.timechar;
	}

	public void setTimechar(String timechar) {
		this.timechar = timechar;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

}
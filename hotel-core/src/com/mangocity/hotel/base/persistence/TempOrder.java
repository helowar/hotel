package com.mangocity.hotel.base.persistence;

import java.util.Date;

/**
 * 
 * xieyanhui: Change to the actual description of this class
 * @version   Revision History
 * <pre>
 * Author     Version       Date        Changes
 * xieyanhui    1.0           Mar 26, 2013     Created
 *
 * </pre>
 * @since 1.
 */
public class TempOrder implements java.io.Serializable {

	private static final long serialVersionUID = 8562426840581379953L;
	private Long orderid;
	private Date assigntime;
	private Long grouptype;
	private Long assignstate;
	private Long relaxtime;
	private String baka;
	private String bakb;
	private String bakc;
	private String bakd;
	private Integer clienttype;

	public Long getOrderid() {
		return this.orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Date getAssigntime() {
		return this.assigntime;
	}

	public void setAssigntime(Date assigntime) {
		this.assigntime = assigntime;
	}

	public Long getGrouptype() {
		return this.grouptype;
	}

	public void setGrouptype(Long grouptype) {
		this.grouptype = grouptype;
	}

	public Long getAssignstate() {
		return this.assignstate;
	}

	public void setAssignstate(Long assignstate) {
		this.assignstate = assignstate;
	}

	public Long getRelaxtime() {
		return this.relaxtime;
	}

	public void setRelaxtime(Long relaxtime) {
		this.relaxtime = relaxtime;
	}

	public String getBaka() {
		return this.baka;
	}

	public void setBaka(String baka) {
		this.baka = baka;
	}

	public String getBakb() {
		return this.bakb;
	}

	public void setBakb(String bakb) {
		this.bakb = bakb;
	}

	public String getBakc() {
		return this.bakc;
	}

	public void setBakc(String bakc) {
		this.bakc = bakc;
	}

	public String getBakd() {
		return this.bakd;
	}

	public void setBakd(String bakd) {
		this.bakd = bakd;
	}

	public Integer getClienttype() {
		return this.clienttype;
	}

	public void setClienttype(Integer clienttype) {
		this.clienttype = clienttype;
	}

}
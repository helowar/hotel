package com.mangocity.hotel.base.persistence;

import java.util.Date;

import com.mangocity.util.Entity;

/**
 * 渠道返现控制实体
 */
public class QueryCashBackControl extends CEntity implements Entity {

    private Long ID;

    // 渠道名称
    private String projectname;
    
    // 渠道编码
    private String projectcode;

    // 渠道组名
    private String groupname;

    // 组编码
    private String groupcode;

    // 组反率是否有效
    private int groupstatus;

    // 返现率名称
    private String cashbackratename;

    // 返现率
    private double cashbackratevalue;

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getProjectcode() {
		return projectcode;
	}

	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getGroupcode() {
		return groupcode;
	}

	public void setGroupcode(String groupcode) {
		this.groupcode = groupcode;
	}

	public int getGroupstatus() {
		return groupstatus;
	}

	public void setGroupstatus(int groupstatus) {
		this.groupstatus = groupstatus;
	}

	public String getCashbackratename() {
		return cashbackratename;
	}

	public void setCashbackratename(String cashbackratename) {
		this.cashbackratename = cashbackratename;
	}

	public double getCashbackratevalue() {
		return cashbackratevalue;
	}

	public void setCashbackratevalue(double cashbackratevalue) {
		this.cashbackratevalue = cashbackratevalue;
	}
}

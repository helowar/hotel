package com.mangocity.hagtb2b.persistence;

import java.io.Serializable;


/**
 * 
 * @author zengyong
 * Mar 11, 2010,5:33:41 PM
 *描述:代理用户实体
 */
public class AgentUser {

	 private Long userid;
	 private String loginname;
	 private String orgid;
	 private String name;
	 private String email;
	 private String usertype;//用户类型1代理，2为芒果
	 private int permission;//1为主管 0为一般员工
	 private String adminArea;//管理区域
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public int getPermission() {
		return permission;
	}
	public void setPermission(int permission) {
		this.permission = permission;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getAdminArea() {
		return adminArea;
	}
	public void setAdminArea(String adminArea) {
		this.adminArea = adminArea;
	}
	
}

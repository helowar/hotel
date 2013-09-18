package com.mangocity.hagtb2b.persistence;

import java.io.Serializable;


/**
 * 
 * @author zengyong
 * Mar 11, 2010,5:33:41 PM
 *描述:代理商实体
 */
public class AgentOrg {

	 private Long org_id;
	 private String org_name;
	 private String linkman;
	 private String agentcode;
	 private String description;
	 private int policyscope;
	 private int agentkind;

	 private String org_address;
	 private String phoneumber;
	 private String faxnumber;
	 private String agentnumber;//代理编号
	 private String areavalue;//所属区域
	 private String agentemail;
	 private String confirmed;
	 
	 private String bankname;//开户行
	 private String account;//账号
	 private String bankusername;//开户姓名
	
	 
	public Long getOrg_id() {
		return org_id;
	}
	public void setOrg_id(Long org_id) {
		this.org_id = org_id;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getAgentcode() {
		return agentcode;
	}
	public void setAgentcode(String agentcode) {
		this.agentcode = agentcode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPolicyscope() {
		return policyscope;
	}
	public void setPolicyscope(int policyscope) {
		this.policyscope = policyscope;
	}
	public int getAgentkind() {
		return agentkind;
	}
	public void setAgentkind(int agentkind) {
		this.agentkind = agentkind;
	}
	public String getOrg_address() {
		return org_address;
	}
	public void setOrg_address(String org_address) {
		this.org_address = org_address;
	}
	public String getPhoneumber() {
		return phoneumber;
	}
	public void setPhoneumber(String phoneumber) {
		this.phoneumber = phoneumber;
	}
	public String getFaxnumber() {
		return faxnumber;
	}
	public void setFaxnumber(String faxnumber) {
		this.faxnumber = faxnumber;
	}
	public String getAgentnumber() {
		return agentnumber;
	}
	public void setAgentnumber(String agentnumber) {
		this.agentnumber = agentnumber;
	}
	public String getAreavalue() {
		return areavalue;
	}
	public void setAreavalue(String areavalue) {
		this.areavalue = areavalue;
	}
	public String getAgentemail() {
		return agentemail;
	}
	public void setAgentemail(String agentemail) {
		this.agentemail = agentemail;
	}
	public String getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(String confirmed) {
		this.confirmed = confirmed;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getBankusername() {
		return bankusername;
	}
	public void setBankusername(String bankusername) {
		this.bankusername = bankusername;
	}



	 
}

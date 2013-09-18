package com.mangocity.hotel.base.util;

/**
 * B2B会员信息
 * @author shizhongwen
 *
 */
public class B2BMember {
	/**
	 * 代理商ID
	 */
	private  String agentId;
	
	/**
	 * 代理商名称
	 */
	private  String agentName;
	
	/**
	 * 代理商编码code (与会员绑定，即会员Code)
	 */
	private  String agentCode;
	
	/**
	 * /销售币种
	 */
	private  String saleCurrency;
	
	/**
	 * 结算币种
	 */
	private  String balanceCurrency;
	
	/**
	 * 汇率
	 */
	private  String rate;
	
	/**
	 * 类型
	 */
	private  String footWay;
	
	/**
	 * 操作者
	 */
	private  String operaterId;
	
	/**
	 * 是否为主管1是，0否
	 */
	private String manageSign ;

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getSaleCurrency() {
		return saleCurrency;
	}

	public void setSaleCurrency(String saleCurrency) {
		this.saleCurrency = saleCurrency;
	}

	public String getBalanceCurrency() {
		return balanceCurrency;
	}

	public void setBalanceCurrency(String balanceCurrency) {
		this.balanceCurrency = balanceCurrency;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getFootWay() {
		return footWay;
	}

	public void setFootWay(String footWay) {
		this.footWay = footWay;
	}

	public String getOperaterId() {
		return operaterId;
	}

	public void setOperaterId(String operaterId) {
		this.operaterId = operaterId;
	}

	public String getManageSign() {
		return manageSign;
	}

	public void setManageSign(String manageSign) {
		this.manageSign = manageSign;
	}
}

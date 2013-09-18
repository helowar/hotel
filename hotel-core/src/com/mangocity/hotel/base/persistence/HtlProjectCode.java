package com.mangocity.hotel.base.persistence;

/**
 * 用于存放 cookie中的值
 * @author xuyiwen
 *
 */
public class HtlProjectCode {
	private Long id;
	
	/**
	 * 订单号
	 */
	private String orderCD;
	
	/**
	 * 存放cookie中的projectCode
	 */
	private String projectCode;
	
	/**
	 * 存放cookie中的exProjectCode1
	 */
	private String exProjectCode1;
	
	/**
	 * 存放cookie中的exProjectCode2
	 */
	private String exProjectCode2;
	
	/**
	 * 备用字段1
	 */
	private String reserve1;
	
	/**
	 * 备用字段2
	 */
	private String reserve2;
	/**
	 * 专辑渠道来源
	 */
	private String zjCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getExProjectCode1() {
		return exProjectCode1;
	}

	public void setExProjectCode1(String exProjectCode1) {
		this.exProjectCode1 = exProjectCode1;
	}

	public String getExProjectCode2() {
		return exProjectCode2;
	}

	public void setExProjectCode2(String exProjectCode2) {
		this.exProjectCode2 = exProjectCode2;
	}

	public String getReserve1() {
		return reserve1;
	}

	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}

	public String getReserve2() {
		return reserve2;
	}

	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}

	public String getZjCode() {
		return zjCode;
	}

	public void setZjCode(String zjCode) {
		this.zjCode = zjCode;
	}	
}

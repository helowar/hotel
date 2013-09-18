package com.mangocity.hotel.order.persistence;

import java.util.Date;

import com.mangocity.util.Entity;


/**
 * B2B 代理商修改订单类
 * @author shizhongwen
 *
 */
public class B2bModifyOrderInfo implements Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3979041867102064057L;

	/**
	 * 订单ID <pk> 
	 */
    private Long ID;
    
    /**
     * 订单编号
     */
    private String orderCD;
    
    /**
     * 订单状态 0 表示修改已处理 1 修改未处理， 2 取消未处理  3 取消已处理
     */
    private int orderState;
    
	/**
	 *入住人名称 ,类型 内宾 1 ， 外宾 2 ，港澳台 3  性别 男：M ，女：F
	 */
	private String fellowInfo;	
	
	 /**
	 *  客人特殊要求
	 */
    private String specialRequest;  
	
    /**
	 *  联系人
	 */
    private String linkMan;
    
    /**
	 *   手机
	 */
    private String mobile;        
    
    /**
	 * 电话号码
	 */
    private String telephone;    
    
    /**
	 *  传真
	 */
    private String customerFax;
    
    /**
	 *  电子邮箱
	 */
    private String email;
    
    /**
     * 支付方式
     */
    private String payMethod;
	
    /**
     *  预订确认方式 传真 1,电邮 2,短信 3 ,电话 4
     */
    private String confirmType;
    
    /**
     * 创建日期
     */
    private Date createDate;    

    

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}

	public String getSpecialRequest() {
		return specialRequest;
	}

	public void setSpecialRequest(String specialRequest) {
		this.specialRequest = specialRequest;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCustomerFax() {
		return customerFax;
	}

	public void setCustomerFax(String customerFax) {
		this.customerFax = customerFax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getConfirmType() {
		return confirmType;
	}

	public void setConfirmType(String confirmType) {
		this.confirmType = confirmType;
	}

	public String getFellowInfo() {
		return fellowInfo;
	}

	public void setFellowInfo(String fellowInfo) {
		this.fellowInfo = fellowInfo;
	}

	public int getOrderState() {
		return orderState;
	}

	public void setOrderState(int orderState) {
		this.orderState = orderState;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	
}

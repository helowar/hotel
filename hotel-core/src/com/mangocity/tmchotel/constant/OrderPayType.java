package com.mangocity.tmchotel.constant;

import java.io.Serializable;

/**
 * 订单的支付类型
 * @author chenjiajie
 *
 */
public class OrderPayType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1188914630004032329L;

	/**
	 * 面付方式：(酒店前台面付:1)
	 */
	public static final int FACE_PAY = 1;
	
	/**
	 * 预付方式：(信用卡支付:2)
	 */
	public static final int CREDIT_PAY = 2;
	
	/**
	 * 预付方式：(营业部付款:3)
	 */
	public static final int COUNTER_PAY = 3;
	
	/**
	 * 直联方式：(网上银行支付:4)
	 */
	public static final int ONLINE_PAY = 4;
}

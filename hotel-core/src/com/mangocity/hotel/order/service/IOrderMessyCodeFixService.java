package com.mangocity.hotel.order.service;
import java.io.Serializable;

/**
 * 订单乱码修复调用接口
 * @author zhouna
 *
 */
public interface IOrderMessyCodeFixService extends Serializable{
	
	/**
	 * 调用乱码修复的存储过程
	 * @param orderCD
	 * @return
	 */
	public String callFixOrderMessyCodeProcdure(String orderCD);

}

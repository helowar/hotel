package com.mangocity.hotel.order.service.assistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mangocity.hotel.order.persistence.DaDailyauditItem;
/**
 * 封装页面所使用的对象
 * @author huangxinyang
 *
 */
public class ReturnInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public Long hotelID;
	public String hotelName;//酒店名称
	public String orderCD;//订单号码
	List<DaDailyauditItem> returnDateList = new ArrayList<DaDailyauditItem>();//回访明细
	
   /*===get   set===*/
	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public List<DaDailyauditItem> getReturnDateList() {
		return returnDateList;
	}

	public void setReturnDateList(List<DaDailyauditItem> returnDateList) {
		this.returnDateList = returnDateList;
	}

	public Long getHotelID() {
		return hotelID;
	}

	public void setHotelID(Long hotelID) {
		this.hotelID = hotelID;
	}

	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}
	
}

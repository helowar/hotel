package com.mangocity.tmchotel.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.mangocity.hweb.persistence.QueryHotelForWebSaleItems;
import javax.servlet.http.HttpServletRequest;

/**
 * add by shengwei.zuo 
 */

public class HtlCalendarHelperBean {
	
	//入住日期
	private Date inDate;
	
	//离店日期
	private Date outDate;

    // "payMethod"预付面付类型
    private String payMethod;
    
    // 必须面转预标志 
    private boolean payToPrepay;
    
    //价格列表list;
    List<QueryHotelForWebSaleItems>  lstHotelSaleItems =  new  ArrayList<QueryHotelForWebSaleItems>();
    
    private HttpServletRequest  requestObj ;

	public Date getInDate() {
		return inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public boolean isPayToPrepay() {
		return payToPrepay;
	}
	
	public boolean hasPayToPrepay() {
		return payToPrepay;
	}

	public void setPayToPrepay(boolean payToPrepay) {
		this.payToPrepay = payToPrepay;
	}

	public List<QueryHotelForWebSaleItems> getLstHotelSaleItems() {
		return lstHotelSaleItems;
	}

	public void setLstHotelSaleItems(
			List<QueryHotelForWebSaleItems> lstHotelSaleItems) {
		this.lstHotelSaleItems = lstHotelSaleItems;
	}

	public HttpServletRequest getRequestObj() {
		return requestObj;
	}

	public void setRequestObj(HttpServletRequest requestObj) {
		this.requestObj = requestObj;
	}

}
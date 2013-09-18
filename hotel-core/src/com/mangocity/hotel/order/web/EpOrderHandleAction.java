package com.mangocity.hotel.order.web;

import java.util.List;

import com.mangocity.ep.entity.EpOrder;
import com.mangocity.ep.entity.RequestParam;
import com.mangocity.ep.service.EpOrderManagerService;
import com.mangocity.webnew.util.action.GenericWebAction;

public class EpOrderHandleAction extends GenericWebAction{
    
	private List<EpOrder> epOrderList;
	
	private String orderCd="";
	
	private String hotelName="";
	
	private String hotelstatus="";
	
	private String ccstatus="0";
	
	private RequestParam requestParam;
	
	private EpOrderManagerService epOrderManagerService;
	
	@Override
	public String execute() throws Exception {
		initParam();
		epOrderList=epOrderManagerService.queryEpOrder(requestParam);
		return SUCCESS;
	}
    
	public RequestParam initParam(){
		requestParam = new RequestParam();
		requestParam.setCcstatus(ccstatus);
		requestParam.setHotelName(hotelName);
		requestParam.setHotelstatus(hotelstatus);
		requestParam.setOrderCd(orderCd);
		return requestParam;
	}
	
	public List<EpOrder> getEpOrderList() {
		return epOrderList;
	}

	public void setEpOrderList(List<EpOrder> epOrderList) {
		this.epOrderList = epOrderList;
	}


	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelstatus() {
		return hotelstatus;
	}

	public void setHotelstatus(String hotelstatus) {
		this.hotelstatus = hotelstatus;
	}

	public String getCcstatus() {
		return ccstatus;
	}

	public void setCcstatus(String ccstatus) {
		this.ccstatus = ccstatus;
	}

	public String getOrderCd() {
		return orderCd;
	}

	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}

	public RequestParam getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(RequestParam requestParam) {
		this.requestParam = requestParam;
	}

	public EpOrderManagerService getEpOrderManagerService() {
		return epOrderManagerService;
	}

	public void setEpOrderManagerService(EpOrderManagerService epOrderManagerService) {
		this.epOrderManagerService = epOrderManagerService;
	}

    
	
	
}

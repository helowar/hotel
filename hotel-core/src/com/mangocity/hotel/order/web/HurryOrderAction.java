package com.mangocity.hotel.order.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;


import com.mangocity.hotel.order.dao.HurryOrderDAO;
import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.service.HurryOrderService;
import com.mangocity.hotel.order.service.OperOrderDerferTimeService;
import com.opensymphony.xwork2.ActionContext;

public class HurryOrderAction extends GenericCCAction {
    
	private HurryOrderService hurryOrderService;
	private Integer hurryOrderTimes;
	private Long orderId;
	
	@Override
	public String execute() throws Exception {
		
		PrintWriter out=ServletActionContext.getResponse().getWriter();
		ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
		hurryOrderTimes=hurryOrderService.modifyHurryOrderNum(orderId);
		out.print(hurryOrderTimes);
		out.flush();
		out.close();
	
		return "null";
	}

    
	public String cleanHurryTimes() throws IOException{
		PrintWriter out=ServletActionContext.getResponse().getWriter();
		ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
		hurryOrderService.cleanHurryTimes(orderId);
		out.print(1);
		out.flush();
		out.close();
	
		return null;
	}
	

	public HurryOrderService getHurryOrderService() {
		return hurryOrderService;
	}



	public void setHurryOrderService(HurryOrderService hurryOrderService) {
		this.hurryOrderService = hurryOrderService;
	}



	public Integer getHurryOrderTimes() {
		return hurryOrderTimes;
	}

	public void setHurryOrderTimes(Integer hurryOrderTimes) {
		this.hurryOrderTimes = hurryOrderTimes;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
   
   
	
}

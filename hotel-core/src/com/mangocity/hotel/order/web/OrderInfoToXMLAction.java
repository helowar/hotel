package com.mangocity.hotel.order.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.mangocity.hotel.order.service.OrderInfoToXMLService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class OrderInfoToXMLAction extends ActionSupport implements ServletRequestAware {
	
	private OrderInfoToXMLService orderInfoToXMLService;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public void getOrdercdByMemAndHtl() throws IOException {
		request = this.getRequest();
		response = this.getResponse();
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		String membercd = (String)request.getParameter("memberCd");
		String hotelId = (String)request.getParameter("hotelId");
		String xml = orderInfoToXMLService.findOrderByMemberCd(membercd, hotelId);
		PrintWriter out = response.getWriter();
		out.print(xml);
		out.flush();
		out.close();
	}
	
	public void getOrderInfoByData() throws IOException {
		
		request = this.getRequest();
		response = this.getResponse();
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		String auditedDate = (String)request.getParameter("auditedDate");
		String xml = orderInfoToXMLService.findOrderInfoByAuditedDate(auditedDate);
		PrintWriter out = response.getWriter();
		out.print(xml);
		out.flush();
		out.close();
	}

	public OrderInfoToXMLService getOrderInfoToXMLService() {
		return orderInfoToXMLService;
	}

	public void setOrderInfoToXMLService(OrderInfoToXMLService orderInfoToXMLService) {
		this.orderInfoToXMLService = orderInfoToXMLService;
	}

	public HttpServletRequest getRequest() {
		ActionContext ctx = ActionContext.getContext();
		request = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
		return request;
	}
	public HttpServletResponse getResponse() {
		ActionContext ctx = ActionContext.getContext();
		response = (HttpServletResponse) ctx.get(ServletActionContext.HTTP_RESPONSE);
		return response;
	}

	public void setServletRequest(HttpServletRequest request) {	    
	    this.request = request;
	}
	
	public void setServletResponse(HttpServletResponse response) {	    
	    this.response = response;
	}
	
	
}

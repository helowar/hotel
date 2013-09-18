package com.mangocity.ep.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.mangocity.ep.entity.EpOrder;
import com.mangocity.ep.entity.RequestParam;
import com.mangocity.ep.service.EpOrderManagerService;
import com.mangocity.hotel.order.web.OrderAction;
import com.opensymphony.xwork2.ActionContext;

public class EpOrderHandleAction extends OrderAction{
    
	private List<EpOrder> epOrderList;
	
	private String orderCd="";
	
	private String hotelName="";
	
	private String hotelstatus="";
	
	private String ccstatus="0";
	
	private RequestParam requestParam;
	
	private EpOrderManagerService epOrderManagerService;
	
	private Integer totalSum;
	
	private Integer pageNo=1;
	
	private Integer perPageSize=12;
	
	@Override
	public String execute() throws Exception {
		initParam();
		totalSum=epOrderManagerService.queryOrderSum(requestParam);
		epOrderList=epOrderManagerService.queryEpOrder(requestParam);
		return SUCCESS;
	}
    
	public String synConfirmOrder(){
		PrintWriter out=null;
		try {
			out=ServletActionContext.getResponse().getWriter();
			ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
			roleUser = getOnlineRoleUser();
			epOrderManagerService.updateConfrimStatus(orderCd,roleUser);
			out.print("1");			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}		
		return null;
	}
	
	public RequestParam initParam(){
		requestParam = new RequestParam();
		requestParam.setCcstatus(ccstatus);
		requestParam.setHotelName(hotelName);
		requestParam.setHotelstatus(hotelstatus);
		requestParam.setOrderCd(orderCd);
		requestParam.setPageNo(pageNo);
		requestParam.setPerPageSize(perPageSize);
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

	public Integer getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(Integer totalSum) {
		this.totalSum = totalSum;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPerPageSize() {
		return perPageSize;
	}

	public void setPerPageSize(Integer perPageSize) {
		this.perPageSize = perPageSize;
	}

    
	
	
}

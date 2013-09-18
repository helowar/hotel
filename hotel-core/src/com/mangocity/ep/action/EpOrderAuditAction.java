package com.mangocity.ep.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.mangocity.ep.entity.AuditOrder;
import com.mangocity.ep.entity.OrderParam;
import com.mangocity.ep.service.EpDailyAuditService;
import com.mangocity.hotel.order.web.OrderAction;
import com.opensymphony.xwork2.ActionContext;

public class EpOrderAuditAction  extends OrderAction{
	
	
	private EpDailyAuditService epDailyAuditService;
	
	private OrderParam orderParam;
	
	private String hotelId;
	
	private Integer pageNo=1;
	
	private Integer pageSize=8;
	
	private String status;
	
	private String confirmStatus;
	
	private String auditStatus="0";
	
	private String orderCd;
	
	private String auditType="0";
	
	private String hotelName="";
	
	private List<AuditOrder> auditOrderList;
	
	private String hotelStatus;
	
	private String ccStatus;
	
    private Long totalSum;
    
    private String checkOutDate;
    
    private String remark;
	
	@Override
	public String execute() throws Exception {

		initParam();
		totalSum = epDailyAuditService.queryOrderAuditSum(orderParam);
		auditOrderList=epDailyAuditService.queryEpOrderAuditData(orderParam);
		return "orderAudit";
	}
    
    
	 
	public String synConfirmOrder(){
		PrintWriter out=null;
		try {
			out=ServletActionContext.getResponse().getWriter();
			ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
			roleUser = getOnlineRoleUser();
			epDailyAuditService.updateOrderCcStatus(roleUser,orderCd);
			out.print("1");			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}		
		return null;
	}
	
	 public String showMessage(){
	    	remark=epDailyAuditService.queryRemark(orderCd, "0");
	    	return "sendMessage";
	 }
	 
	 
	 public String sendMes(){
	    	PrintWriter out=null;
			try {
				out = ServletActionContext.getResponse().getWriter();
				ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
				epDailyAuditService.updateRemark(orderCd, "0", remark);
		    	out.print("1");
			}catch (IOException e) {
				log.error("sendMes:orderCd="+orderCd,e);
			}finally{
				out.flush();
				out.close();
			}			
	    	return null;
	}
	
	public OrderParam initParam(){
	    orderParam = new OrderParam();
		orderParam.setHotelId(hotelId);
		orderParam.setPageNo(pageNo);
		orderParam.setPageSize(pageSize);
		orderParam.setStatus(status);
		orderParam.setConfirmStatus(confirmStatus);
		orderParam.setAuditStatus(auditStatus);
		orderParam.setOrderCd(orderCd);
		orderParam.setAuditType(auditType);
		orderParam.setHotelName(hotelName);
		orderParam.setHotelStatus(hotelStatus);
		orderParam.setCcStatus(ccStatus);
		orderParam.setCheckOutDate(checkOutDate);
		return orderParam;
	}



	public EpDailyAuditService getEpDailyAuditService() {
		return epDailyAuditService;
	}



	public void setEpDailyAuditService(EpDailyAuditService epDailyAuditService) {
		this.epDailyAuditService = epDailyAuditService;
	}



	public OrderParam getOrderParam() {
		return orderParam;
	}



	public void setOrderParam(OrderParam orderParam) {
		this.orderParam = orderParam;
	}



	public String getHotelId() {
		return hotelId;
	}



	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}



	public Integer getPageNo() {
		return pageNo;
	}



	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}



	public Integer getPageSize() {
		return pageSize;
	}



	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getConfirmStatus() {
		return confirmStatus;
	}



	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}



	public String getAuditStatus() {
		return auditStatus;
	}



	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}



	public String getOrderCd() {
		return orderCd;
	}



	public void setOrderCd(String orderCd) {
		this.orderCd = orderCd;
	}



	public String getAuditType() {
		return auditType;
	}



	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}



	public String getHotelName() {
		return hotelName;
	}



	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}



	public List<AuditOrder> getAuditOrderList() {
		return auditOrderList;
	}



	public void setAuditOrderList(List<AuditOrder> auditOrderList) {
		this.auditOrderList = auditOrderList;
	}



	public String getHotelStatus() {
		return hotelStatus;
	}



	public void setHotelStatus(String hotelStatus) {
		this.hotelStatus = hotelStatus;
	}



	public String getCcStatus() {
		return ccStatus;
	}



	public void setCcStatus(String ccStatus) {
		this.ccStatus = ccStatus;
	}



	public Long getTotalSum() {
		return totalSum;
	}



	public void setTotalSum(Long totalSum) {
		this.totalSum = totalSum;
	}



	public String getCheckOutDate() {
		return checkOutDate;
	}



	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		try {
			if(remark!=null)
				remark=URLDecoder.decode(remark,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		this.remark = remark;
	}

    
    
	
	
}

package com.mangocity.hotel.order.web;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;


import com.mangocity.hotel.order.persistence.DeferOrder;
import com.mangocity.hotel.order.persistence.DerferOrderParam;
import com.mangocity.hotel.order.persistence.OrHandleLog;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.service.OperOrderDerferTimeService;
import com.opensymphony.xwork2.ActionContext;

public class DerferTimeOrderAction extends PopulateOrderAction {
   
	private Integer deferTime;
	private Long orderId;
	private OperOrderDerferTimeService operOrderDerferTimeService;
	private List<DeferOrder>  list_1=new ArrayList<DeferOrder> ();
	private Integer pageTotal=0;
	private Integer curPage=1;
	private Integer pageSize=10;
	private String orderCD="";
	private String memberName="";
	private String hotelName="";
	private String bussinessName="";
	private String vipLevel="";
	
	private OrOrder order;
	@Override
	public String execute() throws Exception {
		PrintWriter out=ServletActionContext.getResponse().getWriter();
		ActionContext.getContext().getActionInvocation().getProxy().setExecuteResult(false);
		System.out.println("############"+orderId+"#############");
		operOrderDerferTimeService.modifyOrderDerferTime(deferTime, orderId);
		
		//添加日志
		roleUser = getOnlineRoleUser();
		order = getOrder(orderId);
		OrHandleLog handleLog = new OrHandleLog();
		handleLog.setModifierName(roleUser.getName());
        handleLog.setModifierRole(roleUser.getLoginName());
        handleLog.setContent("暂缓时间："+deferTime+" 分钟");
        handleLog.setModifiedTime(new Date());
        handleLog.setBeforeState(order.getOrderState());
        handleLog.setAfterState(order.getOrderState());
        handleLog.setOrder(order);
        operOrderDerferTimeService.saveDerferTimeLog(handleLog);
        
		out.print("1");
		out.flush();
		out.close();
		return "null";
	}
    
	public String queryDeferOrder(){
		roleUser = getOnlineRoleUser();
		DecimalFormat df=new DecimalFormat("###");
		DerferOrderParam param = new DerferOrderParam();
		param.setAssignTo(roleUser.getLoginName());
		packgeParam(param);
		list_1=operOrderDerferTimeService.queryDerferOrderData(param);
		pageTotal=Integer.parseInt(df.format(Double.parseDouble(operOrderDerferTimeService.querySumRow(param).toString())/pageSize+0.5));
		return "deferOrder";
	}
	
	public void packgeParam(DerferOrderParam param){
		param.setOrderCD(orderCD);
		param.setMemberName(memberName);
		param.setHotelName(hotelName);
		param.setBussinessName(bussinessName);
		param.setVipLevel(vipLevel);
		param.setStartIndex((curPage-1)*pageSize+1);
		param.setEndIndex(curPage*pageSize);		
	}
	
	public Integer getDeferTime() {
		return deferTime;
	}

	public void setDeferTime(Integer deferTime) {
		this.deferTime = deferTime;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public OperOrderDerferTimeService getOperOrderDerferTimeService() {
		return operOrderDerferTimeService;
	}

	public void setOperOrderDerferTimeService(
			OperOrderDerferTimeService operOrderDerferTimeService) {
		this.operOrderDerferTimeService = operOrderDerferTimeService;
	}

	public List<DeferOrder> getList_1() {
		return list_1;
	}

	public void setList_1(List<DeferOrder> list_1) {
		this.list_1 = list_1;
	}

	public Integer getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(Integer pageTotal) {
		this.pageTotal = pageTotal;
	}

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderCD() {
		return orderCD;
	}

	public void setOrderCD(String orderCD) {
		this.orderCD = orderCD;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		try {
			if(memberName!=null && memberName.length()>0)
				memberName=URLDecoder.decode(memberName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		this.memberName = memberName;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		try {
			if(hotelName!=null && hotelName.length()>0)
				hotelName=URLDecoder.decode(hotelName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		this.hotelName = hotelName;
	}

	public String getBussinessName() {
		return bussinessName;
	}

	public void setBussinessName(String bussinessName) {
		try {
			if(bussinessName!=null && bussinessName.length()>0)
				bussinessName=URLDecoder.decode(bussinessName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		this.bussinessName = bussinessName;
	}

	public String getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}

	public OrOrder getOrder() {
		return order;
	}

	public void setOrder(OrOrder order) {
		this.order = order;
	}

   
	
}

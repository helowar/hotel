package com.mangocity.hotel.order.web;

import java.util.List;

import com.mangocity.hotel.base.web.webwork.GenericAction;
import com.mangocity.hotel.order.dao.IOrOrderDAO;
import com.mangocity.hotel.order.persistence.OrOrder;
import com.mangocity.hotel.order.persistence.OrSMSRecv;
import com.mangocity.hotel.order.service.IOrSMSService;

public class SMSQueryAction extends GenericAction{
	private String mobile;//手机号
	private String date;//日期
	private String orderid;
	private String context;
	private IOrSMSService orSMSService;
	private IOrOrderDAO orOrderDao;
	/**
     * 2012.2.3 new add 查询短信取消订单信息
     */
    public String execute(){
    	List<OrSMSRecv> smslist = orSMSService.querySMSByMobileAndDate(mobile, date,orderid);
    	request.setAttribute("smslist", smslist);
    	OrOrder order = orOrderDao.load(OrOrder.class, Long.valueOf(orderid));
    	//String ordercd = order.getOrderCD();
    	String hotelname = order.getHotelName();
    	String telNo = order.isCooperateOrder() ? "4006785167":"4006640066";
    	String sendContent = "芒果网提示您：您今天将入住 "+hotelname+order.getRoomQuantity()+"间"+order.getRoomTypeName()
    			+", 房间将为您保留至 "+order.getLatestArrivalTime()
    			+", 如果不能入住即回短信“"+context
    			+"”;晚到或修改请电"
    			+telNo
    			+".祝您旅途愉快！";
    	request.setAttribute("sendContent",sendContent);
    	return "smslist";
    }
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public IOrSMSService getOrSMSService() {
		return orSMSService;
	}
	public void setOrSMSService(IOrSMSService orSMSService) {
		this.orSMSService = orSMSService;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public IOrOrderDAO getOrOrderDao() {
		return orOrderDao;
	}
	public void setOrOrderDao(IOrOrderDAO orOrderDao) {
		this.orOrderDao = orOrderDao;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
    
}

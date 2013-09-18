package com.mangocity.hotel.dreamweb.orderrecord.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mangocity.hotel.dreamweb.orderrecord.service.QueryOrderRecordService;
import com.mangocity.hotel.orderrecord.model.OrderRecord;
import com.mangocity.hotel.orderrecord.service.AbstractOrderRecord;
import com.mangocity.hotel.orderrecord.service.OrderRecordService;
import com.mangocity.util.StringUtil;
import com.mangocity.util.bean.MyBeanUtil;
import com.mangocity.util.web.CookieUtils;
import com.mangocity.webnew.persistence.HotelOrderFromBean;

public class QueryOrderRecordServiceImpl implements QueryOrderRecordService{

	private static final String ERROR_MESSAGE = "query order record has a wrong ";
	private OrderRecordService  orderRecordService;

	private static final Logger log = Logger.getLogger(BookOrderRecordServiceImpl.class);

	/**
	 * 
	 * @param request
	 * @param response
	 * @param checkInDate
	 * @param checkOutDate
	 * @param hotelId
	 * @param step
	 */
	public void saveOrderRecord(final HttpServletRequest request, final HttpServletResponse response,final Date checkInDate,final Date checkOutDate,final Long hotelId,final int step) {
		try {
			HttpSession httpSession = request.getSession(true);
			final OrderRecord orderRecord = getOrderRecord(request, httpSession, step);
			HotelOrderFromBean hotelOrderFromBean=new HotelOrderFromBean();
			AbstractOrderRecord orderRecoreService = new AbstractOrderRecord(orderRecord, hotelOrderFromBean) {

				public void init(){};
				
				public void combineOrderRecord() {
					orderRecord.setCreateDate(new Date());
					orderRecord.setCreatorName("system");
					orderRecord.setCreatorId("system");
					orderRecord.setCheckoutDate(checkOutDate);
					orderRecord.setCheckinDate(checkInDate);
					orderRecord.setHotelId(hotelId);
					if (orderRecord != null) {											
						String projectcode = (String) request.getAttribute("projectcode");
						if (StringUtil.isValidStr(projectcode) && projectcode.length()<128) {
							orderRecord.setProjectCode(projectcode);
							
							//将request中的projectcode存放在cookie中去
							CookieUtils.setCookie(request, response, "projectcode", projectcode, 0, null, null);
						} else {
							projectcode = CookieUtils.getCookieValue(request, "projectcode");
							if (StringUtil.isValidStr(projectcode)) {
								orderRecord.setProjectCode(projectcode);
							}
						}
					}
				}
			};
			WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext());
			orderRecoreService.createOrderRecordTemplete(context);
			
			httpSession.setAttribute("orderRecord",orderRecord);
			CookieUtils.setCookie(request, response, "actionId",String.valueOf(orderRecord.getActionId()), 0, null, null);
			
		} catch (Exception e) {

			log.error(ERROR_MESSAGE + step, e);
		}
	}

	public void updateOrderRecord(OrderRecord orderRecord){
		try {
			orderRecordService.updateOrderRecord(orderRecord);
		} catch (Exception e) {
			
			log.error("update order record has a wrong", e);
		}
	}
	
	/**
	 * 获取OrderRecord，并设置orderRecord的基本属性,每一步都需要记录的数据
	 * 
	 * @param request
	 * @param httpSession
	 * @param step
	 * @param order
	 * @return
	 */
	private OrderRecord getOrderRecord(HttpServletRequest request, HttpSession httpSession, final int step) {
		OrderRecord orderRecordFromSession = (OrderRecord) httpSession.getAttribute("orderRecord");
		OrderRecord orderRecord=new OrderRecord();;
		if (orderRecordFromSession == null) {
			String actionId = CookieUtils.getCookieValue(request, "actionId");		
			if (StringUtil.isValidStr(actionId)) {
				orderRecord.setActionId(Long.valueOf(actionId));
			}
		} else {
			MyBeanUtil.copyProperties(orderRecord, orderRecordFromSession);
			orderRecord.setRecordId(null);
		}
		
		String sessionId=getApacheSessionId(request);
		if ((sessionId.length() > 64) ||(!sessionId.matches("^(\\d+\\.)+\\d+$"))) {
			sessionId = "";
		}
		orderRecord.setApacheSessionId(sessionId);// 添加sessionId
		
		String ip=getCustomerIp(request);
		if(ip.length() > 32 || !ip.matches("^(\\d+\\.)+\\d+$")){
			ip="";
		}
		orderRecord.setCustomerIp(ip);
		
		orderRecord.setCurrentStep(step);

		orderRecord.setSource("net");
		return orderRecord;
	}

	private String getApacheSessionId(HttpServletRequest request) {

		String clientIp = getCustomerIp(request);
		String sessionId = CookieUtils.getCookieValue(request, "SessionID");

		StringBuilder apacheSessionId = new StringBuilder();
		if (null != sessionId && !"".equals(sessionId)) {
			apacheSessionId.append(clientIp);
			apacheSessionId.append(".");
			apacheSessionId.append(sessionId);// 添加sessionId
		}

		return apacheSessionId.toString();
	}

	private String getCustomerIp(HttpServletRequest request) {
		String ip = request.getHeader("clientip");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public void setOrderRecordService(OrderRecordService orderRecordService) {
		this.orderRecordService = orderRecordService;
	}

	
}
